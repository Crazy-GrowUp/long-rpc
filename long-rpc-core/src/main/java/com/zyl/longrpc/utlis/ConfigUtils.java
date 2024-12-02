package com.zyl.longrpc.utlis;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;

import java.nio.charset.Charset;

/**
 * @program: long-rpc
 * @description: 配置工具类
 * @author: yl.zhan
 * @create: 2024-11-28 14:47
 **/
public class ConfigUtils {


    /**
     * @Description 不需要文件环境
     * @Date 2:56 PM 11/28/2024
     * @Author yl.zhan
     **/
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }


    /**
     * @param tClass      配置类型
     * @param prefix      文件参数前置
     * @param environment 文件环境
     * @return T
     * @Description 使用hutool加载application 文件，返回对应的bean
     * @Date 2:55 PM 11/28/2024
     * @Author yl.zhan
     **/
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        // 配置文件名
        StringBuilder configFileBuilder = new StringBuilder("application");

        // 环境不为空
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        String configFilePrefix = configFileBuilder.toString();
        //yml yaml properties xml


//        configFileBuilder.append(".xml");
//        configFileBuilder.append(".properties");
//        // 使用hutool工具类 加载配置文件
//        Props props = new Props(configFileBuilder.toString());
//        // 加载成对应的bean ,指定前缀（prefix）
//        return props.toBean(tClass, prefix);
        return loadFileProp(tClass, configFilePrefix, prefix);
        //读取YML
//        return loadFile(tClass, configFilePrefix, prefix);
    }


    private static <T> T loadFile(Class<T> tClass, String fileName, String prefix) {
        String fileNameYml = fileName + ".yml";
        System.out.println("fileName:" + fileName);
        System.out.println("prefix:" + prefix);
        try {


            Props props = new Props(fileNameYml);
            T bean1 = props.toBean(tClass, prefix);

            System.out.println("bean1:"+bean1);


            // 设置YAML文件的路径
//            String yamlPath = "config.yaml";

            // 使用Hutool的ResourceUtil读取YAML文件
            Setting setting = new Setting(ResourceUtil.getResourceObj(fileNameYml), Charset.defaultCharset(), false);

            // 设置前缀为person
//            String prefix = "person.";
//            prefix += ".";
            T bean = setting.toBean(prefix, tClass);
            System.out.println("bean:" + bean.toString());
            // 将配置转换为Person对象
            return setting.toBean(prefix, tClass);
//            Resource resourceObj = ResourceUtil.getResourceObj(fileNameYml);
//            return YamlUtil.load(resourceObj.getStream(), tClass);
        } catch (Exception e) {
            System.out.println("yml::" + e.getMessage());
            return loadFileYaml(tClass, fileName, prefix);
        }
    }

    private static <T> T loadFileYaml(Class<T> tClass, String fileName, String prefix) {
        String fileNameYaml = fileName + ".yaml";
        System.out.println("fileNameYaml:" + fileNameYaml);
        try {
            // 使用Hutool的ResourceUtil读取YAML文件
            Setting setting = new Setting(ResourceUtil.getResourceObj(fileNameYaml), Charset.defaultCharset(), false);
            // 设置前缀为person
//            prefix += ".";
            return setting.toBean(prefix, tClass);
        } catch (Exception e) {
            return loadFileProp(tClass, fileName, prefix);
        }
    }

    private static <T> T loadFileProp(Class<T> tClass, String fileName, String prefix) {
        String fileNameProp = fileName + ".properties";
        try {
            Props props = new Props(fileNameProp);
            // 加载成对应的bean ,指定前缀（prefix）
            return props.toBean(tClass, prefix);
//            ResourceUtil.getResourceObj(fileNameYaml);
        } catch (Exception e) {
            e.printStackTrace();
//            return loadFileXml(tClass, fileName);
            return null;
        }
    }

//    private static <T> T loadFileXml(Class<T> tClass, String fileName) {
//        String fileNameYaml = fileName + ".xml";
//        try {
//            ResourceUtil.getResourceObj(fileNameYaml);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
