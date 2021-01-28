package io.kimmking.kmq.core;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class KmqMessage<T> {

    private HashMap<String,Object> headers;

    private T body;


}
