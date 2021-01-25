package io.kimmking.kmq.consume;

import io.kimmking.kmq.core.KmqMessage;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by candy on 2021/1/25.
 */
public class HttpConsumer implements Consumer {

    private final RestTemplate restTemplate = new RestTemplate();

    private Map<String, Object> properties;

    public HttpConsumer(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public KmqMessage poll(String topic) {
        String brokerUrl = properties.get("url").toString() + "/poll?topic=" + topic;
        ResponseEntity<KmqMessage> response = restTemplate.getForEntity(brokerUrl, KmqMessage.class);
        return response.getBody();
    }

}
