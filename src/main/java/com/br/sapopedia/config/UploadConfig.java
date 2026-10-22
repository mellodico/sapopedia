package com.br.sapopedia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.br.sapopedia.model.FotoTopico;
import com.br.sapopedia.service.FotoStorage;

@Configuration
public class UploadConfig implements WebMvcConfigurer {

    private final FotoStorage fotoStorage;

    public UploadConfig(FotoStorage fotoStorage) {
        this.fotoStorage = fotoStorage;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(FotoTopico.URL_BASE + "**")
            .addResourceLocations(fotoStorage.getPasta().toUri().toString(), "classpath:/seed/forum/");
    }
}
