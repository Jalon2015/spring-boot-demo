package com.jalon.security.session.controller;

import com.jalon.security.session.entity.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/6/2 14:55
 */
@RestController
public class UserController {

    @Autowired
    Cat cat;

    @GetMapping("/cat")
    public String cat(){
        System.out.println(cat);
        return cat.toString();
    }

    @GetMapping("/home")
    public String home(HttpSession session){
        System.out.println(session);
        return "home";
    }

    @GetMapping("/userinfo")
    public String userinfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }else{
            return "";
        }
    }


}
