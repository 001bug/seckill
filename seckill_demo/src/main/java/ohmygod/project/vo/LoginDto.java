package ohmygod.project.vo;
import lombok.Data;
import ohmygod.project.Validator.IsMobile;

//登录参数
@Data
public class LoginDto {
    @IsMobile
    private String mobile;
    private String password;
}
