package com.babu6.srpringai.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
       return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @PostMapping("/chat-with-image")
    public String chat(@RequestPart("message") String message,@RequestPart("image") MultipartFile file) {
        return chatClient.prompt()
                .user(promt-> promt
                        .text(message)
                        .media(MimeTypeUtils.IMAGE_PNG,new InputStreamResource(file))
                )
                .call()
                .content();
    }

    //Stemless AI
    @GetMapping("/chat-with-stream-response")
    public Flux<String> chatWithFlux(@RequestPart("message") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content()
                .doOnNext(System.out::print);
    }
}
