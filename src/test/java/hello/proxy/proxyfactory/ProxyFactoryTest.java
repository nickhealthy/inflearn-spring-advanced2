package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * 프록시 팩토리1 테스트
 */
@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        /*
            프록시 팩토리를 생성하는 단계에서 target 정보를 넘긴다.
            프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다.
         */
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());   // 프록시 팩토리를 통해서 만든 프록시가 사용할 부가 기능 로직을 설정한다.
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();    // 프록시 객체를 생성하고 그 결과를 받는다.
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();   // 프록시로 로직 실행

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();                // proxy O
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();         // JDK 동적 프록시 O
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();             // CGLIB 프록시 X

    }
}
