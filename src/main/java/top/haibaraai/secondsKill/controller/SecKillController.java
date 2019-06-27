package top.haibaraai.secondsKill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.haibaraai.secondsKill.domain.JsonData;
import top.haibaraai.secondsKill.domain.Stock;
import top.haibaraai.secondsKill.domain.User;
import top.haibaraai.secondsKill.service.StockService;
import top.haibaraai.secondsKill.service.UserService;
import top.haibaraai.secondsKill.util.DistributedLock;
import top.haibaraai.secondsKill.util.RedisService;
import top.haibaraai.secondsKill.util.UUIDUtil;

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
    private RedisService redisService;

    private String USER_PREFIX = "user_";

    private String STOCK_PREFIX = "stock_";

    private String LOCK_PREIX = "lock_";

    @GetMapping("/start")
    public JsonData start(@RequestParam(value = "token") String token,
                          @RequestParam(value = "id") int id) {
        User user = null;
        Stock stock = null;
        //暂时写死
        String userKey = USER_PREFIX + 1;
        if ((user = (User) redisService.get(userKey)) == null) {
            //暂时写死
            user = userService.findById(1);
            redisService.set(userKey, user);
        }
//        user = userService.findById(1);
        String lockKey = LOCK_PREIX + id;
        String lockValue = UUIDUtil.generate();
        while (!distributedLock.lock(lockKey, lockValue, 100)) {

        }
        //获得锁成功
        try {
            stock = stockService.findById(id);
            if (stock.getCount() > 0) {
                stockService.decrease(id);
            } else {
                return error(null, "库存不足!");
            }
        } catch (Exception e) {
            logger.error("error occur while secKill: " + e);
            return error(null, "发生异常，请重试");
        }finally {
            //释放锁
            distributedLock.unlock(lockKey, lockValue);
        }
        return success(null, "秒杀成功!");
    }

}
