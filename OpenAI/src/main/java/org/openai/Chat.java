package org.openai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Chat {
    public static void main(String[] args) {
        String token = ApiKey.API_KEY;
        OpenAiService service = new OpenAiService(token);

        Scanner sc = new Scanner(System.in);
        System.out.print("prompt: ");
        String prompt = sc.nextLine();

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), prompt);

        messages.add(systemMessage);

        ChatCompletionRequest request = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(200)
                .logitBias(new HashMap<>())
                .build();

        service.streamChatCompletion(request)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(el -> el.getChoices().forEach(msg -> {
                    String content = msg.getMessage().getContent();

                    if (content != null) System.out.print(content);
                }));

        service.shutdownExecutor();
    }
}
