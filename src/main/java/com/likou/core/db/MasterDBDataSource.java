package com.likou.core.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by likou on 2014-10-28.
 */
public class MasterDBDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey(){
        return MasterDBDataSourceHolder.getDataSouce();
    }
}
