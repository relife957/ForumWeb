package com.wangyi.web;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForumHandlerExceptionResolver extends SimpleMappingExceptionResolver {
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response
                            ,Object o,Exception e){
        request.setAttribute("ex",e) ;
        e.printStackTrace();
        return super.doResolveException(request,response,o,e);

    }

}
