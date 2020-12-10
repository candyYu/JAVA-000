package io.github.candy.entity;

import lombok.Data;

/**
 * Created by candy on 2020/12/10.
 */
@Data
public class Order {
    private long orderId;
    private int userId;
    private long payPrice;
    private int state;
    private String locationDetails;
    private long createTime;
    private long updateTime;
    private int payType;
}
