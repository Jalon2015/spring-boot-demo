package com.jalon.security.manually.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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

    @Autowired
    AuthenticationManager authManager;

    @PostMapping(path="/manually-login", consumes={APPLICATION_FORM_URLENCODED_VALUE})
    public String manuallyLogin(HttpServletRequest request, String username, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authManager.authenticate(authenticationToken);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
        System.out.println("是否认证通过："+context.getAuthentication());
        return "redirect:/home" ;
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
