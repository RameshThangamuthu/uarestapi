package com.usageaccounting.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.usageaccounting.entity.Content;
import com.usageaccounting.entity.CreditAddOrDelete;
import com.usageaccounting.service.CreditService;


@RestController
public class AddOrDeleteCreditsController {

	@Autowired
    private CreditService creditsService; 
    
    static Logger log = Logger.getLogger(AddOrDeleteCreditsController.class.getName());
    
	@Autowired
	Environment environment; 
	
	private Content  l_content = new Content();
	
    public AddOrDeleteCreditsController() {
        log.info("AddOrDeleteCredits()");
    }
    
    @RequestMapping(value = "/api/UA/v1.1/addOrDeleteCredits", method = RequestMethod.POST)    
    Content creditAddOrDelete(@RequestBody CreditAddOrDelete creditAddOrDelete) throws InterruptedException {    
    	log.info("CreditsController::operation addOrDeleteCredits");
    	
    	if (creditAddOrDelete.getUserID().isEmpty())
    	{
    		log.error("userId cannot be null or empty.");
    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. value of mandatory field userId is missing."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}    	
    	
    	if ((! creditAddOrDelete.getAddOrDelete().equalsIgnoreCase("ADD")) && (! creditAddOrDelete.getAddOrDelete().equalsIgnoreCase("DELETE")))
    	{
    		log.error("addOrDelete field value not permitted.");
    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. Only add or delete operations are allowed."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}
    	
    	log.info("Calling CreditService::creditAddOrDelete()");
    	
		return creditsService.creditAddOrDelete(creditAddOrDelete);
    }
}
