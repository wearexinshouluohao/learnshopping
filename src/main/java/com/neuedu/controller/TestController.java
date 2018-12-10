//package com.neuedu.controller;
//
//import com.neuedu.common.ServerResponse;
//import com.neuedu.pojo.UserInfo;
//import com.neuedu.service.IUserService;
//import com.neuedu.service.imp.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//
//
//@RestController   //@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
//@RequestMapping(value = "/portal/user/")
//public class TestController {
//    @Autowired
//    IUserService iUserService;
//
//
//@RequestMapping("login.do")
//    public ServerResponse login(UserInfo userInfo){
//
//
//        userInfo.setCreateTime(new Date());
//        userInfo.setUpdateTime(new Date());
//        userInfo.setRole(1);
//        return iUserService.register(userInfo);
//
//    }
//}
