package io.github.candy.proxyDataSource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * Created by candy on 2020/12/3.
 */
@Aspect
@Slf4j
public class AspectJ {


    @Pointcut("@annotation(io.github.candy.proxyDataSource.ReadOnly)")
    public void dataSourcePointCut() {
        System.out.println("proxy");
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        ReadOnly ds = method.getAnnotation(ReadOnly.class);
        if(ds != null){
            if(ds.value().equals("slave")){
                // 负载均衡，简单随即
                final String slave = loadBalance();
                System.out.println("随机：" + slave);
                DynamicDataSourceContext.setRoutingDataSourceKey("slave");
            }else{
                DynamicDataSourceContext.setRoutingDataSourceKey("master");
            }
        }else{
            DynamicDataSourceContext.setRoutingDataSourceKey("master");
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContext.clearRoutingDataSourceKey();
        }
    }

    private String loadBalance(){
        Random random = new Random();
        final int i = random.nextInt(2) + 1;
        switch (i){
            case 1:
                return DataSourceConstant.SLAVE1;
            case 2:
                return DataSourceConstant.SLAVE2;
        }
        return DataSourceConstant.SLAVE1;
    }


}
