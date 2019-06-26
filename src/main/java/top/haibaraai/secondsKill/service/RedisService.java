package top.haibaraai.secondsKill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * redisTemplate的封装
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 字符串写入操作
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串写入操作，并设置过期时间
     * @param key
     * @param value
     * @param expire 秒
     * @return
     */
    public boolean setex(final String key, Object value, Long expire) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key,value,expire,TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串-若不存在key则插入，否则直接返回
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(final String key, Object value) {
        boolean result = false;
        try {
            result = redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串-若key不存在则插入并设置超时时间
     * @param key
     * @param value
     * @param expire 秒
     * @return
     */
    public boolean setnxex(final String key, Object value, Long expire) {
        boolean result = false;
        try {
            result = redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串读取操作
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object object = redisTemplate.opsForValue().get(key);
        return object;
    }

    /**
     * redis删除键值对
     * @param key
     * @return
     */
    public boolean del(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * redis为键值对设置过期时间
     * @param key
     * @param time 秒
     * @return
     */
    public boolean expire(final String key, Long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

}
