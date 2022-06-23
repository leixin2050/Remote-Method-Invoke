package util;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class XMLParser {
	private static DocumentBuilder documentBuilder;
	static {		// 静态块	JVM在加载一个类时，会先执行静态块中的代码，且只执行一次！
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();		
		}	
	}
	
	public abstract void dealElement(Element element, int index) throws Exception;
	
	public static Document getDocument(String XMLPath) throws Exception {
		InputStream is = XMLParser.class.getResourceAsStream(XMLPath);
		if (is == null) {
			throw new Exception("XML[" + XMLPath + "]文件不存在！");
		}
		
		return documentBuilder.parse(is);	
	}
	
	public void parse(Document document, String tagName) throws Exception {
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int index = 0; index < nodeList.getLength(); index++) {
			Element element = (Element) nodeList.item(index);
			dealElement(element, index);
		}	
	}
	
	public void parse(Element element, String tagName) throws Exception {
		NodeList nodeList = element.getElementsByTagName(tagName);
		for (int index = 0; index < nodeList.getLength(); index++) {
			Element ele = (Element) nodeList.item(index);
			dealElement(ele, index);		
		}	
	}
	
	public XMLParser() {
	}
	
}
