package hello.proxy.app.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j

// 스프링MVC는 타입에 @Controller 또는 @RequestMapping 애노테이션이 있어야 스프링 컨트롤러로 인식함
// 여기서는 @Controller 를 사용하면 자동 컴포넌트 스캔의 대상이 되기 때문에 @RequestMapping 사용
@RequestMapping
@ResponseBody
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;

    public OrderControllerV2(OrderServiceV2 orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/v2/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/v2/no-log")
    public String noLog() {
        return "ok";
    }
}