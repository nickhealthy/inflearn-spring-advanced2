package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        // 동적 프록시에 적용할 핸들러 로직
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        /*
            - 동적 프록시는 java.lang.reflect.Proxy 를 통해 생성할 수 있다.
            - 클래스 로더 정보, 인터페이스, 그리고 핸들러 로직을 넣어주면 된다.
              그러면 해당 인터페이스를 기반으로 동적 프록시를 생성하고 그 결과를 반환한다.
         */
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        // 아래와 같은 방법으로도 가능하다. target.getClass().getClassLoader()
        BInterface proxy = (BInterface) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{BInterface.class}, handler);
        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
}
