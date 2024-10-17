package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.decorator.code.DecoratorPatternClient;
import hello.proxy.pureproxy.decorator.code.RealComponent;
import org.junit.jupiter.api.Test;

public class DecoratorPatternTest {

    /**
     * 데코레이터 패턴을 이해하기 위한 예제코드(이전 프록시 적용 전 예제코드와 비슷하다)
     */
    @Test
    void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }
}
