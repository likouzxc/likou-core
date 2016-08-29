package com.likou.core.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlaveDBDataSource extends AbstractRoutingDataSource{

    private Lock lock = new ReentrantLock();
    private int dataSourceNumber = 0;
    private Random random = new Random();

    @Override
    public void setTargetDataSources(java.util.Map<Object,Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        dataSourceNumber = targetDataSources.keySet().size();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        lock.lock();
        try{
            int counter = random.nextInt(200);
            int lookupKey = counter % this.dataSourceNumber;
            return Integer.toString(lookupKey);
        }finally{
            lock.unlock();
        }
    }
}
