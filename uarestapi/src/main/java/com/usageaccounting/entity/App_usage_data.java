package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class App_usage_data implements Serializable {
	
	private String appName;
		
    private List<Data_points> data_points = new LinkedList<Data_points>();
    
    /**
     * Default Constructor
     */
    public App_usage_data() {
        super();        
    }

    /**
     * Parameterized Constructor
     */
    public App_usage_data(String appName, List<Data_points> data_points) {
        super();    
        this.appName = appName;
        this.data_points = data_points;
   }

    /**
     * @return the appName
     */
    public String getAppName() {
    	return appName;
    }
   
	/**
	 * @return data_points
	 */
	public List<Data_points> getData_points() {
		return data_points;
	}
	
	/**
	 * @param microServiceid the microServiceid to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @param data_points the data_points to set
	 */
	public void setData_points(List<Data_points> data_points) {
		this.data_points = data_points;
	}

	   /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "App_usage_data [appName=" + appName + "]";
    }

}
