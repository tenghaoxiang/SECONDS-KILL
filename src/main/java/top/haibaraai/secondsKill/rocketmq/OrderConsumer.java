package top.haibaraai.secondsKill.rocketmq;

import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
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
import top.haibaraai.secondsKill.service.OrderService;
import top.haibaraai.secondsKill.service.StockService;
import top.haibaraai.secondsKill.service.UserService;

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

    public OrderConsumer() throws MQClientException {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(RocketMQConfig.NAME_SERVER_ADDR);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //监听主题以及标签
        consumer.subscribe(RocketMQConfig.TOPIC, "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    Map<String, Object> map = new HashMap<>();
                    for (MessageExt msg : msgs) {
                        byte[] bytes = msg.getBody();
                        String string = new String(bytes);
                        logger.info("consumer receive message: " + string);
                        map = gson.fromJson(string, map.getClass());
                        int userId = ((Double) map.get("userId")).intValue();
                        int stockId = ((Double) map.get("stockId")).intValue();
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
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    logger.error("while consume occur:" + e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        consumer.start();
    }

}
