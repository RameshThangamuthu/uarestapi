package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
 
/**
 * AppAndMSUsage entity class.
 * 
 * @author Aricent
 * @version 1.0
 */
public class AppAndMSUsage implements Serializable {
	
	private String id;    
	    
    private int period;
    
    private String dev_id;
    
    private String app_id;
   
    private String app_name;
    
    private String region;
    
    private String cloudlet_id;
 
    private String telco_id;
    
    private String uuid;
    
    private String network_name;
    
 	private List<Ms_data> ms_data = new LinkedList<Ms_data>();
    
    private int app_user_count;
    
    private String timestamp;
    
    private int app_latency;
	    
    /**
     * Default Constructor
     */
    public AppAndMSUsage() {
        super();        
    }
    
   
    /**
     * Parameterized Constructor
     */
    public AppAndMSUsage(String id, int period, String dev_id, String app_id, 
    		           String app_name,String cloudlet_id, String telco_id,String region,
    		           String uuid, String network_name, String containerCacheUsage,
    		           List<Ms_data> ms_data,
    		           int app_user_count, String timestamp,int app_latency) {
        super();
        this.id = id;    
		this.period = period;
		this.dev_id = dev_id;
		this.app_id= app_id;
		this.app_name = app_name;
		this.cloudlet_id = cloudlet_id;
		this.region =region;
		this.telco_id = telco_id;
		this.uuid = uuid;
		this.network_name = network_name;
		this.timestamp = timestamp;
		this.ms_data =ms_data;
		this.app_latency = app_latency;
    }
   

    /**
     * @return the id
     */
    public String getId() {
    	return id;
    }
 
    /**
     * @return the period
     */
	public int getPeriod() {
		return period;
	}
	

    /**
     * @return the dev_id
     */
    public String getDev_id() {
    	return dev_id;
    }
 
    /**
     * @return the app_id
     */
	public String getApp_id() {
		return app_id;
	}
	

    /**
     * @return the app_name
     */
	public String getApp_name() {
		return app_name;
	}
	
	/**
	 * @return cloudletId
	 */
	public String getCloudlet_id() {
		return cloudlet_id;
	}
	
	
	/**
	 * @return telco_id
	 */
	public String getTelco_id() {

		return telco_id;
	}
	
	
	/**
	 * @return region
	 */
	public String getRegion() {

		return region;
	}
	
	/**
	 * @return uuid
	 */
	public String getUuid() {

		return uuid;
	}
	
	
	/**
	 * @return network_name
	 */
	public String getnetwork_name() {

		return network_name;
	}
	

    /**
     * @return the app_user_count
     */
	public int getApp_user_count() {
		return app_user_count;
	}
	
	/**
	 * @return timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 * @return userId
	 */
	public int getApp_latency() {
		return app_latency;
	}
	
	/**
	* @param id the id to set
	*/
	public void setId(String id) {
		this.id = id;
	}
	

	/**
	* @param period the period to set
	*/
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * @param dev_id the dev_id to set
	 */
	public void  setDev_id(String dev_id) {

		this.dev_id = dev_id;
	}


	/**
	 * @param app_id the app_id to set
	 */
	public void  setApp_id(String app_id) {

		this.app_id = app_id;
	}
	
	/**
	* @param appName the appName to set
	*/
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	/**
	 * @param cloudlet_id the cloudlet_id to set
	 */
	public void  setCloudlet_id(String cloudlet_id) {
		this.cloudlet_id = cloudlet_id;
	}
	
	/**
	 * @param telco_id the telco_id to set
	 */
	public void setTelco_id(String telco_id) {

		this.telco_id = telco_id;
	}
	
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {

		this.region = region;
	}
	
	/**
	 * @param uuid the uuid to set
	 */
	public void  setUuid(String uuid) {

		this.uuid = uuid;
	}

	/**
	 * @param network_name the network_name to set
	 */
	public void  setNetwork_name(String network_name) {

		this.network_name = network_name;
	}


	/**
	 * @param app_user_count the app_user_count to set
	 */
	public void  setApp_user_count(int app_user_count) {

		this.app_user_count = app_user_count;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	/**
	 * @param latency the latency to set
	 */
	public void setApp_latency(int app_latency) {
		this.app_latency = app_latency;
	}
	
	
	public List<Ms_data> getMs_data() {
		return ms_data;
	}
	
	public void setMicroservice(List<Ms_data> ms_data) {
		this.ms_data = ms_data;
	}
	
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AppAndMSUsage [id=" + id + ", period=" + period
        		+ ", dev_id=" + dev_id + ", app_id=" + app_id
        		+ ", app_name=" + app_name + ", cloudlet_id=" + cloudlet_id + ",region=" + region + ", telco_id=" + telco_id
        		+ ", uuid=" + uuid + ", network_name=" + network_name
        		+ ", app_user_count=" + app_user_count 
        		+ ",	timestamp=" + timestamp 
        		+ ",	app_latency=" + app_latency + "]";
    }

	
	
}