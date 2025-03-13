package ohmygod.project.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ohmygod.project.entity.Order;
import ohmygod.project.entity.SeckillGoods;
import ohmygod.project.entity.SeckillOrder;
import ohmygod.project.entity.User;
import ohmygod.project.service.GoodsService;
import ohmygod.project.service.OrderService;
import ohmygod.project.service.SeckillOrderService;
import ohmygod.project.utils.SeckillMessage;
import ohmygod.project.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequestMapping("/seckill")
@Controller
@ResponseBody
public class SeckillController implements InitializingBean {
    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillOrderService seckillOrderService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisTemplate redisTemplate;

    @Resource
    private ohmygod.project.rabbitmq.MQSenderMessage mqSenderMessage;

    @Resource
    private OrderService orderService;

    private HashMap<Long,Boolean> entryStockMap=new HashMap<>();
    @RequestMapping(value="/path",method = RequestMethod.GET)
    @ResponseBody
    public String getPath(User user,Long goodsId){
        if(user==null){
            return "seckillfail";
        }
        String url=orderService.createPath(user,goodsId);
        return url;
    }
    @RequestMapping(value="/doSeckill")
    public String doSeckill(Model model, User user , Long goodsId){
        //获取要抢购的上平信息
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goodsVo.getStockCount()<1){
            return "seckillFail";
        }

        //判断是否重复秒杀,从redis中拿 出订单,解决复购问题
        SeckillOrder o=(SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(o!=null){
            return "seckillFail";
        }

        //如果库存为空,避免总是到redis中去查询库存,给redis增加负担
        if(entryStockMap.get(goodsId)){
            return "seckillFail_stock is null";
        }
        //使用redis分布式锁处理重购和复购

        //获取锁(唯一值)
        String uuid= UUID.randomUUID().toString();
        Boolean lock=redisTemplate.opsForValue().setIfAbsent("lock",uuid,3, TimeUnit.SECONDS);

        if(lock){
            String script="if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            DefaultRedisScript<Long> redisScript=new DefaultRedisScript<>();
            redisScript.setScriptText(script);
            redisScript.setResultType(Long.class);
            //执行自己的业务
            Long decrement=redisTemplate.opsForValue().decrement("seckillGoods:"+goodsId);
            if(decrement<0){
                entryStockMap.put(goodsId,true);
                redisTemplate.opsForValue().increment("seckillGoods:"+goodsId);
                redisTemplate.execute(redisScript, Arrays.asList("lock"),uuid);
                return "seckillFail";
            }
            redisTemplate.execute(redisScript, Arrays.asList("lock"),uuid);
        }
        else{
            return "seckillFail_获取锁失败";
        }
        //抢购
        SeckillMessage seckillMessage=new SeckillMessage(user,goodsId);
        mqSenderMessage.senderMessage(JSONUtil.toJsonStr(seckillMessage));

        return "排队中";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list=goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return ;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue()
                    .set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount().toString());
            //当有库存为false,没有库存为true.防止库存没有了,还去redis中查询
            entryStockMap.put(goodsVo.getId(),false);
        });
    }
}
