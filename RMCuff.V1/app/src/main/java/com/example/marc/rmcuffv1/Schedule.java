package com.example.marc.rmcuffv1;

import java.text.SimpleDateFormat;
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

    public String toString()
    {
        SimpleDateFormat df = new SimpleDateFormat("E MM/dd/yy @ hh:mm a");// "hh:mm MM/dd/yy"
        String theDate = df.format(getDate()) ;

        return String.format(theDate) ;
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
