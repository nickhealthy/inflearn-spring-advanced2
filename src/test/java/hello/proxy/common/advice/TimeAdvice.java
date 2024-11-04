package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 프록시 팩토리 - Advice 구현
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    /**
     * @param invocation: 내부에는 다음 메서드를 호출하는 방법, 현재 프록시 객체 인스턴스, args, 메서드 정보 등이 포함되어 있음
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        log.info("TImeProxy 실행");
        long startTime = System.currentTimeMillis();

        // target 정보는 invocation 에 이미 다 들어있다.
        // ProxyFactory 로 생성할 때 target 를 넘김
        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);

        return result;

    }
}
