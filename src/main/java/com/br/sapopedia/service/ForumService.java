package com.br.sapopedia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.br.sapopedia.model.Comentario;
import com.br.sapopedia.model.Topico;
import com.br.sapopedia.repository.ComentarioRepository;
import com.br.sapopedia.repository.TopicoRepository;
import com.br.sapopedia.repository.UsuarioRepository;

@Service
public class ForumService {

    public static final List<String> TIPOS = List.of("opiniao", "sugestao", "avistamento", "duvida", "outro");

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
    public void createTopic(int userId, String titulo, String tipo, String mensagem, List<MultipartFile> fotos) {
        Topico topico = new Topico(usuarios.getReferenceById(userId), titulo.trim(), tipo, mensagem.trim());
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
