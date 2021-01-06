package io.candy;

import java.util.Collections;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by candy on 2021/1/6.
 */

public class RedisDistributedLock implements DistributionLock {

    private Logger log = LoggerFactory.getLogger(RedisDistributedLock.class);
    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * redis 客户端
     */
    private Jedis jedis;

    /**
     * 分布式锁的键值
     */
    private String lockKey;

    /**
     * 锁的超时时间 10s
     */
    int expireTime = 10 * 1000;

    /**
     * 锁等待，防止线程饥饿
     */
    int acquireTimeout  = 1 * 1000;

    /**
     * 获取指定键值的锁
     * @param jedis jedis Redis客户端
     * @param lockKey 锁的键值
     */
    public RedisDistributedLock(Jedis jedis, String lockKey) {
        this.jedis = jedis;
        this.lockKey = lockKey;
    }

    /**
     * 获取指定键值的锁,同时设置获取锁超时时间
     * @param jedis jedis Redis客户端
     * @param lockKey 锁的键值
     * @param acquireTimeout 获取锁超时时间
     */
    public RedisDistributedLock(Jedis jedis,String lockKey, int acquireTimeout) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.acquireTimeout = acquireTimeout;
    }

    /**
     * 获取指定键值的锁,同时设置获取锁超时时间和锁过期时间
     * @param jedis jedis Redis客户端
     * @param lockKey 锁的键值
     * @param acquireTimeout 获取锁超时时间
     * @param expireTime 锁失效时间
     */
    public RedisDistributedLock(Jedis jedis, String lockKey, int acquireTimeout, int expireTime) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.acquireTimeout = acquireTimeout;
        this.expireTime = expireTime;
    }

    @Override
    public String acquire() {
        try {
            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;
            // 随机生成一个value
            String requireToken = UUID.randomUUID().toString();
            while (System.currentTimeMillis() < end) {
                String result = jedis.set(lockKey, requireToken, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
                if (LOCK_SUCCESS.equals(result)) {
                    return requireToken;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            log.error("acquire lock due to error", e);
        }

        return null;
    }

    @Override
    public boolean release(String identify) {
        if(identify == null){
            return false;
        }

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = new Object();
        try {
            result = jedis.eval(script, Collections.singletonList(lockKey),
                Collections.singletonList(identify));
            if (RELEASE_SUCCESS.equals(result)) {
                log.info("release lock success, requestToken:{}", identify);
                return true;
            }}catch (Exception e){
            log.error("release lock due to error",e);
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }

        log.info("release lock failed, requestToken:{}, result:{}", identify, result);
        return false;
    }
}
