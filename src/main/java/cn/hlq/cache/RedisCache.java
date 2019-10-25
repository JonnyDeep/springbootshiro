package cn.hlq.cache;


import cn.hlq.util.ObjectUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisCache<K,V> implements Cache<K,V> {
    private long exireTime = 120; //缓存超时时间,单位为s
    private RedisTemplate template;
    private String prefix = "shiro_reis";

    public String getPrefix(){
        return prefix+":";
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
    public RedisCache(){
        super();
    }

    public RedisCache(long exireTime,RedisTemplate<K,V> redisTemplate,String prefix){
        super();
        this.exireTime =exireTime;
        this.template = redisTemplate;
        this.prefix = prefix;
    }
    @Override
    public V get(K k) throws CacheException {
        if(k==null){
            return  null;
        }
        try {
            byte[] bytes = getBytesKey(k);
            return (V)template.opsForValue().get(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        if(k==null||v==null){
            return null;
        }
        try {
            byte[] bytes = getBytesKey(k);
            template.opsForValue().set(bytes,v);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        if(k==null){
            return null;
        }
        try {
            byte[] bytes = getBytesKey(k);
            V v = (V)template.opsForValue().get(bytes);
            template.opsForValue().getOperations().delete(bytes);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }

    @Override
    public void clear() throws CacheException {
        template.getConnectionFactory().getConnection().flushDb();
    }

    @Override
    public int size() {
        Long size = template.getConnectionFactory().getConnection().dbSize();
        return size.intValue();

    }

    @Override
    public Set<K> keys() {

        byte[] bytes = (getPrefix()+"*").getBytes();
        Set<byte[]> keys = template.keys( bytes);
        Set<K> sets = new HashSet<>();
        for(byte[] key:keys){
            sets.add((K)key);
        }
        return sets;
    }

    @Override
    public Collection<V> values() {

        Set<K> keys = keys();
        List<V> values = new ArrayList<>(keys.size());
        for(K k :keys){
            values.add(get(k));
        }
        return values;
    }

    private byte[] getBytesKey(K key) throws Exception {
        if(key instanceof String){
            String preKey = this.getPrefix()+key;
            return  preKey.getBytes();
        }else {
            return ObjectUtil.Serializable(key);
        }
    }
}
