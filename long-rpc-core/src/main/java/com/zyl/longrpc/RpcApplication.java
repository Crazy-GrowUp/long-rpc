package com.zyl.longrpc;

import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.constant.RpcConstant;
import com.zyl.longrpc.utlis.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: long-rpc
 * @description: RPC 应用框架，相当于 holder ，存放了项目全局用到的变量。双检锁单例模式实现
 * @author: yl.zhan
 * @create: 2024-11-28 15:01
 **/
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newConfig) {
        rpcConfig = newConfig;
//        log.info("RpcApplication init:{}", rpcConfig.toString());
        log.info("RpcApplication init");
    }


    public static void init(){
        RpcConfig newConfig;
        try{
            newConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            log.error(e.getMessage());
            newConfig = new RpcConfig();
        }
        init(newConfig);
    }

    /**
     * @Description 获取配置
     * @Date 3:13 PM 11/28/2024
     * @Author yl.zhan
     * @return com.zyl.longrpc.config.RpcConfig
     **/
    public static RpcConfig getRpcConfig() {
        if(rpcConfig==null){
            synchronized(RpcApplication.class){
                if(rpcConfig==null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
