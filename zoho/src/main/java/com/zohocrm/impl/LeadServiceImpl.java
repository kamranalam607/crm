package com.zohocrm.impl;

import com.zohocrm.entity.Lead;
import com.zohocrm.exception.LeadExist;
import com.zohocrm.payload.LeadDto;
import com.zohocrm.payload.LeadResponse;
import com.zohocrm.repository.LeadRepository;
import com.zohocrm.service.LeadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeadServiceImpl implements LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LeadDto createLead(LeadDto leadDto) {
        Boolean existEmail = leadRepository.existsByEmail(leadDto.getEmail());
        if (existEmail){
            throw new LeadExist("Email is exist: "+leadDto.getEmail());
        }
        Boolean existMobile = leadRepository.existsByMobile(leadDto.getMobile());
        if (existMobile){
            throw new LeadExist("Mobile is exist: "+leadDto.getMobile());
        }
        Lead lead = mapToLead(leadDto);
        String leadId = UUID.randomUUID().toString();
        lead.setLid(leadId);
        Lead savedLead = leadRepository.save(lead);
        LeadDto dto = mapToLeadDto(savedLead);
        return dto;
    }

    @Override
    public void deleteLeadById(String lid) {
        leadRepository.findById(lid).orElseThrow(
                ()-> new LeadExist("lid is not found")
        );
        leadRepository.deleteById(lid);
    }

    @Override
    public LeadResponse getAllLead(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort =  sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Lead> all = leadRepository.findAll(pageable);
        List<Lead> leads = all.getContent();
        List<LeadDto> dtos = leads.stream().map(lead -> mapToLeadDto(lead)).collect(Collectors.toList());

        LeadResponse leadResponse = new LeadResponse();
        leadResponse.setContent(dtos);
        leadResponse.setPageNo(all.getNumber());
        leadResponse.setPageSize(all.getSize());
        leadResponse.setTotalPages(all.getTotalPages());
        leadResponse.setTotalElements((int)all.getTotalElements());
        leadResponse.setLast(all.isLast());
        return leadResponse;
    }

    @Override
    public LeadDto updateLead(LeadDto leadDto, String lid) {
        Lead lead = leadRepository.findById(lid).orElseThrow(
                () -> new LeadExist("Lead is not found with lid: " + lid)
        );
       lead.setFirstName(leadDto.getFirstName());
       lead.setLastName(leadDto.getLastName());
       lead.setEmail(leadDto.getEmail());
       lead.setMobile(leadDto.getMobile());
       lead.setLeadType(leadDto.getLeadType());
       lead.setAddress(leadDto.getAddress());
       lead.setCompany(leadDto.getCompany());
       lead.setDesignation(leadDto.getDesignation());
       lead.setNote(leadDto.getNote());
        Lead updatedLead = leadRepository.save(lead);
        LeadDto dto = maptToUpdateLeadDto(updatedLead);
        return dto;
    }

    @Override
    public List<Lead> getLeadsExcelReports() {
        List<Lead> leads = leadRepository.findAll();
        return leads;
    }

    private LeadDto maptToUpdateLeadDto(Lead updatedLead) {
        LeadDto dto = modelMapper.map(updatedLead, LeadDto.class);
        return dto;
    }



    private LeadDto mapToLeadDto(Lead savedLead) {
        LeadDto dto = modelMapper.map(savedLead, LeadDto.class);
        return dto;
    }

    Lead mapToLead(LeadDto leadDto){
        Lead lead = modelMapper.map(leadDto, Lead.class);
        return lead;
    }

        LeadDto mapToLeadDtos(Lead lead){
        LeadDto dto = modelMapper.map(lead, LeadDto.class);
        return dto;
    }

}
