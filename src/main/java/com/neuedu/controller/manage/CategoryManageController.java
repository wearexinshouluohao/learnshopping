package com.neuedu.controller.manage;


import com.neuedu.common.ConstCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value ="/manage/category/")
public class CategoryManageController {

     @Autowired
    ICategoryService iCategoryService;

    /**
     * 获取品类的子节点（平级
     * */
    @RequestMapping(value = "get_category.do")
    public ServerResponse get_category(HttpSession session, Integer categoryId){
        UserInfo userInfo =(UserInfo)session.getAttribute(ConstCode.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.createServerResponseByError(ConstCode.ReponseCodeEnum.NEED_LOGIN.getCode(),ConstCode.ReponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=ConstCode.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(ConstCode.ReponseCodeEnum.NO_PRIVILEGE.getCode(),ConstCode.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return iCategoryService.get_category(categoryId);
    }
    /**
     * 增加品类的子节点（平级
     * */
    @RequestMapping(value = "add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false,defaultValue = "0") Integer parentId
                                        ,String categoryName){
        System.out.println("我过来了"+parentId);
        System.out.println("我也来了"+categoryName);

        UserInfo userInfo =(UserInfo)session.getAttribute(ConstCode.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.createServerResponseByError(ConstCode.ReponseCodeEnum.NEED_LOGIN.getCode(),ConstCode.ReponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=ConstCode.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(ConstCode.ReponseCodeEnum.NO_PRIVILEGE.getCode(),ConstCode.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        return iCategoryService.add_category(parentId,categoryName);
    }
    /**
     * 修改节点 set_category_name.do
     * */
    @RequestMapping(value = "set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,
                                             Integer categoryId
                                             ,String categoryName){

        UserInfo userInfo =(UserInfo)session.getAttribute(ConstCode.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.createServerResponseByError(ConstCode.ReponseCodeEnum.NEED_LOGIN.getCode(),ConstCode.ReponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=ConstCode.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(ConstCode.ReponseCodeEnum.NO_PRIVILEGE.getCode(),ConstCode.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return iCategoryService.set_category_name(categoryId,categoryName);
    }

    /**
     * 获取当前分类id及地柜子节点categoryId
     * */

    @RequestMapping(value = "get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session,
                                            Integer categoryId){

        UserInfo userInfo =(UserInfo)session.getAttribute(ConstCode.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.createServerResponseByError(ConstCode.ReponseCodeEnum.NEED_LOGIN.getCode(),ConstCode.ReponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=ConstCode.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByError(ConstCode.ReponseCodeEnum.NO_PRIVILEGE.getCode(),ConstCode.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return iCategoryService.get_deep_category(categoryId);
    }

























}
