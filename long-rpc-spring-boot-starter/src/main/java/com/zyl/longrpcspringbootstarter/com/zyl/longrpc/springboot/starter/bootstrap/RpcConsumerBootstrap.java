package com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.bootstrap;

import com.zyl.longrpc.proxy.ServiceProxy;
import com.zyl.longrpc.proxy.ServiceProxyFactory;
import com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @program: long-rpc
 * @description: RpcRefernce注解解析类
 * @author: yl.zhan
 * @create: 2024-12-18 16:38
 **/
public class RpcConsumerBootstrap implements BeanPostProcessor {
    /**
     * @param bean
     * @param beanName
     * @return java.lang.Object
     * @Description bean注册后执行
     * @Date 5:11 PM 12/18/2024
     * @Author yl.zhan
     **/
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 获取所有spring bean的字段
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            // 此字段是否有@RpcReference注解
            if (rpcReference != null) {
                // 有，进行处理
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                // 开启访问权限，允许修改此field，重新赋值
                field.setAccessible(true);
                Object proxy = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    //重新赋值
                    field.set(bean, proxy);
                    // 关闭访问权限
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(interfaceClass.getName() + " 代理失败", e);
                }

            }
        }


        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
