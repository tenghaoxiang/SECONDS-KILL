package top.haibaraai.secondsKill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.haibaraai.secondsKill.domain.JsonData;
import top.haibaraai.secondsKill.service.OrderService;
import top.haibaraai.secondsKill.service.StockService;
import top.haibaraai.secondsKill.service.UserService;
import top.haibaraai.secondsKill.util.DistributedLock;
import top.haibaraai.secondsKill.util.RedisService;

@RequestMapping("/second-kill")
@RestController
public class SecKillController extends BasicController {

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    private String USER_PREFIX = "user_";

    private String STOCK_PREFIX = "stock_";

    private String LOCK_PREIX = "lock_";

    @GetMapping("/start")
    public JsonData start(@RequestParam(value = "token") String token,
                          @RequestParam(value = "id") int id) {

        //解析token,获取当前用户,若解析出错或者token为空,提醒用户进行登录
        //本地存储一个map,用来记录商品是否已经卖光，减少redis压力。
        //对redis减少库存
        //存入消息队列

        return success();

    }

}
