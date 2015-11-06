package com.example.marc.rmcuffv1;

import java.util.ArrayList;

/**
 * Created by Davidb on 10/26/15.
 */
public class CaregiverList {

    private ArrayList<Caregiver> caregiverList ;

    public CaregiverList()
    {
        caregiverList = new ArrayList<Caregiver>() ;
    }

    public void add(int index, Caregiver caregiver)
    {
        caregiverList.add(index, caregiver) ;
    }

    public void remove(int index)
    {
        caregiverList.remove(index);
    }

    public Caregiver get(int index)
    {
        return caregiverList.get(index) ;
    }

    public void replace(int index, Caregiver caregiver)
    {
        caregiverList.remove(index) ;
        caregiverList.add(index, caregiver) ;
    }

    public int size()
    {
        return caregiverList.size() ;
    }

    public ArrayList<Caregiver> getCaregiverList() {
        return caregiverList;
    }

    public void setCaregiverList(ArrayList<Caregiver> caregiverList) {
        this.caregiverList = caregiverList;
    }

}

