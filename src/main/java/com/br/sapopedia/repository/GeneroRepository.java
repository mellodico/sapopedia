package com.br.sapopedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.sapopedia.model.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Integer> {

    @Query("SELECT g FROM Genero g JOIN FETCH g.familia ORDER BY g.nomeCientifico")
    List<Genero> listarComFamilia();
}
