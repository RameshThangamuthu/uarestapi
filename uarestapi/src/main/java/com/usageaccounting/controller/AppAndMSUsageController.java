package com.usageaccounting.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usageaccounting.entity.AppAndMSUsage;
import com.usageaccounting.entity.AppDataMonitoring;
import com.usageaccounting.entity.AppDataMonitoringInput;
import com.usageaccounting.entity.CloudletVMUsage;
import com.usageaccounting.entity.Content;
import com.usageaccounting.entity.CreditUpdate;
import com.usageaccounting.service.AppAndMSUsageService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author Aricent
 * @version 1.0
 */
@RestController
public class AppAndMSUsageController {

	@Autowired
    private AppAndMSUsageService appAndMSUsageService;
	private CountDownLatch latch = new CountDownLatch(1);
	private Content l_content = new Content();
	static Logger log = Logger.getLogger(AppAndMSUsageController.class.getName());
	
	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	Environment environment;
    
    public AppAndMSUsageController() {
        System.out.println("ApplicationController()");
    }
    
    public CountDownLatch getLatch() {
        return latch;
     }
  
   
    @RequestMapping(value = "/api/UA/v1.1/appAndMSUsageDetails", method = RequestMethod.POST)    
    Content create(@RequestBody AppAndMSUsage appAndMSUsage) throws InterruptedException {
    	try
    	{
        	if (appAndMSUsage.getApp_id().isEmpty() || appAndMSUsage.getDev_id().isEmpty() || appAndMSUsage.getCloudlet_id().isEmpty()
        		|| appAndMSUsage.getnetwork_name().isEmpty() || appAndMSUsage.getMs_data().isEmpty()
        		|| appAndMSUsage.getUuid().isEmpty())
        	{
        		l_content.setCode(HttpStatus.BAD_REQUEST.value());
    	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. One or more mandatory fields are missing."); 
    	   	 	l_content.setStatus("Failure");
    	   	 	return l_content;
        	}
        	
        	for (int j = 0; j < appAndMSUsage.getMs_data().size(); j++) {
            	if (appAndMSUsage.getMs_data().get(j).getMs_name().isEmpty() || appAndMSUsage.getMs_data().get(j).getSubscribed().isEmpty())
                	{
                		l_content.setCode(HttpStatus.BAD_REQUEST.value());
            	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. One or more mandatory fields are missing."); 
            	   	 	l_content.setStatus("Failure");
            	   	 	return l_content;
                	}
        	}
        	
        	
	      	byte[] data = SerializationUtils.serialize(appAndMSUsage);
	     	rabbitTemplate.convertAndSend("usageacc-queue",data );
	    	latch.await(500000,TimeUnit.MILLISECONDS);
	    	l_content.setCode(200);
	   	 	l_content.setMessage("The message pushed successfully"); 
	   	 	l_content.setStatus("Success");
	   	 	
	   	 	return l_content;
    	}
    	catch(Exception e)
     	{
    		l_content.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	   	 	l_content.setMessage("Service execution failed. Please try again after sometime."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}
}
    
    @RequestMapping(value = "/api/UA/v1.1/cloudletInfraUsageDetails", method = RequestMethod.POST)    
    Content createCloudletUsage(@RequestBody CloudletVMUsage cloudletVMUsage) throws InterruptedException {    
    	log.info("CreditsController::operation createCloudletUsage");
    	try {
	    	if (cloudletVMUsage.getCloudlet_id().isEmpty())
	    	{
	    		log.error("CloudletId is null.");
	    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
		   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. value of mandatory field cloudlet_id/Vm_name/Vm_id is missing."); 
		   	 	l_content.setStatus("Failure");
		   	 	return l_content;
	    	}
	    	
			for (int j = 0; j < cloudletVMUsage.getVm_data().size(); j++) {
            	if (cloudletVMUsage.getVm_data().get(j).getVm_name().isEmpty() || cloudletVMUsage.getVm_data().get(j).getVm_id().isEmpty())
            	{
            		l_content.setCode(HttpStatus.BAD_REQUEST.value());
    		   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. value of mandatory field cloudlet_id/Vm_name/Vm_id is missing.");
        	   	 	l_content.setStatus("Failure");
        	   	 	return l_content;
            	}

			}	
	      	byte[] data = SerializationUtils.serialize(cloudletVMUsage);
	     	rabbitTemplate.convertAndSend("usagecloudlet-queue",data );
	    	latch.await(500000,TimeUnit.MILLISECONDS);
	    	
	    	l_content.setCode(200);
	   	 	l_content.setMessage("The message pushed successfully"); 
	   	 	l_content.setStatus("Success");
	     	return l_content;
    	}
    	catch(Throwable e){
    		log.warn("Inside  AppAndMSUsageController::createCloudlet() some exception happened----< " + e.getMessage() +">");
    		l_content.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	   	 	l_content.setMessage("Service execution failed. Please try again after sometime."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}     	
      }
    
    public void appAndMSUsage_Receiver(byte[] bytes) {  
    	try{
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			log.info("appAndMSUsage_Receiver Start time ->"+dateFormat.format(date));
			AppAndMSUsage appAndMSUsage = (AppAndMSUsage) SerializationUtils.deserialize(bytes);
			appAndMSUsageService.createAppAndMSUsage(appAndMSUsage);
			Date date2 = new Date();
			log.info("appAndMSUsage_Receiver End Time -->"+dateFormat.format(date2));
			latch.countDown();
    	}
    	catch(Throwable e){
    		log.warn("Inside  AppAndMSUsageController::appAndMSUsage_Receiver() some exception happened----< " + e.getMessage() +">");
    		latch.countDown();
    	}
      }
    
    public void cloudletVMUsage_Receiver(byte[] bytes) {  
    	try{
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			log.info(" cloudletVMUsage_Receiver Start time ->"+dateFormat.format(date));
			CloudletVMUsage cloudletVMUsage = (CloudletVMUsage) SerializationUtils.deserialize(bytes);
			appAndMSUsageService.createCloudletInfraUsage(cloudletVMUsage);
			Date date2 = new Date();
			log.info(" cloudletVMUsage_Receiver End Time -->"+dateFormat.format(date2));
			latch.countDown();
    	}
    	catch(Throwable e){
    		log.warn("Inside  AppAndMSUsageController::cloudletVMUsage_Receiver() some exception happened----< " + e.getMessage() +">");
    		latch.countDown();
    	}
      }
    
    
    @RequestMapping(value = "/api/UA/v1.1/getAppUsageData", method = RequestMethod.GET)    
    AppDataMonitoring getAppMonitoringData(@RequestParam(value = "userid", required = true) String userId,
            @RequestParam(value = "appName1", required = false) String appName1,
            @RequestParam(value = "appName2", required = false) String appName2,
            @RequestParam(value = "appName3", required = false) String appName3,
            @RequestParam(value = "timeCycle", required = false) String timeCycle,
            @RequestParam(value = "metricType", required = false) String metricType) throws InterruptedException {    
    	log.info("CreditsController::operation getAppMonitoringData");
    try
    {
/*    	if (creditUpdate.getUserID().isEmpty())
    	{
    		log.error("userID is null.");
    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. value of mandatory field userId is missing."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}*/
    	
    	
    	AppDataMonitoringInput l_appDataMonitoringInput = new AppDataMonitoringInput();
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	
    	l_appDataMonitoringInput.setUserID(userId);
    	log.info(" incoming value  ->"+appName1+"##"+appName2+"##"+appName3);

    	l_appDataMonitoringInput.setAppName1((appName1 == null || appName1.isEmpty())  ? "" :appName1);
    	l_appDataMonitoringInput.setAppName2((appName2 == null || appName2.isEmpty())  ? "" :appName2);
    	l_appDataMonitoringInput.setAppName3((appName3 == null || appName3.isEmpty())  ? "" :appName3);
    	l_appDataMonitoringInput.setTimeCycle(timeCycle);
    	l_appDataMonitoringInput.setMetricType(metricType);
    	log.info("Calling appAndMSUsageService::appDataMonitoringInput()" + l_appDataMonitoringInput.toString());
    	Date date = new Date();
    	log.info(" cloudletVMUsage_Receiver Start time ->"+dateFormat.format(date));
    	AppDataMonitoring l_AppDataMonitoring= appAndMSUsageService.getAppDataMonitoring(l_appDataMonitoringInput);
    	log.info("CreditsController::operation getAppMonitoringData --return");
    	Date date_2 = new Date();
    	log.info(" cloudletVMUsage_Receiver End time ->"+dateFormat.format(date_2));
    	return l_AppDataMonitoring;
    	
     }
    	catch(Throwable e){
    		log.warn("Inside  AppAndMSUsageController::getAppMonitoringData() some exception happened----< " + e.getMessage() +">");
    		return new AppDataMonitoring();
    	} 
      }
}
