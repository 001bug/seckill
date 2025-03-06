package ohmygod.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("seckill_user")
public class User implements Serializable {
    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String nickname;

    /*MD5(MD5(pass 明文+固定 salt)+salt)*/
    private String password;

    private String slat;

    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;
}
