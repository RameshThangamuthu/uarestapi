package com.usageaccounting.entity;

import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;
import org.springframework.stereotype.Component;

@Table("Application_Usage_RollUp1440")
@Component
public class Application_Usage_RollUp1440 {

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
	
	@Column("metric_Type")
	private String metric_Type;
	
	@Column("processingDate")
	private Date processingDate;
	
	@Column("metric_Value")
	private String metric_Value;
	
	@Column("day")
	private int day;
	
	@Column("flag")
	private int flag;
	
	
	/**
	 * Default Constructor
	 */
	public Application_Usage_RollUp1440 () {
		super();
	}
	
	
	/**
	 * Parameterised Constructor
	 * @param userId
	 * @param appName
	 * @param region
	 * @param telcoId
	 * @param cloudletId
	 * @param metric_Type
	 * @param processingDate
	 * @param metric_Value
	 * @param day
	 * @param flag
	 */
	public Application_Usage_RollUp1440 (String userId, String appName, String region, String telcoId, String cloudletId, String metric_Type, 
			Date processingDate, String metric_Value, int day, int flag) {
		super();
		this.userId = userId;
		this.appName = appName;
		this.region = region;
		this.telcoId = telcoId;
		this.cloudletId = cloudletId;
		this.metric_Type = metric_Type;
		this.processingDate = processingDate;
		this.metric_Value = metric_Value;
		this.day = day;
		this.flag = flag;
	}
	
	
	/**
	 * 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * 
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 
	 * @return appName
	 */
	public String getAppName() {
		return appName;
	}
	
	/**
	 * 
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * 
	 * @return region
	 */
	public String getRegion() {
		return region;
	}
	
	/**
	 * 
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	
	/**
	 * 
	 * @return telcoId
	 */
	public String getTelcoId() {
		return telcoId;
	}
	
	/**
	 * 
	 * @param telcoId the telcoId to set
	 */
	public void setTelcoId(String telcoId) {
		this.telcoId = telcoId;
	}
	
	/**
	 * 
	 * @return cloudletId
	 */
	public String getCloudletId() {
		return cloudletId;
	}
	
	/**
	 * 
	 * @param cloudletId the cloudletId to set
	 */
	public void setCloudletId(String cloudletId) {
		this.cloudletId = cloudletId;
	}
	
	/**
	 * 
	 * @return metric_Type
	 */
	public String getMetric_Type() {
		return metric_Type;
	}
	
	/**
	 * 
	 * @param metric_Type the metric_Type to set
	 */
	public void setMetric_Type(String metric_Type) {
		this.metric_Type = metric_Type;
	}
	
	/**
	 * 
	 * @return processingDate
	 */
	public Date getProcessingDate() {
		return processingDate;
	}
	
	/**
	 * 
	 * @param processingDate the processingDate to set
	 */
	public void setProcessingDate(Date processingDate) {
		this.processingDate = processingDate;
	}
	
	/**
	 * 
	 * @return metric_Value
	 */
	public String getMetric_Value() {
		return metric_Value;
	}
	
	/**
	 * 
	 * @param metric_Value the metric_Value to set
	 */
	public void setMetric_Value(String metric_Value) {
		this.metric_Value = metric_Value;
	}
	
	/**
	 * 
	 * @return day
	 */
	public int getDay() {
		return day;
	}
	
	
	/**
	 * 
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	
	/**
	 * 
	 * @return flag
	 */
	public int getFlag() {
		return flag;
	}
	
	
	/**
	 * 
	 * @param flag the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Application_Usage_RollUp1440 [userId=" + userId + ", appName=" + appName + ", region=" + region + ", telcoId=" + telcoId
        		+ ", cloudletId=" + cloudletId + ", metric_Type=" + metric_Type + ", processingDate=" + processingDate
        		+ ", metric_Value=" + metric_Value + ", day=" + day + ", flag=" + flag + "]";
    }
}
