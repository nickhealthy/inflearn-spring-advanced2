package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

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

    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointCut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    /**
     * 포인트컷은 ClassFilter, MethodMatcher 둘로 이뤄진다.
     * - 이름 그대로 하나는 클래스가 맞는지, 하나는 메서드가 맞는지 확인할 때 사용하며, 둘 다 true로 반환해야 어드바이스를 사용할 수 있다.
     */
    static class MyPointCut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MethodMatcher() {

                private String matchName = "save";

                /**
                 * boolean result = method.getName().equals(matchName);
                 * - 포인트컷을 save 이름을 가진 메서드에만 적용했으므로 save() 메서드만 어드바이저가 적용된다.
                 * - 따라서 프록시 로직은 save 메서드만 적용된다.
                 */
                @Override
                public boolean matches(Method method, Class<?> targetClass) {
                    boolean result = method.getName().equals(matchName);
                    log.info("포인트컷 호출 method={} targetClass={}", matchName, targetClass);
                    log.info("포인트컷 결과 result={}", result);
                    return result;
                }

                @Override
                public boolean isRuntime() {
                    return false;
                }

                @Override
                public boolean matches(Method method, Class<?> targetClass, Object... args) {
                    return false;
                }
            };
        }
    }

    @Test
    @DisplayName("스프링이 제공하는 포인트컷")
    void advisorTest3() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        /*
            스프링이 제공하는 포인트컷(스프링은 이외에도 무수히 많은 포인트컷을 제공한다, 하지만 나중엔 AspectJ 라이브러리를 사용하게 된다.)
            - 메서드 이름을 기반으로 매칭한다
            - pointcut.setMappedName(); 해당 메서드에 메서드 이름을 지정하면 지정된 메서드만 어드바이스가 적용되는 포인트컷이 완성된다.
            - 내부에서는 Spring 이 제공하는 PatternMatchUtils 를 사용한다.
         */
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("save");

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }
}
