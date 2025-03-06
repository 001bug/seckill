package ohmygod.project.controller;

import lombok.extern.slf4j.Slf4j;
import ohmygod.project.service.UserService;
import ohmygod.project.vo.LoginDto;
import ohmygod.project.vo.LoginVo;
import ohmygod.project.vo.RespVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Resource
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespVo doLogin(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response){
        log.info("{}",loginDto);

        return userService.doLogin(loginDto,request,response);
    }
}
