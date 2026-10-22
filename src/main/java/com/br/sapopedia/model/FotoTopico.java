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
@Table(name = "forum_topic_images")
public class FotoTopico {

    public static final String URL_BASE = "/uploads/forum/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topico topico;

    @Column(name = "file_name", nullable = false, unique = true, length = 60)
    private String nomeArquivo;

    @Column(name = "position", nullable = false)
    private int posicao;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected FotoTopico() {
    }

    public FotoTopico(Topico topico, String nomeArquivo, int posicao) {
        this.topico = topico;
        this.nomeArquivo = nomeArquivo;
        this.posicao = posicao;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    public String getUrl() {
        return URL_BASE + nomeArquivo;
    }

    public Integer getId() {
        return id;
    }

    public Topico getTopico() {
        return topico;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public int getPosicao() {
        return posicao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
