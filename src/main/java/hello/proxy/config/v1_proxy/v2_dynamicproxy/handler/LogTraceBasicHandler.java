package hello.proxy.config.v1_proxy.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK 동적 프록시 적용1 - V1 애플리케이션에 JDK 동적 프록시를 적용할 핸드러를 정의
 * - JDK 동적 프록시는 '인터페이스가 필수'이기 때문에 V1 애플리케이션만 적용 가능
 */
public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;    // 프록시가 호출할 실제 대상
    private final LogTrace trace;

    public LogTraceBasicHandler(Object target, LogTrace trace) {
        this.target = target;
        this.trace = trace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        TraceStatus status = null;

        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = trace.begin(message);

            // 로직 호출
            Object result = method.invoke(target, args);

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
