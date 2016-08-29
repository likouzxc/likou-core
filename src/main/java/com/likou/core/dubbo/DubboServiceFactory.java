package com.likou.core.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.likou.core.exception.DubboException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangli on 16/7/27.
 */
public class DubboServiceFactory {

    /**
     *  应用名称
     */
    private String applicationName;
    /**
     * zookeeper列表,暂时保持暴漏与获取服务的zookeeper的ip是统一地址
     */
    private ArrayList<String> zookeeperList ;
    /**
     * zookeeper服务器的个数
     */
    private int serverSize;

    /**
     * 引用服务Map : className -- version
     */
    private HashMap<Class,String> serviceMap ;


    private ApplicationConfig applicationConfig ;
    private ArrayList<RegistryConfig> registries ;
    private HashMap<Class , ReferenceBean> referenceBeanMap ;



    public DubboServiceFactory(
            String applicationName,
            ArrayList<String> zookeeperList, Integer serverSize,
            HashMap<String, String> serviceMap
    ) {
        this.applicationName = applicationName;
        this.zookeeperList = zookeeperList;
        this.serverSize = serverSize;
        this.referenceBeanMap = new HashMap<Class, ReferenceBean>();
        this.serviceMap = new HashMap<Class, String>();
        for(Map.Entry<String,String> entry : serviceMap.entrySet()){
            try {
                this.serviceMap.put(Class.forName(entry.getKey()), entry.getValue());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        initZookeeper();
    }

    /**
     * 初始化zookeeper序列操作
     * @return
     */
    private boolean initZookeeper() {
        try{
            //当前应用配置
            applicationConfig = new ApplicationConfig();
            applicationConfig.setName(applicationName);

            //多注册中心配置,port固定统一，
            registries = new ArrayList<RegistryConfig>();
            if(serverSize<=0) return false ;
            for(int i = 0; i<serverSize&&i< zookeeperList.size(); i++){
                RegistryConfig registryConfig = new RegistryConfig();
                String address = zookeeperList.get(i);
                registryConfig.setAddress(address);
                registries.add(registryConfig);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public <T> T getDubboService(Class<T> className) throws DubboException {
        try{
            if(serviceMap.containsKey(className)) {
                if(referenceBeanMap.containsKey(className)){
                    ReferenceBean<T> referenceBean = referenceBeanMap.get(className);
                    T t = referenceBean.get();
                    return t;
                }else{
                    ReferenceBean<T> referenceBean = new ReferenceBean<T>();
                    referenceBean.setApplication(applicationConfig);
                    referenceBean.setRegistries(registries);
                    referenceBean.setInterface(className);
                    referenceBean.setVersion(serviceMap.get(className));

                    referenceBean.afterPropertiesSet();
                    if(referenceBean.get() != null) {
                        referenceBeanMap.put(className, referenceBean);
                        return referenceBean.get();
                    }else{
                        throw new DubboException(DubboMessage.UNDEFINED);
                    }
                }
            }else{
                throw new DubboException(DubboMessage.NOSERVICE);
            }
        }catch (DubboException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new DubboException();
        }
    }

}
