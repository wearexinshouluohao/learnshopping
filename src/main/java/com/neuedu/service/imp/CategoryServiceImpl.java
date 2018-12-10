package com.neuedu.service.imp;

import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    //按categoryId查询类别
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //step1：非空校验
        if (categoryId==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2：按categoryId查询类别
        Category cate=categoryMapper.selectByPrimaryKey(categoryId);
        if (cate==null){
            return ServerResponse.createServerResponseByError("查询的类别不存在");
        }
        //step3：查询子类别
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        //step4：返回结果
        return ServerResponse.createServerResponseBySucess("成功",categoryList);
    }
    /**
     * 添加品类节点
     * */
    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
      //step1：参数效验
        if (categoryName==null || categoryName.equals("")){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
      //step2：添加节点
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(1);
        System.out.println("输出name"+category.getName());
        System.out.println("输出name"+ category.getStatus());
        System.out.println("输出name"+category.getParentId());
       int count= categoryMapper.insert(category);
        //step3： 返回结果
        if (count>0){
            return ServerResponse.createServerResponseBySucess();
        }
        return ServerResponse.createServerResponseByError("添加失败");
    }
//修改
    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //stept1：参数的非空校验
        if (categoryId==null || categoryId.equals("")){
            return ServerResponse.createServerResponseByError("类别id不能为空");
        }
        if (categoryName==null || categoryName.equals("")){
            return ServerResponse.createServerResponseByError("类别名不能为空");
        }
        //stept2：根据 categoryId查询
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.createServerResponseByError("没有查询到修改的类别");
        }
        //stept3：修改
        category.setName(categoryName);
           int count=categoryMapper.updateByPrimaryKey(category);
        //stept4：返回结果
        if (count>0){//修改成功
            return ServerResponse.createServerResponseBySucess();
        }
        return ServerResponse.createServerResponseByError("修改失败");
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //step1:参数的非空校验
        if (categoryId==null){
            return  ServerResponse.createServerResponseByError("类别id不能为空");
        }
        //step2：查询
        Set<Category> categorySet=Sets.newHashSet();
        categorySet=findAllChildCategory(categorySet,categoryId);

        Set<Category> integerSet=Sets.newHashSet();
        Iterator<Category> categoryIterator=categorySet.iterator();
        while (categoryIterator.hasNext()){
            Category category =categoryIterator.next();
            integerSet.add(category);
        }
        return ServerResponse.createServerResponseBySucess("成功了",integerSet);
    }

    private Set<Category> findAllChildCategory(Set<Category> categoriesSet,Integer categoryId){
        //查找本节点
        Category  category=   categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            categoriesSet.add(category);//id一样就一样
        }
        //查找categoryId下的子节点（平级）
        List<Category> categoryList=categoryMapper.findChildCategory(categoryId);
       if (categoryList!=null&& categoryList.size()>0){
           for (Category cc:categoryList) {
               findAllChildCategory(categoriesSet,cc.getId());
           }
       }

        return categoriesSet;
    }
}
