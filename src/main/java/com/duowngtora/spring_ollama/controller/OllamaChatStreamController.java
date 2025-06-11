package com.duowngtora.spring_ollama.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ollama-chat/stream")
public class OllamaChatStreamController {

    private final ChatModel chatModel;

    public OllamaChatStreamController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody String message) {
        return chatModel.stream(message);
    }
}