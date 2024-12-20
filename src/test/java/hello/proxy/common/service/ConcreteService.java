package hello.proxy.common.service;

import lombok.extern.slf4j.Slf4j;

/**
 * CGLIB 예제 코드 - 구체 클래스만 있는 서비스 클래스
 */
@Slf4j
public class ConcreteService {
    public void call() {
        log.info("ConcreteService call");
    }
}
