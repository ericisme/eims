/**
 * @title:Rss频道的读取与解析类.
 * @author:马必强
 * @modify:2008-1-8
 */

function RssReader(){};
RssReader.quiet = false; //设置为true,那么有错误也不会进行提示
RssReader.prototype= {
	/**
	 * 获取RSS并解析.该方法是综合了getRss和parseXmlToJSON两个方法,最终的回调函数的值为json对象.
	 */
	getAndParse : function(/*string*/_url, /*function*/callback) {
		var parseXml = this.parseRss;
		this.getRss(_url, function(rssXml) {
			var json = parseXml(rssXml);
			callback(json);
		});
	},
	
	/**
	 * 获取rss频道的xml文件内容，以xml对象返回.
	 * @param _url 要获取的RSS的URL地址
	 * @param callback 获取成功或失败后的回调方法，参数为RSS内容的XML对象（失败则返回null）
	 */
	getRss : function(/*string*/ _url, /*function*/callback) {
		new Ajax.Request(
			_url,
			{
				method : 'GET',
				onComplete : function(xmlHttp) {
					var rssXml = xmlHttp.responseXML;
					if (rssXml == null) {
						if (RssReader.quiet != true) alert('非法的RSS频道内容，无法解析！');
					}
					callback(rssXml);
				}
			}
		);
	},
	
	/**
	 * 将RSS频道的XML内容转换成JSON对象返回.
	 */
	parseRss : function(/*object*/rssXml) {
		try {
			var rss = rssXml.getElementsByTagName('rss');
			var channel = rssXml.getElementsByTagName('channel');
			if (!rss || !channel || rss.length != 1 || channel.length < 1) {
				if (RssReader.quiet != true) alert('无效的RSS格式：没有rss节点和channel节点！');
				return null;
			}
			var version = rss.item(0).getAttribute('version');
			if (!version) {
				if (RssReader.quiet != true) alert('无效的RSS格式：没有指定RSS版本号！');
				return null;
			}
			if (version == '2.0') return RssReader.parseRss2_0(channel.item(0));
			
			if (RssReader.quiet != true) alert('无效的RSS格式：无法确定RSS版本！');
			return null;
		} catch (ex) {
			if (RssReader.quiet != true) alert('解析RSS内容时发生错误：' + ex.message);
			return null;
		}
	}
};

/**
 * Rss2.0格式的静态解析方法，返回结果为JSON对象.
 * 返回的JSON格式为：
 * {"items" : [ {"title" : "标题", "link" : "连接地址"},
 *              {"title" : "标题", "link" : "连接地址"},
 *               {"title" : "标题", "link" : "连接地址"}
 *            ]
 * }
 * 使用方法为（假设最终返回的对象名称为json）：
 * json.items.length - 获取总的信息的条数
 * json.items[i] - 获取每一条信息的对象（其中0<=i<json.items.length）
 * json.items[i].title - 获取单条信息的标题文字
 * json.items[i].link - 获取单条信息的连接地址
 * json.items[i].desc - 获取单条信息的描述简介
 */
RssReader.parseRss2_0 = function(/*object*/channel) {
	var items = channel.getElementsByTagName('item');
	var json = '{"items":[';
	for (var i = 0; i < items.length; i ++) {
		var item = items.item(i);
		var _title = item.getElementsByTagName('title')[0];
		var _link = item.getElementsByTagName('link')[0];
		var _desc = item.getElementsByTagName('description')[0];
		if (!_title.hasChildNodes() || !_link.hasChildNodes()) continue;
		json += '{"title":"' + _title.firstChild.nodeValue.gsub(/"/,"'") + '",';
		json += '"link":"' + _link.firstChild.nodeValue.gsub(/"/,"'") + '",';
		json += '"desc":"' + _desc.firstChild.nodeValue.gsub(/"/, "'") + '"}';
		if (i < items.length - 1) json += ',';
	}
	json += ']}';
	//alert(json);
	return eval('(' + json + ')');
};