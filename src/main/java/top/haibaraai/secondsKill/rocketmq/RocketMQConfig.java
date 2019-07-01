package top.haibaraai.secondsKill.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConfig {

    public static final String NAME_SERVER_ADDR = "192.168.56.101:9876";

    public static final String TOPIC = "SECKILL_TOPIC";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String producerGroup = "order_producer_group";

    @Bean
    public DefaultMQProducer defaultMQProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("while producer start occur: " + e);
        }
        return producer;
    }

}
