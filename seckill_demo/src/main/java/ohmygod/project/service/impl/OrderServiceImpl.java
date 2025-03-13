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
import ohmygod.project.utils.MD5Util;
import ohmygod.project.utils.UUIDUtil;
import ohmygod.project.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

        //原子性库存扣减
        boolean update=seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count=stock_count-1")//执行sql语句,减少库存
                        .eq("goods_id",goodsVo.getId())//更新指定商品的库存
                .gt("stock_count",0));//库存大于0时才允许扣减
        //对数据库里面的数据进行扣减
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goodsVo.getId()));

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
        try {
            seckillOrderService.save(seckillOrder);
        } catch (Exception e) {
            System.out.println("订单保存失败：" + e.getMessage());
            e.printStackTrace();
        }

        //将生成的订单放入redis. 这样再次查询订单的时候不需要去数据库查询
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goodsVo.getId(),seckillOrder);
        return order;
    }

    @Override
    public String createPath(User user, Long goodsId) {

        String str= MD5Util.md5(UUIDUtil.uuid()+"123456");
        //将随机生成的秒杀路径保存到redis中,设置时间为60s
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,str,60, TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if(user==null||goodsId<0){
            return false;
        }
        //取出redis里面保存的对应的用户秒杀路径
        String str=(String) redisTemplate.opsForValue().get("seckillPath:"+user.getId()+":"+goodsId);

        return path.equals(str);
    }
}
