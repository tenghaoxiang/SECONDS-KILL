package top.haibaraai.secondsKill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁的实现
 */
@Component
public class DistributedLock {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 以redisTemplate新增的加了过期时间的setIfAbsent()方法原子性加锁
     * @param key
     * @param value
     * @param expire 秒
     * @return
     */
    public boolean lock(String key, Object value, long expire) {
        try {
            return redisService.setnxex(key, value, expire);
        } catch (Exception e) {
            logger.error("While get DistributedLock occur an Exception: " + e);
        }
        return false;
    }

    /**
     * 以redisTemplate新增的加了过期时间的setIfAbsent()方法原子性加锁
     * @param key
     * @param value
     * @param expire
     * @param timeUnit
     * @return
     */
    public boolean lock(String key, Object value, long expire, TimeUnit timeUnit) {
        try {
            return redisService.setnxex(key, value, expire, timeUnit);
        } catch (Exception e) {
            logger.error("While get DistributedLock occur an Exception: " + e);
        }
        return false;
    }

    /**
     * 以lua脚本的方式原子性地释放锁
     * @param key
     * @param value
     * @return
     */
    public boolean unlock(String key, String value) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/unlock.lua")));
        redisScript.setResultType(Boolean.class);
        List<Object> keyList = new ArrayList<>();
        keyList.add(key);
        keyList.add(value);
        Boolean result = (Boolean) redisTemplate.execute(redisScript, keyList);
        return result;
    }

}
