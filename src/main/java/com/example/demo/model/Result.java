package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author liting fengkuangdejava@outlook.com
 * @Description 统一接口返回格式  格式示例为{code:200,msg:"OK",data:[1,2,3,4]}
 */
@ToString
public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

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
    @JsonIgnore
    public boolean isSuccess(){
        return ResultEnum.SUCCESS.getCode()==this.code;
    }
}
