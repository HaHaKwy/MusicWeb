package com.nju.pojo;

//resp.data:
public class ResponseBody<T>{
    private int status;     //约定的状态码（不是http状态码） 0:正常 -1:错误 ......待定
    private String message; //状态描述信息
    private T content;         //返回的数据

    public ResponseBody(int status, String message, T content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getContent() {
        return content;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setContent(T content) {
        this.content = content;
    }
}