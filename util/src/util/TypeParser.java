package util;

import java.util.HashMap;
import java.util.Map;


public class TypeParser {
	private static final Map<String, Class<?>> typePool;
	static {
		typePool = new HashMap<String, Class<?>>();
		typePool.put("byte", byte.class);
		typePool.put("char", char.class);
		typePool.put("boolean", boolean.class);
		typePool.put("short", short.class);
		typePool.put("int", int.class);
		typePool.put("long", long.class);
		typePool.put("float", float.class);
		typePool.put("double", double.class);
		typePool.put("string", String.class);
	}
	
	public TypeParser() {
	}
	
	public static Class<?> strToClass(String str) throws ClassNotFoundException {
		Class<?> type = typePool.get(str);
		if (type == null) {
			type = Class.forName(str);
		}
		
		return type;
	}
	
	public static Object strToValue(Class<?> type, String strValue) {
		if (type.equals(byte.class)) {
			return Byte.valueOf(strValue);
		}
		if (type.equals(char.class)) {
			return strValue.charAt(0);
		}
		if (type.equals(boolean.class)) {
			return Boolean.valueOf(strValue);
		}
		if (type.equals(short.class)) {
			return Short.valueOf(strValue);
		}
		if (type.equals(int.class)) {
			return Integer.valueOf(strValue);
		}
		if (type.equals(long.class)) {
			return Long.valueOf(strValue);
		}
		if (type.equals(float.class)) {
			return Float.valueOf(strValue);
		}
		if (type.equals(double.class)) {
			return Double.valueOf(strValue);
		}
		if (type.equals(String.class)) {
			return strValue;
		}
		
		return null;
	}
	
	//在原有的byte数组上从buffer下标开始转化
		public static int bytesToInt(byte[] bytes, int buffer) {
			byte[] newByte= new byte[4];
	 		for(int i = 0; i < 4; i++) {
	 			newByte[i] = bytes[i + buffer];
	 		}
			return bytesToInt(newByte);
		}
		
		public static int bytesToInt(byte[] bytes) {
			int value = 0;
			for(int i = 0; i < 4; i ++) {
				value <<= 8;
				value |= bytes[i] & 0xFF; 
			}
			return value;
		}

		//这里是在指定的byte数组里从offset偏移量开始加入value的值的byte数组
		public static byte[] intToBytes(int value, byte[] bytes, int offset) {
			byte[] oldbytes = intToBytes(value);
			for(int i = 0; i < 4; i++) {
				//由分析得，offset + i 即需要添加的位置
				bytes[offset + i] = oldbytes[i];
			}
			return bytes;
		}
		
		
		
		public static byte[] intToBytes(int value) {
			byte[] bytes = new byte[4];
			//因为第一次不需要右移，如果使用每次都在前一次的基础上右移的话那么，对于第一次需要判断一下
			int temp = value;
			for(int i = 0; i < 4; i++) {
				temp = value >> ((3 -i) * 4);
				bytes[i] = (byte) (temp & 0xFF);
			}
			return bytes;
		}
	
	public static long bytesToLong(byte[] value, int offset) {
		byte[] temp = new byte[8];
		for (int index = 0; index < 8; index++) {
			temp[index] = value[index + offset];
		}
		
		return bytesToLong(temp);
	}
	
	public static long bytesToLong(byte[] value) {
		long result = 0;
		
		for (int index = 0; index < 8; index++) {
			result <<= 8;
			result |= value[index] & 0xFF;
		}
		
		return result;
	}

	public static byte[] longToBytes(long value, byte[] bytes, int offset) {
		byte[] bValue = longToBytes(value);
		
		for (int index = 0; index < 8; index++) {
			bytes[offset + index] = bValue[index];
		}
		
		return bytes;
	}
	
	public static byte[] longToBytes(long value) {
		byte[] bytes = new byte[8];
		long temp;
		
		for (int index = 0; index < 8; index++) {
			temp = value >> (index * 8);
			bytes[7 - index] = (byte) (temp & 0xFF);
		}
		
		return bytes;
	}
	
}
