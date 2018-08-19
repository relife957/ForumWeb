package com.wangyi.web;


import com.wangyi.cons.CommonConstant;
import com.wangyi.domain.User;
import com.wangyi.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/login")
public class LoginController  extends BaseController{
    private UserService userService ;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/doLogin")
    public ModelAndView login(HttpServletRequest request , User user){
        User dbUser = userService.getUserByUserName(user.getUserName());    //user是客户端传来的信息对象,dbUser是从数据库传来的持久化对象
        ModelAndView mav = new ModelAndView() ;
        mav.setViewName("forward:/login.jsp");
        if (dbUser == null) {
            mav.addObject("errorMsg","用户名不存在") ;
        }else if(!dbUser.getPassword().equals(user.getPassword())){
            mav.addObject("errorMsg","用户密码不正确");
        }else if(dbUser.getLocked() == User.USER_LOCK){
            mav.addObject("errorMsg","用户已经被锁定,不能登录");
        }else{
            dbUser.setLastIp(request.getRemoteAddr());
            dbUser.setLastVisit(new Date());
            userService.loginSuccess(dbUser);
            setSessionUser(request,dbUser);        //保存user的session
            String toUrl = (String)request.getSession().getAttribute(CommonConstant.LOGIN_TO_URL) ;
            request.getSession().removeAttribute(CommonConstant.LOGIN_TO_URL);
            //如果当前回话中没有保存登录之前的请求URL,则直接跳转到主页
            if(StringUtils.isEmpty(toUrl)){
                toUrl = "/index.html" ;
            }
            mav.setViewName("redirect:"+toUrl);
        }
        return mav ;
    }

    @RequestMapping("/doLogout")
    public String logout(HttpSession session){
        session.removeAttribute(CommonConstant.USER_CONTEXT);
        return "forward:/index.jsp" ;
    }
}
