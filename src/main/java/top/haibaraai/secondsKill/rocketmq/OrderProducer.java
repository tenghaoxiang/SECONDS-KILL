package top.haibaraai.secondsKill.rocketmq;

import com.google.gson.Gson;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class OrderProducer {

    @Autowired
    private DefaultMQProducer producer;

    private Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendMessage(Object map) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        byte[] bytes = gson.toJson(map).getBytes("UTF-8");
        Message message = new Message(RocketMQConfig.TOPIC, bytes);
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("消息发送成功: " + sendResult);

            }

            @Override
            public void onException(Throwable e) {
                logger.error("while producer send message occurs: " + e);
            }
        });
    }

}
