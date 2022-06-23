package com.lw.rmi.server;

import com.lw.rmi.server.method.MethodDefinition;
import com.lw.rmi.server.method.MethodFactory;
import com.lw.rmi.server.method.MethodNotFindException;
import com.lw.rmi.server.method.ReturnValue;
import util.ArgumentMaker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.Socket;

/**
 * @author shkstart
 * 侦听到的客户端连接后，创建通信信道，接收与处理发送端发送的请求信息，返回结果给客户端
 */
public class RmiServerActioner implements Runnable{
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket client;

    public RmiServerActioner(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            this.dis = new DataInputStream(this.client.getInputStream());
            this.dos = new DataOutputStream(this.client.getOutputStream());

            //接收客户端发送的字符串
            String methodIdStr = dis.readUTF();
            //方法id得到后，通过取出容器中所存储的方法映射实例，借鉴了SpringIOC的思想
            Integer methodId = Integer.valueOf(methodIdStr);
            MethodDefinition methodDefinition = MethodFactory.methodPool.get(methodId);
            ReturnValue returnValue = new ReturnValue();
            try {
                if (methodDefinition == null) {
                        throw new MethodNotFindException("方法【" + methodIdStr + "】未找到！");
                }
                //准备实参
                String argsString = dis.readUTF();

                Object[] args = jsonToArgs(methodDefinition.getMethod(), argsString);

                //执行方法
                Method method = methodDefinition.getMethod();
                Object object = methodDefinition.getObject();

                //但是如果指仅做返回值为Object，无法获得方法执行的真实情况，
                // 故添加一个返回值类，包含方法执行结果，方法是否执行成功，以及执行不成功的异常是什么
                Object result = method.invoke(object, args);
                //返回值构建
                String returnStr = ArgumentMaker.gson.toJson(result);
                returnValue.setOk(true);
                returnValue.setResultStr(returnStr);
            } catch (Exception e) {
                close();
                returnValue.setOk(false);
                returnValue.setE(e);
            }
            dos.writeUTF(ArgumentMaker.gson.toJson(returnValue));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        if(this.dis == null) {
            try {
                this.dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.dis = null;
            }
        }
        if(this.dos == null) {
            try {
                this.dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.dos = null;
            }
        }
        if(this.client == null) {
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.client = null;
            }
        }
    }

    private Object[] jsonToArgs(Method method, String argsString) {
        ArgumentMaker argumentMaker = new ArgumentMaker(argsString);
        Parameter[] parameters = method.getParameters();
        int parametersCount = parameters.length;

        if(parametersCount <= 0) {
            return new Object[] {};
        }
        Object[] args = new Object[parametersCount];

        for(int i = 0; i < parametersCount; i++) {
            Type type = parameters[i].getParameterizedType();
            args[i] = argumentMaker.getArg("arg" + i, type);
        }

        return args;
    }
}
