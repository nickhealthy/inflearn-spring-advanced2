package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubjectImpl implements Subject {

    @Override
    public String operation() {
        log.info("실제 객체 호출");
        sleep(1000);    // 호출할 때 마다 시스템에 큰 부하를 주는 데이터 조회(1초 소요)
        return "data";
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
