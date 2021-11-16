package com.jalon.security.userinfo.config;

import org.springframework.security.core.Authentication;

/**
 * <p>
 *
 * </p>
 *
 * @author: syj
 * @date: 2021/11/16
 */
public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
