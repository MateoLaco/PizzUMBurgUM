package com.example.PizzUMBurgUM.config;

import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String uri = req.getRequestURI();
        boolean privada = uri.startsWith("/cliente/") || uri.startsWith("/funcionario/");
        if (!privada) return true;

        HttpSession s = req.getSession(false);
        boolean ok = s != null && (s.getAttribute("clienteLogueado")!=null || s.getAttribute("funcionarioLogueado")!=null);
        if (ok) return true;

        // guardar destino para "volver a donde ibas"
        String qs = req.getQueryString();
        String full = qs==null ? uri : uri+"?"+qs;
        req.getSession(true).setAttribute("NEXT_URL", full);
        res.sendRedirect(req.getContextPath()+"/auth/login");
        return false;
    }
}