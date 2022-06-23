package util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	public static final int DEFAULT_CORE_POOL_SIZE = 10;
	public static final int DEFAULT_MAXIMUM_POOL_SIZE = 100;
	public static final long DEFAULT_KEEP_ALIVE_TIME = 10000;

	private int corePoolSize;
	private int maximumPoolSize;
	private long keepAliveTime;

	public ThreadPool() {
		this.corePoolSize = DEFAULT_CORE_POOL_SIZE;
		this.maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE;
		this.keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
	}
	
	public ThreadPoolExecutor createThreadPool() {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, 
				TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
		return threadPool;
	}
	
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
	
	public void loadThreadPoolConfig(String configPath) {
		PropertiesParser.load(configPath);
		
		int intValue = 0;
		int longValue = 0;
		
		try {
			intValue = PropertiesParser.get("core_pool_size", int.class);
			if (intValue > 0) {
				setCorePoolSize(intValue);
			}
		} catch (Exception e) {
		}
		try {
			intValue = PropertiesParser.get("max_pool_size", int.class);
			if (intValue > 0) {
				setMaximumPoolSize(intValue);
			}
		} catch (Exception e) {
		}
		try {
			longValue = PropertiesParser.get("keep_alive_time", long.class);
			if (longValue > 0) {
				setKeepAliveTime(longValue);
			}
		} catch (Exception e) {
		}
	}
	
}
