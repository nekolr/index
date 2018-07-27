package com.nekolr.index.dao.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class IndexRedisRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, Object, Object> hashOperations;

    public void addCrowd(String key, Object value) {
        hashOperations.putAll(key, (Map<?, ?>) value);
    }

    public Map<Object, Object> getCrowd(String key) {
        return hashOperations.entries(key);
    }
}
