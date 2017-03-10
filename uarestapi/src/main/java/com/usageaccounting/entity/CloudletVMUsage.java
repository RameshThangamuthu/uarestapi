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
public class CloudletVMUsage implements Serializable {
	
	private String id;    
	    
    private int period;
    
    private String cloudlet_id;
 
    private String telco_id;
    
 	private List<Vm_data> vm_data = new LinkedList<Vm_data>();
    
   private String timestamp;
    
    /**
     * Default Constructor
     */
    public CloudletVMUsage() {
        super();        
    }
    
   
    /**
     * Parameterized Constructor
     */
    public CloudletVMUsage(String id, int period,String cloudlet_id, String telco_id,
    		           List<Vm_data> vm_data,
    		           String timestamp) {
        super();
        this.id = id;    
		this.period = period;
		this.cloudlet_id = cloudlet_id;
		this.telco_id = telco_id;
		this.timestamp = timestamp;
		this.vm_data =vm_data;
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
	 * @return timestamp
	 */
	public String getTimestamp() {
		return timestamp;
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
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	public List<Vm_data> getVm_data() {
		return vm_data;
	}

	public void setVMdata(List<Vm_data> vm_data) {
		this.vm_data = vm_data;
	}
	

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CloudletUsage [id=" + id + ", period=" + period
        		+ ", cloudlet_id=" + cloudlet_id + ", telco_id=" + telco_id
        		+ ",	timestamp=" + timestamp + "]";
    }

	
	
}