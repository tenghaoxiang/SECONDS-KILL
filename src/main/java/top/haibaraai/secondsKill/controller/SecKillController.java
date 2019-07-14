package top.haibaraai.secondsKill.controller;

import com.google.common.util.concurrent.RateLimiter;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.haibaraai.secondsKill.domain.JsonData;
import top.haibaraai.secondsKill.domain.Stock;
import top.haibaraai.secondsKill.rocketmq.OrderProducer;
import top.haibaraai.secondsKill.service.*;
import top.haibaraai.secondsKill.util.DistributedLock;
import top.haibaraai.secondsKill.util.JwtUtils;
import top.haibaraai.secondsKill.util.RedisService;

import javax.annotation.PostConstruct;
import java.util.List;

@RequestMapping("/second-kill")
@RestController
public class SecKillController extends BasicController {

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private StockService stockService;

    @Autowired
    private FlagService flagService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StockBloomFilterService stockBloomFilterService;

    @Autowired
    private OrderProducer orderProducer;

    private String STOCK_PREFIX = "stock_";

    private String LOCK_PREFIX = "lock_";

    /**
     * 记录商品是否卖光
     * 0 表示有货
     * 1 表示无货
     */
    private byte[] sellOut;

    //进行限流
    private RateLimiter limiter = RateLimiter.create(500);

    @GetMapping("/start")
    public JsonData start(@RequestParam(value = "token") String token,
                          @RequestParam(value = "id") int stockId) {

        //解析token,获取当前用户id,若解析出错或者token为空,提醒用户进行登录
        Claims claims = JwtUtils.checkJWT(token);
        int userId = (Integer) claims.get("id");
//        int userId = 1;

        /**
         * 用布隆过滤器记录已卖完的商品可能会出现问题，所以采用byte[]数组进行记录。
         * 为了降低redis压力
         */
        if (sellOut[stockId] == 1) {
            return success(null, "商品已售空!");
        }

        /**
         * 进行限流
         */
        if (!limiter.tryAcquire()) {
            return success(null, "活动太火爆了，请重试");
        }

        /**
         * 商品
         */
        Stock stock = null;
        /**
         * redis 商品key
         */
        String stockKey = STOCK_PREFIX + stockId;
        /**
         * redis 分布式锁key
         */
        String lockKey = LOCK_PREFIX + stockId;

        /**
         * 尝试获得商品锁
         */
        while (!distributedLock.lock(lockKey, userId, 1)) {
            /**
             * 不成功一直尝试获取
             */
        }
        /**
         * 成功获得锁
         */
        try{
            //尝试从redis中获取，若没有则从mysql中获取并添加到redis
            Integer left;
            if ((left = (Integer) redisService.get(stockKey)) == null) {
                /**
                 * 若此时key被移除或者redis宕机，需要从数据库中获取，但可能消息队列信息还没有执行完，
                 * 此时直接从数据库读取库存可能会造成超卖问题。所以需要等消息队列执行完成才能读取库存。
                 * (以上分析仅限单机模式，若是集群则需另外考虑)
                 */
                //等待消息队列中消息执行完
                plan(stockId);
                //消息队列中消息已执行完，可以从数据库查询库存
                stock = stockService.findById(stockId);
                left = stock.getCount();
                redisService.set(stockKey, stock.getCount());
            }
            if (left == 0) {
                return success(null, "商品已售空!");
            } else if (left == 1) {
                sellOut[stockId] = 1;
            }
            //在redis中预减库存
            redisService.decr(stockKey);
        }finally {
            /**
             * 释放分布式锁
             */
            distributedLock.unlock(lockKey, userId);
        }

        try {
            //判断是否已经购买过
            String key = stockId + "_" + userId;
            if (stockBloomFilterService.isExist(key)) {
                return success(null, "不能重复购买!");
            }
            stockBloomFilterService.add(key);
            //放入消息队列
            orderProducer.sendMessage(stockId, userId);
            return success(null, "秒杀成功!");
        } catch (Exception e) {
            logger.error("Exception: " + e);
            return error(null, "系统发生异常，请重试!");
        }

    }

    /**
     * 进行系统初始化，系统启动时自动调用一次
     * @return
     */
    @GetMapping("/init")
    @PostConstruct
    public JsonData init() {
        List<Stock> stockList = stockService.findAll();
        String stockKey;
        for (Stock stock : stockList) {
            stockKey = STOCK_PREFIX + stock.getId();
            redisService.set(stockKey, stock.getCount());
            flagService.update(stockKey, 0);
        }
        sellOut = new byte[stockList.size() + 1];
        return success(null, "初始化成功!");
    }

    /**
     * 当redis中的key被移除或者redis宕机时调用此方法
     */
    private void plan(int stockId) {
        try {
            orderProducer.sendMessage(stockId, -1);
        } catch (Exception e) {
            logger.error("给生产者发送消息时出现错误:" + e);
        }
        String queue = STOCK_PREFIX + stockId;
        while (flagService.selectByQueue(queue) != 1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        flagService.update(queue, 0);
    }

}
