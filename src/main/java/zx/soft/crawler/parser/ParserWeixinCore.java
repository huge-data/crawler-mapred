package zx.soft.crawler.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.crawler.domain.WeixinRecord;
import zx.soft.crawler.utils.HttpUtils;
import zx.soft.crawler.utils.JsonUtils;

public class ParserWeixinCore {

	private static Logger logger = LoggerFactory.getLogger(ParserWeixinCore.class);

	private static final String BASE_URL = "http://weixin.sogou.com/weixin?";

	private static final String LEAF_URL = "&type=1&ie=utf8&p=40040100&dp=1&w=01019900&dr=1&";

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Usage: <keyword>");
			System.exit(-1);
		}
		List<WeixinRecord> records = ParserWeixinCore.parserWeixinInfo(args[0]);
		System.out.println(JsonUtils.toJson(records));

	}

	public static List<WeixinRecord> parserWeixinInfo(String keyword) {

		List<WeixinRecord> result = new ArrayList<>();
		// 获取html页面
		String html = getHtml(keyword, 1);

		// 解析
		Elements divs = Jsoup.parse(html).select("div[href]");
		if (divs.size() == 0) {
			return result;
		}
		String verifyInfo = "", lastArticleUrl = "";
		for (Element div : divs) {
			if (div.select("p").size() > 2) {
				verifyInfo = div.select("p").get(1).select("span").get(1).text();
				lastArticleUrl = div.select("p").get(2).select("a").attr("href");
			} else if (div.select("p").size() > 1) {
				lastArticleUrl = div.select("p").get(1).select("a").attr("href");
			}
			result.add(new WeixinRecord.Builder(div.select("h4").text().substring(4), div.select("h3").text())
					.setOpenId(div.attr("href").substring(12)).setHeadUrl(div.select("img").get(0).attr("src"))
					.setDescription(div.select("p").get(0).select("span").get(1).text()).setVerifyInfo(verifyInfo)
					.setLastArticleUrl(lastArticleUrl).build());
			verifyInfo = "";
			lastArticleUrl = "";
		}

		return result;
	}

	private static String getHtml(String keyword, int page) {
		String url = getUrl(keyword, page);
		logger.info("url=" + url);
		return HttpUtils.doGet(url, "UTF-8");
	}

	private static String getUrl(String keyword, int page) {
		String encodedURL = "";
		try {
			encodedURL = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return BASE_URL + "query=" + encodedURL + LEAF_URL + "page=" + page;
	}

}
