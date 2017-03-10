package com.usageaccounting.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("cloudlet_usage")
public class Cloudlet_usage implements Serializable {
	
	@PrimaryKey("telcoid")
	private String telcoid;

	@Column("cloudletid")	
	private String cloudletid;    
    
	@Column("vmname")
	private String vmname;
	  
    @Column("metric_type")
    private String metric_type;
    
    @Column("processingDate")
    private Date processingDate;
 
    @Column("metric_value")
    private Double metric_value;
  
    @Column("hour")
    private int hour;
    
    @Column("flag")
    private int flag;    
    
    /**
     * Default Constructor
     */
    public Cloudlet_usage() {
        super();        
    }

    /**
     * Parameterized Constructor
     */
    public Cloudlet_usage(String telcoId,String cloudletId,String vmname,
    					String metric_type,Date  processingDate, Double metric_value, int hour,int flag) {
        super();
		this.telcoid = telcoId;		
        this.cloudletid = cloudletId;
        this.metric_type = metric_type;
        this.processingDate = processingDate;
        this.metric_value = metric_value;
        this.hour = hour;
        this.flag = flag;
		this.vmname = vmname;
    }
 
	/**
	 * @return telcoId
	 */
	public String getTelcoid() {

		return telcoid;
	}

	
	/**
	 * @return cloudletId
	 */
	public String getCloudletid() {
		return cloudletid;
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
	public Double getMetric_value() {

		return metric_value;
	}

	/**
	 * @return hour
	 */
	public int getHour() {

		return hour;
	}
	
	/**
	 * @return flag
	 */
	public int getflag() {

		return flag;
	}
	
	/**
	 * @param telcoId the telcoId to set
	 */
	public void setTelcoId(String telcoid) {

		this.telcoid = telcoid;
	}
	
	/**
	 * @param cloudletId the cloudletId to set
	 */
	public void  setCloudletId(String cloudletid) {
		this.cloudletid = cloudletid;
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
	public void  setMetric_value(Double metric_value) {

		this.metric_value = metric_value;
	}

	/**
	 * @param min the min to set
	 */
	public void  setHour(int hour) {

		this.hour = hour;
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
        return "Cloudlet  [cloudletId=" + cloudletid
        		+ ", telcoId=" + telcoid + ", metric_type=" + metric_type + ",	processingDate=" + processingDate + ","
             	+ " metric_value=" + metric_value + ",	flag=" + flag + ", Hour " + hour +"]";
    }
}

