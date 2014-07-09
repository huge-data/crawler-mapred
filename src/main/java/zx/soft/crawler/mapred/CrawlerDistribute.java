package zx.soft.crawler.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import zx.soft.crawler.utils.HDFSUtils;

/**
 * 训练模块
 * 
 * @author wanggang
 *
 */
public class CrawlerDistribute extends Configured implements Tool {

	/**
	 * 运行作业
	 */
	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();

		Path trainData = new Path(conf.get("input"));
		Path output = new Path(conf.get("output"));
		int numReducers = conf.getInt("reducers", 10);

		// Job 1a: 提取每个词语的信息
		HDFSUtils.delete(conf, output);
		Job crawlerJob = new Job(conf, "Crawler-Distribute");
		crawlerJob.setJarByClass(CrawlerDistribute.class);
		crawlerJob.setNumReduceTasks(numReducers);
		crawlerJob.setMapperClass(CrawlerMapper.class);
		crawlerJob.setReducerClass(CrawlerReducer.class);

		crawlerJob.setInputFormatClass(TextInputFormat.class);
		crawlerJob.setOutputFormatClass(TextOutputFormat.class);

		crawlerJob.setMapOutputKeyClass(LongWritable.class);
		crawlerJob.setMapOutputValueClass(Text.class);
		crawlerJob.setOutputKeyClass(Text.class);
		crawlerJob.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(crawlerJob, trainData);
		FileOutputFormat.setOutputPath(crawlerJob, output);

		if (!crawlerJob.waitForCompletion(true)) {
			System.err.println("ERROR: Word training failed!");
			return 1;
		}

		return 0;
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {
		try {
			int exitCode = ToolRunner.run(new CrawlerDistribute(), args);
			System.exit(exitCode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
