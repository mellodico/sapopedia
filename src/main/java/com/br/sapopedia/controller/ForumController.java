package com.br.sapopedia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.br.sapopedia.config.FotoInvalidaException;
import com.br.sapopedia.model.Topico;
import com.br.sapopedia.service.ForumService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("/mensagens")
    public String showMessagesPage(Model model) {
        model.addAttribute("mensagens", forumService.listTopics());
        return "mensagens";
    }

    @PostMapping("/mensagens")
    public String addMessage(@RequestParam String titulo,
                             @RequestParam String tipo,
                             @RequestParam String mensagem,
                             @RequestParam(required = false) List<MultipartFile> fotos,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Integer usuarioId = SessaoUsuario.idUsuario(session);
        if (usuarioId == null) return "redirect:/login?voltar=/mensagens";

        String erro = null;
        if (titulo.trim().length() < 2 || titulo.trim().length() > 80) erro = "O título deve ter de 2 a 80 caracteres.";
        else if (!ForumService.TIPOS.contains(tipo)) erro = "Tipo de mensagem inválido.";
        else if (mensagem.trim().length() < 5 || mensagem.trim().length() > 500)
            erro = "A mensagem deve ter de 5 a 500 caracteres.";

        if (erro == null) {
            List<MultipartFile> fotosEnviadas = fotos == null ? List.of()
                : fotos.stream().filter(f -> f != null && !f.isEmpty()).toList();
            try {
                forumService.createTopic(usuarioId, titulo, tipo, mensagem, fotosEnviadas);
                redirectAttributes.addFlashAttribute("sucesso", "Mensagem publicada com sucesso.");
                return "redirect:/mensagens";
            } catch (FotoInvalidaException e) {
                erro = e.getMessage();
            }
        }
        redirectAttributes.addFlashAttribute("erro", erro);
        return "redirect:/mensagens";
    }

    @GetMapping("/mensagens/{id}")
    public String showMessage(@PathVariable int id, Model model) {
        model.addAttribute("mensagem", findTopic(id));
        model.addAttribute("comentarios", forumService.listReplies(id));
        return "post";
    }

    @PostMapping("/mensagens/{id}/comentarios")
    public String addComment(@PathVariable int id,
                             @RequestParam String comentario,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Integer usuarioId = SessaoUsuario.idUsuario(session);
        if (usuarioId == null) return "redirect:/login?voltar=/mensagens/" + id;

        findTopic(id);
        if (comentario.trim().length() < 2 || comentario.trim().length() > 500) {
            redirectAttributes.addFlashAttribute("erro", "O comentário deve ter de 2 a 500 caracteres.");
        } else {
            forumService.createReply(usuarioId, id, comentario);
            redirectAttributes.addFlashAttribute("sucesso", "Comentário publicado com sucesso.");
        }
        return "redirect:/mensagens/" + id + "#comentarios";
    }

    private Topico findTopic(int id) {
        return forumService.findTopic(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));
    }
}
