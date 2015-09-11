package com.example.marc.rmcuffv1;

/**
 * Created by Davidb on 9/11/15.
 */
public class Caregiver
{
    private String name ;
    private String phoneNum ;
    private boolean notify ;

    public Caregiver(String name, String phoneNum, boolean notify)
    {
        this.name = name ;
        this.phoneNum = phoneNum ;
        this.notify = notify ;
    }

    // Get methods

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public boolean getNotify() {
        return notify;
    }

    // Set methods

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
