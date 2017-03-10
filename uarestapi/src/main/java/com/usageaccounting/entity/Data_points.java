package com.usageaccounting.entity;

import java.io.Serializable;

import org.springframework.data.cassandra.mapping.Column;

public class Data_points implements Serializable {

    private String plotDataPoint;    
    
    private String appLatency;
    
    private String sessionLength;
 
    private String cpuUsage;
    
    private String memoryUsage;
 
    /**
     * Default Constructor
     */
    public Data_points() {
        super();        
    }

    /**
     * Parameterized Constructor
     */
    public Data_points(String plotDataPoint, String appLatency, String sessionLength,String cpuUsage,String memoryUsage) {
        super();
        this.plotDataPoint = plotDataPoint;    
		this.appLatency = appLatency;
		this.sessionLength = sessionLength;
		this.cpuUsage = cpuUsage;
		this.memoryUsage = memoryUsage;
    }
    
    /**
     * @return the plotDataPoint
     */
    public String getPlotDataPoint() {
    	return plotDataPoint;
    }
    
    
    /**
     * @return the appLatency
     */
	public String getAppLatency() {
		return appLatency;
	}
	
	
	/**
	 * @return sessionLength
	 */
	public String getSessionLength() {
		return sessionLength;
	}
	
	
	/**
	 * @return cpuUsage
	 */
	public String getCpuUsage() {
		return cpuUsage;
	}
	
	
	/**
	 * @return memoryUsage
	 */
	public String getMemoryUsage() {

		return memoryUsage;
	}
	

	/**
	* @param plotDataPoint the plotDataPoint to set
	*/
	public void setPlotDataPoint(String plotDataPoint) {
		this.plotDataPoint = plotDataPoint;
	}


	/**
	* @param appLatency the appLatency to set
	*/
	public void setAppLatency(String appLatency) {
		this.appLatency = appLatency;
	}


	/**
	 * @param sessionLength the sessionLength to set
	 */
	public void setsessionLength(String sessionLength) {
		this.sessionLength = sessionLength;
	}


	/**
	 * @param cpuUsage the cpuUsage to set
	 */
	public void  setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	/**
	 * @param memoryUsage the memoryUsage to set
	 */
	public void setMemoryUsage(String memoryUsage) {

		this.memoryUsage = memoryUsage;
	}


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Data_points [plotDataPoint=" + plotDataPoint + ", appLatency=" + appLatency
        		+ ", sessionLength=" + sessionLength + ", CPUUsage=" + cpuUsage
        		+ ", memoryUsage=" + memoryUsage + "]";
    }
}

