package com.br.sapopedia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sapopedia.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);
}
