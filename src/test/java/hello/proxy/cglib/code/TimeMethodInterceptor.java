package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * JDK 동적 프록시에서 실행 로직을 위해 InvocationHandler를 제공했듯,
 * CGLIB(Code Generator Library)은 MethodInterceptor를 제공한다.
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target;    // 프록시가 호출할 실제 대상

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    /**
     * JDK 동적 프록시와 거의 같은 코드
     *
     * @param obj:      CGLIB가 적용된 객체
     * @param method:   호출된 메서드
     * @param args:     메서드를 호출하면서 전달된 인수
     * @param proxy:    메서드 호출에 사용
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        log.info("TImeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = proxy.invoke(target, args); // 실제 대상을 동적으로 호출한다.

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);

        return result;
    }
}
