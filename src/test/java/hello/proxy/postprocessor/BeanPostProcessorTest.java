package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 빈 후처리기(적용) - 예제 코드
 * - 빈 후처리기를 통해 빈을 스프링 컨테이너에 등록하기 전 빈의 조작을 가능하게 한다.
 * - 프록시처럼 부가기능이나 객체 자체를 바꿔버릴 수도 있다.
 */
public class BeanPostProcessorTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigClass.class);

        /**
         * 실행결과
         *
         * 00:16:07.659 [Test worker] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'beanA'
         * 00:16:07.659 [Test worker] INFO hello.proxy.postprocessor.BeanPostProcessorTest$AToBPostProcessor - beanName = beanA bean = hello.proxy.postprocessor.BeanPostProcessorTest$A@37d80fe7
         * 00:16:07.668 [Test worker] INFO hello.proxy.postprocessor.BeanPostProcessorTest$B - hello B
         */
        // BeanA 이름을 가진 빈은 B 클래스로 등록된다.
        // beanA의 빈 이름은 ConfigClass 설정 클래스처럼 그대로지만, 빈 후처리기에 의해 빈의 객체가 변경된 것을 알 수 있다.
        B bean = applicationContext.getBean("beanA", B.class);
        bean.helloB();

        // A는 이제 빈으로 등록되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
    }

    @Slf4j
    @Configuration
    static class ConfigClass {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        @Bean
        public AToBPostProcessor aToBPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    /**
     * postProcessBeforeInitialization : 객체 생성 이후에 `@PostConstruct` 같은 초기화가 발생하기 전에 호출되는 포스트 프로세서이다.
     * postProcessAfterInitialization : 객체 생성 이후에 `@PostConstruct` 같은 초기화가 발생한 다음에 호출되는 포스트 프로세서이다.
     */
    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName = {} bean = {}", beanName, bean);
            // A Bean이 들어오면 B로 교체해버린다.
            if (bean instanceof A) {
                return new B();
            }

            return bean;

        }
    }
}
