package ohmygod.project.MQdeamo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @RabbitListener(queues="queue")
    public void receive(Object msg){
        log.info("receive message:"+msg);
    }
    @RabbitListener(queues="queue_fanout01")
    public void receive1(Object msg){
        log.info("从queue_fanout01 接受消息"+msg);
    }
    @RabbitListener(queues="queue_fanout02")
    public void receive2(Object msg){
        log.info("from queue_fanout02 接受消息"+msg);
    }
    @RabbitListener(queues="queue_direct01")
    public void queue_direct1(Object msg){
        log.info("queue_direct1 接受消息"+msg);
    }
    @RabbitListener(queues="queue_direct02")
    public void queue_direct2(Object msg){
        log.info("queue_direct2 接受消息"+msg);
    }
    @RabbitListener(queues="queue_topic01")
    public void queue_topic1(Object msg){
        log.info("queue_topic1 接受消息"+msg);
    }
    @RabbitListener(queues="queue_topic02")
    public void queue_topic2(Object msg){
        log.info("queue_topic2 接受消息"+msg);
    }
}
