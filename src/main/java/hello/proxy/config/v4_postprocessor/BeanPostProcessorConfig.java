package hello.proxy.config.v4_postprocessor;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.config.v4_postprocessor.postprocessor.PackageLogTraceProxyPostProcessor;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 강의: 빈 후처리기 - 적용
 * - V1, V2, V3 의 프록시 적용을 이 클래스로 모두 적용시켜 스프링 빈으로 등록한다.
 */
@Slf4j
@Configuration
// 빈은 기본적으로 등록되어 있어야 빈 후처리기를 통해 처리할 수 있다.({AppV1Config.class, AppV2Config.class})
// V3 버전은 컴포넌트 스캔 대상이기 때문에 별도로 빈으로 등록하지 않아도 된다.
// @Import 를 스프링의 시작점에 등록해도 상관없다.
@Import({AppV1Config.class, AppV2Config.class})
public class BeanPostProcessorConfig {

    @Bean
    public PackageLogTraceProxyPostProcessor logTraceProxyPostProcessor(LogTrace trace) {
        return new PackageLogTraceProxyPostProcessor("hello.proxy.app", getAdvisor(trace));
    }

    private Advisor getAdvisor(LogTrace trace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(trace);

        // advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
