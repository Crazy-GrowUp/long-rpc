package com.zyl.longrpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @program: long-rpc
 * @description: 服务元信息（注册接口服务信息）
 * @author: yl.zhan
 * @create: 2024-12-04 15:31
 **/
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     **/
    private String serviceName;

    /**
     * 服务版本号
     **/
    private String serviceVersion = "1.0";

    /**
     * 服务域名
     **/
    private String serviceHost;

    /**
     * 服务端口号
     **/
    private Integer servicePort;

    /**
     * 服务分组
     **/
    private String serviceGroup;


    /**
     * @Description 获取服务键名
     * @Date 3:37 PM 12/4/2024
     * @Author yl.zhan
     * @return java.lang.String
     **/
    public String getServiceKey(){
        // 扩展服务分组
//        return String.format("%s:%s:%s",serviceName,serviceVersion,serviceGroup);
        return String.format("%s:%s",serviceName,serviceVersion);
    }


    /**
     * @Description 获取服务注册节点键名
     * @Date 3:38 PM 12/4/2024
     * @Author yl.zhan
     * @return java.lang.String
     **/
    public String getServiceNodeKey(){
        return String.format("%s:%s:%s",getServiceKey(),serviceHost,servicePort);
    }

    /**
     * @Description 获取完整服务地址
     * @Date 4:35 PM 12/4/2024
     * @Author yl.zhan
     * @return java.lang.String
     **/
    public String getServiceAddress(){
        if(!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
        }
        return String.format("%s:%s",serviceHost,servicePort);
    }

}
