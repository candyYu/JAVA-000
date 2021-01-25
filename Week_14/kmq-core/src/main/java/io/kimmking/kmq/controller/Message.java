package io.kimmking.kmq.controller;

import lombok.Data;

/**
 * Created by candy on 2021/1/25.
 */
@Data
public class Message {

    private  String topic;

    private  String content;

    public Message(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }
}
