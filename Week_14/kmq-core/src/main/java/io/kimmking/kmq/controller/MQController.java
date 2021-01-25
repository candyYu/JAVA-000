package io.kimmking.kmq.controller;

import io.kimmking.kmq.core.KmqBroker;
import io.kimmking.kmq.core.KmqMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by candy on 2021/1/25.
 */
@RestController
public class MQController {

    private final KmqBroker kmqBroker;

    public MQController(KmqBroker kmqBroker) {
         this.kmqBroker = kmqBroker;
    }

    @GetMapping("/poll")
    public KmqMessage consume(@RequestParam(value = "topic")String topic) {
        return this.kmqBroker.findKmq(topic).poll();
    }

    @PostMapping("/send")
    public boolean send(@RequestBody Message message) {
        this.kmqBroker.createTopic(message.getTopic());
        this.kmqBroker.createProducer().send(message.getTopic(), new KmqMessage(null, message.getContent()));
        return true;
    }
}
