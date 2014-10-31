import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.Test;


public class TextonsTest {

	@Test
	public void test() throws IOException {
		
		float[] rgb = new float[] {155, 189, 199};
		float[] lab = Textons.rgb2lab(rgb);
		System.out.println(Arrays.toString(lab));

		System.out.println("hehe");
		Textons.loadFilters();
		BufferedImage img = null;
		String imageFilename = "/home/ang/projects/slda/slda-spark/feature-textons/test.jpg";
		img = ImageIO.read(new File(imageFilename));
		String outputFileName = "/home/ang/projects/slda/slda-spark/feature-textons/output.png";
		ImageIO.write(img, "png", new File(outputFileName));
		double[][] response = Textons.filtering(img);
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
		System.out.println(Arrays.toString(response[0]));
		
		assertEquals("response[0][3] = 0.2364", true, Math.abs(response[0][3] - 0.23634283478) < 1e-6);
	}

}
