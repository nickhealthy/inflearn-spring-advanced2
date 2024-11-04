package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * 어드바이저 구현 - 하나의 포인트 컷과, 하나의 어드바이스를 가지고 있다.
 */
@Slf4j
public class AdvisorTest {

    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // Advisor 인터페이스의 가장 일반적인 구현체이다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        /*
            프록시 팩토리에 적용할 어드바이저를 지정한다.
            어드바이저는 내부에 포인트컷과 어드바이스를 모두 가지고 있기 때문에, 어디에 어떤 부가 로직을 적용해야 하는지 알 수 있다.
            프록시 팩토리를 사용할 땐 어드바이저는 '필수'이다.
         */
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();

    }
}
