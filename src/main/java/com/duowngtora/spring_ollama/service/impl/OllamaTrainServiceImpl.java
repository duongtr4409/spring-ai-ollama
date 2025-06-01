package com.duowngtora.spring_ollama.service.impl;

import com.duowngtora.spring_ollama.service.IOllamaTrainService;
import com.duowngtora.spring_ollama.utils.VectorStoreUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OllamaTrainServiceImpl implements IOllamaTrainService {

    private final VectorStoreUtil vectorStoreUtil;

    public OllamaTrainServiceImpl(VectorStoreUtil vectorStoreUtil) {
        this.vectorStoreUtil = vectorStoreUtil;
    }

    @Override
    public void ollamaTrainFile(MultipartFile multipartFile) {
        vectorStoreUtil.saveDocument(multipartFile.getResource());
    }
}
