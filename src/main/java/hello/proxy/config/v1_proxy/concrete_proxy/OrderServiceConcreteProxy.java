package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace trace;

    /**
     * 클래스 기반 프록시의 단점
     *
     * - super(null); - 자식 클래스를 생성할 땐 항상 super() 로 부모 클래스의 생성자를 호출해야 하는데,
        부모 클래스에는 파라미터가 있는 생성자로서 아래와 같이 불필요한 코드가 들어가게 된다.
     * - 프록시는 부모 객체의 기능을 사용하지 않기 때문에 super(null); 로 입력해도 된다.
     * - 인터페이스 기반 프록시는 이런 고민을 하지 않아도 된다.
     */
    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace trace) {
        super(null);
        this.target = target;
        this.trace = trace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.orderItem()");
            // target 호출
            target.orderItem(itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
