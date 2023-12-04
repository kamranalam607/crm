package com.zohocrm.service;

import com.zohocrm.payload.ContactDto;
import com.zohocrm.payload.LeadDto;

import java.util.List;

public interface ContactService {
    ContactDto createContact(String leadId);


}
