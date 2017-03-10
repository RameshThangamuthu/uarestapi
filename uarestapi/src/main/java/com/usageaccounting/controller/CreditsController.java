package com.usageaccounting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.usageaccounting.entity.Content;
import com.usageaccounting.entity.CreditUpdate;
import com.usageaccounting.service.CreditService;

import org.apache.log4j.Logger;



@RestController
public class CreditsController {

    @Autowired
    private CreditService updateCreditsService; 
    
    static Logger log = Logger.getLogger(CreditsController.class.getName());
    
	@Autowired
	Environment environment; 
	
	private Content l_content = new Content();
	
    public CreditsController() {
        log.info("UpdateCreditsController()");
    }
    
    @RequestMapping(value = "/api/UA/v1.1/updateCredits", method = RequestMethod.POST)    
    Content creditAdjustment(@RequestBody CreditUpdate creditUpdate) throws InterruptedException {    
    	log.info("CreditsController::operation updateCredits");
    	
    	if (creditUpdate.getUserID().isEmpty())
    	{
    		log.error("userID is null.");
    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. value of mandatory field userId is missing."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}
    	
    	log.info("Calling CreditService::creditAdjustment()");
     	return updateCreditsService.creditAdjustment(creditUpdate);
      }
}
