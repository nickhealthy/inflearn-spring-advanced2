package hello.proxy.config.v1_proxy.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

/**
 * JDK 동적 프록시 적용1
 * 이전에는 프록시 클래스를 직접 하나씩 개발했지만, 이제는 JDK 동적 프록시 기술을 사용해 각각의 컨트롤러, 서비스, 리포지토리에 맞는 동적 프록시를 생성하면 된다.
 */
@Configuration
public class DynamicProxyBasicConfig {

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace trace) {
        OrderControllerV1 orderControllerV1 = new OrderControllerV1Impl(orderServiceV1(trace));

        OrderControllerV1 proxy = (OrderControllerV1) Proxy.newProxyInstance(orderControllerV1.getClass().getClassLoader(),
                new Class[]{OrderControllerV1.class},
                new LogTraceBasicHandler(orderControllerV1, trace));

        return proxy;
    }

    private OrderServiceV1 orderServiceV1(LogTrace trace) {
        OrderServiceV1 orderServiceV1 = new OrderServiceV1Impl(orderRepositoryV1(trace));

        OrderServiceV1 proxy = (OrderServiceV1) Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class},
                new LogTraceBasicHandler(orderServiceV1, trace));

        return proxy;

    }

    private OrderRepositoryV1 orderRepositoryV1(LogTrace trace) {
        OrderRepositoryV1 orderRepositoryV1 = new OrderRepositoryV1Impl();

        OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceBasicHandler(orderRepositoryV1, trace));

        return proxy;

    }
}
