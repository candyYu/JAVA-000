package io.kimmking.kmq.produce;

/**
 * Created by candy on 2021/1/25.
 */
public interface Producer {
    void  send (String topic, String message);
}
