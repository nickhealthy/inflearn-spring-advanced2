package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v1.*;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 프록시 팩토리 적용1 - Advisor
 * 이전에는 JDK 동적 프록시, CGLIB 프록시 구현 방식에 따라 개별적으로 프록시 클래스를 지정해서 설정해야했지만,
 * 이제는 프록시 팩토리를 통해 구현 방식에 상관없이 동일한 방식으로 프록시를 적용할 수 있다.
 */
@Slf4j
@Configuration
public class ProxyFactoryConfigV1 {

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace trace) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1(trace));
        ProxyFactory factory = new ProxyFactory(orderController);
        factory.addAdvisor(getAdvisor(trace));

        OrderControllerV1 proxy = (OrderControllerV1) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(),
                orderController.getClass());
        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace trace) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1(trace));
        ProxyFactory factory = new ProxyFactory(orderService);
        factory.addAdvisor(getAdvisor(trace));

        OrderServiceV1 proxy = (OrderServiceV1) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(),
                orderService.getClass());
        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace trace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        ProxyFactory factory = new ProxyFactory(orderRepository);
        factory.addAdvisor(getAdvisor(trace));

        OrderRepositoryV1 proxy = (OrderRepositoryV1) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(),
                orderRepository.getClass());
        return proxy;
    }

    private Advisor getAdvisor(LogTrace trace) {
        // 포인트컷
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // 어드바이저
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new LogTraceAdvice(trace));
        return advisor;
    }

}
