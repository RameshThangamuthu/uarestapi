package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Vm_data implements Serializable {
	
	private String vm_name;
	
	private String vm_id;
		
    private int cpu_usage;
    
    private int mem_usage;
    
    private int uptime;
   
    private int vol_usage;
    
    private int ha_usage;
    
    private int sec_usage;
    
	private String cpu_type;
	
	private int nw_bandwidth;
	  
	private String storage_type;
	
    private int storage_space;
    
    /**
     * Default Constructor
     */
    public Vm_data() {
        super();        
    }

    /**
     * Parameterized Constructor
     */
    public Vm_data(String vm_name, String vm_id, int cpu_usage, int mem_usage,
    		int uptime, int vol_usage, int ha_usage,int sec_usage, String cpu_type,
			int nw_bandwidth, String storage_type, int storage_space) {
        super();    
        this.vm_name = vm_name;
		this.vm_id = vm_id;
        this.cpu_usage = cpu_usage;
        this.mem_usage = mem_usage;
        this.uptime = uptime;
        this.vol_usage = vol_usage;
        this.ha_usage = ha_usage;
        this.sec_usage = sec_usage;
        this.cpu_type = cpu_type;
        this.nw_bandwidth = nw_bandwidth;
        this.storage_type = storage_type;
        this.storage_space = storage_space;
   }

    /**
     * @return the vm_name
     */
    public String getVm_name() {
    	return vm_name;
    }
  
    /**
     * @return the vm_id
     */
    public String getVm_id() {
    	return vm_id;
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
	 * @return cpu_type
	 */
	public String getCpu_type() {
		return cpu_type;
	}

	/**
	 * @return nw_bandwidth
	 */
	public int getNw_bandwidth() {
		return nw_bandwidth;
	}
	
	public String getStorage_type() {
		return storage_type;
	}

	/**
	 * @return storage_space
	 */
	public int getStorage_space() {
		return storage_space;
	}

	
	/**
	 * @param vm_name the vm_name to set
	 */
	public void setVm_name(String vm_name) {
		this.vm_name = vm_name;
	}

	/**
	 * @param vm_name the vm_name to set
	 */
	public void setVm_id(String vm_id) {
		this.vm_id = vm_id;
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
	 * @param cpu_type the cpu_type to set
	 */
	public void setCpu_type(String cpu_type) {
		this.cpu_type = cpu_type;
	}

	/**
	 * @param nw_bandwidth the nw_bandwidth to set
	 */
	public void setNw_bandwidth(int nw_bandwidth) {
		this.nw_bandwidth = nw_bandwidth;
	}
	
	/**
	 * @param storage_type the storage_type to set
	 */
	public void setStorage_type(String storage_type) {
		this.storage_type = storage_type;
	}
	
	/**
	 * @param storage_space the storage_space to set
	 */
	public void setStorage_space(int storage_space) {
		this.storage_space = storage_space;
	}



	   /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Vm_data [vm_name=" + vm_name + ", vm_id=" + vm_id
        		+ ", cpu_usage=" + cpu_usage
        		+ ", mem_usage=" + mem_usage + ", uptime=" + uptime
        		+ ", vol_usage=" + vol_usage + ", ha_usage=" + ha_usage
        		+ ", sec_usage=" + sec_usage + ","
        		+ ", cpu_type=" + cpu_type + ", nw_bandwidth=" + nw_bandwidth
     			+ "	storage_space=" + storage_space
        		+ ", storage_type=" + storage_type + "]";
    }

}
