package ohmygod.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ohmygod.project.entity.Goods;
import ohmygod.project.mapper.GoodsMapper;
import ohmygod.project.service.GoodsService;
import ohmygod.project.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements GoodsService{
    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    /**
     * 根据商品ID查找商品视图对象。
     *
     * @param goodsId 商品的唯一标识符
     * @return 返回与商品ID对应的商品视图对象
     */
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        // 调用goodsMapper的findGoodsVoByGoodsId方法，根据商品ID查找商品视图对象
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }

}
