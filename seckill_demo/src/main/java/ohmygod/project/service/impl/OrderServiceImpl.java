package ohmygod.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohmygod.project.entity.Order;
import ohmygod.project.entity.SeckillGoods;
import ohmygod.project.entity.SeckillOrder;
import ohmygod.project.entity.User;
import ohmygod.project.mapper.OrderMapper;
import ohmygod.project.service.OrderService;
import ohmygod.project.service.SeckillGoodsService;
import ohmygod.project.service.SeckillOrderService;
import ohmygod.project.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private SeckillGoodsService seckillGoodsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeckillOrderService seckillOrderService;
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goodsVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);

        boolean update=seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count=stock_count-1").eq("goods_id",goodsVo.getId()).gt("stock_count",0));
        //判断是否扣库存成功
        if(!update){
            return null;
        }
        //生成普通订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderService.save(seckillOrder);

        //将生成的订单放入redis. 这样再次查询订单的时候不需要去数据库查询
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goodsVo.getId(),seckillOrder);
        return order;
    }
}
