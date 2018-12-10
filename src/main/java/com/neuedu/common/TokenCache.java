package com.neuedu.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TokenCache {
    //缓存类
    private static LoadingCache<String,String> loadingCache=CacheBuilder.newBuilder()
            //初始化缓存为1000
            .initialCapacity(1000)
            //最大返回项
            .maximumSize(10000)
            //过期时间  12小时后自动清空
            .expireAfterAccess(12,TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //当不存在时 调用该方法
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    //向缓存添加键值对
    public static void put(String key,String value){
        loadingCache.put(key, value);
    }
    //获取缓存值
    public static String get(String key){
        try {
            String value=loadingCache.get(key);
            if (value.equals("null")){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }





}
