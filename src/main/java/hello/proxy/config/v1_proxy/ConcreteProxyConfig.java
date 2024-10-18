package hello.proxy.config.v1_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppV2 - 프록시 적용, 여기서 핵심은 원본 코드를 전혀 수정하지 않고, 서비스 코드에는 오직 서비스 로직만 있음
 *  - 구체 클래스에 프록시 사용(인터페이스 사용 X, 상속 사용 O)
 */
@Configuration
public class ConcreteProxyConfig {

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace trace) {
        OrderControllerV2 orderControllerImpl = new OrderControllerV2(orderServiceV2(trace));
        return new OrderControllerConcreteProxy(orderControllerImpl, trace);
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace trace) {
        OrderServiceV2 orderServiceImpl = new OrderServiceV2(orderRepositoryV2(trace));
        return new OrderServiceConcreteProxy(orderServiceImpl, trace);
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace trace) {
        OrderRepositoryV2 orderRepositoryImpl = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(orderRepositoryImpl, trace);
    }
}
