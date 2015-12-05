package com.example.marc.rmcuffv1.Settings.Patient;

import com.example.marc.rmcuffv1.Settings.Reading.ReadingList;
import com.example.marc.rmcuffv1.Settings.Schedule.ScheduleList;


/**
 * Created by Davidb on 9/11/15.
 */
public class MyPatient
{
    private String patientID ;
    private String name ;
    private ReadingList readings ;               /* List of last N readings (N to be determined later) */
    private ScheduleList scheduled ;                /* The next scheduled reading */


    public MyPatient(String name)
    {
        this.name = name ;
        this.patientID = null ;
        this.readings = null ;
        this.scheduled = null ;
    }
    public MyPatient(String patientID, String name, ReadingList readings, ScheduleList scheduled)
    {
        this.patientID = patientID ;
        this.name = name ;
        this.readings = readings ;
        this.scheduled = scheduled ;
    }

    // Get Methods
    public String getPatientID() {return patientID;}

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // Set methods

    public ReadingList getReadings() {
        return readings;
    }

    public void setReadings(ReadingList readings) {
        this.readings = readings;
    }

    public ScheduleList getScheduled() {
        return scheduled;
    }

    public void setScheduled(ScheduleList scheduled) {this.scheduled = scheduled;}

}
