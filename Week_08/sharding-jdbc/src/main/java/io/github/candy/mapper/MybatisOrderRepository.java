package io.github.candy.mapper;

import io.github.candy.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by candy on 2020/12/10.
 */
@Mapper
public interface MybatisOrderRepository extends CommonRepository<Order, Long> {
}
