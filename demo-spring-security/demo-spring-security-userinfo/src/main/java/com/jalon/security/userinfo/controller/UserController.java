package com.jalon.security.userinfo.controller;

import com.jalon.security.userinfo.config.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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
    IAuthenticationFacade authenticationFacade;

    @GetMapping("/home")
    public String home(){
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

    @GetMapping("/userinfo-principal")
    public String userinfoByPrincipal(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/userinfo-authentication")
    public String userinfoByAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("User has authorities: " + userDetails.getAuthorities());
        return authentication.getName();
    }

    @GetMapping("/userinfo-request")
    public String userinfoByRequest(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return principal.getName();
    }

    @GetMapping("/userinfo-custom-interface")
    public String userinfoByCustomInterface() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.getName();
    }


}
