package com.usageaccounting.entity;

public class CreditUpdate implements java.io.Serializable {

	   private String userID;    
	   private String appName;    
	   private String microServiceName;
	   private int creditsPerAPICall;
	   private int creditsForDownload;
	   private int onboardingCharge;
	  
	    
	    /**
	     * Default Constructor
	     */
	    public CreditUpdate() {
	        super();        
	    }

	    /**
	     * Parameterized Constructor
	     * @param id
	     * @param Credits
	     */
	    public CreditUpdate(String userID,String appName,String microServiceName,int creditsPerAPICall,int creditsForDownload, int onboardingCharge) {
	        super();
	        this.userID = userID;
	        this.appName = appName;
	        this.microServiceName = microServiceName;
	        this.creditsPerAPICall = creditsPerAPICall;
	        this.creditsForDownload = creditsForDownload;
	        this.onboardingCharge = onboardingCharge;
	     }

	    /**
	     * @return the id
	     */
	    public String getUserID() {
	        return userID;
	    }

	    /**
	     * @param id the id to set
	     */
	    public void setUserID(String userID) {
	        this.userID = userID;
	    }

	    /**
	     * @return the name
	     */
	    public String getappName() {
	        return appName;
	    }

	    /**
	     * @param name the name to set
	     */
	    public void setappName(String appName) {
	        this.appName = appName;
	    }

	    /**
	     * @return the credits
	     */
	    public String getmicroServiceName() {
	        return microServiceName;
	    }

	    /**
	     * @param name the name to set
	     */
	    public void setmicroServiceName(String microServiceName) {
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
	    
	    /*
	     * @return creditsForDownload
	     */
	    public int getCreditsForDownload() {
	    	return creditsForDownload;
	    }
	    
	    
	    /**
	     * @param creditsForDownload the creditsForDownload to set
	     */
	    public void setCreditsForDownload(int creditsForDownload) {
	    	this.creditsForDownload = creditsForDownload;
	    }
	    
	    /**
	     * 
	     * @return onboardingCharge
	     */
	    public int getOnboardingCharge() {
	    	return onboardingCharge;
	    }
	    
	    
	    /**
	     * 
	     * @param onboardingCharge the onboardingCharge to set
	     */
	    public void setOnboardingCharge(int onboardingCharge) {
	    	this.onboardingCharge = onboardingCharge;
	    }

	    /* (non-Javadoc)
	     * @see java.lang.Object#toString()
	     */
	    @Override
	    public String toString() {
	        return "Credit [UserID=" + userID + ", AppName=" + appName + ", MicroServiceName=" 
	    + microServiceName + ", creditsPerAPICall=" + creditsPerAPICall + ", creditsForDownload=" + creditsForDownload + ", onboardingCharge="
	    + onboardingCharge + "]";
	    }   
	
}
