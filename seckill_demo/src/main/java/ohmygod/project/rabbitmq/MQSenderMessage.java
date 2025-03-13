package ohmygod.project.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MQSenderMessage {
    @Resource
    private RabbitTemplate  rabbitTemplate;
    public void senderMessage(String message){
        System.out.println("发送消息了="+message);
        log.info("发送消息: "+message);
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message",message);
    }
}
