package com.zohocrm.controller;

import com.zohocrm.entity.Lead;
import com.zohocrm.impl.ExcelHelperService;
import com.zohocrm.impl.PdfHelperServices;
import com.zohocrm.payload.LeadDto;
import com.zohocrm.payload.LeadResponse;
import com.zohocrm.service.EmailService;
import com.zohocrm.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.InputStreamResource;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    //http://localhost:8080/api/leads
    @Autowired
    private LeadService leadService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> createLead(@RequestBody LeadDto leadDto){
       LeadDto dto = leadService.createLead(leadDto);
       return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{lid}")
    public ResponseEntity<String> deleteLeadById(@PathVariable String lid){
        leadService.deleteLeadById(lid);
        return new ResponseEntity<>("Lead is Deleted: "+lid,HttpStatus.OK);
    }

    //http://localhost:8080/api/leads?pageNo=1&pageSize=3&sortBy=lid&sortDir=asc
    @GetMapping
    public ResponseEntity<LeadResponse> getAllLead(@RequestParam(value = "pageNo",defaultValue = "0",required = false)int pageNo,
                                                         @RequestParam(value = "pageSize",defaultValue = "2",required = false)int pageSize,
                                                         @RequestParam(value = "sortBy", defaultValue = "lid",required = false)String sortBy,
                                                         @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir){
        LeadResponse leadResponse =  leadService.getAllLead(pageNo,pageSize,sortBy,sortDir);
       return new ResponseEntity<>(leadResponse,HttpStatus.OK);
    }

    @PutMapping("/{lid}")
    public ResponseEntity<LeadDto> updateLead(@RequestBody LeadDto leadDto,@PathVariable String lid){
       LeadDto dto = leadService.updateLead(leadDto,lid);
       return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    //http://localhost:8080/api/leads/excelReports
    @GetMapping("/excelReports")
    public ResponseEntity<Resource> getLeadsExcelReports(){
        List<Lead> leads = leadService.getLeadsExcelReports();
        ByteArrayInputStream leadReports = ExcelHelperService.leadsToExcel(leads);
        String filename = "leads.xlsx";
        InputStreamResource file = new InputStreamResource(leadReports);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    //http://localhost:8080/api/leads/leadsPDFReports

    @GetMapping(value = "/leadsPDFReports", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getleadPDFReport() throws IOException {
        List<Lead> leads = leadService.getLeadsExcelReports();

        ByteArrayInputStream pdfReport = PdfHelperServices.leadPDFReport(leads);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","inline; filename=leads.pdf");

        return ResponseEntity.ok().headers(headers).contentType
                        (MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfReport));
    }


}
