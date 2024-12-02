package com.zyl.longrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @program: long-rpc
 * @description: mock 服务代理
 * @author: yl.zhan
 * @create: 2024-12-02 15:00
 **/
@Slf4j
public class MockServerProxy implements InvocationHandler {
    /**
     * @param proxy
     * @param method
     * @param args
     * @return java.lang.Object
     * @Description 调用代理
     * @Date 3:04 PM 12/2/2024
     * @Author yl.zhan
     **/
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据方法的返回值类型，生成特定的默认值对象
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke{}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * @Description 生成默认值
     * @Date 3:07 PM 12/2/2024
     * @Author yl.zhan
     * @param type
     * @return java.lang.Object
     **/
    private Object getDefaultObject(Class<?> type) throws InstantiationException, IllegalAccessException {
        //判断基础类型
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            }

            if(type == byte.class){
                return (byte)0;
            }

            if (type == short.class) {
                return (short) 0;
            }

            if (type == int.class) {
                return 0;
            }

            if (type == long.class) {
                return 0L;
            }

            if (type == float.class) {
                return 0.0f;
            }

            if (type == double.class) {
                return 0.0d;
            }

            if(type == char.class){
                return (char)0;
            }
            //
            if (type == void.class) {
                return null;
            }

        }
        //对象类型 自动调用无参构造器
        return type.newInstance();
    }

}
