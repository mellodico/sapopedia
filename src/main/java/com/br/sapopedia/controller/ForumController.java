package com.br.sapopedia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.br.sapopedia.config.FotoInvalidaException;
import com.br.sapopedia.config.SemPermissaoException;
import com.br.sapopedia.model.Topico;
import com.br.sapopedia.model.TopicoForm;
import com.br.sapopedia.service.ForumService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("/mensagens")
    public String showMessagesPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new TopicoForm());
        }
        model.addAttribute("mensagens", forumService.listTopics());
        return "mensagens";
    }

    @PostMapping("/mensagens")
    public String addMessage(@Valid @ModelAttribute("form") TopicoForm form,
                             BindingResult result,
                             @RequestParam(required = false) List<MultipartFile> fotos,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        Integer usuarioId = SessaoUsuario.idUsuario(session);
        if (usuarioId == null) return "redirect:/login?voltar=/mensagens";

        if (result.hasErrors()) {
            model.addAttribute("mensagens", forumService.listTopics());
            return "mensagens";
        }

        List<MultipartFile> fotosEnviadas = fotos == null ? List.of()
            : fotos.stream().filter(f -> f != null && !f.isEmpty()).toList();
        try {
            forumService.createTopic(usuarioId, form, fotosEnviadas);
        } catch (FotoInvalidaException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("mensagens", forumService.listTopics());
            return "mensagens";
        }
        redirectAttributes.addFlashAttribute("sucesso", "Mensagem publicada com sucesso.");
        return "redirect:/mensagens";
    }

    @GetMapping("/mensagens/{id}")
    public String showMessage(@PathVariable int id, HttpSession session, Model model) {
        Topico topico = findTopic(id);
        model.addAttribute("mensagem", topico);
        model.addAttribute("podeEditar", forumService.ehAutor(topico, SessaoUsuario.idUsuario(session)));
        model.addAttribute("comentarios", forumService.listReplies(id));
        return "post";
    }

    @GetMapping("/mensagens/{id}/editar")
    public String showEditForm(@PathVariable int id, HttpSession session, Model model) {
        Integer usuarioId = SessaoUsuario.idUsuario(session);
        if (usuarioId == null) return "redirect:/login?voltar=/mensagens/" + id + "/editar";

        Topico topico = findTopic(id);
        if (!forumService.ehAutor(topico, usuarioId)) {
            return "redirect:/mensagens/" + id;
        }

        TopicoForm form = new TopicoForm();
        form.setTitulo(topico.getTitulo());
        form.setTipo(topico.getTipo());
        form.setMensagem(topico.getMensagem());
        model.addAttribute("form", form);
        model.addAttribute("topicoId", id);
        return "editar-post";
    }

    @PostMapping("/mensagens/{id}/editar")
    public String editMessage(@PathVariable int id,
                              @Valid @ModelAttribute("form") TopicoForm form,
                              BindingResult result,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        Integer usuarioId = SessaoUsuario.idUsuario(session);
        if (usuarioId == null) return "redirect:/login?voltar=/mensagens/" + id + "/editar";

        findTopic(id);
        if (result.hasErrors()) {
            model.addAttribute("topicoId", id);
            return "editar-post";
        }

        try {
            forumService.updateTopic(usuarioId, id, form);
            redirectAttributes.addFlashAttribute("sucessoPost", "Post atualizado com sucesso.");
        } catch (SemPermissaoException e) {
            redirectAttributes.addFlashAttribute("erroPost", e.getMessage());
        }
        return "redirect:/mensagens/" + id;
    }

    @PostMapping("/mensagens/{id}/excluir")
    public String deleteMessage(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        Integer usuarioId = SessaoUsuario.idUsuario(session);
        if (usuarioId == null) return "redirect:/login?voltar=/mensagens/" + id;

        findTopic(id);
        try {
            forumService.deleteTopic(usuarioId, id);
            redirectAttributes.addFlashAttribute("sucesso", "Post excluído com sucesso.");
            return "redirect:/mensagens";
        } catch (SemPermissaoException e) {
            redirectAttributes.addFlashAttribute("erroPost", e.getMessage());
            return "redirect:/mensagens/" + id;
        }
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
