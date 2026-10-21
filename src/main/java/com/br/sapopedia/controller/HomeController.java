package com.br.sapopedia.controller;

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

    @GetMapping("/glossario")
    public String showGlossary() { return "glossario"; }
}
