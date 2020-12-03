package io.github.candy.proxyDataSource;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by candy on 2020/12/3.
 */

@Slf4j
public class DynamicDataSourceContext {

    /**
     * 所有配置过的数据源的KEY
     */
    public static List<String> dataSourceKeys = new CopyOnWriteArrayList<>();

    /**
     * 默认数据源的KEY
     */
    public static String defaultDataSourceKey;

    /**
     * 当前SQL执行之前，在{@link ThreadLocal}中设置的数据源的KEY
     */
    private static final ThreadLocal<String> RESOURCE = new ThreadLocal<>();


    /**
     * 获取「当前在{@link ThreadLocal}中设置的数据源的KEY」
     */
    public static String getRoutingDataSourceKey(){
        return RESOURCE.get();
    }

    /**
     * 获取「当前在{@link ThreadLocal}中设置的数据源的KEY」
     */
    public static void setRoutingDataSourceKey(String routingDataSourceKey){
        log.debug("切换至「{}」数据源", routingDataSourceKey);
        RESOURCE.set(routingDataSourceKey);
    }

    /**
     * 动态路由完成之后，清空设置的数据源的KEY
     */
    public static void clearRoutingDataSourceKey(){
        RESOURCE.remove();
    }

    /**
     * 添加配置过的数据源的KEY
     */
    public static void addDataSourceKey(String dataSourceKey, boolean ifDefaultDataSourceKey){
        dataSourceKeys.add(dataSourceKey);

        if(ifDefaultDataSourceKey){
            defaultDataSourceKey = dataSourceKey;
        }
    }

    /**
     * 判断是否已经配置某个数据源
     */
    public static boolean containsDataSource(String dataSourceKey){
        return dataSourceKeys.contains(dataSourceKey);
    }





}
