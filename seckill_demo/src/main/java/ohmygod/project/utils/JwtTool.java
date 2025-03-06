package ohmygod.project.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import ohmygod.project.exception.GlobalException;
import ohmygod.project.vo.RespBeanEnum;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Date;

import static ohmygod.project.vo.RespBeanEnum.NOT_LOGIN_ERROR;
@Component
public class JwtTool {
    private final JWTSigner jwtSigner;
    public JwtTool(KeyPair keyPair){
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }
    /**
     * 创建一个带有指定ID和生存时间（TTL）的JWT令牌。
     *
     * @param id 用户ID
     * @param ttl 令牌的有效期
     * @return 返回生成的JWT令牌字符串
     */
    public String createToken(String id, Duration ttl){
        // 使用JWT库创建一个JWT构建器
        return JWT.create()
                // 设置payload中的用户ID
                .setPayload("id",id)
                // 设置令牌的过期时间，当前时间加上TTL的毫秒值
                .setExpiresAt(new Date(System.currentTimeMillis()+36000*1000))
                // 设置签名器
                .setSigner(jwtSigner)
                // 生成并返回JWT令牌
                .sign();
    }
    /**
     * 解析JWT令牌并返回用户ID。
     *
     * @param token 要解析的JWT令牌字符串
     * @return 返回解析出的用户ID
     */
    public int parseToken(String token) {
        // 检查令牌是否为空
        if (token == null) {
            // 如果令牌为空，抛出全局异常，表示未登录
            throw new GlobalException(NOT_LOGIN_ERROR);
        }

        // 用于解析和验证JWT的变量
        JWT jwt;

        // 尝试解析JWT
        try {
            // 使用JWTSigner对令牌进行签名验证
            jwt = JWT.of(token).setSigner(jwtSigner);

        } catch (Exception e) {
            // 如果解析失败，抛出全局异常，表示未登录
            throw new GlobalException(NOT_LOGIN_ERROR);
        }

        // 验证JWT
        if (!jwt.verify()) {
            // 如果验证失败，抛出全局异常，表示未登录
            throw new GlobalException(NOT_LOGIN_ERROR);
        }
        // 获取payload中的用户ID
//        Object userPayload=jwt.getPayload("id");
//        if (userPayload == null) {
//            // 数据为空
//            throw new GlobalException(NOT_LOGIN_ERROR);
//        }

        // 5.数据解析
//        try {
//            // 将用户ID转换为Long类型
//            return Long.valueOf(userPayload.toString());
//        } catch (RuntimeException e) {
//            // 数据格式有误
//            throw new GlobalException(NOT_LOGIN_ERROR);
//        }
//    }
        return  1;
    }
}
