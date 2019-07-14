package top.haibaraai.secondsKill.rocketmq;

import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.haibaraai.secondsKill.domain.Order;
import top.haibaraai.secondsKill.domain.Stock;
import top.haibaraai.secondsKill.domain.User;
import top.haibaraai.secondsKill.service.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderConsumer {

    private DefaultMQPushConsumer consumer;
    private String consumerGroup = "order_consumer_group";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson = new Gson();

    @Autowired
    private UserService userService;

    @Autowired
    private StockService stockService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockBloomFilterService stockBloomFilterService;

    @Autowired
    private FlagService flagService;

    public OrderConsumer() throws MQClientException {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(RocketMQConfig.NAME_SERVER_ADDR);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //监听主题以及标签
        consumer.subscribe(RocketMQConfig.TOPIC, "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                Map<String, Object> map = new HashMap<>();
                for (MessageExt msg : msgs) {
                    byte[] bytes = msg.getBody();
                    String string = new String(bytes);
                    logger.info("consumer receive message: " + string);
                    map = gson.fromJson(string, map.getClass());
                    int stockId = ((Double) map.get("stockId")).intValue();
                    int userId = ((Double) map.get("userId")).intValue();
                    if (userId != -1) {
                        String key = msg.getKeys();
//                        if (stockBloomFilterService.isExist(key)) {
//                            logger.info("不能重复购买");
//                            continue;
//                        }
                        runDB(stockId, userId);
//                        stockBloomFilterService.add(key);
                    } else {
                        String queue = "stock_" + stockId;
                        flagService.update(queue, 1);
                    }
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
    }

    private void runDB(int stockId, int userId) {
        //修改商品库存
        stockService.decrease(stockId);
        //订单入库
        User user = userService.findById(userId);
        Stock stock = stockService.findById(stockId);
        Order order = new Order();
        order.setUserId(userId);
        order.setStockId(stockId);
        order.setPrice(stock.getPrice());
        order.setAddress(user.getAddress());
        order.setStatus(0);
        order.setCreateTime(new Date());
        order.setFinishTime(new Date());
        orderService.save(order);
    }

}
