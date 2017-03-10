package com.usageaccounting.dao;

import java.util.List;

import com.usageaccounting.entity.BillingRateCatalog;
import com.usageaccounting.entity.Credit;


/**
 * Service implementation for Credit update to on board App/Microservice
 * @author Aricent
 * @version 1.0
 */

public interface CreditDAO {

	    /**
		* Getting the User credit details from  user_profile table
		* @param UserID
		* @return {@link Credit}
		*/	
	    public Credit getUserCreditDetails(String UserID);

	    /**
		* Update the User credit details in user_profile table for input user id
		* @param credit
		* @return {@link credit}
		*/	
	    public Credit onBoardCreditAdjustment(Credit credit);
	    
	    /**
	     * Micro-service onboarding
	     * @param creditUpdate
	     * @return {@link CreditUpdate}
	     */
	    public BillingRateCatalog microserviceOnboarding(BillingRateCatalog billingRateCatalog);
	    
	    
	    /**
	     * Update catalog
	     * @param updateCatalog
	     * @return {@link BillingRateCatalog}
	     */
	    public BillingRateCatalog updateCatalog(BillingRateCatalog billingRateCatalog);
	    
	    /**
	     * Update catalog
	     * @param deleteCatalog
	     * @return {@link BillingRateCatalog}
	     */
	    public void deleteCatalog(BillingRateCatalog billingRateCatalog);
	    
	    /**
	     * @param component
	     * @return int
	     */
	    public boolean checkMicroservice(String component);
	    
}
