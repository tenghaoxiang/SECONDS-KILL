package top.haibaraai.secondsKill.controller;

import com.google.common.util.concurrent.RateLimiter;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.haibaraai.secondsKill.domain.JsonData;
import top.haibaraai.secondsKill.domain.Order;
import top.haibaraai.secondsKill.domain.Stock;
import top.haibaraai.secondsKill.rocketmq.OrderProducer;
import top.haibaraai.secondsKill.service.OrderService;
import top.haibaraai.secondsKill.service.StockBloomFilterService;
import top.haibaraai.secondsKill.service.StockService;
import top.haibaraai.secondsKill.service.UserService;
import top.haibaraai.secondsKill.util.DistributedLock;
import top.haibaraai.secondsKill.util.JwtUtils;
import top.haibaraai.secondsKill.util.RedisService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private StockBloomFilterService stockBloomFilterService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderProducer orderProducer;

    private String STOCK_PREFIX = "stock_";

    private String LOCK_PREFIX = "lock_";

//    private RateLimiter limiter = RateLimiter.create(2000);

    @GetMapping("/start")
    public JsonData start(@RequestParam(value = "token", required = false) String token,
                          @RequestParam(value = "id") int stockId) {

        //解析token,获取当前用户id,若解析出错或者token为空,提醒用户进行登录
//        Claims claims = JwtUtils.checkJWT(token);
//        int userId = (Integer) claims.get("id");
        int userId = 1;
        //在本地通过bloom过滤器进行判断此商品是否已经卖完
//        if (stockBloomFilterService.isExist(stockId)) {
//            return success(null, "商品已售空!");
//        }

        Stock stock = null;
        String stockKey = STOCK_PREFIX + stockId;
        String lockKey = LOCK_PREFIX + stockId;

        //限流
//        if (!limiter.tryAcquire()) {
//            return success(null, "活动太火爆了，请重试");
//        }

        //尝试获得商品锁
        while (!distributedLock.lock(lockKey, userId, 1)) {
        }
        try{
            //尝试从redis中获取，若没有则从mysql中获取并添加到redis
            if (redisService.get(stockKey) == null) {
                stock = stockService.findById(stockId);
                redisService.set(stockKey, stock);
            }
            //在redis中预减库存
            if (redisService.decr(stockKey) < 0) {
//                stockBloomFilterService.add(stockId);
                return success(null, "商品已售空!");
            }
        }finally {
            distributedLock.unlock(lockKey, userId);
        }
//        if (redisService.decr(stockKey) < 0) {
//            return success(null, "商品已售空!");
//        }

        try {
            //放入消息队列
            Map<String, Object> map = new HashMap<>();
            map.put("stockId", stockId);
            map.put("userId", userId);
            orderProducer.sendMessage(map);
            return success(null, "秒杀成功!");
        } catch (Exception e) {
            logger.error("Exception: " + e);
            return error(null, "系统发生异常，请重试!");
        }

    }

    @GetMapping("/init")
    @PostConstruct
    public JsonData init() {
        List<Stock> stockList = stockService.findAll();
        String stockKey;
        for (Stock stock : stockList) {
            stockKey = STOCK_PREFIX + stock.getId();
            redisService.set(stockKey, stock.getCount());
        }
        return success(null, "初始化成功!");
    }

}
