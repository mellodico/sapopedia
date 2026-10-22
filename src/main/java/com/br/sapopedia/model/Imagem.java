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
@Table(name = "wiki_images")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    private Especie especie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private Usuario enviadaPor;

    @Column(name = "file_path", nullable = false, unique = true, length = 255)
    private String caminho;

    @Column(name = "alt_text", nullable = false, length = 200)
    private String textoAlternativo;

    @Column(name = "caption", nullable = false, length = 300)
    private String legenda = "";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected Imagem() {
    }

    public Imagem(String caminho, String textoAlternativo, String legenda) {
        this.caminho = caminho;
        this.textoAlternativo = textoAlternativo;
        this.legenda = legenda;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Usuario getEnviadaPor() {
        return enviadaPor;
    }

    public void setEnviadaPor(Usuario enviadaPor) {
        this.enviadaPor = enviadaPor;
    }

    public String getCaminho() {
        return caminho;
    }

    public String getTextoAlternativo() {
        return textoAlternativo;
    }

    public String getLegenda() {
        return legenda;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
