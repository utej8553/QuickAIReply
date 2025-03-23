package com.EmailReply.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Mail")
@CrossOrigin(origins = "*")
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }


    @PostMapping("/process")
    public ResponseEntity<String> processContent(@RequestBody MailRequest request) {
        String result = mailService.processContent(request);
        return ResponseEntity.ok(result);
    }
}