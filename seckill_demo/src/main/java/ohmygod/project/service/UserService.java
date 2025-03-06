package ohmygod.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ohmygod.project.entity.User;
import ohmygod.project.vo.LoginDto;
import ohmygod.project.vo.LoginVo;
import ohmygod.project.vo.RespVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {
    RespVo doLogin(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response);
}
