package com.duowngtora.spring_ollama.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ollama-chat")
public class OllamaChatController {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    private String prompt = """
            Your task is to answer the questions and answers by vietnamese. Use the information from the DOCUMENTS
                        section to provide accurate answers. If unsure or if the answer isn't found in the DOCUMENTS section,\s
                        simply state that you don't know the answer.
                        
                        QUESTION:
                        {input}
                        
                        DOCUMENTS:
                        {documents}
                         
            """;

    public OllamaChatController(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String message) {
        return ResponseEntity.ok(chatModel.call(message));
    }

    @GetMapping("/prompt")
    public ResponseEntity<String> prompt(@RequestParam String message) {
        PromptTemplate promptTemplate = new PromptTemplate(prompt);

        Map<String, Object> params = new HashMap<>();
        params.put("input", message);
        params.put("documents", findVectorData(message));

        String response = chatModel.call(promptTemplate.createMessage(params));
        return ResponseEntity.ok(response);
    }

    private String findVectorData(String message){
        List<Document> documents = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(message)
                            .topK(5)
                            .build()
                );
        return documents
                .stream()
                .map(Document::getFormattedContent)
                .collect(Collectors.joining());
    }
}
