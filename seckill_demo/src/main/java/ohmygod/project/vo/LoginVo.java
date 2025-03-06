package ohmygod.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class LoginVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String mobile;
    private String token;
}
