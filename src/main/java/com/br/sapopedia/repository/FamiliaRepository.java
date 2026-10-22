package com.br.sapopedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sapopedia.model.Familia;

public interface FamiliaRepository extends JpaRepository<Familia, Integer> {

    List<Familia> findAllByOrderByNomeCientificoAsc();
}
