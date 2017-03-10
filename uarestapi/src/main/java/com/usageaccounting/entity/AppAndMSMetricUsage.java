package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class AppAndMSMetricUsage implements Serializable {
	
	private List<Application_usage> applications_usage = new LinkedList<Application_usage>();
	private List<Microservice_usage> microServices_usage = new LinkedList<Microservice_usage>();
	
	public List<Application_usage> getApplications() {
		return applications_usage;
	}
	
	public void setApplication(List<Application_usage> applications_usage) {
		this.applications_usage = applications_usage;
	}
	
	
	public List<Microservice_usage> getMicroservices() {
		return microServices_usage;
	}
	
	public void setMicroservice(List<Microservice_usage> microServices_usage) {
		this.microServices_usage = microServices_usage;
	}
	
    /**
     * Default Constructor
     */
    public AppAndMSMetricUsage() {
        super();        
    }
    
    /**
     * Parameterized Constructor
     */
    public AppAndMSMetricUsage(List<Application_usage> applications_usage, List<Microservice_usage> microServices) {
    	super();
    	this.applications_usage = applications_usage;
    	this.microServices_usage = microServices_usage;
    }
}
