package com.zohocrm.service;

import com.zohocrm.payload.EmailDto;

public interface EmailService {

    EmailDto sendSimpleMail(EmailDto emailDto);

}
