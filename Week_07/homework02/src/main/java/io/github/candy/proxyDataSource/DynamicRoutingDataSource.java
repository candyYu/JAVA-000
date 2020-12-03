package io.github.candy.proxyDataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by candy on 2020/12/3.
 */

@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        //如果 currentDataSourceKey 为null，则调用方「#determineTargetDataSource」方法会在当前方法返回null之后自动使用默认数据源
        String currentDataSourceKey = DynamicDataSourceContext.getRoutingDataSourceKey();

        log.info("当前使用的数据源是：「{}」（这里为null表示使用的是默认数据源）", currentDataSourceKey);
        return currentDataSourceKey;

    }
}
