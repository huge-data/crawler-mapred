package zx.soft.crawler.driver;

import org.apache.hadoop.util.ProgramDriver;

import zx.soft.crawler.mapred.CrawlerDistribute;
import zx.soft.crawler.parser.ParserWeixinCore;

/**
 * 驱动类
 * 
 * @author wanggang
 *
 */
public class CrawlerDriver {

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		int exitCode = -1;
		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("parserWeixinCore", ParserWeixinCore.class, "微信页面解析器");
			pgd.addClass("crawlerDistribute", CrawlerDistribute.class, "分布式网络爬虫");
			pgd.driver(args);
			// Success
			exitCode = 0;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		System.exit(exitCode);

	}

}
