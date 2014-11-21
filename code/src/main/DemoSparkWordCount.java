import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class DemoSparkWordCount {
	public static void main(String... args) {
		JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("Spark Count"));
	    final int threshold = Integer.parseInt(args[1]);
	    

	}
}
