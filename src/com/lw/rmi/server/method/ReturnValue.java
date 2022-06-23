package com.lw.rmi.server.method;

/**
 * @author leiWei
 * 执行方法的返回值
 */
public class ReturnValue {
    //执行结果的json字符串
    private String resultStr;
    //方法执行是否成功
    private boolean ok;
    //方法执行未成功的异常
    private Exception e;

    public ReturnValue() {
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }
}
