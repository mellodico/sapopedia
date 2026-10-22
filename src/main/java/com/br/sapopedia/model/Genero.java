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
@Table(name = "wiki_genera")
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "family_id", nullable = false)
    private Familia familia;

    @Column(name = "scientific_name", nullable = false, unique = true, length = 60)
    private String nomeCientifico;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String descricao = "";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected Genero() {
    }

    public Genero(Familia familia, String nomeCientifico, String descricao) {
        this.familia = familia;
        this.nomeCientifico = nomeCientifico;
        this.descricao = descricao;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Familia getFamilia() {
        return familia;
    }

    public String getNomeCientifico() {
        return nomeCientifico;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
