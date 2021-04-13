package com.jalon.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  认证成功处理器，用来处理登录成功后页面的跳转（根据对应权限跳转）
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/12 17:48
 */
@Component
@Slf4j
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    // Spring Security 通过 RedirectStrategy 对象负责所有重定向事务
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    // 覆写 handle 方法，通过 redirectStrategy 策略重定向到指定的url
    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    // 从 Authentication 对象提取登录用户的角色， 根据角色返回对应的url
    public String determineTargetUrl(Authentication authentication){
        String url = "";
        // 获取角色集合
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        ArrayList<String> roles = new ArrayList<>();
        // java8 流式操作可以优化
        for (GrantedAuthority grantedAuthority : authorities){
            roles.add(grantedAuthority.getAuthority());
        }
        if(isAdmin(roles)){
            url = "/admin";
        }else if(isUser(roles)){
            url = "/home";
        }else{
            url = "/accessDenied";
        }
        log.info("url: " + url);
        return url;
    }

    private boolean isUser(List<String> roles){
        if(roles.contains("ROLE_USER")){
            return true;
        }
        return false;
    }

    private boolean isAdmin(List<String> roles){
        if(roles.contains("ROLE_ADMIN")){
            return true;
        }
        return false;
    }

}
