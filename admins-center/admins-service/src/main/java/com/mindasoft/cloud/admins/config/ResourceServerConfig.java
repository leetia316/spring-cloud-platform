package com.mindasoft.cloud.admins.config;

import com.mindasoft.cloud.commons.oauth2.AuthExceptionEntryPoint;
import com.mindasoft.cloud.commons.oauth2.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: min
 * @date: 2018/12/20 9:27
 * @version: 1.0.0
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .exceptionHandling() //异常处理
            .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)) //认证异常流程
            .and()
            .authorizeRequests() // 匹配需要资源认证路径
            .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                    "/swagger-ui.html", "/webjars/**", "/doc.html","/admin/login")
            .permitAll()            // 匹配不需要资源认证路径
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 返回格式化
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler);
    }
}
