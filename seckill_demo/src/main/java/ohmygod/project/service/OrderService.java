package ohmygod.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ohmygod.project.entity.Order;
import ohmygod.project.entity.User;
import ohmygod.project.vo.GoodsVo;

public interface OrderService extends IService<Order> {
    Order seckill(User user, GoodsVo goodsVo);
    String createPath(User user,Long goodsId);
    boolean checkPath(User user,Long goodsId, String path);
}
