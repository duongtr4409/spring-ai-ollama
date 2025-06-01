package com.duowngtora.spring_ollama.utils;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class VectorStoreUtil {

    private final VectorStore vectorStore;

    public VectorStoreUtil(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void saveDocument(Resource resource){
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPagesPerDocument(1)
                .build();

        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);
        TokenTextSplitter splitter = new TokenTextSplitter();
        vectorStore.accept(splitter.apply(reader.get()));
    }
}
