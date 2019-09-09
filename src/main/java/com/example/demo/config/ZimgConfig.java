package com.example.demo.config;

import lombok.Data;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Data
@Configuration
public class ZimgConfig {
    @Value("${app.zimg.server}")
    private String zimgServer;
    @Value("${app.zimg.router}")
    private String router;

    /**
     * 注册http代理 拦截前台/zimg/* 请求 转发到图片服务器。用于希望 “访问图片需要带上token或session” 的情况
     * */
    @Bean
    @Order(Integer.MAX_VALUE-1)
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet(),getRouter()+"/*");
        //这个setName必须要设置，并且多个的时候，名字需要不一样
        servletRegistrationBean.setName("zimg");
        servletRegistrationBean.addInitParameter("targetUri",getZimgServer());
        servletRegistrationBean.addInitParameter(ProxyServlet.P_LOG, "false");
        return servletRegistrationBean;
    }
}
