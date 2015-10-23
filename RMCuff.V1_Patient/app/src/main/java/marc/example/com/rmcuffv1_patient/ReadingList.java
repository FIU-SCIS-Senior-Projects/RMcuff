package marc.example.com.rmcuffv1_patient;

/**
 * Created by Davidb on 10/22/15.
 */
import java.util.ArrayList;


public class ReadingList
{
    private ArrayList<Reading> readingList ;

    public ReadingList()
    {
        readingList = new ArrayList<Reading>() ;
    }

    public void add(int index, Reading reading)
    {
        readingList.add(index, reading) ;
    }

    public Reading get(int index)
    {
        return readingList.get(index) ;
    }

    public int size()
    {
        return readingList.size() ;
    }

    public ArrayList<Reading> getReadingList() {
        return readingList;
    }

    public void setReadingList(ArrayList<Reading> readingList) {
        this.readingList = readingList;
    }
}
