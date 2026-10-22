package com.br.sapopedia.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "wiki_glossary_terms")
public class TermoGlossario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "term", nullable = false, unique = true, length = 80)
    private String termo;

    @Column(name = "slug", nullable = false, unique = true, length = 80)
    private String slug;

    @Column(name = "term_label", nullable = false, length = 100)
    private String rotulo = "";

    @Column(name = "definition", nullable = false, columnDefinition = "text")
    private String definicao;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected TermoGlossario() {
    }

    public TermoGlossario(String termo, String slug, String rotulo, String definicao) {
        this.termo = termo;
        this.slug = slug;
        this.rotulo = rotulo;
        this.definicao = definicao;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public String getTermo() {
        return termo;
    }

    public String getSlug() {
        return slug;
    }

    public String getRotulo() {
        return rotulo;
    }

    public String getDefinicao() {
        return definicao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
