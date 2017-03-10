package com.usageaccounting.service;

import com.usageaccounting.entity.Content;
import com.usageaccounting.entity.CreditAddOrDelete;
import com.usageaccounting.entity.CreditUpdate;
import com.usageaccounting.entity.UpdateDeleteCatalog;

/**
 * Service interface for Credit update to on board App/Microservice
 * @author Aricent
 * @version 1.0
 */

public interface CreditService {

    /**
     * Used to Create the Update credit of a User during on boarding of a App/Micro Service
     * @param CreditUpdate
     * @return {@link Content}
     */
	 public Content creditAdjustment(CreditUpdate creditUpdate);
	 
    /**
     * 
     * @param creditAddOrDelete
     * @return {@link Content}
     */
    public Content creditAddOrDelete(CreditAddOrDelete creditAddOrDelete);
    
    
    /**
     * 
     * @param billingRateCatalog
     * @return {@link Content}
     */
    public Content updateOrDeleteCatalog(UpdateDeleteCatalog updateDeleteCatalog);
    
}
