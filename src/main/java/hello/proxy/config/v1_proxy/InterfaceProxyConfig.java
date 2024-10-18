package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppV1 - 프록시 적용, 여기서 핵심은 원본 코드를 전혀 수정하지 않고, 서비스 코드에는 오직 서비스 로직만 있음
 *  - 인터페이스가 있는 구현 클래스에 적용
 */
@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace trace) {
        OrderControllerV1Impl target = new OrderControllerV1Impl(orderService(trace));
        return new OrderControllerInterfaceProxy(target, trace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace trace) {
        OrderServiceV1Impl target = new OrderServiceV1Impl(orderRepository(trace));
        return new OrderServiceInterfaceProxy(target, trace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace trace) {
        OrderRepositoryV1Impl target = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(target, trace);
    }
}