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
}
