package io.candy;

/**
 * Created by candy on 2021/1/6.
 */
public interface DistributionLock {
    /**
     * 获取锁
     * @author candy
     * @return 锁标识
     */
    String acquire();

    /**
     * 释放锁
     * @author candy
     * @param indentifier
     * @return
     */
    boolean release(String indentifier);
}
