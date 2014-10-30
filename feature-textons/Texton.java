package texton;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Texton {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("hehe");
		String filterFileName = "/home/ang/projects/slda/slda-spark/feature-textons/filters.dat";
		ArrayList<double[][]> filters = readFilters(filterFileName);
		BufferedImage img = null;
		String imageFilename = "/home/ang/projects/slda/slda-spark/feature-textons/test.jpg";
		img = ImageIO.read(new File(imageFilename));
		String outputFileName = "/home/ang/projects/slda/slda-spark/feature-textons/output.png";
		ImageIO.write(img, "png", new File(outputFileName));
		double[][] response = filtering(img, filters);
		DataOutputStream os = new DataOutputStream(new FileOutputStream(
				"/home/ang/projects/slda/slda-spark/feature-textons/output.dat"));
		int w = img.getWidth();
		int h = img.getHeight();
		os.writeInt(w * h);
		os.writeInt(19);
		for (int i = 0; i < w * h; ++ i)
			for (int j = 0; j < 19; ++ j)
				os.writeDouble(response[i][j]);
		os.close();
		System.out.println("hoho");
	}
	
	public static double[][] convolve(double[][] filter, float[][] data, int w, int h) {
		double[][] response = new double[w][h];
		for (int x = 0; x < w; ++ x)
			for (int y = 0; y < h; ++ y) {
				double current = 0;
				// (x, y) - (7, 7), (x + t, y) - (7 - t, 7)
				for (int dx = -7; dx <= 7; ++ dx)
					for (int dy = -7; dy <= 7; ++ dy) {
						double cdata;
						if (x - dx >= 0 && x - dx < w && y - dy >= 0 && y - dy < h)
							cdata = data[x - dx][y - dy];
						else cdata = 0;
						current += cdata * filter[7 + dx][7 + dy];
					}
				response[x][y] = current;
			}
		return response;
	}
	
	public static double[][] filtering(BufferedImage img, ArrayList<double[][]> filters) {
		int w = img.getWidth();
		int h = img.getHeight();
		float [][] colorR = new float[w][h];
		float [][] colorG = new float[w][h];
		float [][] colorB = new float[w][h];
		float [][] CIEL = new float[w][h];
		float [][] CIEA = new float[w][h];
		float [][] CIEB = new float[w][h];
		for (int y = 0; y < h; ++ y)
			for (int x = 0; x < w; ++ x) {
				// get response for (x, y)
				Color color = new Color(img.getRGB(x, y));
				colorR[x][y] = color.getRed();
				colorG[x][y] = color.getGreen();
				colorB[x][y] = color.getBlue();
				float[] Lab = CIELab.getInstance().fromRGB(
						new float[]{colorR[x][y], colorG[x][y], colorB[x][y]});
				CIEL[x][y] = Lab[0];
				CIEA[x][y] = Lab[1];
				CIEB[x][y] = Lab[2];
			}
		// filtering
		ArrayList<double[][]> responses = new ArrayList<double[][]>();
		for (int i = 0; i < 3; ++ i) { // 9 responses
			responses.add(convolve(filters.get(i), CIEL, w, h));
			responses.add(convolve(filters.get(i), CIEA, w, h));
			responses.add(convolve(filters.get(i), CIEB, w, h));
		}
		for (int i = 3; i < 11; ++ i) // 8 responses
			responses.add(convolve(filters.get(i), CIEL, w, h));
		
		double[][] response = new double[w*h][19];
		for (int x = 0; x < w; ++ x)
			for (int y = 0; y < h; ++ y) {
				response[x * h + y][0] = x;
				response[x * h + y][1] = y;
			}
		for (int i = 0; i < 17; ++ i)
			for (int x = 0; x < w; ++ x)
				for (int y = 0; y < h; ++ y) {
					response[x * h + y][i + 2] = responses.get(i)[x][y];
				}
		// in total 17 responses
		return response;
	}
	
	public static ArrayList<double[][]> readFilters(String filename) throws IOException {
		Path path = Paths.get(filename);
		byte[] bytes = Files.readAllBytes(path);
		if (bytes.length != 19800)
			return null;
		byte[] current = new byte[8];
		ArrayList<double[][]> filters = new ArrayList<double[][]>();
		for (int fid = 0, cnt = 0; fid < 11; ++ fid) {
			double[][] doubles = new double[15][15];
			for (int x = 0; x < 15; ++ x)
			for (int y = 0; y < 15; ++ y) {
				for (int j = 0; j < 8; ++ j)
					current[j] = bytes[cnt + 7 - j];
				doubles[x][y] = ByteBuffer.wrap(current).getDouble();
				cnt += 8;
			}
			filters.add(doubles);
		}
		return filters;
	}

}
