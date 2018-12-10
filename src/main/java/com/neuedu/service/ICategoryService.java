package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


public interface ICategoryService {
    /**
     * 获取品类的子节点（平级
     */
    ServerResponse get_category(Integer categoryId);

    /**
     * 添加品类的子节点
     */
    ServerResponse add_category(Integer parentId, String categoryName);

    /**
     * 修改节点
     */
    ServerResponse set_category_name(Integer categoryId, String categoryName);
    /**
     * 获取当前分类id及递归子节点categoryId
     * */
    ServerResponse get_deep_category(Integer categoryId);
}