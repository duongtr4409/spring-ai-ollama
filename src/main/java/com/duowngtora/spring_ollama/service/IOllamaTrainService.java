package com.duowngtora.spring_ollama.service;

import org.springframework.web.multipart.MultipartFile;

public interface IOllamaTrainService {

    void ollamaTrainFile(MultipartFile multipartFile);
}
