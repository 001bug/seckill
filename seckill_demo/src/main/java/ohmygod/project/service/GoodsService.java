package ohmygod.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ohmygod.project.entity.Goods;
import ohmygod.project.vo.GoodsVo;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    List<GoodsVo> findGoodsVo();
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
