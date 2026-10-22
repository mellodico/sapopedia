package com.br.sapopedia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.sapopedia.model.Especie;
import com.br.sapopedia.model.Familia;
import com.br.sapopedia.model.Genero;
import com.br.sapopedia.model.Imagem;
import com.br.sapopedia.model.TermoGlossario;
import com.br.sapopedia.repository.EspecieRepository;
import com.br.sapopedia.repository.FamiliaRepository;
import com.br.sapopedia.repository.GeneroRepository;
import com.br.sapopedia.repository.ImagemRepository;
import com.br.sapopedia.repository.TermoGlossarioRepository;

@Service
@Transactional(readOnly = true)
public class WikiService {

    private final FamiliaRepository familias;
    private final GeneroRepository generos;
    private final EspecieRepository especies;
    private final TermoGlossarioRepository termos;
    private final ImagemRepository imagens;

    public WikiService(FamiliaRepository familias, GeneroRepository generos, EspecieRepository especies,
                       TermoGlossarioRepository termos, ImagemRepository imagens) {
        this.familias = familias;
        this.generos = generos;
        this.especies = especies;
        this.termos = termos;
        this.imagens = imagens;
    }

    public List<Familia> listFamilies() {
        return familias.findAllByOrderByNomeCientificoAsc();
    }

    public List<Genero> listGenera() {
        return generos.listarComFamilia();
    }

    public List<Especie> listSpecies() {
        return especies.listarComGeneroEBiomas();
    }

    public List<TermoGlossario> listGlossary() {
        return termos.findAllByOrderByTermoAsc();
    }

    public List<Imagem> listImages() {
        return imagens.findAllByOrderByCriadoEmDescIdDesc();
    }
}
