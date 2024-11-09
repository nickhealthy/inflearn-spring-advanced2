package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 강의: 스프링이 제공하는 빈 후처리기1
 * Advisor 만 빈으로 등록해주면 스프링이 제공하는 빈 후처리기(AnnotationAwareAspectJAutoProxyCreator)가
 * Advisor 의 PointCut 을 통해 자동으로 프록시를 적용할 지, 적용하지 않을 지 판단 후 스프링 컨테이너에 빈을 등록하게 된다.
 * - 따라서 별도로 이전처럼 BeanPostProcessor 빈 후처리기를 등록하지 않아도 된다.
 */
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    // 의도치 않게 스프링의 메서드에도 어드바이스가 적용된다.(포인트컷의 한계)
//    @Bean
    public Advisor advisor1(LogTrace trace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(trace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    // AspectJ 라는 포인트컷 표현식을 통해 정밀한 표현이 가능.
    // 현재는 `package`를 기준으로 포인트컷을 매칭했기 떄문에 no-log도 출력하는 문제가 발생
//    @Bean
    public Advisor advisor2(LogTrace trace) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
        LogTraceAdvice advice = new LogTraceAdvice(trace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    // no-log 출력안됨
    @Bean
    public Advisor advisor3(LogTrace trace) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");
        LogTraceAdvice advice = new LogTraceAdvice(trace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
