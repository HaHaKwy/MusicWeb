package com.nju.web.filter;

import com.alibaba.fastjson.JSON;
import com.nju.pojo.ResponseBody;
import com.nju.pojo.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;


@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResonse = (HttpServletResponse)response;
        String path=httpRequest.getRequestURI();

        if(path.contains("Songs") || path.contains("singerImage")){
            path = URLDecoder.decode(path, "utf-8");
            path = path.substring(1);
            path = path.substring(path.indexOf('/'));
            httpRequest.getRequestDispatcher(path).forward(request,response);
        }
        else if(path.contains("allLikelist") ||path.contains("likeSong") || path.contains("publishComment")){
            //1. 判断session中是否有user
            HttpSession session = httpRequest.getSession();
            Object user = session.getAttribute("user");
            //2. 判断user是否为null
            if(user != null){
                chain.doFilter(request, response);
            }else {
                // 没有登陆，存储提示信息，跳转到登录页面
                String jsonString = JSON.toJSONString(new ResponseBody<User>(-1,"未登陆",null));
                httpResonse.setContentType("text/json;charset=utf-8");
                httpResonse.getWriter().write(jsonString);
            }
        }
        else {
            chain.doFilter(request,response);
        }
        return;
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
