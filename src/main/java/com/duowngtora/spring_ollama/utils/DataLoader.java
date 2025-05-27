package com.duowngtora.spring_ollama.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataLoader {

    private final VectorStore vectorStore;

    private final JdbcClient jdbcClient;

    @Value("classpath:/lekl101.pdf")
    private Resource initResource;

    public DataLoader(VectorStore vectorStore, JdbcClient jdbcClient) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
    }

    @PostConstruct
    public void init() {

        Integer count = jdbcClient.sql("select count(1) from vector_store ")
                .query(Integer.class)
                .single();
        System.out.println("Vector in database: " + count);

        if(count != 0)
            return;

//        List<Document> documents = List.of(
//        new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
//        new Document("The World is Big and Salvation Lurks Around the Corner"),
//        new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));
//
//        // Add the documents to PGVector
//        vectorStore.add(documents);

        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPagesPerDocument(1)
                .build();

        PagePdfDocumentReader reader = new PagePdfDocumentReader(initResource, config);
        var textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(reader.get()));

        System.out.println("Load init database is done.");
    }
}
