
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
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Textons {

	public static float lanswer = 0;
	
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
	
	public static double f(double val) {
		if (val > 0.008856)
			return Math.pow(val, 1./3.);
		else return 7.787 * val + 16./116.;
	}
	
	public static float[] rgb2lab(float[] rgb) {
		double R = rgb[0];
		double G = rgb[1];
		double B = rgb[2];
		if (R > 1 || G > 1 || B > 1) {
			R /= 255;
			G /= 255;
			B /= 255;
		}
		double T = 0.008856;
		double [] M = new double [] {0.412453, 0.357580, 0.180423, 0.212671, 0.715160, 0.072169, 0.019334, 0.119193, 0.950227};
		double X = M[0] * R + M[1] * G + M[2] * B;
		double Y = M[3] * R + M[4] * G + M[5] * B;
		double Z = M[6] * R + M[7] * G + M[8] * B;
		X /= 0.950456;
		Z /= 1.088754;
		double L, a, b;
		if (Y > T)
			L = 116 * Math.pow(Y, 1./3.) - 16;
		else L = 903.3 * Y;
		a = 500 * (f(X) - f(Y));
		b = 200 * (f(Y) - f(Z));
		// normalize
		L = L / 100;
		a = (a + 128) / 256;
		b = (b + 128) / 256;
		return new float[] {(float)L, (float)a, (float)b};
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
//				float[] Lab = CIELab.getInstance().fromRGB(
//						new float[]{colorR[x][y], colorG[x][y], colorB[x][y]});
				float[] Lab = rgb2lab(new float[]{colorR[x][y], colorG[x][y], colorB[x][y]});
				CIEL[x][y] = Lab[0];
				CIEA[x][y] = Lab[1];
				CIEB[x][y] = Lab[2];
			}
		System.out.println("RGB(7, 7) = " + colorR[7][7] + ", " + colorG[7][7] + ", " + colorB[7][7]);
		System.out.println("LAB(7, 7) = " + CIEL[7][7] + ", " + CIEA[7][7] + ", " + CIEB[7][7]);
		lanswer = CIEL[7][7];
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
					current[j] = bytes[cnt + j];
				doubles[x][y] = ByteBuffer.wrap(current).getDouble();
				cnt += 8;
			}
			filters.add(doubles);
		}
		System.out.println(filters.get(0)[7][7]);
		return filters;
	}

}
