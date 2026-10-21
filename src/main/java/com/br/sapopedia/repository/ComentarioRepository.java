package com.br.sapopedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.sapopedia.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    @Query("SELECT c FROM Comentario c JOIN FETCH c.autor WHERE c.topico.id = :topicoId ORDER BY c.criadoEm, c.id")
    List<Comentario> listarPorTopico(int topicoId);
}
