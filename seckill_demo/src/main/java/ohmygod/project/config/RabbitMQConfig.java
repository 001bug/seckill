package ohmygod.project.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {
    //队列名
    private static final String QUEUE="queue";
    private static final String QUEUE1="queue_fanout01";
    private static final String QUEUE2="queue_fanout02";
    private static final String exchange="fanoutExchange";

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

    /**
     * 定义一个Spring Bean，创建一个名为QUEUE1的队列。
     * 该队列是持久化的，即队列中的消息在服务器重启后仍然存在。
     *
     * @return 返回创建的队列对象
     */
    @Bean
    public Queue queue1(){
        // 创建一个持久化(指mq服务器重启后，队列仍然存在)的队列，队列名称为QUEUE1
        return new Queue(QUEUE1,true);
    }
    @Bean
    public Queue queue2(){
        return new Queue(QUEUE2);
    }
    @Bean
    public FanoutExchange exchange(){
        return new FanoutExchange(exchange);
    }
    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue1()).to(exchange());
    }
    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue2()).to(exchange());
    }

    //direct路由模式
    private static final String QUEUE_Direct1= "queue_direct01";
    private static final String QUEUE_Direct2= "queue_direct02";
    private static final String EXCHANGE_Direct= "directExchange";
    private static final String rountingkey01="queue.red";
    private static final String rountingkey02="queue.blue";
    @Bean
    public Queue queue_direct1(){
        return new Queue(QUEUE_Direct1);
    }
    @Bean
    public Queue queue_direct2(){
        return new  Queue(QUEUE_Direct2);
    }
    @Bean
    public DirectExchange exchang_direct(){
        return new DirectExchange(EXCHANGE_Direct);
    }
    /**
     * 定义一个Bean，用于创建一个直接绑定（Direct Binding）。
     * 该绑定将指定的队列与指定的交换机关联，并使用指定的路由键。
     *
     * @return 返回创建的Binding对象
     */
    @Bean
    public Binding binding_direct1(){
        // 使用BindingBuilder创建绑定
        return BindingBuilder.bind(queue_direct1()).to(exchang_direct()).with(rountingkey01);
    }
    //绑定好交换器,并设置好路由
    @Bean
    public Binding  binding_direct2(){
        return BindingBuilder.bind(queue_direct2()).to(exchang_direct()).with(rountingkey02);
    }
}
