package com.br.sapopedia.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "forum_replies")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topico topico;

    @Column(name = "body", nullable = false, length = 500)
    private String texto;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected Comentario() {
    }

    public Comentario(Usuario autor, Topico topico, String texto) {
        this.autor = autor;
        this.topico = topico;
        this.texto = texto;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public Topico getTopico() {
        return topico;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
