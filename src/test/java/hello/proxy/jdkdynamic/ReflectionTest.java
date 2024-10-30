package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JDK 동적 프록시를 이해하기 위한 기초적인 리플렉션 기술을 알아보기
 */
@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        /**
         * 로직1과 로직2는 호출하는 메서드만 다르고, 전체 코드 흐름이 완전히 같다.
         * - 로직1과 로직2의 공통 로직을 분리하면 좋겠지만, 중간에 호출하는 메서드가 다르기 때문에 공통화가 쉽지 않다.
         * - 따라서 호출하는 메서드인 target.callA(), target.callB() 이 부분만 동적으로 처리할 수 있으면 문제를 해결할 수 있다.
         */
        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA();    // 호출하는 메서드가 다름
        log.info("result={}", result1);
        // 공통 로직1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB();    // 호출하는 메서드가 다름
        log.info("result={}", result2);
        // 공통 로직2 종료
    }

    @Test
    void reflection1() throws Exception {
        // 클래스 정보 - 클래스 메타정보를 획득한다.
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        // callA 메서드 정보 - 해당 클래스의 call 메서드 메타정보를 획득한다.
        Method methodCallA = classHello.getMethod("callA");
        /**
         * 획득한 메서드 메타정보로 실제 인스턴스의 메서드를 호출한다.
         * methodCallA는 Hello 클래스의 callA()이라는 메서드 메타정보이다.
         * methodCallA.invoke(인스턴스)를 호출하면서 인스턴스를 넘겨주면 해당 인스턴스의 callA 메서드를 찾아 실행한다.
         */
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}", result1);

        // callB 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);
    }

    /*
        메서드 정보를 획득해서 메서드를 호출했다.
        여기서 중요한 핵심은 클래스나 메서드 정보를 동적으로 변경할 수 있다는 점이다.
        덕분에 이제 공통 로직을 만들 수 있게 되었다.
     */
    @Test
    void reflection2() throws Exception {
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    /**
     * 로직1, 로직2를 한번에 처리할 수 있는 통합된 공통 로직
     * - Method method: 이제 Method 라는 메타정보를 통해 호출할 메서드 정보가 동적으로 제공된다.
     * - 실제 실행할 인스턴스 정보가 넘어온다.
     */
    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("START");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}
