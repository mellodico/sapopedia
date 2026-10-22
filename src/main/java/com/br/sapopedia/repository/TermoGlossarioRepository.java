package com.br.sapopedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sapopedia.model.TermoGlossario;

public interface TermoGlossarioRepository extends JpaRepository<TermoGlossario, Integer> {

    List<TermoGlossario> findAllByOrderByTermoAsc();
}
