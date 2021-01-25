package io.kimmking.kmq.demo;

import io.kimmking.kmq.consume.Consumer;
import io.kimmking.kmq.consume.HttpConsumer;
import io.kimmking.kmq.produce.HttpProducer;
import io.kimmking.kmq.produce.Producer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by candy on 2021/1/25.
 */
public class CandyDemo {

    public static void main(String[] args) {

        int messageAmount = 1;//1000;
        String topic = "testTopic";
        
        
        startHttpMQProducer(messageAmount);
        startHttpMQConsumer(messageAmount, topic);

    }

    private static void startHttpMQConsumer(int messageAmount, String topic) {

        Map<String, Object> properties = new HashMap<>(1);
        properties.put("url", "http://localhost:8080");
        Consumer consumer = new HttpConsumer(properties);
        int amount = messageAmount;

        System.out.println("Start consumer test");
        long start = System.currentTimeMillis();

        while (amount > 0) {
            consumer.poll(topic);
            amount -= 1;
        }

        System.out.println("Consumer " + messageAmount + " messages spend time : " + (System.currentTimeMillis() - start) + " " +
            "ms");

    }

    private static void startHttpMQProducer(int messageAmount) {

        Map<String, Object> properties = new HashMap<>(1);
        properties.put("url", "http://localhost:8080");
        Producer producer = new HttpProducer(properties);
        String topic = "testTopic";

        System.out.println("start producer test");
        long start = System.currentTimeMillis();

        for(int i = 0; i < messageAmount; i++) {
            producer.send(topic, String.valueOf(i));
        }

        System.out.println("Producer " + messageAmount + " messages spend time : " +
            (System.currentTimeMillis() - start) + " ms ");
    }
}
