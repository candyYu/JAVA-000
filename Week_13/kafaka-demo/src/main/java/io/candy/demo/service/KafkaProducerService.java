package io.candy.demo.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Created by candy on 2021/2/5.
 */
@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * producer 同步方式发送数据
     * @param topic    topic名称
     * @param key      一般用业务id，相同业务在同一partition保证消费顺序
     * @param message  producer发送的数据
     */
    public void sendMessageSync(String topic, String key, String message) throws InterruptedException, ExecutionException, TimeoutException {
        // 默认轮询partition
        kafkaTemplate.send(topic, message).get(10, TimeUnit.SECONDS);
//        // 根据key进行hash运算，再将运算结果写入到不同partition
//        kafkaTemplate.send(topic, key, message).get(10, TimeUnit.SECONDS);
//        // 第二个参数为partition,当partition和key同时设置时partition优先。
//        kafkaTemplate.send(topic, 0, key, message);
//        // 组装消息
//        Message msg = MessageBuilder.withPayload("Send Message(payload,headers) Test")
//                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
//                .setHeader(KafkaHeaders.TOPIC, topic)
//                .setHeader(KafkaHeaders.PREFIX,"kafka_")
//                .build();
//        kafkaTemplate.send(msg).get(10, TimeUnit.SECONDS);
//        // 组装消息
//        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test", "Send ProducerRecord(topic,value) Test");
//        kafkaTemplate.send(producerRecord).get(10, TimeUnit.SECONDS);
    }

    /**
     * producer 异步方式发送数据
     * @param topic    topic名称
     * @param message  producer发送的数据
     */
    public void sendMessageAsync(String topic, String message) {
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topic, message);

        // 设置异步发送消息获取发送结果后执行的动作
        ListenableFutureCallback listenableFutureCallback = new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("failure");
            }
        };

        // 将listenableFutureCallback与异步发送消息对象绑定
        future.addCallback(listenableFutureCallback);
    }

    public void test(String topic, Integer partition, String key, String message) throws InterruptedException, ExecutionException, TimeoutException {
        kafkaTemplate.send(topic, partition, key, message).get(10, TimeUnit.SECONDS);
    }
}

