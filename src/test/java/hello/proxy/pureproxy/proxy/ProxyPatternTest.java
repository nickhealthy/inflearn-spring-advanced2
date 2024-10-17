package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.SubjectImpl;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {

    /**
     * 프록시를 적용하기 전, 프록시를 적용하기 전이라 총 3초가 소요된다.(캐싱 효과 x)
     * 템플릿 콜백 패턴을(전략 패턴, 필드로 전략을 위임) 적용한 것과 같은 코드이다.
     * - ProxyPatternClient(템플릿, Context), SubjectImpl(변경 사항, Strategy)
     */
    @Test
    void noProxyTest() {
        SubjectImpl subject = new SubjectImpl();
        ProxyPatternClient client = new ProxyPatternClient(subject);
        client.execute();
        client.execute();
        client.execute();
    }
}
