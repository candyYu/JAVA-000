package io.kimmking.kmq.produce;

import io.kimmking.kmq.controller.Message;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by candy on 2021/1/25.
 */
@Slf4j
public class HttpProducer implements Producer {

    private final RestTemplate restTemplate = new RestTemplate();

    private Map<String, Object> properties;

    public HttpProducer(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public void send(String topic, String message) {
        String brokerUrl = properties.get("url").toString() + "/send";
        HttpEntity<Message> request = new HttpEntity<>(new Message(topic, message));
        ResponseEntity<Boolean> response = restTemplate.postForEntity(brokerUrl, request, Boolean.class);
        log.info("send response : " + response);
    }
}
