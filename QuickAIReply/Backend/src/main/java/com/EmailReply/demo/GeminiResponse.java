package com.EmailReply.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponse {
    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        private Content content;

        public Content getContent() {
            return content;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private List<Part> parts;

        public List<Part> getParts() {
            return parts;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        private String text;

        public String getText() {
            return text;
        }
    }
}
