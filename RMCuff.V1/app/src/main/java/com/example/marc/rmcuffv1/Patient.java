package com.example.marc.rmcuffv1;

import android.telephony.SmsManager;
import java.util.Date ;



/**
 * Created by Davidb on 9/11/15.
 */
public class Patient
{
    private String name ;
    private Date birthDate ;
    private Caregiver caregivers [] ;           /* List of the user's caregivers */
    private Reading readings [] ;               /* List of last N readings (N to be determined later) */
    private Schedule scheduled ;                /* The next scheduled reading */
    private Device device ;                     /* The actual bluetooth cuff */

    public Patient(String name)
    {
        this.name = name ;
        this.birthDate = null ;
        this.caregivers = null ;
        this.readings = null ;
        this.scheduled = null ;
        this.device = null ;
    }
    public Patient(String name, Date dob, Caregiver [] caregivers, Reading [] readings, Schedule scheduled, Device device)
    {
        this.name = name ;
        this.birthDate = dob ;
        this.caregivers = caregivers ;
        this.readings = readings ;
        this.scheduled = scheduled ;
        this.device = device ;
    }

    public Reading beginReading()
    {
        device.connectDevice() ;
        Reading latestRead = device.runDevice() ;
        readings[0] = latestRead ; // add latest reading to array, it was empty for now so place it at index 0

        return latestRead ;
    }

    public void alertCaregivers()
    {
        String message = "Patient Name: " + getName() + "\n" ;
        for (Reading r : readings)
        {
            if(r != null) {
                message += r.toString();
            }
        }

        SmsManager msgManager = SmsManager.getDefault() ;

        for (Caregiver cg : caregivers) {
            if (cg.getNotify() == true) {
                msgManager.sendTextMessage(cg.getPhoneNum(), null, message, null, null);
            }
        }
    }

    // Get Methods

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Caregiver[] getCaregivers() {
        return caregivers;
    }

    public Reading[] getReadings() {
        return readings;
    }

    public Schedule getScheduled() {
        return scheduled;
    }

    public Device getDevice() {
        return device;
    }

    // Set methods

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setCaregivers(Caregiver[] caregivers) {
        this.caregivers = caregivers;
    }

    public void setReadings(Reading[] readings) {
        this.readings = readings;
    }

    public void setScheduled(Schedule scheduled) {
        this.scheduled = scheduled;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
