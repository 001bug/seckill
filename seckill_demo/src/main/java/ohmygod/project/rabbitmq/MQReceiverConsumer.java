package ohmygod.project.rabbitmq;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import ohmygod.project.entity.User;
import ohmygod.project.service.GoodsService;
import ohmygod.project.service.OrderService;
import ohmygod.project.utils.SeckillMessage;
import ohmygod.project.vo.GoodsVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



@Service
@Slf4j
public class MQReceiverConsumer {
    @Resource
    private GoodsService goodsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private OrderService orderService;

    @RabbitListener(queues="seckillQueue")
    public void receive(String message){
        log.info("receive message:"+message);
        SeckillMessage seckillMessage= JSONUtil.toBean(message, SeckillMessage.class);
        Long goodsId=seckillMessage.getGoodsId();
        User user=seckillMessage.getUser();
        //获取抢购的商品信息
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(goodsId);
        orderService.seckill(user,goodsVo);
    }
}
