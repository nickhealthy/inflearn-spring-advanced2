package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code2.DecoratorPatternClient;
import hello.proxy.pureproxy.decorator.code2.MessageDecorator;
import hello.proxy.pureproxy.decorator.code2.RealComponent;
import hello.proxy.pureproxy.decorator.code2.TimeDecorator;
import org.junit.jupiter.api.Test;

/**
 * Abstract 추상화 적용한 데코레이터 패턴
 */
public class DecoratorPatternTest2 {

    /**
     * 데코레이터 패턴을 이해하기 위한 예제코드(이전 프록시 적용 전 예제코드와 비슷하다)
     */
    @Test
    void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }

    /**
     * 데코레이터 패턴 적용 - 핵심은 클라이언트의 코드를 전혀 건드리지 않았다는 점이다.
     *  - 부가 기능 추가(메시지 변경)
     *
     */
    @Test
    void decorator1() {
        RealComponent realComponent = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
        client.execute();
    }

    /**
     * 데코레이터 패턴 적용 - 핵심은 클라이언트의 코드를 전혀 건드리지 않았다는 점이다.
     *  - 부가 기능 추가(메시지 변경)
     *  - 부가 기능 추가(시간 경과 로그 추가)
     */
    @Test
    void decorator2() {
        RealComponent realComponent = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(realComponent);
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
    }
}
