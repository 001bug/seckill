package ohmygod.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ohmygod.project.entity.Order;
import ohmygod.project.entity.SeckillGoods;
import ohmygod.project.entity.SeckillOrder;
import ohmygod.project.entity.User;
import ohmygod.project.service.GoodsService;
import ohmygod.project.service.OrderService;
import ohmygod.project.service.SeckillOrderService;
import ohmygod.project.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@RequestMapping("/seckill")
@Controller
@ResponseBody
public class SeckillController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillOrderService seckillOrderService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisTemplate redisTemplate;

    @Resource
    private OrderService orderService;
    @RequestMapping(value="/doSeckill")
    public String doSeckill(Model model, User user , Long goodsId){

        //判断是否重复秒杀,从redis中拿出订单,解决复购问题
        SeckillOrder o=(SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(o!=null){
            return "seckillFail";
        }
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            return "seckillFail";
        }
        //减库存
        Order order=orderService.seckill(user,goodsVo);
        if(order==null){
            return "seckillFail";
        }
        return "seckillSuccess";
//        SeckillOrder seckillOrder=seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",user.getId()).eq("goods_id",goodsVo.getId()));
//        if(seckillOrder!=null){
//            return "seckillFail";
//        }
//        Order order=orderService.seckill(user,goodsVo);
//        if(order==null){
//            return "seckillFail";
//        }
    }

}
