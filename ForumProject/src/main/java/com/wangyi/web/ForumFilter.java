package com.wangyi.web;

import com.wangyi.domain.User;
import org.apache.commons.lang.StringUtils;

import static com.wangyi.cons.CommonConstant.* ;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ForumFilter implements javax.servlet.Filter {

    private static final String FILTERED_REQUUEST = "@@session_context_filtered_request";

    //不需要登录也能访问的url
    private static final String[] INHERENT_ESCAPE_URIS = {"/indexx.jsp",
                        "index.html","/login.jsp","/login/doLogin.html",
                        "/register.jsp","/register.html","/board/listBardTopics-",
                        "/board/listTopicPosts-"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //保证该过滤器只被调用一次
        if(servletRequest != null && servletRequest.getAttribute(FILTERED_REQUUEST) != null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            //设置标志,防止一次请求被多次过滤
            servletRequest.setAttribute(FILTERED_REQUUEST,Boolean.TRUE);
            HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest ;
            User userContext = getSessionUser(httpServletRequest);
            
            //如果用户没登录,且当前url资源需要登录才能访问
            if(userContext == null && !isURLLogin(httpServletRequest.getRequestURI(),httpServletRequest)){
                String toUrl = httpServletRequest.getRequestURL().toString() ;
                if(!StringUtils.isEmpty(httpServletRequest.getQueryString())){         //获得请求的参数部分
                    toUrl += "?" + httpServletRequest.getQueryString() ;
                }
                //将用户的请求保存在session中,用于登陆成功后,跳到目标url
                httpServletRequest.getSession().setAttribute(LOGIN_TO_URL,toUrl);

                //转发到登录界面
                servletRequest.getRequestDispatcher("/login.jsp").forward(servletRequest,servletResponse);
                return ;
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }

    private boolean isURLLogin(String requestURI, HttpServletRequest httpServletRequest) {
        if(httpServletRequest.getContextPath().equalsIgnoreCase(requestURI) ||
                (httpServletRequest.getContextPath()+"/").equalsIgnoreCase(requestURI)){
            return true ;
        }
        for(String uri :INHERENT_ESCAPE_URIS){
            if(requestURI != null && requestURI.indexOf(uri)>=0){
                return true ;
            }
        }
        return false ;
    }


    protected User getSessionUser(HttpServletRequest request){
        return (User)request.getSession().getAttribute(USER_CONTEXT);
    }
    @Override
    public void destroy() {

    }
}
