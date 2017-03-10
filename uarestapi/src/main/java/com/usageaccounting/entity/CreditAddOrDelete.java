package com.usageaccounting.entity;


import java.io.Serializable;


/**
 * Input data entity class.
 * 
 * @author Aricent 
 * @version 1.0
 */

public class CreditAddOrDelete implements Serializable {

	private String userID;    
	private int numberOfCredits;
	private String addOrDelete;
	
	/**
	 * Default Constructor
	 */
	public CreditAddOrDelete() {
		super();
	}
	
	/**
     * Parameterised Constructor
     * @param userID
     * @param credits
     * @param addOrDelete
     */
    public CreditAddOrDelete(String userID, int numberOfCredits, String addOrDelete) {
    	super();
    	this.userID = userID;
    	this.numberOfCredits = numberOfCredits;
    	this.addOrDelete = addOrDelete;
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

    /**
     * @return the numberOfCredits
     */
    public int getNumberOfCredits () {
        return numberOfCredits;
    }

    /**
     * @param numberOfCredits the numberOfCredits to set
     */
    public void setNumberOfCredits(int numberOfCredits) {
        this.numberOfCredits = numberOfCredits;
    }

    /**
     * 
     * @return addOrDelete
     */
    public String getAddOrDelete() {
    	return addOrDelete;
    }
    
    /**
     * 
     * @param addOrDelete the addOrDelete to set
     */
    public void setAddOrDelete(String addOrDelete) {
    	this.addOrDelete = addOrDelete;
    }
    
    
    /* 
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CreditAddOrDelete [userID=" + userID + ", numberOfCredits=" + numberOfCredits + ", addOrDelete=" + addOrDelete + "]";
    } 
}
