package zx.soft.crawler.mapred;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 读取输入文件数据，输出格式为：word——>cate
 * 
 * @author wgybzb
 *
 */
public class CrawlerMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

	@Override
	public void map(LongWritable key, Text value, Context context) throws InterruptedException, IOException {

		context.write(key, value);

	}

}
