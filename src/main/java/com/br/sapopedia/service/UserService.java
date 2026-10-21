package com.br.sapopedia.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.sapopedia.model.Usuario;
import com.br.sapopedia.repository.UsuarioRepository;

@Service
public class UserService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;

    public UserService(UsuarioRepository usuarios, PasswordEncoder passwordEncoder) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public int register(String username, String name, String email, String rawPassword) {
        var usuario = new Usuario(username.trim(), name.trim(), normalizeEmail(email),
            passwordEncoder.encode(rawPassword));
        return usuarios.saveAndFlush(usuario).getId();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> authenticate(String email, String rawPassword) {
        return usuarios.findByEmail(normalizeEmail(email))
            .filter(u -> passwordEncoder.matches(rawPassword, u.getPasswordHash()));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findById(int id) {
        return usuarios.findById(id);
    }

    @Transactional
    public void update(int id, String username, String name, String email, String bio) {
        Usuario usuario = usuarios.findById(id).orElseThrow();
        usuario.setUsername(username.trim());
        usuario.setName(name.trim());
        usuario.setEmail(normalizeEmail(email));
        usuario.setBio(bio.trim());
        usuarios.flush(); // dispara aqui a violação de UNIQUE, se houver
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
