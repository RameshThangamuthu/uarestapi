package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
 
/**
 * AppDataMonitoringInput entity class.
 * 
 * @author Aricent
 * @version 1.0
 */
public class AppDataMonitoringInput implements Serializable {
	
	private String userid; 
	
	private String appName1;  
	
	private String appName2;  
	
	private String appName3;  	
	
	private String timeCycle;
	
	private String metricType;
   
    /**
     * Default Constructor
     */
    public AppDataMonitoringInput() {
        super();        
    }
    
   
    /**
     * Parameterized Constructor
     */
    public AppDataMonitoringInput(String userid,String appName1,String appName2,String appName3,
								  String timeCycle,String metricType) {
        super();
        this.userid = userid;    
		this.appName1 = appName1;
        this.appName2 = appName2;    
		this.appName3 = appName3;
        this.timeCycle = timeCycle;    
		this.metricType = metricType;
    }
   

    /**
     * @return the userID
     */
    public String getUserID() {
    	return userid;
    }
 
    /**
     * @return the AppName1
     */
    public String getAppName1() {
    	return appName1;
    }
 
    /**
     * @return the AppName2
     */
    public String getAppName2() {
    	return appName2;
    }
 
    /**
     * @return the appName3
     */
    public String getAppName3() {
    	return appName3;
    }
 
    /**
     * @return the timeCycle
     */
    public String getTimeCycle() {
    	return timeCycle;
    }
 
    /**
     * @return the metricType
     */
    public String getMetricType() {
    	return metricType;
    }
 
	/**
	* @param userID the userID to set
	*/
	public void setUserID(String userid) {
		this.userid = userid;
	}

		/**
	* @param appName1 the appName1 to set
	*/
	public void setAppName1(String appName1) {
		this.appName1 = appName1;
	}

		/**
	* @param appName2 the appName2 to set
	*/
	public void setAppName2(String appName2) {
		this.appName2 = appName2;
	}
	
		/**
	* @param appName3 the appName3 to set
	*/
	public void setAppName3(String appName3) {
		this.appName3 = appName3;
	}
		

		/**
	* @param timeCycle the timeCycle to set
	*/
	public void setTimeCycle(String timeCycle) {
		this.timeCycle = timeCycle;
	}

		/**
	* @param metricType the metricType to set
	*/
	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	    return "AppDataMonitoringInput [userID=" + userid + ", appName1=" + appName1
        		+ ", appName2=" + appName2 + ", appName3=" + appName3
        		+ ", timeCycle=" + timeCycle + ", metricType=" + metricType + "]";
    }

	
	
}