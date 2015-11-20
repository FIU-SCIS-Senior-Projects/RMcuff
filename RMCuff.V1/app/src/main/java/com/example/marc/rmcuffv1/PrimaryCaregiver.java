package com.example.marc.rmcuffv1;

import android.telephony.SmsManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Davidb on 10/8/15.
 */
public class PrimaryCaregiver implements Serializable
{
    private String primaryCaregiverID ;         /* Unique Identifier (phoneNum in this case) */
    private String name ;
    private MyPatient patient ;                 /* A condensed version of the Patient App's Patient Class */
    private CaregiverList secondaryCaregivers ;            /* All the secondary caregivers */

    public PrimaryCaregiver(String primaryCaregiverID,String pcgName, MyPatient patient, CaregiverList secondaryCaregivers)
    {
        this.primaryCaregiverID = primaryCaregiverID ;
        this.name = pcgName ;
        this.patient = patient ;
        this.secondaryCaregivers = secondaryCaregivers ;
    }

    public boolean alertSecondaryCaregivers()
    {
        // Send out text messages
        // THIS CODE HAS ALREADY BEEN BUILT
        String message = "Patient Name: " + patient.getName() + "\n" ;
        for (int i = 0; i < patient.getReadings().size(); i++)
        {
            message += patient.getReadings().get(i).toString();
        }

        SmsManager msgManager = SmsManager.getDefault() ;

        for (Caregiver cg : secondaryCaregivers.getCaregiverList()) {
            if (cg.getNotify() == true) {
                msgManager.sendTextMessage(cg.getPhoneNum(), null, message, null, null);
            }
        }
        return true ;
    }


    public String getPrimaryCaregiverID() {
        return primaryCaregiverID;
    }

    public String getPrimaryCaregiverName() {
        return name;
    }
    public void setPrimaryCaregiverID(String primaryCaregiverID) {
        this.primaryCaregiverID = primaryCaregiverID;
    }

    public void setPrimaryCaregiverName(String pcgName) {
        this.name = pcgName;
    }
    public MyPatient getPatient() {
        return patient;
    }

    public void setPatient(MyPatient patient) {
        this.patient = patient;
    }

    public CaregiverList getSecondaryCaregivers() {
        return secondaryCaregivers;
    }

    public void setSecondaryCaregivers(CaregiverList secondaryCaregivers) {
        this.secondaryCaregivers = secondaryCaregivers;
    }
}
