package ohmygod.project.utils;

import ohmygod.project.Validator.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
    private boolean required=false;
    /**
     * 初始化方法，用于设置约束验证器。
     * 该方法重写了父类的initialize方法，以处理特定的约束注解。
     *
     * @param constraintAnnotation 约束注解实例
     */
    @Override
    public void initialize(IsMobile constraintAnnotation) {

        required= constraintAnnotation.required();
    }



    /**
     * 验证给定的字符串是否为有效的手机号码。
     * 如果required为true，则必须验证字符串是否为有效的手机号码。
     * 如果required为false，则当字符串为空时返回true，否则验证字符串是否为有效的手机号码。
     *
     * @param s 要验证的字符串
     * @param constraintValidatorContext 验证上下文
     * @return 如果字符串有效返回true，否则返回false
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        // 如果required为true，直接验证字符串是否为有效的手机号码
        if(required){
            return ValidatorUtils.isMobile(s);
        }else{
            // 如果字符串为空，返回true
            if(!StringUtils.hasText(s)){
                return true;
            }else{
                // 否则验证字符串是否为有效的手机号码
                return ValidatorUtils.isMobile(s);
            }
        }
    }


}
