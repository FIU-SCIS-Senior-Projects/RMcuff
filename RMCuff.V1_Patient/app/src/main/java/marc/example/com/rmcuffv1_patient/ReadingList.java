package marc.example.com.rmcuffv1_patient;

import java.util.ArrayList;

public class ReadingList {
    private ArrayList<Reading> readingList;

    public ReadingList() {
        readingList = new ArrayList<>();
    }

    public void add(int index, Reading reading) {
        readingList.add(index, reading);
    }

    public Reading get(int index) {
        return readingList.get(index);
    }

    public int size() {
        return readingList.size();
    }

    public ArrayList<Reading> getReadingList() {
        return readingList;
    }

    public void setReadingList(ArrayList<Reading> readingList) {
        this.readingList = readingList;
    }
}
