package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 封装返回前端的高复用对象
 * */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {
    //状态码
    private int status;
    // 接口提示信息
    private String msg;
    //返回接口数据
    private T data;

    private ServerResponse(){}
    private ServerResponse(int status){
        this.status=status;
        this.data=data;
    }
    private ServerResponse(int status,T data){
       this.status=status;
       this.data=data;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }


    /**
     * 判断接口是否调用成功
     * */
    // 自动扫描isSuccess这个类，生成一个实体类Success，有get和set方法，所以就出现了success这个字符串
    @JsonIgnore   //忽略调success这个字段
    public  boolean isSuccess(){
        return   this.status==Const.SUCCESS_CODE;
    }



    /**
    * 成功 （对外界提供的）
    * */
   public static ServerResponse createServerResponseBySucess(){
        return  new ServerResponse(Const.SUCCESS_CODE);
    }

    public static ServerResponse createServerResponseBySucess(String msg){
        return  new ServerResponse(Const.SUCCESS_CODE,msg);
    }

    public static <T> ServerResponse createServerResponseBySucess(String msg,T data){
        return  new ServerResponse(Const.SUCCESS_CODE,msg,data);
    }


/*
* 失败（对外界提供的）
*
* **/
    public  static ServerResponse createServerResponseByError(){
        return new ServerResponse(Const.SUCCESS_ERROR);
     }

    public  static ServerResponse createServerResponseByError(String mag){
        return new ServerResponse(Const.SUCCESS_ERROR,mag);
    }
//自定义的一个状态码
    public  static ServerResponse createServerResponseByError(int status){
        return new ServerResponse(status);
   }

//返回状态
    public  static ServerResponse createServerResponseByError(int status,String msg){
        return new ServerResponse(status,msg);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
