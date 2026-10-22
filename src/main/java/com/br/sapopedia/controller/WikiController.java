package com.br.sapopedia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.br.sapopedia.service.WikiService;

@Controller
public class WikiController {

    private final WikiService wikiService;

    public WikiController(WikiService wikiService) {
        this.wikiService = wikiService;
    }

    @GetMapping("/wiki")
    public String showWikiPage(Model model) {
        model.addAttribute("glossario", wikiService.listGlossary());
        model.addAttribute("familias", wikiService.listFamilies());
        model.addAttribute("generos", wikiService.listGenera());
        model.addAttribute("especies", wikiService.listSpecies());
        return "wiki";
    }

    @GetMapping("/glossario")
    public String showGlossary() {
        return "redirect:/wiki#glossario";
    }

    @GetMapping("/imagens")
    public String showImagesPage(Model model) {
        model.addAttribute("imagens", wikiService.listImages());
        return "imagens";
    }
}
