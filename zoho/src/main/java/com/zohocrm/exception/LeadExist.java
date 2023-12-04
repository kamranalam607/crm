package com.zohocrm.exception;

public class LeadExist extends RuntimeException{

    public LeadExist(String message) {
        super(message);
    }
}
