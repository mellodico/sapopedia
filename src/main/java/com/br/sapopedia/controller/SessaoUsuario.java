package com.br.sapopedia.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.br.sapopedia.model.Usuario;
import com.br.sapopedia.service.UserService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class SessaoUsuario {

    static final String ATRIBUTO_ID = "usuarioId";

    private final UserService userService;

    public SessaoUsuario(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("usuarioLogado")
    public Usuario usuarioLogado(HttpSession session) {
        Integer id = idUsuario(session);
        if (id == null) return null;
        Usuario usuario = userService.findById(id).orElse(null);
        if (usuario == null) session.removeAttribute(ATRIBUTO_ID);
        return usuario;
    }

    static Integer idUsuario(HttpSession session) {
        return (Integer) session.getAttribute(ATRIBUTO_ID);
    }
}
