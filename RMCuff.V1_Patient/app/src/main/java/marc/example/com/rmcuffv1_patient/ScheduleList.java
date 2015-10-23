package marc.example.com.rmcuffv1_patient;

import java.util.ArrayList;

/**
 * Created by Davidb on 10/21/15.
 */
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
