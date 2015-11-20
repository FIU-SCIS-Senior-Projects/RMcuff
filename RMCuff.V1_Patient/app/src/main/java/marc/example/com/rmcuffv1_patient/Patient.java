package marc.example.com.rmcuffv1_patient;

import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Davidb on 10/8/15.
 */
public class Patient
{
    private String patientID ;                  /* Unique patient identifier (PhoneNum in our case) */
    private String primaryCaregiverID ;         /* Unique ID for the primary caregiver (PhoneNum in our case) */
    private String primaryCaregiverName ;
    private String name ;
    private ReadingList readings ;               /* List of last N readings (N to be determined later) */
    private ScheduleList schedule ;                /* The next scheduled reading */
    private Device device ;                     /* The actual bluetooth cuff */

    /*
    public Patient(String name)
    {
        this.patientID = null ; // this will technically not be allowed, just for quick testing purposes
        this.primaryCaregiverID = null ;
        this.name = name ;
        this.
        this.readings = null ;
        this.schedule = null ;
        this.device = null ;
    }
    */
    public Patient(String patientID, String primaryCaregiverID, String name, String pcgName,  ReadingList readings, ScheduleList schedule, Device device)
    {
        this.patientID = patientID ;
        this.primaryCaregiverID = primaryCaregiverID ;
        this.primaryCaregiverName = pcgName ;
        this.name = name ;
        this.readings = readings ;
        this.schedule = schedule ;
        this.device = device ;
    }

    public Reading beginReading()
    {
        device.connectDevice() ;
        Reading latestRead = device.runDevice() ;
        readings.add(0,latestRead) ; // add latest reading to array, it was empty for now so place it at index 0

        return latestRead ;
    }



// Get Methods

    public String getPatientID() { return patientID; }

    public String getPrimaryCaregiverID() {return primaryCaregiverID;}

    public String getPrimaryCaregiverName() {return primaryCaregiverName;}

    public String getName() { return name; }




    public ReadingList getReadings() {
        return readings;
    }

    public ScheduleList getSchedule() {
        return schedule;
    }

    public Device getDevice() {
        return device;
    }

    // Set methods

    public void setPatientID(String patientID) {this.patientID = patientID;}

    public void setPrimaryCaregiverID(String primaryCaregiverID) {this.primaryCaregiverID = primaryCaregiverID;}

    public void setPrimaryCaregiverName(String primaryCaregiverName) {this.primaryCaregiverName = primaryCaregiverName;}

    public void setName(String name) {
        this.name = name;
    }


    public void setReadings(ReadingList readings) {
        this.readings = readings;
    }

    public void setSchedule(ScheduleList schedule) {
        this.schedule = schedule;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
