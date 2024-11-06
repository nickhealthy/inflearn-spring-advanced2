package hello.proxy.config.v3_proxyfactory.advice;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 프록시 팩토리1
 * - V1 인터페이스 Advice 구현(인터페이스를 사용하기 때문에 기본적으로 프록시 팩토리는 JDK 동적 프록시를 사용함)
 */
@Slf4j
public class LogTraceAdvice implements MethodInterceptor {

    private final LogTrace trace;

    public LogTraceAdvice(LogTrace trace) {
        this.trace = trace;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TraceStatus status = null;

        try {
            Method method = invocation.getMethod();
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = trace.begin(message);

            // 로직 호출
            Object result = invocation.proceed();

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
