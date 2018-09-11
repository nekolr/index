package com.nekolr.index.dao.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Repository
public class IndexRedisRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, Object, Object> hashOperations;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> listOperations;

    @Resource(name = "redisTemplate")
    private SetOperations<String, Object> setOperations;

    public void addAll(String key, Object value) {
        hashOperations.putAll(key, (Map<?, ?>) value);
    }

    public Map<Object, Object> getEntries(String key) {
        return hashOperations.entries(key);
    }

    public void addListElement(String key, Object value) {
        listOperations.leftPush(key, value);
    }

    public void addSetElement(String key, Object value) {
        setOperations.add(key, value);
    }

    public Set<Object> getSetElements(String key) {
        return setOperations.members(key);
    }

    public boolean delKey(String key) {
        return redisTemplate.delete(key);
    }
}
