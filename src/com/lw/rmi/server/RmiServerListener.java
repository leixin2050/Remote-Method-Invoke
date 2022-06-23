package com.lw.rmi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author LeiWei
 * 真正开启服务器，与开始侦听客户端连接的实现
 */
public class RmiServerListener implements Runnable {
    private ServerSocket server;
    private int port;
    //决定是否继续侦听客户端连接，使用volatile避免寄存器优化，即让线程用此变量，保证数据的一致性与原子性
    private volatile boolean goon;

    //lock对象锁，主要目的是为了保证在成功开启服务器前不允许其他操作
    private volatile Object lock;

    private ThreadPoolExecutor threadPool;

    public RmiServerListener(int port){
        this.port = port;
        this.goon = false;
        this.lock = new Object();
    }

    public boolean isStartUp() {
        return goon;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPool = threadPoolExecutor;
    }

    public void startUp() throws IOException {
        this.server = new ServerSocket(this.port);
        this.goon = true;
        synchronized(this.lock) {
            try {
                this.threadPool.execute(this);
                //阻塞线程，在成功开启服务器时唤醒此线程
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //以线程的方式来侦听客户端的连接
    @Override
    public void run() {
        synchronized (this.lock) {
            //唤醒线程，保证服务器的正常开启
            lock.notify();
        }

        while (this.goon) {
            try {
                Socket client = this.server.accept();
                //监听到客户端连接后启动线程处理客户端连接 RmiServerActioner
                threadPool.execute(new RmiServerActioner(client));
            } catch (IOException e) {
                //发生异常后，立刻关闭相关服务器
                this.goon = false;
            }
        }
    }

    void close() {
        this.goon = false;

        //关闭服务器
        if (this.server != null || !this.server.isClosed()) {
            try {
                this.server.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.server = null;
            }
        }
    }
}
