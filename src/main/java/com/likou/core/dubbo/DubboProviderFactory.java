package com.likou.core.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.likou.common.net.NetUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;


/**
 * Created by jiangli on 16/7/30.
 */
public class DubboProviderFactory implements ApplicationContextAware {

    /**
     *  应用名称
     */
    private String applicationName;
    /**
     * 版本号
     */
    private String version;
    /**
     * zookeeper服务器的个数
     */
    private int serverSize;
    /**
     * 服务器
     */
    private ArrayList<String> zookeeperList;
    /**
     * 需要暴漏的服务的地址
     */
    private String providerHost ;
    /**
     * 需要暴漏的服务的地址
     */
    private int providerPort ;
    /**
     * 需要暴漏的服务的接口名
     */
    private ArrayList<String> providerList ;
    /**
     * 服务提供者的支持的线程总数
     */
    private int protocolThreadSize ;
    /**
     * dubbo所处目录
     */
    private String path ;

    private ApplicationContext context = null ;
    private ApplicationConfig applicationConfig ;
    private ArrayList<RegistryConfig> registries ;
    private ProtocolConfig protocolConfig;

    public DubboProviderFactory(
            String applicationName,
            ArrayList<String> zookeeperList, Integer serverSize,
            ArrayList<String> providerList,
            String path, String version,
            String providerHost, Integer protocolThreadSize
    ) {
        this.applicationName = applicationName;
        this.version = version;
        this.serverSize = serverSize;
        this.zookeeperList = zookeeperList;
        this.providerHost = providerHost;
        this.providerList = providerList;
        this.protocolThreadSize = protocolThreadSize;
        this.path = path;
        try {
            this.providerPort = NetUtils.getRandomPortUnUsing("127.0.0.1");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(context==null)
        {
            context =applicationContext;
        }
        System.out.println("ApplicationContext setter is called...");
    }

    public void init(){
        if(initZookeeper()){
            initDubbo();
        }
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

            // 服务提供者协议配置，port可变
            protocolConfig = new ProtocolConfig();
            protocolConfig.setName("dubbo");
            protocolConfig.setHost(providerHost);
            protocolConfig.setPort(providerPort);
            protocolConfig.setThreads(protocolThreadSize);
            protocolConfig.setThreadpool("cached");

            StringBuffer log = new StringBuffer();
            log.append("服务提供者的host地址：").append(providerHost).append(",");
            log.append("使用端口为：").append(providerPort).append(".");
            System.out.println(log.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    private void initDubbo() {
        int size = 0;
        for(String className : providerList){
            try{
                Class t = Class.forName(className);
                Class[] interfaceNames = t.getInterfaces();
                if(interfaceNames.length<1)continue;
                Class interfaceName = interfaceNames[0];

                Object object = context.getBean(interfaceName);

                ServiceConfig service =new ServiceConfig();// 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
                service.setApplication(applicationConfig);
                // 多个注册中心可以用setRegistries()
                service.setRegistries(registries);
                // 多个协议可以用setProtocols()
                service.setProtocol(protocolConfig);

                service.setInterface(interfaceName);
                service.setRef(object);
                service.setTimeout(1000);
                service.setVersion(version);
                service.setPath(path);
                service.export();

                StringBuffer log = new StringBuffer();
                log.append(size+"、服务注册成功：");
                log.append("\n\t服务class:").append(className);
                System.out.println(log.toString());
                size++;
            }catch(Exception e){
                StringBuffer log = new StringBuffer();
                log.append("服务注册失败：");
                log.append("\n\t失败服务class:").append(className);
                System.out.println(log.toString());
                e.printStackTrace();
            }
        }
    }
}
