package marc.example.com.rmcuffv1_patient.Settings.Readings;

import java.util.ArrayList;

public class ReadingList {
    private ArrayList<marc.example.com.rmcuffv1_patient.Settings.Readings.Reading> readingList;

    public ReadingList() {
        readingList = new ArrayList<>();
    }

    public void add(int index, marc.example.com.rmcuffv1_patient.Settings.Readings.Reading reading) {
        readingList.add(index, reading);
    }

    public marc.example.com.rmcuffv1_patient.Settings.Readings.Reading get(int index) {
        return readingList.get(index);
    }

    public int size() {
        return readingList.size();
    }

    public ArrayList<marc.example.com.rmcuffv1_patient.Settings.Readings.Reading> getReadingList() {
        return readingList;
    }

    public void setReadingList(ArrayList<marc.example.com.rmcuffv1_patient.Settings.Readings.Reading> readingList) {
        this.readingList = readingList;
    }
}
