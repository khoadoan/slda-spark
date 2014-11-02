package texton;

import java.awt.image.BufferedImage;
import java.io.File;
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
		ArrayList<double[]> filters = readFilters(filterFileName);
		BufferedImage img = null;
		String imageFilename = "/home/ang/projects/slda/slda-spark/feature-textons/test.jpg";
		img = ImageIO.read(new File(imageFilename));
		String outputFileName = "/home/ang/projects/slda/slda-spark/feature-textons/output.png";
		ImageIO.write(img, "png", new File(outputFileName));
		ArrayList<BufferedImage> response = filtering(img, filters);
	}
	
	public static ArrayList<BufferedImage> filtering(BufferedImage img, ArrayList<double[]> filters) {
		int w = img.getWidth();
		int h = img.getHeight();
		for (int y = 0; y < h; ++ y)
			for (int x = 0; x < w; ++ x) {
				// get response for (x, y)
			}
	}
	
	public static ArrayList<double[]> readFilters(String filename) throws IOException {
		Path path = Paths.get(filename);
		byte[] bytes = Files.readAllBytes(path);
		if (bytes.length != 19800)
			return null;
		byte[] current = new byte[8];
		ArrayList<double[]> filters = new ArrayList<double[]>();
		for (int fid = 0, cnt = 0; fid < 11; ++ fid) {
			double[] doubles = new double[15 * 15];
			for (int i = 0; i < 15*15; ++ i) {
				for (int j = 0; j < 8; ++ j)
					current[j] = bytes[cnt + 7 - j];
				doubles[i] = ByteBuffer.wrap(current).getDouble();
				cnt += 8;
			}
			filters.add(doubles);
		}
		return filters;
	}

}
