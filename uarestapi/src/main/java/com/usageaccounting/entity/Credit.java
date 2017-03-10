package com.usageaccounting.entity;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * User profile entity class.
 * 
 * @author Aricent
 * @version 1.0
 */

@Table("user_profile")
public class Credit {

    @PrimaryKey("userID")
    private String userID;    
    
    @Column("credits")
    private int credits;     
  
    
    /**
     * Default Constructor
     */
    public Credit() {
        super();        
    }

    /**
     * Parameterized Constructor
     * @param id
     * @param credits
     */
    public Credit(String userID, int credits) {
        super();
        this.userID = userID;
        this.credits = credits;
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
    public int getCredits () {
        return credits;
    }

    /**
     * @param name the name to set
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * @return the credits
     */
 

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Credit [userID=" + userID + ", credits=" + credits + "]";
    }   
}
