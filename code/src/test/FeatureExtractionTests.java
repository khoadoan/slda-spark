import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class FeatureExtractionTests {
	
	@Test
	public void testConvertJpegToPngInMemory() throws IOException {
		String path = "/tmp/ca_map_1.jpg";
		BufferedImage origImage = ImageIO.read(new File(path));
		
		byte[] value = FileUtils.readFileToByteArray(new File(path));
		ByteArrayOutputStream tempOS = new ByteArrayOutputStream();
		ImageIO.write(ImageIO.read(new ByteArrayInputStream(value)), "png", tempOS);
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(tempOS.toByteArray()));
		
		assertEquals(origImage.getHeight(), image.getHeight());
	}
}
