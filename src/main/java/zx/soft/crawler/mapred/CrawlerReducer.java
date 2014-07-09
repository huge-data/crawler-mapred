package zx.soft.crawler.mapred;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import zx.soft.crawler.domain.WeixinRecord;
import zx.soft.crawler.parser.ParserWeixinCore;
import zx.soft.crawler.utils.JsonUtils;

/**
 * 统计每个词对应类别及其出现次数，输出格式word——>catei:n1 catej:n2 catek:n3 ...
 * 
 * @author wgybzb
 *
 */
public class CrawlerReducer extends Reducer<LongWritable, Text, Text, Text> {

	private static List<WeixinRecord> weixins;

	@Override
	public void reduce(LongWritable key, Iterable<Text> keywords, Context context) throws InterruptedException,
			IOException {

		for (Text keyword : keywords) {
			weixins = ParserWeixinCore.parserWeixinInfo(keyword.toString());
			for (WeixinRecord weixin : weixins) {
				context.write(new Text(weixin.getOpenId()), new Text(JsonUtils.toJsonWithoutPretty(weixin)));
			}
		}

	}

}
