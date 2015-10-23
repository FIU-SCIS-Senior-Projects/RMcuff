package marc.example.com.rmcuffv1_patient;

import java.util.Date;

/**
 * Created by Davidb on 10/8/15.
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
