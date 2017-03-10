package com.usageaccounting.entity;

public class Content {
	
	private int code;
	
	private String message;
	
	private String status;
	
	/**
	 * Default Constructor
	 */
	public Content() {
		super();
	}
	
	/**
	 * Parameterised Constrctor
	 * @param message
	 */
	public Content (int code,String message,String status) {
		super();
		this.code = code;
		this.message = message;
		this.status = status;
	}
	
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * @param code the code to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
    	return "Content : { message=" + message + "code=" + code+ "status=" + status + "}";
    }
}
