package com.lw.rmi.server.method;

import org.w3c.dom.Element;
import util.XMLParser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * 方法工厂，用来存储与获取对应的方法
 */
public class MethodFactory {
    //方法的hashId与存储的methodDefinition类的映射关系
    public static Map<Integer, MethodDefinition> methodPool;
    static {
        methodPool = new HashMap<>();
    }

    public MethodFactory() {
    }

    //XML解析得到接口方法池,以及接口的实现类对象
    public static void XMLConfigPaser(String pathName) throws Exception {
        new XMLParser() {
            @Override
            public void dealElement(Element element, int index) throws Exception {
                String interfaceName = element.getAttribute("inteface");
                String className = element.getAttribute("class");

                Class<?> interfase = Class.forName(interfaceName);
                //接口的实现类，作用在于提供对象，供之后远程调用方法
                Class<?> klass = Class.forName(className);
                Object object = klass.newInstance();

                Method[] methods = interfase.getDeclaredMethods();
                for (Method method : methods) {
                    //获取methodPool键
                    Integer methodId = method.toString().hashCode();
                    //获取methodPool值
                    MethodDefinition methodDefinition = new MethodDefinition();
                    methodDefinition.setMethod(method);
                    methodDefinition.setObject(object);

                    methodPool.put(methodId, methodDefinition);
                }
            }
        }.parse(XMLParser.getDocument(pathName), "implement");
    }

}
