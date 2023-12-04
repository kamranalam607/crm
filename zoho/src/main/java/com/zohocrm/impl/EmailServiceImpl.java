package com.zohocrm.impl;

import com.zohocrm.entity.Contact;
import com.zohocrm.entity.Email;
import com.zohocrm.entity.Lead;
import com.zohocrm.exception.ContactExist;
import com.zohocrm.exception.LeadExist;
import com.zohocrm.payload.EmailDto;
import com.zohocrm.repository.ContactRepository;
import com.zohocrm.repository.EmailRepository;
import com.zohocrm.repository.LeadRepository;
import com.zohocrm.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {


    private EmailRepository emailRepository;
    private LeadRepository leadRepository;
    private JavaMailSender javaMailSender;
    private ModelMapper modelMapper;
    private ContactRepository contactRepository;

    public EmailServiceImpl(EmailRepository emailRepository, LeadRepository leadRepository,JavaMailSender javaMailSender,ModelMapper modelMapper,ContactRepository contactRepository) {
        this.emailRepository = emailRepository;
        this.leadRepository = leadRepository;
        this.javaMailSender = javaMailSender;
        this.modelMapper = modelMapper;
        this.contactRepository = contactRepository;
    }

    @Override
    public EmailDto sendSimpleMail(EmailDto emailDto) {
        Lead lead = leadRepository.findByEmail(emailDto.getTo()).orElseThrow(
                () -> new LeadExist("Email id not registered" + emailDto.getTo())
        );

//        Contact contact = contactRepository.findByEmail(emailDto.getTo()).orElseThrow(
//                () -> new ContactExist("Email id not registered" + emailDto.getTo())
//        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getSub());
        message.setText(emailDto.getBody());
        javaMailSender.send(message);

        Email email = mapToEmail(emailDto);
        String eid = UUID.randomUUID().toString();
        email.setEid(eid);
        Email sentEmail = emailRepository.save(email);

        EmailDto dto = mapToEmailDto(sentEmail);
        return dto;
    }

    private EmailDto mapToEmailDto(Email sentEmail) {
        EmailDto dto = modelMapper.map(sentEmail, EmailDto.class);
        return dto;
    }

    Email mapToEmail(EmailDto emailDto){
        Email email = modelMapper.map(emailDto, Email.class);
        return email;
    }

}
