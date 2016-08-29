package com.likou.core.dubbo;

import com.likou.core.exception.DubboException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jiangli on 16/8/2.
 */
public abstract class AbstractService {

    private static Log logger = LogFactory.getLog("service");

    @Autowired
    DubboServiceFactory serviceFactory ;

    public <T> T getService(Class<T> className) throws DubboException {
        return serviceFactory.getDubboService(className);
    }

    public Log getLogger(){
        return logger;
    }

}
