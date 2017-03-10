package com.usageaccounting.service;

import java.io.IOException;
import java.util.List;

import com.usageaccounting.entity.AppAndMSUsage;
import com.usageaccounting.entity.AppDataMonitoring;
import com.usageaccounting.entity.AppDataMonitoringInput;
import com.usageaccounting.entity.CloudletVMUsage;;


/**
 * Service interface for Application to perform CRUD operation.
 * @author Aricent
 * @version 1.0
 */
public interface AppAndMSUsageService {
	
    /**
     * Used to Create the Application Information
     * @param AppAndMSUsage
     * @return {@link AppAndMSUsage} 
     */
    public void createAppAndMSUsage(AppAndMSUsage AppAndMSUsage)  throws  IOException;
    
    public void createCloudletInfraUsage(CloudletVMUsage cloudletUsage)  throws  IOException;
    
    public AppDataMonitoring getAppDataMonitoring(AppDataMonitoringInput appDataMonitoringInput) throws  IOException;
}
