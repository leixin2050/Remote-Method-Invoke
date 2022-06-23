package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertiesParser {
	private final static Map<String, String> propertyPool;
	static {
		propertyPool = new HashMap<String, String>();
	}
	
	public static void load(String propertiesPath) {
		InputStream is = PropertiesParser.class.getResourceAsStream(propertiesPath);
		if (is == null) {
			throw new RuntimeException("Properties文件" + propertiesPath + "不存在");
		}
		
		try {
			Properties properties = new Properties();
			properties.load(is);
			for (Object objKey : properties.keySet()) {
				String key = (String) objKey;
				String value = properties.getProperty(key);
				
				propertyPool.put(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<?> klass) throws Exception {
		String strValue = PropertiesParser.get(key);
		if (strValue == null || strValue.length() <= 0) {
			throw new Exception("键[" + key + "]不存在或未赋值！");
		}

		return (T) TypeParser.strToValue(klass, strValue);
	}
	
	public static String get(String key) {
		return propertyPool.get(key);
	}
	
	public static List<String> getKeyList() {
		if (propertyPool.isEmpty()) {
			return null;
		}
		
		List<String> keyList = new ArrayList<String>();
		for (String key : propertyPool.keySet()) {
			keyList.add(key);
		}
		
		return keyList;
	}
	
}
