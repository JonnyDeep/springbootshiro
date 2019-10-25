package cn.hlq.cacheManager;

import cn.hlq.cache.RedisCache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheManager implements CacheManager {
    private static Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);
    private RedisTemplate<byte[],byte[]> redisTemplate;
    private long expireTime;
    public RedisCacheManager(RedisTemplate redisTemplate,long expireTime){
        this.redisTemplate = redisTemplate;
        this.expireTime = expireTime;
    }
    @Override
    public  Cache getCache(String name) throws CacheException {
        logger.info("--->cache name:{}",name);

        return new RedisCache(expireTime,redisTemplate,name);
    }
}
