package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK 동적 프록시 적용2 - V1 애플리케이션에 JDK 동적 프록시를 적용할 핸드러를 정의
 * - JDK 동적 프록시는 '인터페이스가 필수'이기 때문에 V1 애플리케이션만 적용 가능
 * - no-log 부분은 로그가 남기면 안되기 때문에 특정 조건을 만족할 때만 로그를 남기는 기능을 개발함
 */
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;    // 프록시가 호출할 실제 대상
    private final LogTrace trace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace trace, String[] patterns) {
        this.target = target;
        this.trace = trace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 메서드 이름 필터
        String methodName = method.getName();
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            // 매칭이 안된 메서드들은 실제 서비스 로직만 수행하고, 종료(프록시의 부가 기능 사용 X)
            return method.invoke(target, args);
        }

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
