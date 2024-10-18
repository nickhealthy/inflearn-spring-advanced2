package hello.proxy.pureproxy.concreteproxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 구체 클래스에 프록시를 전용(인터페이스 사용 X, 클래스 상속 사용)
 */
@Slf4j
@RequiredArgsConstructor
public class TimeProxy extends ConcreteLogic {

    private final ConcreteLogic realLogic;

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();

        String result = realLogic.operation();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime={} ms", resultTime);

        return result;
    }
}
