package ohmygod.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ohmygod.project.entity.Goods;
import ohmygod.project.vo.GoodsVo;

import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods> {
    List<GoodsVo> findGoodsVo();
    //获取指定商品详情-根据id
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
