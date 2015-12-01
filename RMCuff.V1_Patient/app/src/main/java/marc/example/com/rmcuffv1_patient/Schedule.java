package marc.example.com.rmcuffv1_patient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule {
    private Date date;

    public Schedule() {

    }

    public Schedule(Date date) {
        this.date = date;
    }

    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("E MM/dd/yy @ hh:mm a");// "hh:mm MM/dd/yy"

        return df.format(getDate());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
