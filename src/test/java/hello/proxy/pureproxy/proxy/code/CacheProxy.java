package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 프록시 적용
 */
@Slf4j
public class CacheProxy implements Subject {

    // 클라이언트가 프록시를 호출하면 프록시가 최종적으로 실제 객체를 호출해야하므로,
    // 실제 객체에 대한 참조를 가지고 있어야한다.
    private Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}
