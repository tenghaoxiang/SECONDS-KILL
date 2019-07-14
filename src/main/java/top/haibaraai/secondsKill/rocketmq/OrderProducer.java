package top.haibaraai.secondsKill.rocketmq;

import com.google.gson.Gson;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderProducer {

    @Autowired
    private DefaultMQProducer producer;

    private Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendMessage(int stockId, int userId) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Map<String, Integer> map = new HashMap<>();
        map.put("stockId", stockId);
        map.put("userId", userId);
        byte[] bytes = gson.toJson(map).getBytes("UTF-8");
        String key = stockId + "_" + userId;
        Message message = new Message(RocketMQConfig.TOPIC,"", key, bytes);
        SendResult sendResult = producer.send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                int index = (int)arg % mqs.size();
                return mqs.get(index);
            }
        }, stockId);
        logger.info("消息发送成功: " + sendResult);
    }

}
