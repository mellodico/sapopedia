package com.br.sapopedia.controller;

import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.br.sapopedia.model.Usuario;
import com.br.sapopedia.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    // Mesmas regras dos CHECKs da tabela users
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_]{3,50}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String voltar, HttpSession session, Model model) {
        if (SessaoUsuario.idUsuario(session) != null) return "redirect:/conta";
        model.addAttribute("voltar", voltar);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        @RequestParam(required = false) String voltar,
                        HttpServletRequest request,
                        Model model) {
        var usuario = userService.authenticate(email, senha);
        if (usuario.isEmpty()) {
            model.addAttribute("erro", "E-mail ou senha incorretos.");
            model.addAttribute("email", email);
            model.addAttribute("voltar", voltar);
            return "login";
        }
        iniciarSessao(request, usuario.get().getId());
        return "redirect:" + destinoSeguro(voltar, "/conta");
    }

    @GetMapping("/registro")
    public String showRegister(HttpSession session) {
        if (SessaoUsuario.idUsuario(session) != null) return "redirect:/conta";
        return "registro";
    }

    @PostMapping("/registro")
    public String register(@RequestParam String nome,
                           @RequestParam String usuario,
                           @RequestParam String email,
                           @RequestParam String senha,
                           @RequestParam String confirmacao,
                           HttpServletRequest request,
                           Model model) {
        String erro = validarPerfil(nome, usuario, email);
        if (erro == null && senha.length() < 6) erro = "A senha precisa ter pelo menos 6 caracteres.";
        if (erro == null && !senha.equals(confirmacao)) erro = "As senhas precisam ser iguais.";

        if (erro == null) {
            try {
                int id = userService.register(usuario, nome, email, senha);
                iniciarSessao(request, id);
                return "redirect:/conta";
            } catch (DataIntegrityViolationException e) {
                erro = "Já existe uma conta com esse e-mail ou nome de usuário.";
            }
        }

        model.addAttribute("erro", erro);
        model.addAttribute("nome", nome);
        model.addAttribute("usuario", usuario);
        model.addAttribute("email", email);
        return "registro";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/conta")
    public String showAccount(Model model) {
        if (model.getAttribute("usuarioLogado") == null) return "redirect:/login?voltar=/conta";
        model.addAttribute("perfil", model.getAttribute("usuarioLogado"));
        return "conta";
    }

    @PostMapping("/conta")
    public String updateAccount(@RequestParam String nome,
                                @RequestParam String usuario,
                                @RequestParam String email,
                                @RequestParam(defaultValue = "") String bio,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Integer id = SessaoUsuario.idUsuario(session);
        if (id == null) return "redirect:/login?voltar=/conta";

        String erro = validarPerfil(nome, usuario, email);
        if (erro == null && bio.trim().length() > 240) erro = "O texto sobre você pode ter no máximo 240 caracteres.";

        if (erro == null) {
            try {
                userService.update(id, usuario, nome, email, bio);
                redirectAttributes.addFlashAttribute("sucesso", "Dados atualizados com sucesso.");
                return "redirect:/conta";
            } catch (DataIntegrityViolationException e) {
                erro = "Já existe uma conta com esse e-mail ou nome de usuário.";
            }
        }

        model.addAttribute("erro", erro);
        var perfil = new Usuario(usuario, nome, email, null);
        perfil.setBio(bio);
        model.addAttribute("perfil", perfil);
        return "conta";
    }

    private static String validarPerfil(String nome, String usuario, String email) {
        if (nome.isBlank() || nome.trim().length() > 100) return "Informe um nome com até 100 caracteres.";
        if (!USERNAME.matcher(usuario.trim()).matches())
            return "O nome de usuário deve ter de 3 a 50 caracteres: letras, números ou _.";
        if (email.trim().length() > 100 || !EMAIL.matcher(email.trim()).matches()) return "Informe um e-mail válido.";
        return null;
    }

    private static void iniciarSessao(HttpServletRequest request, int usuarioId) {
        HttpSession session = request.getSession();
        request.changeSessionId(); // evita fixação de sessão
        session.setAttribute(SessaoUsuario.ATRIBUTO_ID, usuarioId);
    }

    /** Só permite redirecionar para caminhos internos do site. */
    static String destinoSeguro(String voltar, String padrao) {
        if (voltar == null || !voltar.startsWith("/") || voltar.startsWith("//") || voltar.contains("\\")) {
            return padrao;
        }
        return voltar;
    }
}
