package com.lw.rmi.client;

import com.lw.rmi.server.INodeAddress;
import com.lw.rmi.server.ModelDialog;

import javax.swing.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author shkstart
 * @create 2022-06-22 21:30
 * Rmi客户端
 */
public class RmiClient {
    //通过接口得到服务器的ip地址和port
    private INodeAddress nodeAddress;

    //用户选取服务器地址创建客户端
    public RmiClient(INodeAddress nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    /**
     * 1、模态框进行RMI请求
     * @param parent 父窗口
     * @param title 模态框标题
     * @param interfase 请求的方法的接口
     * @param <T> 方法执行后的返回值
     * @return
     */
    public <T> T getJDKProxy(JFrame parent, String title, Class<?> interfase) {
        ClassLoader classLoader = interfase.getClassLoader();
        Class<?>[] interfaces = new Class<?>[]{interfase};

        return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RmiClientActioner rmiClientActioner = new RmiClientActioner(nodeAddress, method, args);
                //设置模态框，并且再显示模态框后进行RMI请求
                ModelDialog modelDialog = new ModelDialog(parent, title);
                modelDialog.setRmiClientActioner(rmiClientActioner);
                modelDialog.showDialog();

                Object result = rmiClientActioner.getResult();
                return (T)result;
            }
        });
    }

    //2、未提供窗口界面时不使用模态框的方式，直接进行RMI请求
    public <T> T getJDKProxy(Class<?> interfase) {
        ClassLoader classLoader = interfase.getClassLoader();
        Class<?>[] interfaces = new Class<?>[]{interfase};

        return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RmiClientActioner rmiClientActioner = new RmiClientActioner(nodeAddress, method, args);
                //设置模态框，并且再显示模态框后进行RMI请求
                rmiClientActioner.doRmi();
                Object result = rmiClientActioner.getResult();
                return (T)result;
            }
        });
    }
}
