package slda.spark.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
import slda.hadoop.SimpleOptions;

import com.google.common.base.Splitter;

public class DemoWordCount {
	private static final String INPUT = "i";
	private static final String OUTPUT = "o";
	private static SimpleOptions so = new SimpleOptions("i:o:");
	private static Splitter splitter = Splitter.on(" ");
	public static void main(String... args) {
		so.parse(args);
		if(!so.hasOption(INPUT) || !so.hasOption(OUTPUT)) {
			System.out.println("Missing input or output: java ... -i [input file] -o [output file]");
		}
		
		String inputFile = so.getOption(INPUT);
		String outputFile = so.getOption(OUTPUT);
		SparkConf conf = new SparkConf();
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		JavaRDD<String> textData = sc.textFile(inputFile).cache();
		
		textData.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String line) throws Exception {
				return splitter.split(line);
			}
		}).mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String, Integer>(word, 1);
			}
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			
			@Override
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2;
			}
		}).saveAsTextFile(outputFile);
	}
}
