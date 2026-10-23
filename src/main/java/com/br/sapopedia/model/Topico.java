package com.br.sapopedia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Formula;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "forum_topics")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario autor;

    @Column(name = "title", nullable = false, length = 80)
    private String titulo;

    @Column(name = "type", nullable = false, length = 20)
    private String tipo;

    @Column(name = "body", nullable = false, length = 500)
    private String mensagem;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Formula("(SELECT COUNT(*) FROM forum_replies r WHERE r.topic_id = id)")
    private int totalComentarios;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("posicao")
    private List<FotoTopico> fotos = new ArrayList<>();

    protected Topico() {
    }

    public Topico(Usuario autor, String titulo, String tipo, String mensagem) {
        this.autor = autor;
        this.titulo = titulo;
        this.tipo = tipo;
        this.mensagem = mensagem;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    public void adicionarFoto(String nomeArquivo) {
        fotos.add(new FotoTopico(this, nomeArquivo, fotos.size()));
    }

    public String tipoLabel() {
        return switch (tipo) {
            case "opiniao" -> "Opinião";
            case "sugestao" -> "Sugestão";
            case "avistamento" -> "Avistamento";
            case "duvida" -> "Dúvida";
            default -> "Outro";
        };
    }

    public Integer getId() {
        return id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public int getTotalComentarios() {
        return totalComentarios;
    }

    public List<FotoTopico> getFotos() {
        return fotos;
    }
}
