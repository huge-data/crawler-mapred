package zx.soft.crawler.demo;

import java.util.List;

import zx.soft.crawler.domain.WeixinRecord;
import zx.soft.crawler.parser.ParserWeixinCore;
import zx.soft.crawler.utils.JsonUtils;

public class ParserWeixinDemo {

	public static void main(String[] args) {

		List<WeixinRecord> records = ParserWeixinCore.parserWeixinInfo("大数据");
		System.out.println(JsonUtils.toJson(records));

	}

}
