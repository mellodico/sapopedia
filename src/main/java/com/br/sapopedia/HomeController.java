package com.br.sapopedia;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/wiki")
    public String showWikiPage() {
        return "wiki";
    }

    @GetMapping("/imagens")
    public String showImagesPage() {
        return "imagens";
    }

    @GetMapping("/mensagens")
    public String showMessagesPage() { return "mensagens"; }

    @GetMapping("/login")
    public String showLogin() { return "login"; }

    @GetMapping("/registro")
    public String showRegister() { return "registro"; }

    @GetMapping("/glossario")
    public String showGlossary() { return "glossario"; }
}