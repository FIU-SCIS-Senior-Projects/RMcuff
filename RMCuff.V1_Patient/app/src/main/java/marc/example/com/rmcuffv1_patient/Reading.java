package marc.example.com.rmcuffv1_patient;

import java.util.Date;

/**
 * Created by Davidb on 10/8/15.
 */
public class Reading
{
    private int systolic ;
    private int diastolic ;
    private String readingRating ;
    private Date date ;

    public Reading(int systolic, int diastolic, Date date)
    {
        this.systolic = systolic ;
        this.diastolic = diastolic ;
        this.date = date ;

        // Now that systolic and diastolic values are in, we can rate them
        generateReadingRating() ;
        this.readingRating = getReadingRating() ;
    }

    // calculations for rating of the reading. GOOD, Bad, Great, etc
    public void generateReadingRating()
    {
        if ( systolic != 0 && diastolic != 0)
        {
            setReadingRating("GOOD") ;
        }
        setReadingRating("BAD") ;
    }
    public String toString()
    {
        return getSystolic() + "/" + getDiastolic() + " @ " + getDate() ;
    }

    // Get Methods
    public int getSystolic() {
        return systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }
    public String getReadingRating() {
        return readingRating;
    }

    public Date getDate() {
        return date;
    }

    // Set methods

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public void setReadingRating(String readingRating) {
        this.readingRating = readingRating ;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
