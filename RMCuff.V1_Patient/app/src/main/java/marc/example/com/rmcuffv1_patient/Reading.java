package marc.example.com.rmcuffv1_patient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reading {
    private int systolic;
    private int diastolic;
    private String readingRating;
    private Date date;

    public Reading(int systolic, int diastolic, Date date) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.date = date;

        // Now that systolic and diastolic values are in, we can rate them
        generateReadingRating();
        this.readingRating = getReadingRating();
    }

    // calculations for rating of the reading. GOOD, Bad, Great, etc
    public void generateReadingRating() {
        if (systolic != 0 && diastolic != 0) {
            setReadingRating("GOOD");
        }
        setReadingRating("BAD");
    }

    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a  MM/dd/yy");
        String theDate = df.format(getDate());

        return getSystolic() + "/" + getDiastolic() + " @ " + theDate;
    }

    // Get Methods
    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    // Set methods
    public String getReadingRating() {
        return readingRating;
    }

    public void setReadingRating(String readingRating) {
        this.readingRating = readingRating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}