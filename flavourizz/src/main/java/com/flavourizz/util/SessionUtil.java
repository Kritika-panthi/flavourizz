package com.flavourizz.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static void setAttribute(HttpServletRequest req, String key, Object value) {
        HttpSession session = req.getSession();
        session.setAttribute(key, value);
    }

    public static Object getAttribute(HttpServletRequest req, String key) {
        HttpSession session = req.getSession(false);
        return (session != null) ? session.getAttribute(key) : null;
    }

    public static void invalidate(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}