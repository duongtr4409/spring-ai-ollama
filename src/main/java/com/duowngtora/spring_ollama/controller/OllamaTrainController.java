package com.duowngtora.spring_ollama.controller;

import com.duowngtora.spring_ollama.constant.CustomResponse;
import com.duowngtora.spring_ollama.service.IOllamaTrainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ollama-train")
public class OllamaTrainController {

    private final IOllamaTrainService ollamaTrainService;

    public OllamaTrainController(IOllamaTrainService ollamaTrainService) {
        this.ollamaTrainService = ollamaTrainService;
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> trainWithFile(@RequestParam("file")MultipartFile multipartFile) {
        this.ollamaTrainService.ollamaTrainFile(multipartFile);
        return ResponseEntity.ok(CustomResponse.Success());
    }
}
