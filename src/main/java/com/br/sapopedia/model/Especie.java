package com.br.sapopedia.model;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "wiki_species")
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genus_id", nullable = false)
    private Genero genero;

    @Column(name = "epithet", nullable = false, length = 60)
    private String epiteto;

    @Column(name = "common_name", nullable = false, length = 100)
    private String nomePopular;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String descricao = "";

    @Column(name = "habitat", nullable = false, columnDefinition = "text")
    private String habitat = "";

    @Column(name = "conservation_status", nullable = false, length = 2)
    private String statusConservacao = "NE";

    @ManyToMany
    @JoinTable(name = "wiki_species_biomes",
        joinColumns = @JoinColumn(name = "species_id"),
        inverseJoinColumns = @JoinColumn(name = "biome_id"))
    private Set<Bioma> biomas = new LinkedHashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime atualizadoEm;

    protected Especie() {
    }

    public Especie(Genero genero, String epiteto, String nomePopular) {
        this.genero = genero;
        this.epiteto = epiteto;
        this.nomePopular = nomePopular;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = criadoEm;
    }

    @PreUpdate
    void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    public String getNomeCientifico() {
        return genero.getNomeCientifico() + " " + epiteto;
    }

    public String getNomesBiomas() {
        return biomas.stream().map(bioma -> bioma.getNome()).sorted().collect(Collectors.joining(", "));
    }

    public Integer getId() {
        return id;
    }

    public Genero getGenero() {
        return genero;
    }

    public String getEpiteto() {
        return epiteto;
    }

    public String getNomePopular() {
        return nomePopular;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getStatusConservacao() {
        return statusConservacao;
    }

    public void setStatusConservacao(String statusConservacao) {
        this.statusConservacao = statusConservacao;
    }

    public Set<Bioma> getBiomas() {
        return biomas;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
