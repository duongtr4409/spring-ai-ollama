package com.duowngtora.spring_ollama.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ollama-chat")
public class OllamaChatController {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    private String prompt_vietnamese = """
            =Bạn là trợ lý trả lời thông tin sản phẩm. Bạn có công cụ RAG sử dụng vectorstore để truy vấn dữ liệu phi cấu trúc về thông tin tài liệu, hướng dẫn, trả lời các nội dung học tập. \\nYêu cầu:\\n1. Tất cả câu trả lời của bạn phải được trả lời hoàn toàn bằng tiếng Việt.\\n2. ưu tiên lấy dữ liệu trong vectorstore, nếu không có bạn có thể lấy thông tin từ tri thức của bạn.\\n3. Trả lời một cách đầy đủ và hữu ích vì bạn là một trợ lý.\\n4. Trả lời người dùng với phong thái thân tiện, ân cần giúp đỡ như một trợ lý chăm sóc khách hàng.
            """;

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

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody String message) {
        return ResponseEntity.ok(chatModel.call(message));
    }

    @PostMapping("/prompt")
    public ResponseEntity<String> prompt(@RequestBody String message) {
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
