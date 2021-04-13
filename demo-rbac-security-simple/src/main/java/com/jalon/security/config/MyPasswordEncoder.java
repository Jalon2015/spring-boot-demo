package com.jalon.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 *  实现自己的密码加密类
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/12 17:37
 */
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(charSequence.toString());
    }
}
