package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 강의: 빈 후처리기 - 적용
 * - 원본 객체를 프록시 객체로 변환하는 역할을 한다.
 * - 한 곳에서 프록시를 생성한다.(이전처럼 별도로 클래스마다 프록시를 생성하는 코드를 작성하지 않아도 된다.)
 */
@Slf4j
public class PackageLogTraceProxyPostProcessor implements BeanPostProcessor {

    private final String basePackage;
    private final Advisor advisor;

    public PackageLogTraceProxyPostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    /**
     * 수행내용: 스프링 컨테이너에 원본 객체 대신에 객체가 스프링 빈으로 등록된다. 원본 객체는 등록되지 않는다.
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        log.info("param beanName={} bean={}", beanName, bean);

        // 프록시 적용 대상 여부 체크
        // 프록시 적용 대상이 아니면 원본을 그대로 반환
        String packageName = bean.getClass().getPackageName();
        System.out.println("packageName = " + packageName);
        if (!packageName.startsWith(basePackage)) {
            return bean;
        }

        // 프록시 적용 대상이면 프록시로 만들어서 반환
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);

        Object proxy = proxyFactory.getProxy();
        log.info("create proxy: target={} proxy={}", bean.getClass(), proxy.getClass());

        return proxy;
    }
}
