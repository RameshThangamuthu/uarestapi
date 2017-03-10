package com.usageaccounting.entity;

import java.io.Serializable;

import org.springframework.data.cassandra.mapping.Column;

public class Events implements Serializable {

    private String event_name;    
    
    private String event_type;
    
    private int api_hits;
 
    private int notify_in;
    
    private int notify_out;
    
    private int latency;
    
    private int sess_duration;
    
  
    /**
     * Default Constructor
     */
    public Events() {
        super();        
    }

    /**
     * Parameterized Constructor
     */
    public Events(String event_name, String event_type, int api_hits, int notify_in, int notify_out, 
    				  int latency,int sess_duration) {
        super();
        this.event_name = event_name;    
		this.event_type = event_type;
		this.api_hits = api_hits;
		this.latency = latency;
		this.notify_out = notify_out;
		this.latency = latency;
		this.sess_duration = sess_duration;
    }
    
    /**
     * @return the event_name
     */
    public String getEvent_name() {
    	return event_name;
    }
    
    
    /**
     * @return the event_type
     */
	public String getEvent_type() {
		return event_type;
	}
	
	
	/**
	 * @return api_hits
	 */
	public int getApi_hits() {
		return api_hits;
	}
	
	
	/**
	 * @return notify_in
	 */
	public int getNotify_in() {
		return notify_in;
	}
	
	
	/**
	 * @return notify_out
	 */
	public int getNotify_out() {

		return notify_out;
	}
	
	
	/**
	 * @return latency
	 */
	public int getLatency() {

		return latency;
	}
	
	
	/**
	 * @return sess_duration
	 */
	public int getSess_duration() {

		return sess_duration;
	}
	
	/**
	* @param Event_name the Event_name to set
	*/
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}


	/**
	* @param event_type the event_type to set
	*/
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}


	/**
	 * @param api_hits the api_hits to set
	 */
	public void setApi_hits(int api_hits) {
		this.api_hits = api_hits;
	}


	/**
	 * @param notify_in the notify_in to set
	 */
	public void  setNotify_in(int notify_in) {
		this.notify_in = notify_in;
	}


	/**
	 * @param notify_out the notify_out to set
	 */
	public void setNotify_out(int notify_out) {

		this.notify_out = notify_out;
	}


	/**
	 * @param duration the duration to set
	 */
	public void  setLatency(int latency) {

		this.latency = latency;
	}


	/**
	 * @param sess_duration the sess_duration to set
	 */
	public void  setSess_duration(int sess_duration) {

		this.sess_duration = sess_duration;
	}



    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Events [event_name=" + event_name + ", event_type=" + event_type
        		+ ", api_hits=" + api_hits + ", notify_in=" + notify_in
        		+ ", notify_out=" + notify_out + ", latency=" + latency + ", sess_duration=" 
        		+ sess_duration + "]";
    }
}

