package com.lw.rmi.server;

import util.IListener;
import util.ISpeaker;
import util.PropertiesParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author shkstart
 * @create 2022-06-22 21:03
 * 提供给用户使用的服务器端
 * 服务器的建立,但是服务器的真正开启在RmiServerListener中
 */
//ISpeaker侦听者模式，可以传输想要发送的信息给侦听者(IListener)
public class RmiServer implements ISpeaker {
    public static final int DEFAULT_RMI_SERVER_PORT = 54188;
    //服务器端口号
    private int port;
    //倾听者列表，主要用来输出服务器相关信息
    private List<IListener> listenerList;
    //开启Rmi服务器以及侦听客户端连接
    private RmiServerListener rmiServerListener;
    //线程池，由外部设置进来
    private ThreadPoolExecutor threadPool;

    public RmiServer() {
        this(DEFAULT_RMI_SERVER_PORT);
    }

    public RmiServer(int port) {
        this.port = port;
        listenerList = new ArrayList<>();
        this.rmiServerListener = new RmiServerListener(port);
    }

    //设置线程池
    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPool = threadPoolExecutor;
    }

    //允许set进来服务器端口号
    public void setPort(int port) {
        this.port = port;
    }

    //允许读取配置文件获得端口号
    public void ParserConifg(String path) {
        PropertiesParser.load(path);
        int intValue = 0;
        try {
            intValue = PropertiesParser.get("rmi_server_port", int.class);
            setPort(intValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //开启Rmi服务器
    public void startUp() throws IOException {
        if (isStartUp()) {
            speakOut("Rmi服务器已开启，无需开启");
        }
        speakOut("开启Rmi服务器...");
        this.rmiServerListener.setThreadPoolExecutor(this.threadPool);
        this.rmiServerListener.startUp();
        speakOut("Rmi服务器开启成功...");
    }

    public void shutDown() {
        if(!this.rmiServerListener.isStartUp()) {
            speakOut("RMI服务器未开启！无需宕机");
            return;
        }
        this.rmiServerListener.close();
        //以防报错不知名错误，等关闭服务器后再关闭线程池
        this.threadPool.shutdown();
        speakOut("RMI服务器已宕机！");
    }

    public boolean isStartUp() {
        return this.rmiServerListener.isStartUp();
    }

    //添加倾听者
    @Override
    public void addListener(IListener listener) {
        if (this.listenerList.contains(listener)) {
            return;
        }
        this.listenerList.add(listener);
    }

    @Override
    public void removeListener(IListener listener) {
        if (this.listenerList.contains(listener)) {
            this.listenerList.remove(listener);
        }
    }

    @Override
    public void speakOut(String message) {
        for (IListener listener : this.listenerList) {
            listener.dealMessage(message);
        }
    }


}
