package util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ArgumentMaker {
	private static final Type type = new TypeToken<Map<String, String>>() {}.getType();
	public static final Gson gson = new GsonBuilder().create();
	private Map<String, String> argPool;
	
	public ArgumentMaker() {
		this.argPool = new HashMap<String, String>();
	}
	
	public ArgumentMaker(String json) {
		this.argPool = gson.fromJson(json, type);
	}
	
	public int getArgumentCount() {
		return this.argPool.size();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getArg(String name, Class<?> type) {
		String strValue = this.argPool.get(name);
		return (T) gson.fromJson(strValue, type);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getArg(String name, Type type) {
		String strValue = this.argPool.get(name);
		return (T) gson.fromJson(strValue, type);
	}
	
	public ArgumentMaker addArg(String argName, Object argValue) {
		this.argPool.put(argName, gson.toJson(argValue));
		
		return this;
	}

	@Override
	public String toString() {
		return gson.toJson(this.argPool);
	}
	
}
