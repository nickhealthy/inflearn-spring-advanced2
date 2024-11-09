package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 강의: 8장 - @Aspect 프록시 - 적용
 * 이전 7장 마지막 강의에서 어드바이저(Advisor)를 빈으로 등록하면
 * 스프링에서 제공하는 자동 프록시 생성기(AnnotationAwareAspectJAutoProxyCreator)를 통해 자동으로 프록시를 적용해준다고 했었다.
 * 이번에는 어노테이션을 통해 조금 더 편리하게 빈(Advisor)을 등록하지 않고도 자동으로 프록시를 적용하는 코드이다.
 *
 */
@Slf4j
@Aspect // 애노테이션 기반 프록시를 적용할 때 필요하다
public class LogTraceAspect {

    private final LogTrace trace;

    public LogTraceAspect(LogTrace trace) {
        this.trace = trace;
    }

    /**
     * - Around의 값에 포인트컷 표현식을 넣는다.
     * - Around의 메서드는 어드바이스가 된다.
     * @param ProceedingJoinPoint joinPoint: 어드바이스에서 살펴본 MethodInvocation invocation과 유사한 기능이다.
     */
    @Around("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        TraceStatus status = null;

        log.info("target={}", joinPoint.getTarget());   // 실제 호출 대상
        log.info("getArgs={}", joinPoint.getArgs());    // 전달인자
        log.info("getSignature={}", joinPoint.getSignature());  // join point 시그니처

        try {
            String message = joinPoint.getSignature().toShortString();
            status = trace.begin(message);

            // 로직 호출
            Object result = joinPoint.proceed();

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
