package com.usageaccounting.entity;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("billing_rate_catalog")
public class BillingRateCatalog {
	
	@PrimaryKey("component")
	private String component;
	
	@Column("rate")
	private int rate;
	
	/**
	 * Default Constructor
	 */
	public BillingRateCatalog() {
		super();
	}
	
	
	/**
	 * Parameterised Constructor
	 * @param component
	 * @param rate
	 */
	public BillingRateCatalog(String component, int rate) {
		super();
		this.component = component;
		this.rate = rate;
	}
	
	
	/**
	 * @return component
	 */
	public String getComponent() {
		return component;
	}
	
	/**
	 * 
	 * @param component the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}
	
	/**
	 * 
	 * @return rate
	 */
	public int getRate() {
		return rate;
	}
	
	/**
	 * 
	 * @param rate the rate to set
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BillingRateCatalog [component=" + component + ", rate=" + rate + "]";
    }
}
