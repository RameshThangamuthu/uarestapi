package com.usageaccounting.entity;

public class UpdateDeleteCatalog {

	private String userID;
	private String microServiceName;
	private int creditsPerAPICall;
	private int creditsForDownload;
	//private String update;
	
	
	/**
	 * Default Constructor
	 */
	public UpdateDeleteCatalog() {
		super();
	}
	
	/**
	 * Parameterised Constructor
	 * @param userID
	 * @param microServiceId
	 * @param creditsPerAPICall
	 * @param creditsForDownload
	 * @param update
	 */
	public UpdateDeleteCatalog(String userID, String microServiceName, int creditsPerAPICall, int creditsForDownload, String update) {
		super();
		this.userID = userID;
		this.microServiceName = microServiceName;
		this.creditsPerAPICall = creditsPerAPICall;
		this.creditsForDownload = creditsForDownload;
		//this.update = update;
	}
	
	/**
	 * 
	 * @return userID
	 */
	public String getUserID() {
		return userID;
	}
	
	
	/**
	 * 
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
	/**
	 * 
	 * @return microServiceName
	 */
	public String getMicroServiceName() {
		return microServiceName;
	}
	
	
	/**
	 * 
	 * @param microServiceId the microServiceId to set
	 */
	public void setMicroServiceName(String microServiceName) {
		this.microServiceName = microServiceName;
	}
	
	
	/**
	 * 
	 * @return creditsPerAPICall
	 */
	public int getCreditsPerAPICall() {
		return creditsPerAPICall;
	}
	
	
	/**
	 * 
	 * @param creditsPerAPICall the creditsPerAPICall to set
	 */
	public void setCreditsPerAPICall(int creditsPerAPICall) {
		this.creditsPerAPICall = creditsPerAPICall;
	}
	
	
	/**
	 * 
	 * @return creditsForDownload
	 */
	public int getCreditsForDownload() {
		return creditsForDownload;
	}
	
	
	/**
	 * 
	 * @param creditsForDownload the creditsForDownload to set
	 */
	public void setCreditsForDownload(int creditsForDownload) {
		this.creditsForDownload = creditsForDownload;
	}
	
	
	/**
	 * 
	 * @return update
	 */
/*	public String getUpdate() {
		return update;
	}*/
	
	
	/**
	 * 
	 * @param update the update to set
	 */
/*	public void setUpdate(String update) {
		this.update = update;
	}
	*/
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UpdateDeleteCatalog [userID=" + userID + ", microServiceName=" + microServiceName + ", creditsPerAPICall=" + creditsPerAPICall
        		+ ", creditsForDownload=" + creditsForDownload + "]";
    }
}
