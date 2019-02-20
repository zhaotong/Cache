package com.tone.cache.dataprovider;

import java.io.Serializable;

public class DataResult<T> implements Serializable {

    public static final int CODE_CACHE_NULL = -1000;
    public static final int CODE_CACHE_SUCCESS = -1001;
    public static final int CODE_NETWORK_SUCCESS = -1002;
    public static final int CODE_NETWORK_NULL = -1003;


    // 返回的描述
    private int code;
    // 返回码
    private String msg;
    // 返回的数据
    private T data;


    public DataResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
