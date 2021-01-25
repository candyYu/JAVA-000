package io.kimmking.kmq.consume;

import io.kimmking.kmq.core.KmqMessage;

/**
 * Created by candy on 2021/1/25.
 */
public interface Consumer {

    KmqMessage poll(String topic);
}
