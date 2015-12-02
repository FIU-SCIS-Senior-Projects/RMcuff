package marc.example.com.rmcuffv1_patient.Settings.Schedules;

import java.util.ArrayList;

public class ScheduleList {
    ArrayList<marc.example.com.rmcuffv1_patient.Settings.Schedules.Schedule> scheduleList;

    public ScheduleList() {
        scheduleList = new ArrayList<>();
    }

    public void add(int index, marc.example.com.rmcuffv1_patient.Settings.Schedules.Schedule schedule) {
        scheduleList.add(index, schedule);
    }

    public marc.example.com.rmcuffv1_patient.Settings.Schedules.Schedule get(int index) {
        return scheduleList.get(index);
    }

    public int size() {
        return scheduleList.size();
    }


    public ArrayList<marc.example.com.rmcuffv1_patient.Settings.Schedules.Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(ArrayList<marc.example.com.rmcuffv1_patient.Settings.Schedules.Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
