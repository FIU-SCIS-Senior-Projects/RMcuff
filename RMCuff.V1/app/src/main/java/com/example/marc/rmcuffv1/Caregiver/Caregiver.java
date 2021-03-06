package com.example.marc.rmcuffv1.Caregiver;
import java.io.Serializable;
/**
 * Created by Davidb on 9/11/15.
 */
public class Caregiver implements Serializable
{
    private String firstName = "";
    private String lastName = "";
    private String phoneNum = "";
    private String emailAddress = "";
    private int userID = -1;
    private int contactPreference = 1;
    private boolean notify = false ;

    public Caregiver()
    {

    }
    public Caregiver(String firstName, String lastName, String phoneNum, String emailAddress)
    {
        this.firstName = firstName ;
        this.lastName = lastName ;
        this.phoneNum = phoneNum ;
        this.emailAddress = emailAddress ;
    }

    // Get methods
    public String getFirstName() { return firstName; }

    // Set methods
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) { this.userID = userID; }

    public int getContactPreference() {
        return contactPreference;
    }

    public void setContactPreference(int contactPreference) { this.contactPreference = contactPreference; }

    public boolean getNotify() {
        return notify;
    }

    public void setNotify(boolean notify) { this.notify = notify; }

    @Override
    public String toString() {
        return "[CAREGIVER]: " + firstName + " : " + lastName + " : " + phoneNum + " : " + emailAddress + " : " + userID
                + " : " + contactPreference + " : " + notify;
    }
}
