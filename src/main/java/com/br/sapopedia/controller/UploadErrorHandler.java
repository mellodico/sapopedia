package com.br.sapopedia.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class UploadErrorHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String uploadGrandeDemais(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erro", "As fotos excedem o limite: até 4 fotos de 5 MB cada.");
        return "redirect:/mensagens";
    }
}
