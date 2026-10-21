package com.br.sapopedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.sapopedia.model.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Integer> {

    // join fetch: os templates exibem o autor fora da transação
    @Query("SELECT t FROM Topico t JOIN FETCH t.autor ORDER BY t.criadoEm DESC, t.id DESC")
    List<Topico> listarComAutor();

    @Query("SELECT t FROM Topico t JOIN FETCH t.autor WHERE t.id = :id")
    Optional<Topico> buscarComAutor(int id);
}
