package com.lw.rmi.server.method;

import java.lang.reflect.Method;

/**
 * @author shkstart
 * 存储接口方法的类
 */
public class MethodDefinition {
    private Method method;
    //方法反射调用时得到
    private Object object;

    public MethodDefinition() {
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
