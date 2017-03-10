package com.usageaccounting.service.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usageaccounting.dao.CreditDAO;
import com.usageaccounting.entity.BillingRateCatalog;
import com.usageaccounting.entity.Content;
import com.usageaccounting.entity.Credit;
import com.usageaccounting.entity.CreditAddOrDelete;
import com.usageaccounting.entity.CreditUpdate;
import com.usageaccounting.entity.UpdateDeleteCatalog;
import com.usageaccounting.entity.UserProfile;
import com.usageaccounting.service.CreditService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Service implementation for Credit update to on board App/Microservice
 * @author Aricent
 * @version 1.0
 */

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired  
    private CreditDAO creditDAO;
    
    private Credit l_credit;
    private Content  l_content = new Content(); 
    static Logger log = Logger.getLogger(CreditServiceImpl.class.getName());
    private BillingRateCatalog billingRateCatalog = new BillingRateCatalog();
    private UserProfile l_userProfile;
    private int l_userCredit=0;
    
    /**
     * Default Constructor
     */
    public CreditServiceImpl() {
        super();    
    }

    @Override
    public Content creditAdjustment(CreditUpdate creditUpdate) {
    	log.info("CreditService::creditAdjustment()");
    	try{
    		
	    	int onboardingCharge=creditUpdate.getOnboardingCharge();
	    	String userId = creditUpdate.getUserID();
	    	
	    	if ((!creditUpdate.getappName().isEmpty()) || (!creditUpdate.getappName().equals("")))
	    	{
	    		log.info("appName found.");
		    	
		    	if (onboardingCharge == 0)
		    	{
		    		log.error("onboardingCharge is 0.");
		    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
	    	   	 	l_content.setMessage("The request couldn’t be processed. Onboarding charge for application cannot be zero."); 
	    	   	 	l_content.setStatus("Failure");
	    	   	 	return l_content;
		    	}
	    	
		    	log.info("Fetching credit details.");
		    	
		    	//get the user credit details from user_profile based on input user id
		    	l_userCredit = findAndRetrunCredit(userId);
		    	if (l_userCredit == -9999)
		    	{
		    		return l_content;
		    	}
		    	
		    	log.info("Calculating new credit.");
		    	int new_credit= l_userCredit - onboardingCharge;
		    	
	    		billingRateCatalog.setComponent(userId + "_" + creditUpdate.getappName() + "_" + "ON_BOARDING_CHARGE");
	    		billingRateCatalog.setRate(creditUpdate.getOnboardingCharge());
	    		
	    		log.info("Calling CreditDAO::applicationOnboarding() for on boarding charge.");
	    		creditDAO.microserviceOnboarding(billingRateCatalog);
		    	
		    	log.info("Calling CreditDAO::onBoardCreditAdjustment()");
		    	updateCreditOfUser(userId,new_credit);
			
			/*	if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
				}*/

		    	log.info("Credits Deducted successfully");
		        l_content.setMessage("Credits Deducted successfully");
		        l_content.setStatus("Success");
		        l_content.setCode(200);
		        return l_content;
	    	}
	    	else if ((!creditUpdate.getmicroServiceName().isEmpty()) || (!creditUpdate.getmicroServiceName().equals("")))
	    	{
	    		log.info("microServiceID found.");

	    		
	    		if (onboardingCharge == 0)
		    	{
		    		log.error("onboardingCharge is 0.");
		    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
	    	   	 	l_content.setMessage("The request couldn’t be processed. Onboarding charge for microservice cannot be zero."); 
	    	   	 	l_content.setStatus("Failure");
	    	   	 	return l_content;
		    	}
	    	
		    	log.info("Fetching credit details.");
		    	
		    	//get the user credit details from user_profile based on input user id
		    	l_userCredit = findAndRetrunCredit(userId);
		    	if (l_userCredit == -9999)
		    	{
		    		return l_content;
		    	}
		    	
		    	log.info("Calculating new credit.");
		    	int new_credit= l_userCredit - onboardingCharge;

		    	
		    	updateCreditOfUser(userId,new_credit);
		    	log.info("Credits Deducted successfully");
	
	    		billingRateCatalog.setComponent(userId + "_" + creditUpdate.getmicroServiceName() + "_" + "ON_BOARDING_CHARGE");
	    		billingRateCatalog.setRate(creditUpdate.getOnboardingCharge());
	    		
	    		creditDAO.microserviceOnboarding(billingRateCatalog);
	    		billingRateCatalog.setComponent(userId + "_" + creditUpdate.getmicroServiceName() + "_" + "API_CHARGE");
	    		billingRateCatalog.setRate(creditUpdate.getCreditsPerAPICall());
	    		
	    		log.info("Calling CreditDAO::microserviceOnboarding() for API charge.");
	    		creditDAO.microserviceOnboarding(billingRateCatalog);

	    		billingRateCatalog.setComponent(userId + "_" + creditUpdate.getmicroServiceName() + "_" + "LIBRARY_CHARGE");
	    		billingRateCatalog.setRate(creditUpdate.getCreditsForDownload());
	    		
	    		log.info("Calling CreditDAO::microserviceOnboarding() for library charge.");
	    		creditDAO.microserviceOnboarding(billingRateCatalog);

	    		log.info("Microservice onboarding successful.");
	    		l_content.setMessage("Microservice onboarding successful.");
	    		l_content.setStatus("Success");
		        l_content.setCode(200);
		        return l_content;
	    		
	    	}
	    	else {
	    		log.error("Both appName and microServiceId null.");
	    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
    	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. Both appName and microServiceId cannot be null."); 
    	   	 	l_content.setStatus("Failure");
    	   	 	return l_content;
	    	}
	    }
    	catch(Throwable exception)
    	{
    		log.error("Error encountered." + exception.getMessage());
    		l_content.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		l_content.setMessage("Service execution failed. Please try again after sometime."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
      	}   	
     }
    
    @Override
    public Content creditAddOrDelete(CreditAddOrDelete creditAddOrDelete) {
    	
    	int newCredit = 0;
    	Credit creditToUpdate = null;
    	
    	
    	log.info("CreditService::creditAddOrDelete()");
    	try {
    		
    		log.info("Fetching userID.");
    		String l_userId=creditAddOrDelete.getUserID();
	    	l_userCredit = findAndRetrunCredit(l_userId);
	    	if (l_userCredit == -9999)
	    	{
	    		return l_content;
	    	}

	    	if (creditAddOrDelete.getAddOrDelete().equalsIgnoreCase("ADD")) {
    			log.info("Adding credits.");
    			newCredit = l_userCredit + creditAddOrDelete.getNumberOfCredits();
    		}
    		else if (creditAddOrDelete.getAddOrDelete().equalsIgnoreCase("DELETE")) {
    			log.info("Subtracting credits.");
    			newCredit = l_userCredit - creditAddOrDelete.getNumberOfCredits();
    		}
	    	
	    	log.info("Updating the user credit details.");
	    	updateCreditOfUser(l_userId,newCredit);
    		
    		log.info("Updating credits.");
    		//creditDAO.onBoardCreditAdjustment(creditToUpdate);
    		
    		
    		log.info("Credits update successful.");
    		l_content.setMessage("New Credit details updated successfully.");
    		l_content.setStatus("Success");
	        l_content.setCode(200);
	        return l_content;
    	}
    	catch (Throwable exception) {
    		log.error("Error encountered." + exception.getMessage());
    		l_content.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	   	 	l_content.setMessage("Service execution failed. Please try again after sometime."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}
    }
    
    @Override
    public Content updateOrDeleteCatalog(UpdateDeleteCatalog updateDeleteCatalog) {
    	log.info("CreditService::updateOrDeleteCatalog()");
    	try {
    		
    		String userId = updateDeleteCatalog.getUserID();
    		String microserviceId = updateDeleteCatalog.getMicroServiceName();
    		//String process = updateDeleteCatalog.getUpdate();
    		
/*    		if (! process.equalsIgnoreCase("UPDATE")) {
    			log.error("only update allowed.");
	    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
    	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. Only update operation is  allowed."); 
    	   	 	l_content.setStatus("Failure");
    	   	 	return l_content;
    		}
    		*/
    		if (creditDAO.checkMicroservice(userId + "_" + microserviceId + "_" + "API_CHARGE")) {
    			
    			billingRateCatalog.setComponent(userId + "_" + microserviceId + "_" + "API_CHARGE");
    			billingRateCatalog.setRate(updateDeleteCatalog.getCreditsPerAPICall());

    			log.info("Calling CreditDAO::updateCatalog for API charge.");
        			
        		creditDAO.updateCatalog(billingRateCatalog);

        		billingRateCatalog.setComponent(userId + "_" + microserviceId + "_" + "LIBRARY_CHARGE");
        		billingRateCatalog.setRate(updateDeleteCatalog.getCreditsForDownload());
        		
	    		log.info("Calling CreditDAO::updateCatalog for library charge.");
	    		creditDAO.updateCatalog(billingRateCatalog);
	    		
	    		log.info("Billing_Rate_catalog table updated successfully.");
	    		l_content.setMessage("Billing_Rate_catalog table updated successfully.");
	    		l_content.setStatus("Success");
		        l_content.setCode(200);    	    		
    		}
    		else {
    			log.error("MicroService Name for the UserId not found");
    			l_content.setCode(HttpStatus.BAD_REQUEST.value());
    	   	 	l_content.setMessage("The request couldn’t be understood. Please check the JSON request parameters. MicroService Name not found."); 
    	   	 	l_content.setStatus("Failure");
    		}
    		
    		return l_content;
    	}
    	catch(Throwable exception) {
    		log.error("Error Encountered." + exception.getMessage());
    		l_content.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	   	 	l_content.setMessage("Service execution failed. Please try again after sometime."); 
	   	 	l_content.setStatus("Failure");
	   	 	return l_content;
    	}
    }
    

	private int findAndRetrunCredit(String  i_UserId) {
		log.info("Inside  CreditServiceImpl::findAndRetrunCredit.");
		int l_CurrentCredit=0;
		
		try {			
			//String url_link="http://172.19.74.253:8081/central/userprofile/users?userId="+i_UserId;
			
			log.info("The incoming url link ---------------<<<<<<<<"+ System.getProperty("CR_LINK_2") +">>>>>>>");
			String url_link=System.getenv("CR_LINK_2")+"central/userprofile/users?userId="+i_UserId;

			URL url = new URL(url_link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			System.out.println(conn.getResponseCode());
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				//System.out.println(output);
				String[] parts = output.split(",");
				String[] parts_2 =parts[0].split(":");
				if (parts_2[0].equalsIgnoreCase("{\"message\""))
				{
					parts_2 =parts[2].split(":");
					if (parts_2[1].equalsIgnoreCase("\"404\"}"))	
					{
						log.info("The incoming user id does not exist in the system");
			    		l_content.setCode(HttpStatus.BAD_REQUEST.value());
		    	   	 	l_content.setMessage("The request couldn’t be processed. The incoming user id does not exist."); 
		    	   	 	l_content.setStatus("Failure");
		    	   	 	return -9999;
					}
				}
				else
				{
					parts_2 =parts[4].split(":");
					if (parts_2[0].equalsIgnoreCase("\"credits\""))
					{
						l_CurrentCredit = Integer.parseInt(parts_2[1]);
					}
				}
			}
			conn.disconnect();

		  } catch (Throwable e) {
			 log.warn("Inside  CreditServiceImpl::findAndRetrunCredit some exception happened----< " + e.getMessage() +">");
			//e.printStackTrace();
		  }
		return l_CurrentCredit;
	}
	
	private void updateCreditOfUser(String  i_UserId, int i_userCredit) {
		log.info("Inside  CreditServiceImpl::updateCreditOfUser.");
    	try
    	{
			log.info("The incoming url link ---------------<<<<<<<<"+ System.getProperty("CR_LINK_2") +">>>>>>>");
			String url_link=System.getenv("CR_LINK_2")+"central/userprofile/users?userId="+i_UserId+"&fields=credits";
			
	    	URL url = new URL(url_link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
	
			String input = "{\"credits\":"+i_userCredit+"}";
			
			log.info("Calling CreditDAO::onBoardCreditAdjustment()"+ input);
			
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}
    	}
	   catch (Throwable e) {
		e.printStackTrace();

	  }
    	
	}
}
