package com.EmailReply.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class MailService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public MailService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    private String BuildPrompt(MailRequest request) {
        if (request.getTone() == null || request.getTone().isEmpty()) {
            throw new IllegalArgumentException("Tone cannot be null or empty");
        }

        StringBuilder prompt = new StringBuilder();

        switch (request.getTone()) {
            case "Formal":
                prompt.append("Write a professional email reply to the following message. Do not include 'Re:' in the subject. Only generate the response content:\n\n");
                break;
            case "Polite":
                prompt.append("Write a courteous email reply to the following message. Keep it respectful and warm. Do not include 'Re:' in the subject. Output only the reply:\n\n");
                break;
            case "Casual":
                prompt.append("Write a friendly email reply to the following message. Keep it informal and natural. Do not include 'Re:' in the subject. Only generate the reply itself:\n\n");
                break;
            case "Direct":
                prompt.append("Write a clear and concise email reply to the following message. Get straight to the point. Do not include 'Re:' in the subject. Only generate the response:\n\n");
                break;
            default:
                throw new IllegalArgumentException("Unknown tone: " + request.getTone());
        }

        prompt.append("Original email:\n\"").append(request.getContent()).append("\"\n\n");
        prompt.append("Reply:\n");

        return prompt.toString();
    }



    public String processContent(MailRequest request){
        String prompt = BuildPrompt(request);
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {
        try {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
                if (firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty()) {
                    System.out.println(firstCandidate.getContent().getParts().get(0).getText());
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
            return "No content found in response";
        } catch (Exception e) {
            return "Error Parsing: " + e.getMessage();
        }
    }


}
