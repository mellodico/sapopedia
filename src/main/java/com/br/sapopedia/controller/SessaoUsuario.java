package com.br.sapopedia.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.br.sapopedia.model.Usuario;
import com.br.sapopedia.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * Guarda o id do usuário logado na sessão HTTP e disponibiliza o usuário
 * como "usuarioLogado" para todos os templates.
 */
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
        return id == null ? null : userService.findById(id).orElse(null);
    }

    static Integer idUsuario(HttpSession session) {
        return (Integer) session.getAttribute(ATRIBUTO_ID);
    }
}
