package ohmygod.project.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {
    //队列名
    private static final String QUEUE="queue";
    /**
     * 定义一个Spring Bean，创建一个队列对象。
     * 该队列对象用于消息传递，具有持久化特性。
     *
     * @return 返回一个持久化的队列对象
     */
    @Bean
    public Queue queue(){
        // 创建一个持久化的队列对象，队列名称为QUEUE
        return new Queue(QUEUE,true);
    }

}
