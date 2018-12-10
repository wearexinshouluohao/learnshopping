package com.neuedu.service.imp;

import com.neuedu.common.*;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserInfoMapper userInfoMapper;
    /**
     *注册
     **/
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //step1：参数非空校验
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("参数为空");
        }
        //step2：效验用户名
        //int result=userInfoMapper.checkUsername(userInfo.getUsername());
//        if (result>0){//用户有了
//            return  ServerResponse.createServerResponseByError("用户名已存在");
//        }
        ServerResponse serverResponse=chaeck_valid(userInfo.getUsername(),Const.USERNAME);
        if (!serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError("用户名已存在");
        }


        //step3：效验邮箱
//        int resultemail=userInfoMapper.checkEmail(userInfo.getEmail());
//        if (resultemail>0){//邮箱有了
//            return  ServerResponse.createServerResponseByError("邮箱已注册");
//        }
        ServerResponse emailserverResponse=chaeck_valid(userInfo.getEmail(),Const.EMAIL);
        if (!emailserverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError("邮箱已注册");
        }



        //step4:注册
        userInfo.setRole(ConstCode.RoleEnum.ROLE_CUSTOMER.getCode());//给他个等级
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));//密码加密
        int count=userInfoMapper.insert(userInfo);
        //step5：返回结果
        if (count>0){
            return ServerResponse.createServerResponseBySucess("注册成功");
        }

            return ServerResponse.createServerResponseByError("注册失败");
    }


    /**
     * 登录
     * */
//因为在apache提供的commons-lang包中定义了方法  StringUtils.isBlank()  和 StringUtils.isEmpty()
    //StringUtils.isBlank()能判断空格为空
    @Override
    public ServerResponse login(String username, String password) {
        //step1：参数非空效验     如果是ture，这个值就是空的
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByError("用户名不能为空");
        }
        if (StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByError("密码不能为空");
        }

        //step2：检查username是否存在   查询count（*） 大于0就有 <=0 就没有
//      int result=userInfoMapper.checkUsername(username);
//        if (result<=0){//用户名没有
//            return  ServerResponse.createServerResponseByError("用户名不存在");
//        }
        ServerResponse serverResponse=chaeck_valid(username,Const.USERNAME);
        if (serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError("用户名不存在");
        }

        //step3：根据用户名和密码查询                                             密码也加密进行对比
      UserInfo userInfo= userInfoMapper.selectUserByUsernameAndPassword(username,MD5Utils.getMD5Code(password));
        if (userInfo==null){//密码错误
            return ServerResponse.createServerResponseByError("密码错误");
        }
        //step4：处理结果并返回
        userInfo.setPassword("");//因为要查询出来，不能显示密码
        return ServerResponse.createServerResponseBySucess(null,userInfo);
    }

    /**
     * 用户名和邮箱是否有效
     * */
    @Override                       //      姓名或者邮箱        参数
    public ServerResponse chaeck_valid(String str, String type) {
        //step1：参数非空效验
        if (StringUtils.isBlank(str) || StringUtils.isBlank(type)){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2：判断有用户名或这邮箱是否存在
        if (type.equals(Const.USERNAME)){
            int username_result=userInfoMapper.checkUsername(str);
            if (username_result>0){
                return ServerResponse.createServerResponseByError(3,"用户名已存在");
            }
            return ServerResponse.createServerResponseBySucess("成功");
        }else if (type.equals(Const.EMAIL)){
            int email_result=userInfoMapper.checkEmail(str);
            if (email_result>0){
                return ServerResponse.createServerResponseByError(4,"邮箱已存在");
            }
            return ServerResponse.createServerResponseBySucess("成功");
        }
        //step3：返回结果
        return ServerResponse.createServerResponseByError("参数有误");
    }

    @Override
    public ServerResponse forget_get_question(String username) {
        //step1：参数非空校验
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByError("用户名不能为空");
        }
        //step2：判断用户是否存在
      ServerResponse  serverResponse=  chaeck_valid(username,Const.USERNAME);//type:username
        if (serverResponse.getStatus()!=ResponseCode.EXISTS_USERNAME.getStatus()){//ResponseCode.EXISTS_USERNAME.getStatus()=3 用户名存在
            return serverResponse;
        }
        //step3：查询密保问题
        String question=userInfoMapper.selectQuestionByusername(username);
        if (StringUtils.isBlank(question)){
            return ServerResponse.createServerResponseByError("密保问题为空");
        }
        //返回结果
        return ServerResponse.createServerResponseBySucess(null,question);
    }
// 密保
    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //step1：参数的非空校验
        if (StringUtils.isBlank(username) || StringUtils.isBlank(question) || StringUtils.isBlank(answer)){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2：校验答案

        //sql --> select count（1） from username=    and   question  and  answer

        int count=userInfoMapper.checkAnswerByUsernameQuestion(username,question,answer);
        if (count<=0){
            return ServerResponse.createServerResponseByError("答案错误");
        }
        //返回一个用户的唯一标识    username  ---> token
                                  //每次都是唯一的字符串
        String use_token= UUID.randomUUID().toString();
        TokenCache.put(username,use_token);

        //step3：返回结果
        return ServerResponse.createServerResponseBySucess(null,use_token);
    }


    //忘记密码的重置
    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew,String forgetToken) {
        //step1：参数非空校验
        if (StringUtils.isBlank(username) || StringUtils.isBlank(passwordNew) ||StringUtils.isBlank(forgetToken)){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step3 校验 forgetToken
        System.out.println(username);
         String token=TokenCache.get(username);
        System.out.println("token值："+token);
        if (StringUtils.isBlank(token)){
            return  ServerResponse.createServerResponseByError("token不存在/或过期");
        }
        if (!token.equals(forgetToken)){
            return  ServerResponse.createServerResponseByError("token不一致,不能修改他人密码");
        }
        //step2：更新密码
      int count=userInfoMapper.updatePasswordByusername(username,MD5Utils.getMD5Code(passwordNew));
        if (count<=0){
            return ServerResponse.createServerResponseByError("修改失败");
        }
        //返回结果

        return ServerResponse.createServerResponseBySucess();
    }
//在登录状态下进行修改
    @Override
    public ServerResponse reset_password(UserInfo userInfo, String passwordOld, String passwordNew) {
        //step1：参数的非空校验
        if (StringUtils.isBlank(passwordOld) || StringUtils.isBlank(passwordNew)) {
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2：校验旧密码是否正确      防止用户横向越权
        UserInfo user = userInfoMapper.selectUserByUsernameAndPassword(userInfo.getUsername(),MD5Utils.getMD5Code(passwordOld));
        if (user == null) {
            return ServerResponse.createServerResponseByError("旧密码错误");
        }
        //step3：修密码
        int count = userInfoMapper.updatePasswordByusername(user.getUsername(), MD5Utils.getMD5Code(passwordNew));
            //step4：返回结果
            return ServerResponse.createServerResponseBySucess();
        }
}
