package marc.example.com.rmcuffv1_patient.Settings.Patients;

import marc.example.com.rmcuffv1_patient.Settings.Readings.ReadingList;
import marc.example.com.rmcuffv1_patient.Settings.Schedules.ScheduleList;

public class Patient {
    private String patientID;                  /* Unique patient identifier (PhoneNum in our case) */
    private String primaryCaregiverID;         /* Unique ID for the primary caregiver (PhoneNum in our case) */
    private String primaryCaregiverName;
    private String name;
    private ReadingList readings;               /* List of last N readings (N to be determined later) */
    private ScheduleList schedule;                /* The next scheduled reading */

    public Patient(String patientID, String primaryCaregiverID, String name, String pcgName, ReadingList readings, ScheduleList schedule) {
        this.patientID = patientID;
        this.primaryCaregiverID = primaryCaregiverID;
        this.primaryCaregiverName = pcgName;
        this.name = name;
        this.readings = readings;
        this.schedule = schedule;
    }

    // Get Methods
    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPrimaryCaregiverID() {
        return primaryCaregiverID;
    }

    public void setPrimaryCaregiverID(String primaryCaregiverID) {
        this.primaryCaregiverID = primaryCaregiverID;
    }

    public String getPrimaryCaregiverName() {
        return primaryCaregiverName;
    }

    public void setPrimaryCaregiverName(String primaryCaregiverName) {
        this.primaryCaregiverName = primaryCaregiverName;
    }

    public String getName() {
        return name;
    }

    // Set methods
    public void setName(String name) {
        this.name = name;
    }

    public ReadingList getReadings() {
        return readings;
    }

    public void setReadings(ReadingList readings) {
        this.readings = readings;
    }

    public ScheduleList getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleList schedule) {
        this.schedule = schedule;
    }
}
