package com.zyl.longrpc.utlis;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @program: long-rpc
 * @description: 配置工具类
 * @author: yl.zhan
 * @create: 2024-11-28 14:47
 **/
public class ConfigUtils {


    /**
     * @Description  不需要文件环境
     * @Date 2:56 PM 11/28/2024
     * @Author yl.zhan
     **/
    public static <T> T loadConfig(Class<T> tClass, String prefix){
        return loadConfig(tClass, prefix, "");
    }


        /**
         * @Description 使用hutool加载application 文件，返回对应的bean
         * @Date 2:55 PM 11/28/2024
         * @Author yl.zhan
         * @param tClass 配置类型
         * @param prefix 文件参数前置
         * @param environment 文件环境
         * @return T
         **/
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment){
        // 配置文件名
        StringBuilder configFileBuilder = new StringBuilder("application");

        // 环境不为空
        if(StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
//        configFileBuilder.append(".xml");
        configFileBuilder.append(".properties");
        // 使用hutool工具类 加载配置文件
        Props props = new Props(configFileBuilder.toString());
        // 加载成对应的bean ,指定前缀（prefix）
        return props.toBean(tClass, prefix);
    }

}
