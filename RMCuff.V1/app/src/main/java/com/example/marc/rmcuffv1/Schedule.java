package com.example.marc.rmcuffv1;

import java.util.Date;

/**
 * Created by Davidb on 9/11/15.
 */
public class Schedule
{
    private Date date ;

    public Schedule()
    {

    }
    public Schedule(Date date)
    {
        this.date = date ;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
