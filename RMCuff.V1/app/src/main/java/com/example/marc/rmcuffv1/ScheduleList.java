package com.example.marc.rmcuffv1;

/**
 * Created by Davidb on 11/19/15.
 */
import java.util.ArrayList;

public class ScheduleList
{
    ArrayList<Schedule> scheduleList ;

    public ScheduleList()
    {
        scheduleList = new ArrayList<Schedule>() ;
    }

    public void add(int index, Schedule schedule)
    {
        scheduleList.add(index, schedule) ;
    }

    public Schedule get(int index)
    {
        return scheduleList.get(index) ;
    }
    public int size()
    {
        return scheduleList.size() ;
    }


    public ArrayList<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(ArrayList<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}

