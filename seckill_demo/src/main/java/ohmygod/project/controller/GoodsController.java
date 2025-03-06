package ohmygod.project.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohmygod.project.entity.User;
import ohmygod.project.service.UserService;
import ohmygod.project.utils.JwtTool;
import ohmygod.project.vo.GoodsVo;
import ohmygod.project.vo.LoginDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private final JwtTool jwtTool;
    @Resource
    private UserService userService;
    @Resource
    private ohmygod.project.service.GoodsService goodsService;
    @RequestMapping("/toList")
    public String toList(LoginDto dto, HttpServletRequest request, HttpServletResponse response){
        String authHeader=request.getHeader("Authorization");
        // 校验是否为空，并且是否以 "Bearer " 开头
        String token=authHeader.substring(7);
        try{
            jwtTool.parseToken(token);
        } catch(Exception e){
            log.warn(e.getMessage());
            return null;
        }

        return "goodsList";
    }
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model,
                           @PathVariable Long goodsId,
                           User user){
        model.addAttribute("user",user);
        //find goodsVo by goodsId
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate=goodsVo.getStartDate();
        Date endDate=goodsVo.getEndDate();
        Date nowDate=new Date();
        //秒杀状态
        int secKillStatus=0;
        //秒杀倒计时
        int remainSeconds=0;
        if(nowDate.before(startDate)){
            //秒杀还没开始，倒计时
            remainSeconds=(int)((startDate.getTime()-nowDate.getTime())/1000);
        }else if(nowDate.after(endDate)){
            //秒杀已经结束
            secKillStatus=2;
            remainSeconds=-1;
        }else{
            //秒杀进行中
            secKillStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("goods",goodsVo);
        return "goodsDetail";
    }
}
