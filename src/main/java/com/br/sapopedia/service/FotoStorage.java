package com.br.sapopedia.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.br.sapopedia.config.FotoInvalidaException;

@Service
public class FotoStorage {

    private static final Logger log = LoggerFactory.getLogger(FotoStorage.class);

    public static final int MAX_FOTOS = 4;
    public static final long MAX_BYTES = 5 * 1024 * 1024;

    private final Path pasta;

    public FotoStorage(@Value("${sapopedia.uploads.dir}") String uploadsDir) throws IOException {
        this.pasta = Path.of(uploadsDir, "forum").toAbsolutePath().normalize();
        Files.createDirectories(pasta);
    }

    public Path getPasta() {
        return pasta;
    }

    public List<String> salvar(List<MultipartFile> fotos) {
        if (fotos == null || fotos.isEmpty()) {
            return List.of();
        }
        if (fotos.size() > MAX_FOTOS) {
            throw new FotoInvalidaException("Envie no máximo " + MAX_FOTOS + " fotos.");
        }

        List<byte[]> conteudos = new ArrayList<>();
        List<String> nomes = new ArrayList<>();
        try {
            for (MultipartFile foto : fotos) {
                if (foto.getSize() > MAX_BYTES) {
                    throw new FotoInvalidaException("Cada foto pode ter no máximo 5 MB.");
                }
                byte[] bytes = foto.getBytes();
                String extensao = descobrirExtensao(bytes);
                if (extensao == null) {
                    throw new FotoInvalidaException("Envie fotos em JPG, PNG, WebP ou GIF.");
                }
                conteudos.add(bytes);
                nomes.add(UUID.randomUUID().toString().replace("-", "") + "." + extensao);
            }

            for (int i = 0; i < nomes.size(); i++) {
                Files.write(pasta.resolve(nomes.get(i)), conteudos.get(i));
            }
        } catch (IOException e) {
            apagar(nomes);
            throw new RuntimeException("Erro ao salvar as fotos", e);
        }
        return nomes;
    }

    public void apagar(List<String> nomes) {
        for (String nome : nomes) {
            try {
                Files.deleteIfExists(pasta.resolve(nome));
            } catch (IOException e) {
                log.warn("Não foi possível apagar a foto {}", nome, e);
            }
        }
    }

    private String descobrirExtensao(byte[] b) {
        if (b.length < 12) {
            return null;
        }
        if ((b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF) {
            return "jpg";
        }
        if ((b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G') {
            return "png";
        }
        if (b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8') {
            return "gif";
        }
        if (b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F'
                && b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P') {
            return "webp";
        }
        return null;
    }
}
