package com.likou.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by jiangli on 16/9/23.
 */
@Component
public class TokenCache {
    @Autowired
    ShardedJedisPool shardedJedisPool;

    private final String SUFFIX = "_TOKEN";

    public void saveToken(String sessionID,String token){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            String key = sessionID + SUFFIX;
            shardedJedis.set(key,token);
            shardedJedis.expire(sessionID,60*60);
        } catch (Exception ex) {
            shardedJedisPool.returnBrokenResource(shardedJedis);
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }
    public String checkToken(String sessionID , String token){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            String key = sessionID + SUFFIX;
            return shardedJedis.get(key);
        } catch (Exception ex) {
            shardedJedisPool.returnBrokenResource(shardedJedis);
            return null;
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }
}
