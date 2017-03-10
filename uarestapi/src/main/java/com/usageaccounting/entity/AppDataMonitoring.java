package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
 
/**
 * AppDataMonitoring entity class.
 * 
 * @author Aricent
 * @version 1.0
 */
public class AppDataMonitoring implements Serializable {
	
	private String userID;    

	private List<App_usage_data> app_usage_data = new LinkedList<App_usage_data>();
    
    
    /**
     * Default Constructor
     */
    public AppDataMonitoring() {
        super();        
    }
    
   
    /**
     * Parameterized Constructor
     */
    public AppDataMonitoring(String userID,List<App_usage_data> app_usage_data) {
        super();
        this.userID = userID;    
		this.app_usage_data = app_usage_data;
    }
   

    /**
     * @return the userID
     */
    public String getUserID() {
    	return userID;
    }
 
 	
	/**
	* @param userID the userID to set
	*/
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public List<App_usage_data> getApp_usage_data() {
		return app_usage_data;
	}
	
	public void setApp_usage_data(List<App_usage_data> app_usage_data) {
		this.app_usage_data = app_usage_data;
	}
	
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AppDataMonitoring [userID=" + userID + "]";
    }

	
	
}