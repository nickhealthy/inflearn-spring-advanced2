package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK 동적 프록시(공통 로직 한 곳에 모아두기) - 예제 코드
 * - JDK 동적 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들어준다. 따라서 인터페이스가 필수이다.
 * - JDK 동적 프록시에 적용할 코드는 InvocationHandler 인터페이스를 구현해서 작성하면 된다.
 */
@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;    // 동적 프록시가 호출할 대상

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeInvocationHandler 실행");
        long startTime = System.currentTimeMillis();

        // 리플렉션을 사용해서 target 인스턴스의 메서드를 실행한다.
        // args는 메서드 호출시 넘겨줄 인수이다.
        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeInvocationHandler 종료 resultTime={}", resultTime);

        return result;
    }
}
