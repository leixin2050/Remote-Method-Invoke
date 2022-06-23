package com.lw.rmi.client;

import com.lw.rmi.server.INodeAddress;
import com.lw.rmi.server.method.ReturnValue;
import util.ArgumentMaker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author shkstart
 * @create 2022-06-22 20:43
 * 建立通信信道并连接服务器并发送请求信息的类
 */
public class RmiClientActioner {
    private INodeAddress nodeAddress;
    private Method method;
    private Object[] args;
    private Object result;

    //由RmiClient初始化
    public RmiClientActioner(INodeAddress nodeAddress, Method method, Object[] args) {
        this.nodeAddress = nodeAddress;
        this.method = method;
        this.args = args;
    }

    /**
     * 通信信道的建立，及传输信息
     */
    public void doRmi() throws Exception {
        Socket client = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            client = new Socket(nodeAddress.getNodeAddress().getIp(), nodeAddress.getNodeAddress().getPort());
            dos = new DataOutputStream(client.getOutputStream());
            dis = new DataInputStream(client.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String methodId = method.toString().hashCode() + "";
        //对于参数的传递，需要转换为一个json字符串
        String argString = argsToJson(args);
        //转换完毕后发送请求调用信息
        dos.writeUTF(methodId);
        dos.writeUTF(argString);

        //发送完毕后等待服务器返回执行的结果
        String res = dis.readUTF();
        ReturnValue returnValue = ArgumentMaker.gson.fromJson(res, ReturnValue.class);
        if (returnValue.isOk()) {
            this.result = ArgumentMaker.gson.fromJson(returnValue.getResultStr(), method.getGenericReturnType());
        } else {
            System.out.println("远程方法调用失败！");
            client.close();
            throw returnValue.getE();
        }
        client.close();
    }

    public Object getResult() {
        return result;
    }

    //参数转换为json字符串
    private String argsToJson(Object[] args) {
        ArgumentMaker argumentMaker = new ArgumentMaker();
        for(int i = 0; i < args.length; i ++) {
            argumentMaker.addArg("arg" + i, args[i]);
        }
        return argumentMaker.toString();
    }
}
