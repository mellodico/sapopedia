package com.br.sapopedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TopicoForm {

    @NotBlank(message = "O título é obrigatório.")
    @Size(min = 2, max = 80, message = "O título deve ter de 2 a 80 caracteres.")
    private String titulo;

    @NotBlank(message = "Escolha o tipo da mensagem.")
    @Pattern(regexp = "opiniao|sugestao|avistamento|duvida|outro", message = "Tipo de mensagem inválido.")
    private String tipo = "opiniao";

    @NotBlank(message = "A mensagem é obrigatória.")
    @Size(min = 5, max = 500, message = "A mensagem deve ter de 5 a 500 caracteres.")
    private String mensagem;

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
}
