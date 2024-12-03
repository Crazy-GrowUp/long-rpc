package com.zyl.longrpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.zyl.longrpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: long-rpc
 * @description: SPI 加载器 支持键值对映射
 * @author: yl.zhan
 * @create: 2024-12-03 11:28
 **/
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类：接口名=》(key=>实现类路径)
     **/
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap();

    /**
     * 对象实例缓存（避免重复new）,  类路径=》对象实例（单例，只能存在一个）
     **/
    private static Map<String, Object> instanceCache = new ConcurrentHashMap();

    /**
     * 系统 SPI 目录
     **/
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义 SPI 目录
     **/
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     **/
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载的类列表
     **/
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * @param loadClass 加载类
     * @return java.util.Map<java.lang.String, java.lang.Class < ?>> 方法实现类名称和实现类全限定名Class
     * @Description 加载某个类型
     * @Date 11:53 AM 12/3/2024
     * @Author yl.zhan
     **/
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("加载类型为{}的SPI", loadClass.getName());

        Map<String, Class<?>> keyClassMap = new HashMap<>();
        //扫描路径，用户自定义的SPI 优先级高于系统SPI
        for (String scanDir : SCAN_DIRS) {
            // hutool工具类
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            //读取每个资源文件
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        if (strArray.length == 2) {
                            String key = strArray[0].trim();
                            String className = strArray[1].trim();
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
//                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
//                    throw new RuntimeException(e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }

    /**
     * @Description 加载所有类型
     * @Date 3:17 PM 12/3/2024
     * @Author yl.zhan
     **/
    public static void loadAll() {
        log.info("加载所有 SPI");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }


    /**
     * @param tClass 接口类型
     * @param key    实现的接口名（就是写在META-INF里面的key）
     * @return T
     * @Description 获取某个接口的实例
     * @Date 3:18 PM 12/3/2024
     * @Author yl.zhan
     **/
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);

        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClassName));
        }

        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key));
        }
        // 获取到要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);
        // 从实例缓存中加载指定类型的实例
        String implClassName = implClass.getName();
        // 查看缓存中是否已经加载了
        if (!instanceCache.containsKey(implClassName)) {
            // 没加载，重新创建
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (Exception e) {
                String errorMsg = String.format("%s 类实例化失败", implClassName);
                log.error(errorMsg, e);
                throw new RuntimeException(errorMsg, e);
            }
        }

        return (T) instanceCache.get(implClassName);
    }


}
