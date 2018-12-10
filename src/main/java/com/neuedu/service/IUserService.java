package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;

import javax.servlet.http.HttpSession;

public interface IUserService {
    /**
     * 注册
     */
    public ServerResponse register(UserInfo userInfo);

    /***
     * 登录
     */
    public ServerResponse login(String username, String password);

    //判断用户名和邮箱是否有效
    public ServerResponse chaeck_valid(String str, String type);

    //根据用户获取密保问题
    public ServerResponse forget_get_question(String username);
    //提交答案
    ServerResponse forget_check_answer(String username, String question, String answer);
    //忘记密码的重置密码
    public ServerResponse forget_reset_password(String username,String passwordNew,String forgetToken);
   //下登录状态下进行修改密码
   public ServerResponse reset_password(UserInfo userInfo, String passwordOld, String passwordNew);
}
