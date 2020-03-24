package com.taoyuan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @ProjectName permission
 * @ClassName RedisPool
 * @Date 2020/3/11 22:07
 * @Author taoyuan
 * @Version 1.0
 */
@Service
@Slf4j
public class RedisPool {

    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    //从连接池得到连接
    public ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    //归还给连接池
    public void safeClose(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            log.error("return redis resource exception", e);
        }
    }
}
