package io.candy.demo.controller;

import io.candy.demo.service.KafkaProducerService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by candy on 2021/2/5.
 */
@RestController
public class KafkaProducerController {

    @Autowired
    private KafkaProducerService producerService;

    @GetMapping("/sync")
    public void sendMessageSync(@RequestParam String topic) throws InterruptedException, ExecutionException, TimeoutException {
        producerService.sendMessageSync(topic, null, "同步发送消息测试");
    }

    @GetMapping("/async")
    public void sendMessageAsync(){
        producerService.sendMessageAsync("test","异步发送消息测试");
    }

    @GetMapping("/test")
    public void test(@RequestParam String topic, @RequestParam(required = false) Integer partition, @RequestParam(required = false) String key, @RequestParam String message) throws InterruptedException, ExecutionException, TimeoutException {
        producerService.test(topic, partition, key, message);
    }

}
