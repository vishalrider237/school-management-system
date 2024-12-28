package com.school.config;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
     HttpServletResponse response = (HttpServletResponse) servletResponse;
     HttpServletRequest request = (HttpServletRequest) servletRequest;
     Map<String,String>map=new HashMap<>();
     String originHeader=request.getHeader("Origin");
        System.out.println("Request Origin: " + originHeader);
        response.setHeader("Access-Control-Allow-Origin", originHeader);
     response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
     response.setHeader("Access-Control-Max-Age", "3600");
     response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, X-Requested-With");
     response.setHeader("Access-Control-Expose-Headers", "Authorization");
     response.setHeader("Access-Control-Allow-Headers", "Authorization,X-Pingother,Origin,X-Requested-With,Content-Type,Accept,X-Custom-header");
     if ("OPTIONS".equals(request.getMethod())) {
         response.setStatus(HttpServletResponse.SC_OK);
     }
     else{
         filterChain.doFilter(request, response);
     }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}