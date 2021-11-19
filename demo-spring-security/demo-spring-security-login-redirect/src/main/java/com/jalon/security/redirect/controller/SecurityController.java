package com.jalon.security.redirect.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 *  安全控制器
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/12 18:03
 */
@Controller
@Slf4j
public class SecurityController {

    @RequestMapping("/login")
    public String login(){
        log.info("=== login ===");
        return "login";
    }

    @RequestMapping("/home")
    public String home(Model model){
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "home";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "admin";
    }

    @RequestMapping("/accessDenied")
    public String accessDenied(Model model){
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "access_denied";
    }

    // 获取当前登录的用户名
    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // 获取当前登录的用户角色
    private String getAuthority(){
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        ArrayList<String> list = new ArrayList<>();
        for(GrantedAuthority authority: authorities){
            list.add(authority.getAuthority());
        }
        log.info("=== authority：" + list);
        return list.toString();
    }
}
