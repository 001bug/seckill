package ohmygod.project.exception;

import ohmygod.project.vo.RespBeanEnum;
import ohmygod.project.vo.RespVo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;

@RestControllerAdvice//同一捕获springmvc中@RestController抛出的异常
public class GlobalExceptionHandler {
    /**
     * 异常处理方法，用于捕获并处理不同类型的异常。
     * 该方法根据异常类型返回相应的响应对象（RespVo）。
     *
     * @param e 捕获到的异常
     * @return 返回包含错误信息的RespVo对象
     */
    @ExceptionHandler(Exception.class)
    public RespVo ExceptionHandler(Exception e){
        // 如果异常是GlobalException类型
        if(e instanceof GlobalException){
            // 将异常转换为GlobalException
            GlobalException globalException=(GlobalException)e;
            // 返回包含GlobalException错误信息的RespVo对象
            return RespVo.error(globalException.getRespBeanEnum());
            // 如果异常是BindException类型
        }else if(e instanceof BindException){
            // 将异常转换为BindException
            BindException bindException=(BindException)e;
            // 创建一个包含参数校验错误的RespVo对象
            RespVo respVo=RespVo.error(RespBeanEnum.BING_ERROR);
            // 设置错误信息，包含具体的校验错误
            respVo.setMessage("参数校验异常~"+bindException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            // 返回包含错误信息的RespVo对象
            return respVo;
        }
        // 如果异常类型不匹配，返回一个通用错误的RespVo对象
        return RespVo.error(RespBeanEnum.ERROR);
    }

}
