package ohmygod.project.MQdeamo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MQSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void  send(Object msg){
        log.info("发送消息-"+msg);
        rabbitTemplate.convertAndSend("queue",msg);
    }

    public void sendFanout(Object msg){
        log.info("fanout-"+msg);
        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
    }
    public void sendDirect1(Object msg){
        log.info("路由模式发送信息-",msg);
        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
    }
    public void sendDirect2(Object msg){
        log.info("路由模式发送信息-",msg);
        rabbitTemplate.convertAndSend("directExchange","queue.blue",msg);
    }

    public void sendTopic1(Object msg){
        log.info("topic模式发topic1送信息-",msg);
        rabbitTemplate.convertAndSend("topicExchange","queue.red.topic",msg);
    }
    public void sendTopic2(Object msg){
        log.info("topic模式发topic2送信息-",msg);
        rabbitTemplate.convertAndSend("topicExchange",".green.queue.blue.topic",msg);
    }
}
