package com.likou.core.db;

/**
 * Created by likou on 2014-10-28.
 */
public class MasterDBDataSourceHolder {
    public static final ThreadLocal<String> holder = new ThreadLocal<String>();

    public static void putDataSource(String name) {
        holder.set(name);
    }

    public static String getDataSouce() {
        return holder.get();
    }
}
