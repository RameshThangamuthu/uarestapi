package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.Date;


import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("application_Usage")
public class Application_usage implements Serializable {
	
	@PrimaryKey("userId")
	private String userId;

	@Column("appName")	
	private String appName;    
    
	@Column("region")
	private String region;
	  
    @Column("telcoId")
    private String telcoId;
    
    @Column("cloudletId")
    private String cloudletId;
 
    @Column("metric_type")
    private String metric_type;
    
    @Column("processingDate")
    private Date processingDate;
 
    @Column("metric_value")
    private String metric_value;
  
    @Column("day_hour_min")
    private String day_hour_min;
  
    @Column("min")
    private int min;
    
    @Column("flag")
    private int flag;    
    
    /**
     * Default Constructor
     */
    public Application_usage() {
        super();        
    }

    /**
     * Parameterized Constructor
     */
    public Application_usage(String userId,String appName, String region,String telcoId,String cloudletId,
    					String metric_type,Date  processingDate, String metric_value, String day_hour_min,int min,int flag) {
        super();
        this.userId = userId;
        this.appName = appName;   
		this.region = region;  
		this.telcoId = telcoId;		
        this.cloudletId = cloudletId;
        this.metric_type = metric_type;
        this.processingDate = processingDate;
        this.metric_value = metric_value;
        this.day_hour_min = day_hour_min;
        this.min = min;
        this.flag = flag;
    }
 
	/**
	 * @return userId
	 */
	public String getUserId() {

		return userId;
	}
	
    /**
     * @return the appName
     */
    public String getAppName() {
    	return appName;
    }
    
	/**
	 * @return region
	 */
	public String getRegion() {

		return region;
	}
	
	/**
	 * @return telcoId
	 */
	public String getTelcoId() {

		return telcoId;
	}

	
	/**
	 * @return cloudletId
	 */
	public String getCloudletId() {
		return cloudletId;
	}

	/**
	 * @return metric_type
	 */
	public String getMetric_type() {

		return metric_type;
	}
	
	/**
	 * @return processingDate
	 */
	public Date getProcessingDate() {
		return processingDate;
	}
	
	/**
	 * @return metric_value
	 */
	public String getMetric_value() {

		return metric_value;
	}

	/**
	 * @return day_hour_min
	 */
	public String getDay_hour_min() {

		return day_hour_min;
	}
	
	/**
	 * @return min
	 */
	public int getMin() {

		return min;
	}
	
	/**
	 * @return flag
	 */
	public int getflag() {

		return flag;
	}
	
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {

		this.userId = userId;
	}


	/**
	* @param appName the appName to set
	*/
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {

		this.region = region;
	}

	/**
	 * @param telcoId the telcoId to set
	 */
	public void setTelcoId(String telcoId) {

		this.telcoId = telcoId;
	}
	
	/**
	 * @param cloudletId the cloudletId to set
	 */
	public void  setCloudletId(String cloudletId) {
		this.cloudletId = cloudletId;
	}

	/**
	* @param microServiceIds the microServiceIds to set
	*/
	public void setMetric_type(String metric_type) {
		this.metric_type = metric_type;
	}


	/**
	 * @param Date the Date to set
	 */
	public void setProcessingDate(java.util.Date date) {
		this.processingDate = date;
	}

	/**
	 * @param metric_value the metric_value to set
	 */
	public void  setMetric_value(String metric_value) {

		this.metric_value = metric_value;
	}

	/**
	 * @param min the day_hour_min to set
	 */
	public void  setDay_hour_min(String day_hour_min) {

		this.day_hour_min = day_hour_min;
	}

	/**
	 * @param min the min to set
	 */
	public void  setMin(int min) {

		this.min = min;
	}
	
	/**
	 * @param flag the flag to set
	 */
	public void  setflag(int flag) {

		this.flag = flag;
	}
	

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Application [appName=" + appName + ", cloudletId=" + cloudletId
        		+ ", region=" + region + ", telcoId=" + telcoId + ", userId=" + userId
             	+ ", metric_type=" + metric_type + ",	processingDate=" + processingDate + ","
             	+ " metric_value=" + metric_value + ",	flag=" + flag + ", min= " + min 
             	+ ",day_hour_min= " + day_hour_min +"]";
    }
}

