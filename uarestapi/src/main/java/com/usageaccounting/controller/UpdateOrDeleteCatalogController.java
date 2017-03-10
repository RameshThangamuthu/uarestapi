package com.usageaccounting.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.usageaccounting.entity.BillingRateCatalog;
import com.usageaccounting.entity.Content;
import com.usageaccounting.entity.UpdateDeleteCatalog;
import com.usageaccounting.service.CreditService;

@RestController
public class UpdateOrDeleteCatalogController {
	
	@Autowired
    private CreditService creditsService; 
    
    static Logger log = Logger.getLogger(UpdateOrDeleteCatalogController.class.getName());
    
	@Autowired
	Environment environment; 
	
	private Content l_content = new Content();
	
    public UpdateOrDeleteCatalogController() {
        log.info("UpdateOrDeleteCatalog()");
    }
    
    BillingRateCatalog billingRateCatalog = new BillingRateCatalog();
    
    @RequestMapping(value = "/api/UA/v1.1/updateRateCatalog", method = RequestMethod.POST)    
    Content updateOrDeleteCatalog(@RequestBody UpdateDeleteCatalog updateDeleteCatalog) throws InterruptedException {
    	
    	log.info("UpdateOrDeleteCatalogController::operation updateOrDeleteCatalog");
    	
    	if (updateDeleteCatalog.getUserID().isEmpty() || updateDeleteCatalog.getUserID().equals(""))
    	{
    		log.error("userID is null.");
    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. Mandatory field (userid or microServiceId) is missing."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}
    	else if (updateDeleteCatalog.getMicroServiceName().isEmpty() || updateDeleteCatalog.getMicroServiceName().equals("")) {
    		log.error("microServiceId is null.");
    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. Mandatory field (userid or microServiceId) is missing.");
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}

    	return creditsService.updateOrDeleteCatalog(updateDeleteCatalog);
    }

}
