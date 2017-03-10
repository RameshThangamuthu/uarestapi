package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Ms_data implements Serializable {
	
	private String ms_name;
		
    private int cpu_usage;
    
    private int mem_usage;
    
    private int uptime;
    
    private int image_reg_usage;
    
    private int vol_usage;
    
    private int ha_usage;
    
    private int sec_usage;
    
    private int storage_space;
    
    private List<Events> events = new LinkedList<Events>();
    
    private String subscribed;
    
    private String userId;
    
    private double latency;
    
    
    
    
    /**
     * Default Constructor
     */
    public Ms_data() {
        super();        
    }

    /**
     * Parameterized Constructor
     */
    public Ms_data(String ms_name, int cpu_usage, int mem_usage,
    		int uptime, int image_reg_usage,int vol_usage, int ha_usage,int sec_usage, int storage_space,
    		List<Events> eventUsage,String subscribed,String userId, double latency) {
        super();    
        this.ms_name = ms_name;
        this.cpu_usage = cpu_usage;
        this.mem_usage = mem_usage;
        this.uptime = uptime;
        this.image_reg_usage = image_reg_usage;
        this.vol_usage = vol_usage;
        this.ha_usage = ha_usage;
        this.sec_usage = sec_usage;
        this.storage_space = storage_space;
        this.events = eventUsage;
        this.subscribed = subscribed;
        this.userId = userId;
        this.latency = latency;
   }

    /**
     * @return the ms_name
     */
    public String getMs_name() {
    	return ms_name;
    }
    
    
    /**
     * @return the cpu_usage
     */
	public int getCpu_usage() {
		return cpu_usage;
	}

    /**
     * @return the mem_usage
     */
	public int getMem_usage() {
		return mem_usage;
	}
	
    /**
     * @return the uptime
     */
	public int getUptime() {
		return uptime;
	}
	
    /**
     * @return the image_reg_usage
     */
	public int getImage_reg_usage() {
		return image_reg_usage;
	}
	
    /**
     * @return the vol_usage
     */
	public int getVol_usage() {
		return vol_usage;
	}
	
    /**
     * @return the ha_usage
     */
	public int getHa_usage() {
		return ha_usage;
	}
	
    /**
     * @return the sec_usage
     */
	public int getSec_usage() {
		return sec_usage;
	}
	
	/**
	 * @return storage_space
	 */
	public int getStorage_space() {
		return storage_space;
	}
	
	
	/**
	 * @return cloudletId
	 */
	public List<Events> getEventUsage() {
		return events;
	}
	
	/**
	 * @return subscribed
	 */
	public String getSubscribed() {

		return subscribed;
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
	 * @return userId
	 */
	public double getLatency() {
		return latency;
	}
	
	/**
	 * @param microServiceid the microServiceid to set
	 */
	public void setMs_name(String ms_name) {
		this.ms_name = ms_name;
	}


	/**
	 * @param cpu_usage the cpu_usage to set
	 */
	public void setCpu_usage(int cpu_usage) {
		this.cpu_usage = cpu_usage;
	}


	/**
	 * @param mem_usage the mem_usage to set
	 */
	public void setMem_usage(int mem_usage) {
		this.mem_usage = mem_usage;
	}
	
	/**
	 * @param uptime the uptime to set
	 */
	public void setUptime(int uptime) {
		this.uptime = uptime;
	}
	
	
	/**
	 * @param image_reg_usage the image_reg_usage to set
	 */
	public void setImage_reg_usage(int image_reg_usage) {
		this.image_reg_usage = image_reg_usage;
	}
	
	
	/**
	 * @param vol_usage the vol_usage to set
	 */
	public void setVol_usage(int vol_usage) {
		this.vol_usage = vol_usage;
	}
	
	
	/**
	 * @param ha_usage the ha_usage to set
	 */
	public void setHa_usage(int ha_usage) {
		this.ha_usage = ha_usage;
	}
	
	/**
	 * @param sec_usage the sec_usage to set
	 */
	public void setSec_usage(int sec_usage) {
		this.sec_usage = sec_usage;
	}
	
	
	/**
	 * @param storage_space the storage_space to set
	 */
	public void setStorage_space(int storage_space) {
		this.storage_space = storage_space;
	}


	/**
	 * @param eventUsage the eventUsage to set
	 */
	public void setEvents(List<Events> eventUsage) {
		this.events = eventUsage;
	}

	
	/**
	 * @param subscribed the subscribed to set
	 */
	public void setSubscribed(String subscribed) {

		this.subscribed = subscribed;
	}

	
	/**
	 * 
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	/**
	 * @param latency the latency to set
	 */
	public void setLatency(double latency) {
		this.latency = latency;
	}
	
	   /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Ms_data [ms_name=" + ms_name + ", cpu_usage=" + cpu_usage
        		+ ", mem_usage=" + mem_usage + ", uptime=" + uptime
        		+ ", image_reg_usage=" + image_reg_usage + ", vol_usage=" + vol_usage + ", ha_usage=" + ha_usage
        		+ ", sec_usage=" + sec_usage + ",	storage_space=" + storage_space
        		+ ", subscribed=" + subscribed + ",latency=" + latency + ",userId=" + userId + "]";
    }

}
