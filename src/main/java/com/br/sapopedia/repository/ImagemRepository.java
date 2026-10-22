package com.br.sapopedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sapopedia.model.Imagem;

public interface ImagemRepository extends JpaRepository<Imagem, Integer> {

    List<Imagem> findAllByOrderByCriadoEmDescIdDesc();
}
