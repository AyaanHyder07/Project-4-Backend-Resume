package com.resume.dashboard.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof String) {
            return (String) auth.getPrincipal();
        }
        return null;
    }
}
