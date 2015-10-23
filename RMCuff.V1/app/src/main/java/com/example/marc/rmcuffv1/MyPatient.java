package com.example.marc.rmcuffv1;

import android.telephony.SmsManager;
import java.util.Date ;



/**
 * Created by Davidb on 9/11/15.
 */
public class MyPatient
{
    private String patientID ;
    private String name ;
    private Date birthDate ;
    private ReadingList readings ;               /* List of last N readings (N to be determined later) */
    private Schedule scheduled ;                /* The next scheduled reading */


    public MyPatient(String name)
    {
        this.name = name ;
        this.patientID = null ;
        this.birthDate = null ;
        this.readings = null ;
        this.scheduled = null ;
    }
    public MyPatient(String patientID, String name, Date dob, ReadingList readings, Schedule scheduled)
    {
        this.patientID = patientID ;
        this.name = name ;
        this.birthDate = dob ;
        this.readings = readings ;
        this.scheduled = scheduled ;
    }

    // Get Methods
    public String getPatientID() {return patientID;}

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public ReadingList getReadings() {return readings;}

    public Schedule getScheduled() {
        return scheduled;
    }


    // Set methods

    public void setPatientID(String patientID) {this.patientID = patientID;}

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setReadings(ReadingList readings) {
        this.readings = readings;
    }

    public void setScheduled(Schedule scheduled) {this.scheduled = scheduled;}

}
