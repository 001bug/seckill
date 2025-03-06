package ohmygod.project.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum RespBeanEnum {
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务端异常"),
    //登录模块
    LOGIN_ERROR(500210, "用户名或者密码错误"),
    NOT_LOGIN_ERROR(506,"未登录"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    BING_ERROR(500212, "参数绑定异常"),
    MOBILE_NOT_EXIST(500213, "手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500214, "更新密码失败");
    private final Integer code;
    private final String message;
}
