package marc.example.com.rmcuffv1_patient;

import android.telephony.SmsManager;

import java.util.Date;

/**
 * Created by Davidb on 10/8/15.
 */
public class Patient
{
    private String name ;
    private Date birthDate ;
    private Reading readings [] ;               /* List of last N readings (N to be determined later) */
    private Schedule schedule[] ;                /* The next scheduled reading */
    private Device device ;                     /* The actual bluetooth cuff */

    public Patient(String name)
    {
        this.name = name ;
        this.birthDate = null ;
        this.readings = null ;
        this.schedule = null ;
        this.device = null ;
    }
    public Patient(String name, Date dob, Reading [] readings, Schedule [] schedule, Device device)
    {
        this.name = name ;
        this.birthDate = dob ;
        this.readings = readings ;
        this.schedule = schedule ;
        this.device = device ;
    }

    public Reading beginReading()
    {
        device.connectDevice() ;
        Reading latestRead = device.runDevice() ;
        readings[0] = latestRead ; // add latest reading to array, it was empty for now so place it at index 0

        return latestRead ;
    }



    // Get Methods

    public String getName() { return name; }

    public Date getBirthDate() {
        return birthDate;
    }



    public Reading[] getReadings() {
        return readings;
    }

    public Schedule[] getSchedule() {
        return schedule;
    }

    public Device getDevice() {
        return device;
    }

    // Set methods

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public void setReadings(Reading[] readings) {
        this.readings = readings;
    }

    public void setSchedule(Schedule [] schedule) {
        this.schedule = schedule;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
