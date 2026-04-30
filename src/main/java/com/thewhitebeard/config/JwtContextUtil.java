package com.thewhitebeard.config;

import com.thewhitebeard.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;

public class JwtContextUtil {

    public static Long getUserId(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("userId");
        if (id == null) throw new UnauthorizedException("You are not logged in");
        return id;
    }

    public static String getRole(HttpServletRequest request) {
        String role = (String) request.getAttribute("userRole");
        if (role == null) throw new UnauthorizedException("You are not logged in");
        return role;
    }

    public static void requireRole(HttpServletRequest request, String... allowedRoles) {
        String role = getRole(request);
        for (String allowed : allowedRoles) {
            if (allowed.equalsIgnoreCase(role)) return;
        }
        throw new UnauthorizedException("Access denied. Required role: " + String.join(" or ", allowedRoles));
    }

    public static boolean hasRole(HttpServletRequest request, String role) {
        try {
            return role.equalsIgnoreCase(getRole(request));
        } catch (Exception e) {
            return false;
        }
    }
}
