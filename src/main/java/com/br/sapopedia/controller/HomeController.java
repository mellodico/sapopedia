package com.br.sapopedia.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private static final List<Mensagem> MENSAGENS = new ArrayList<>(List.of(
        new Mensagem("Melhor terrário para pererecas", "sapoverde13", "opiniao",
            "Depois de testar alguns modelos, acho que os terrários verticais com bastante vegetação são os que deixam as pererecas mais à vontade. A umidade constante faz toda a diferença no comportamento delas ao longo do dia.", false),
        new Mensagem("Sugestão: página de sons dos anuros", "biopaula", "sugestao",
            "Seria muito legal se a SapoPédia tivesse uma seção com os cantos de acasalamento gravados em campo. Ajudaria bastante na identificação de espécies parecidas, principalmente à noite.", false),
        new Mensagem("Fotografei um girino gigante hoje!", "renatocampos", "avistamento",
            "Estava em uma trilha perto de casa e encontrei um girino enorme, provavelmente perto da metamorfose. Tirei algumas fotos de perto pra registrar o tamanho das patas já formadas.", true),
        new Mensagem("Dúvida sobre coloração do Pithecopus", "ana.herpeto", "duvida",
            "Alguém sabe se a coloração muda entre o dia e a noite nessa espécie? Reparei uma diferença bem grande em fotos tiradas em horários diferentes, quase parecem animais distintos.", true),
        new Mensagem("Sugestão de melhoria no glossário", "joaoanuro", "sugestao",
            "Achei o glossário ótimo, mas senti falta de termos como \"amplexo\" e \"desova\". Seria interessante a comunidade poder sugerir novos termos técnicos direto por aqui.", false)));

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
    public String showMessagesPage(Model model) {
        model.addAttribute("mensagens", MENSAGENS);
        return "mensagens";
    }

    @PostMapping("/mensagens")
    public String addMessage(@RequestParam String titulo,
                             @RequestParam String tipo,
                             @RequestParam String mensagem,
                             RedirectAttributes redirectAttributes) {
        MENSAGENS.add(new Mensagem(titulo, "comunidade SapoPédia", tipo, mensagem, false));
        redirectAttributes.addFlashAttribute("sucesso", "Mensagem publicada com sucesso.");
        return "redirect:/mensagens";
    }

    @GetMapping("/login")
    public String showLogin() { return "login"; }

    @GetMapping("/registro")
    public String showRegister() { return "registro"; }

    @GetMapping("/conta")
    public String showAccount() { return "conta"; }

    @GetMapping("/glossario")
    public String showGlossary() { return "glossario"; }

    public record Mensagem(String titulo, String autor, String tipo, String mensagem, boolean temImagem) {
        public String tipoLabel() {
            return switch (tipo) {
                case "opiniao" -> "Opinião";
                case "sugestao" -> "Sugestão";
                case "avistamento" -> "Avistamento";
                case "duvida" -> "Dúvida";
                default -> "Outro";
            };
        }
    }
}