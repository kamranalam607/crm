package com.zohocrm.controller;

import com.zohocrm.payload.ContactDto;
import com.zohocrm.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    //http://localhost:8080/api/contacts?leadId=387af1cb-d417-4aa9-9484-b33829fbb76e
    @PostMapping
    public ResponseEntity<ContactDto> createContact(@RequestParam("leadId") String leadId){
        ContactDto dto = contactService.createContact(leadId);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);

    }

}
