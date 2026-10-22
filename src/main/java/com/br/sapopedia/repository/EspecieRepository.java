package com.br.sapopedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.sapopedia.model.Especie;

public interface EspecieRepository extends JpaRepository<Especie, Integer> {

    @Query("""
        SELECT DISTINCT e FROM Especie e
        JOIN FETCH e.genero g
        LEFT JOIN FETCH e.biomas
        ORDER BY g.nomeCientifico, e.epiteto
        """)
    List<Especie> listarComGeneroEBiomas();
}
