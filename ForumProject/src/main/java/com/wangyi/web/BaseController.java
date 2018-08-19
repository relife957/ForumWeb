package com.wangyi.web;

import com.wangyi.cons.CommonConstant;
import com.wangyi.domain.User;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    protected static final String ERROR_MSG_KEY = "errorMsg" ;

    /**
     * 获取某个用户的session
     * @param request
     * @return
     */
    protected User getSessionUser(HttpServletRequest request){
        return (User)request.getSession().getAttribute(CommonConstant.USER_CONTEXT) ;
    }

    /**
     * 保存某个用户的session
     * @param request
     * @param user
     */
    protected void setSessionUser(HttpServletRequest request ,User user){
        request.getSession().setAttribute(CommonConstant.USER_CONTEXT,user);
    }

    public final String getAppbaseUrl(HttpServletRequest request,String url){
        Assert.hasLength(url,"url不能为空");
        Assert.isTrue(url.startsWith("/"),"必须以/开头");
        return request.getContextPath() + url ;
    }
}
