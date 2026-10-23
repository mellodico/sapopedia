package com.br.sapopedia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.br.sapopedia.config.SemPermissaoException;
import com.br.sapopedia.model.Comentario;
import com.br.sapopedia.model.FotoTopico;
import com.br.sapopedia.model.Topico;
import com.br.sapopedia.model.TopicoForm;
import com.br.sapopedia.repository.ComentarioRepository;
import com.br.sapopedia.repository.TopicoRepository;
import com.br.sapopedia.repository.UsuarioRepository;

@Service
public class ForumService {

    private final TopicoRepository topicos;
    private final ComentarioRepository comentarios;
    private final UsuarioRepository usuarios;
    private final FotoStorage fotoStorage;

    public ForumService(TopicoRepository topicos, ComentarioRepository comentarios, UsuarioRepository usuarios,
                        FotoStorage fotoStorage) {
        this.topicos = topicos;
        this.comentarios = comentarios;
        this.usuarios = usuarios;
        this.fotoStorage = fotoStorage;
    }

    @Transactional(readOnly = true)
    public List<Topico> listTopics() {
        return topicos.listarComAutor();
    }

    @Transactional(readOnly = true)
    public Optional<Topico> findTopic(int id) {
        return topicos.buscarComAutor(id);
    }

    @Transactional
    public void createTopic(int userId, TopicoForm form, List<MultipartFile> fotos) {
        Topico topico = new Topico(usuarios.getReferenceById(userId), form.getTitulo().trim(), form.getTipo(),
            form.getMensagem().trim());
        List<String> arquivos = fotoStorage.salvar(fotos);
        for (String arquivo : arquivos) {
            topico.adicionarFoto(arquivo);
        }

        try {
            topicos.saveAndFlush(topico);
        } catch (RuntimeException e) {
            fotoStorage.apagar(arquivos);
            throw e;
        }
    }

    @Transactional
    public void updateTopic(int userId, int topicId, TopicoForm form) {
        Topico topico = buscarDoAutor(userId, topicId);
        topico.setTitulo(form.getTitulo().trim());
        topico.setTipo(form.getTipo());
        topico.setMensagem(form.getMensagem().trim());
        topicos.save(topico);
    }

    @Transactional
    public void deleteTopic(int userId, int topicId) {
        Topico topico = buscarDoAutor(userId, topicId);
        List<String> arquivos = new ArrayList<>();
        for (FotoTopico foto : topico.getFotos()) {
            arquivos.add(foto.getNomeArquivo());
        }
        topicos.delete(topico);
        topicos.flush();
        fotoStorage.apagar(arquivos);
    }

    public boolean ehAutor(Topico topico, Integer userId) {
        return userId != null && topico.getAutor().getId().equals(userId);
    }

    private Topico buscarDoAutor(int userId, int topicId) {
        Topico topico = topicos.findById(topicId).orElseThrow();
        if (!ehAutor(topico, userId)) {
            throw new SemPermissaoException("Só o autor do post pode alterar ou excluir ele.");
        }
        return topico;
    }

    @Transactional(readOnly = true)
    public List<Comentario> listReplies(int topicId) {
        return comentarios.listarPorTopico(topicId);
    }

    @Transactional
    public void createReply(int userId, int topicId, String texto) {
        comentarios.save(new Comentario(usuarios.getReferenceById(userId), topicos.getReferenceById(topicId),
            texto.trim()));
    }

}
