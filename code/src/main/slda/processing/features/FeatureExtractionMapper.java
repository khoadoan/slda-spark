package slda.processing.features;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.Mapper;

import slda.image.Textons;
import edu.umd.cloud9.io.array.Array2DOfDoublesWritable;
import edu.umd.cloud9.io.pair.PairOfIntString;

/**
 * Map-only job to convert image into matrix of texton's weights
 * @author Khoa
 *
 */
public class FeatureExtractionMapper extends Mapper<PairOfIntString, BytesWritable, PairOfIntString, Array2DOfDoublesWritable> {
	public static final String TEXTON_FILTERS_FILE = "./cache.textons";
	
	private final Array2DOfDoublesWritable VALUE = new Array2DOfDoublesWritable();
	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		Textons.loadFilters(TEXTON_FILTERS_FILE);
	}
	
	@Override
	protected void map(PairOfIntString key, BytesWritable value, Context context)
			throws IOException, InterruptedException {
		//Convert from jpeg into PNG in memory
		ByteArrayOutputStream tempOS = new ByteArrayOutputStream();
		ImageIO.write(ImageIO.read(new ByteArrayInputStream(value.getBytes())), "png", tempOS);
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(tempOS.toByteArray()));
		
		//Compute the response matrix for this image;
		double[][] response = Textons.filtering(image);
		VALUE.set(response);
		
		context.write(key, VALUE);
	}
}