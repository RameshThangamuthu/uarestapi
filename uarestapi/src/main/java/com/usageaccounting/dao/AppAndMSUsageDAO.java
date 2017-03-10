package com.usageaccounting.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.usageaccounting.entity.Content;
import com.usageaccounting.entity.Cloudlet_usage;
import com.usageaccounting.entity.AppAndMSMetricUsage;
import com.usageaccounting.entity.AppDataMonitoring;
import com.usageaccounting.entity.AppDataMonitoringInput;

/**
 * DAO interface for Application to perform CRUD operation.
 * @author Aricent
 * @version 1.0
 */
public interface AppAndMSUsageDAO {
	
	/**
	* Used to Create the Application Information
	* @param appAndMSUsage
	* @return {@link AppAndMSUsage} 
	*/
	public void createAppAndMSUsage(AppAndMSMetricUsage appAndMSMetricUsage);
	
	public void createCloudletUsage(List<Cloudlet_usage> aggcloudletUsage);
	
	public AppDataMonitoring getLastOnedayAppDate(AppDataMonitoringInput appDataMonitoringInput,int l_StartDay,int l_Starthour,int l_Startminute);
	
	public AppDataMonitoring getPreviousDaysAppData(AppDataMonitoringInput appDataMonitoringInput,int l_StartDay,int l_Starthour,int l_Startminute, int l_Year, int l_intervel);
	
	public AppDataMonitoring getLastOneHourAppData(AppDataMonitoringInput appDataMonitoringInput,int l_StartDay,int l_Starthour,int l_Startminute);
}
