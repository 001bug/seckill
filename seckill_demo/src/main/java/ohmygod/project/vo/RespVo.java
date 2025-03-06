package ohmygod.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespVo {
    private long code;
    private String message;
    private Object obj;

    public static RespVo success(Object obj){
        return new RespVo(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),obj);
    }

    public static RespVo success(){
        return new RespVo(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
    }
    public static RespVo error(RespBeanEnum respBeanEnum){
        return new RespVo(respBeanEnum.SUCCESS.getCode(), respBeanEnum.getMessage(), null);
    }
    /**
     * 生成一个表示错误响应的RespVo对象。
     *
     * @param respBeanEnum 响应枚举，包含错误代码和消息
     * @param obj 响应对象，可以包含错误详情或其他数据
     * @return 返回一个包含错误代码、消息和响应对象的RespVo实例
     */
    public static RespVo error(RespBeanEnum respBeanEnum, Object obj){
        // 创建一个新的RespVo对象，传入错误代码、错误消息和响应对象
        return new RespVo(respBeanEnum.getCode(), respBeanEnum.getMessage(),obj);
    }

}
