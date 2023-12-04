package com.zohocrm.controller;

import com.zohocrm.payload.EmailDto;
import com.zohocrm.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    //http://localhost:8080/api/emails
    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<EmailDto> sendEmail(@RequestBody EmailDto emailDto){
        EmailDto dto = emailService.sendSimpleMail(emailDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
