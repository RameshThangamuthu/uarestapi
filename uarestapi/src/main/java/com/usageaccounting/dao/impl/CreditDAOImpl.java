package com.usageaccounting.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.usageaccounting.dao.CreditDAO;
import com.usageaccounting.entity.BillingRateCatalog;
import com.usageaccounting.entity.Credit;
import com.usageaccounting.util.MyCassandraTemplate;



/**
 * DAOImpl implementation for Credit update to on board App/Microservice
 * @author Aricent
 * @version 1.0

 */
@Repository
public class CreditDAOImpl implements CreditDAO{

    @Autowired
    private MyCassandraTemplate myCassandraTemplate;
    static Logger log = Logger.getLogger(CreditDAOImpl.class.getName());
   
    
    @Override
    public Credit getUserCreditDetails(String UserID){
    	return myCassandraTemplate.getCreditsByuserID(UserID, Credit.class);
    }
    
    @Override
    public Credit onBoardCreditAdjustment(Credit credit) {     
        return myCassandraTemplate.onBoardCreditAdjustment(credit, Credit.class);
    }
    
    @Override
    public BillingRateCatalog microserviceOnboarding(BillingRateCatalog billingRateCatalog) {
    	return myCassandraTemplate.create(billingRateCatalog);
    }
    
    @Override
    public BillingRateCatalog updateCatalog(BillingRateCatalog billingRateCatalog) {
    	return myCassandraTemplate.update(billingRateCatalog);
    }

    @Override
    public void deleteCatalog(BillingRateCatalog billingRateCatalog) {
    	myCassandraTemplate.deleteById(billingRateCatalog.getComponent(), BillingRateCatalog.class);
    }
    
    @Override
    public boolean checkMicroservice(String component) {
    	return myCassandraTemplate.exists(component, BillingRateCatalog.class);
    }
}
