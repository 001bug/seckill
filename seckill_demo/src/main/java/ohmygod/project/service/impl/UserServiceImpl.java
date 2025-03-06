package ohmygod.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohmygod.project.config.JwtProperties;
import ohmygod.project.entity.User;
import ohmygod.project.exception.GlobalException;
import ohmygod.project.mapper.UserMapper;
import ohmygod.project.service.UserService;
import ohmygod.project.utils.JwtTool;
import ohmygod.project.utils.MD5Util;
import ohmygod.project.utils.ValidatorUtils;
import ohmygod.project.vo.LoginDto;
import ohmygod.project.vo.LoginVo;
import ohmygod.project.vo.RespBeanEnum;
import ohmygod.project.vo.RespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    /**
     * 处理用户登录请求。
     *
     * @param loginDto 包含用户登录信息的DTO对象
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 返回登录结果的响应对象
     */
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 处理用户登录请求。
     *
     * @param loginDto 登录请求的数据传输对象，包含手机号和密码
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 返回登录结果的响应对象
     */
    @Override
    public RespVo doLogin(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

        // 获取用户输入的手机号
        String mobile = loginDto.getMobile();

        // 获取用户输入的密码
        String password=loginDto.getPassword();

        // 通过手机号查询用户信息
        User user=userMapper.selectById(mobile);

        // 如果用户不存在，抛出全局异常
        /*if(user==null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }*/

        // 检查用户输入的密码是否与数据库中的密码匹配
//        if(password.equals(user.getPassword())){
//            // 如果密码不匹配，返回登录错误的响应
//            return RespVo.error(RespBeanEnum.LOGIN_ERROR);
//        }

        // 记录成功登录的日志信息
        log.info("{}",RespVo.success());

        // 生成JWT令牌
        String token = jwtTool.createToken(mobile, jwtProperties.getTokenTTL());

        // 创建登录成功返回的对象
        LoginVo loginVo=new LoginVo(mobile,token);
        // 返回成功登录的响应
        Map<String,Object> userMap= BeanUtil.beanToMap(user,new HashMap<>(), CopyOptions.create()
                .setIgnoreNullValue(true)
                .setFieldValueEditor((fieldName, fieldValue) -> fieldValue != null ? fieldValue.toString() : ""));
        String key=String.valueOf(user.getId());
        stringRedisTemplate.opsForValue().set("user"+key,token);

        //redisTemplate.opsForHash().putAll(key,userMap);
        // 返回包含登录信息的成功响应
        return RespVo.success(loginVo);
    }

}
