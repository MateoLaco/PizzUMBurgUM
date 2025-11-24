package com.example.PizzUMBurgUM.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String uri = req.getRequestURI();
        // Protegemos /cliente/** y /funcionario/** (ajustá si necesitás)
        boolean zonaPrivada = uri.startsWith("/cliente/") || uri.startsWith("/funcionario/");
        if (!zonaPrivada) return true;

        HttpSession session = req.getSession(false);
        boolean logueado = session != null &&
                (session.getAttribute("clienteLogueado") != null || session.getAttribute("funcionarioLogueado") != null);

        if (logueado) return true;

        // Guardar a dónde iba (con querystring)
        String qs = req.getQueryString();
        String full = qs == null ? uri : uri + "?" + qs;
        req.getSession(true).setAttribute("NEXT_URL", full);

        res.sendRedirect(req.getContextPath() + "/auth/login");
        return false;
    }
}
