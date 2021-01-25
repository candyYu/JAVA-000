package io.kimmking.kmq.queue;

import io.kimmking.kmq.core.KmqMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by candy on 2021/1/25.
 */
@Slf4j
public class CandyQueue {

    private KmqMessage[] queue;

    private int consumeOffset;

    private int positionOffset;


    public  CandyQueue(int capactiy) {
        this.queue = new KmqMessage[capactiy];
        this.consumeOffset = 0;
        this.positionOffset = 0;
    }

    public synchronized void offer(KmqMessage kmqMessage) {
        try {
            this.queue[positionOffset] = kmqMessage;
            positionOffset++;
        }catch (Exception ex) {
            log.error("塞入mq内存消息出错", ex);
        }
    }

    public synchronized KmqMessage poll() {
        if (consumeOffset > positionOffset) {
            throw  new RuntimeException("没有可读数据");
        }else {
            return  this.queue[consumeOffset++];
        }
    }
}
