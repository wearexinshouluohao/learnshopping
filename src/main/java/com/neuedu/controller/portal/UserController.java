package com.neuedu.controller.portal;

import com.neuedu.common.ConstCode;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/user/")
public class UserController {
    @Autowired
    IUserService userService;

    /**
     * 登录
     * */
    @RequestMapping(value = "login.do")
    public ServerResponse login(HttpSession session, String username, String password){
        ServerResponse serverResponse=userService.login(username,password);
        if (serverResponse.isSuccess()){//登录状态
            UserInfo userInfo=(UserInfo) serverResponse.getData();
            //登录成功了放进去session里
            session.setAttribute(ConstCode.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }


    /**
     * 注册
     * */
    @RequestMapping(value = "register.do")             //对象绑定
    public ServerResponse register(UserInfo userInfo){
        ServerResponse serverResponse=userService.register(userInfo);
        return serverResponse;
    }


    /**
     * 用户名和邮箱是否有效
     * */
    @RequestMapping(value = "chaeck_valid.do")
    public ServerResponse chaeck_valid(String str, String type){
        return userService.chaeck_valid(str, type);
    }
    /**
     * 获取用户的信息
     * */
    @RequestMapping(value = "get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
          Object o=session.getAttribute(ConstCode.CURRENTUSER);
          if (o!=null && o instanceof UserInfo){
              UserInfo userInfo=(UserInfo) o;
              UserInfo user=new UserInfo();
              user.setId(userInfo.getId());
              user.setUsername(userInfo.getUsername());
              user.setEmail(userInfo.getEmail());
              user.setUpdateTime(userInfo.getUpdateTime());
              user.setCreateTime(userInfo.getCreateTime());
              return ServerResponse.createServerResponseBySucess(null,user);
          }
        return ServerResponse.createServerResponseByError("用户未登录");
    }
    /**
     * 获取详细信息
     * */
    @RequestMapping(value = "get_inforamtion.do")
    public ServerResponse get_inforamtion(HttpSession session){
        Object o=session.getAttribute(ConstCode.CURRENTUSER);
        if (o!=null && o instanceof UserInfo){
            UserInfo userInfo=(UserInfo) o;

            return ServerResponse.createServerResponseBySucess(null,userInfo);

        }
        return ServerResponse.createServerResponseByError("用户未登录");
    }


    /**
     * 根据用户名获取密保问题
     *
     * */
    @RequestMapping("forget_get_question.do")
    public ServerResponse forget_get_question(String username){
        return userService.forget_get_question(username);
    }
    /**
     *
     * 提交问题答案接口
     */
    @RequestMapping(value = "forget_check_answer.do")
    public ServerResponse forget_check_answer(String username,String question,String answer){
        return userService.forget_check_answer(username,question,answer);
    }
    /**
     * 修改密码
     * */
    @RequestMapping(value = "forget_reset_password.do")
    public ServerResponse forget_reset_password(String username,String passwordNew,String forgetToken){

        System.out.println("名字："+username);
        System.out.println("名字："+passwordNew);
        System.out.println("名字："+forgetToken);
     return userService.forget_reset_password(username,passwordNew,forgetToken);
    }

     /**
      * 在登录状态下进行修改密码
      * */
     @RequestMapping(value = "reset_password.do")
                                           // 获取用户信息
     public ServerResponse reset_password(HttpSession session,String passwordOld,String passwordNew){
           //试试能提取出username
       Object object=session.getAttribute(ConstCode.CURRENTUSER);
       if (object!=null&& object instanceof UserInfo){
           UserInfo userInfo=(UserInfo) object;
           return  userService.reset_password(userInfo,passwordOld,passwordNew);
       }
      return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
     }













    /**
     * 退出登录
     * */
    @RequestMapping(value = "logout.do")
    public ServerResponse logout(HttpSession session){
       session.removeAttribute(ConstCode.CURRENTUSER);
      return ServerResponse.createServerResponseBySucess();
    }


}
