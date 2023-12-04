package com.zohocrm.impl;

import com.zohocrm.entity.Contact;
import com.zohocrm.entity.Lead;
import com.zohocrm.exception.LeadExist;
import com.zohocrm.payload.ContactDto;
import com.zohocrm.repository.ContactRepository;
import com.zohocrm.repository.LeadRepository;
import com.zohocrm.service.ContactService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    private LeadRepository leadRepository;
    private ContactRepository contactRepository;
    private ModelMapper modelMapper;

    public ContactServiceImpl(LeadRepository leadRepository, ContactRepository contactRepository,ModelMapper modelMapper) {
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public ContactDto createContact(String leadId) {
        Lead lead = leadRepository.findById(leadId).orElseThrow(
                () -> new LeadExist("Lead is not with lead" + leadId)
        );
        Contact contact = convertLeadToContact(lead);
        String cid = UUID.randomUUID().toString();
        contact.setCid(cid);
        Contact savedContact = contactRepository.save(contact);
        if (savedContact.getCid()!=null){
            leadRepository.deleteById(lead.getLid());
        }
        ContactDto dto = mapToContactDto(savedContact);
        return dto;
    }
    Contact convertLeadToContact(Lead lead){
        Contact contact = modelMapper.map(lead, Contact.class);
        return contact;
    }

    ContactDto mapToContactDto(Contact savedContact){
        ContactDto dto = modelMapper.map(savedContact, ContactDto.class);
        return dto;
    }

}
