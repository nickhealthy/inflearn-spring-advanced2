package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
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


    /**
     * 프록시 적용, 1초만 소요되었다.(캐시 효과 O)
     * 프록시 안에 이전에 실제 객체를 참조한 적이 있으면 내부에 value 를 가지고 있으므로
     * 해당 value 를 바로 return 하여 캐시효과를 낼 수 있다.
     * 런타임 의존관계: client -> cacheProxy -> subject(실제 객체) 관계
     */
    @Test
    void cacheProxyTest() {
        SubjectImpl subject = new SubjectImpl();
        CacheProxy cacheProxy = new CacheProxy(subject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
    }
}
