package com.example.marc.rmcuffv1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date ;

public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    Patient patient ;
    private Intent caregiversPage;

    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //Caregiver [] caregivers = { new Caregiver("Person", "7863158886", true) } ;
        //patient = new Patient("David", new Date(), caregivers, new Reading[10], null, new Device("XYZ007") ) ;


        objectPreference = (ObjectPreference) this.getApplication();
        objectPreference.createNewComplexFile("caregivers");
        complexPreferences = objectPreference.getComplexPreference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendTextMessage(View view) {
        sendTextMessage();
    }

    private void sendTextMessage() {
        /*
        String message = "Name: David Baez\n109/68/87 @ 15:15 08/25\n102/69/84 @ 20:15 08/25\n..." ;
        SmsManager msgManager = SmsManager.getDefault() ;

        for (String caregiverNumber : caregivers) {
            msgManager.sendTextMessage(caregiverNumber, null, message, null, null);
        }
        */

        //OLD
        //patient.beginReading() ; // for Testing // will later go on Start Reading button
        //patient.alertCaregivers() ;

        String message = "Patient: Dustin M\n109/68 @ 15:15 08/25\n102/69 @ 20:15 08/24\n100/70 @ 17:12 08/23\n..." ;
        SmsManager msgManager = SmsManager.getDefault() ;
        TextView v = (TextView) findViewById(R.id.edit);
        // NEW
        try {
            ArrayList<Caregiver> caregiverResults = GetSearchResultsFromPreferences();

            if (!caregiverResults.isEmpty()) {
                for (Caregiver cg : caregiverResults) {
                    if (cg.getNotify()) // if the notify box is checked
                    {
                        msgManager.sendTextMessage(cg.getPhoneNum(), null, message, null, null);
                    }
                }
                v.setText("Message Sent!");
            } else {
                v.setText("CaregiverList is empty..");
            }
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, "EMPTY LIST");
        }
    }

    private ArrayList<Caregiver> GetSearchResultsFromPreferences() {
        ArrayList<Caregiver> results = new ArrayList<>();
        int count;

        count = complexPreferences.getCount();

        for (int i = 0; i < count; i++) {
            Caregiver c = complexPreferences.getObject(String.valueOf(i), Caregiver.class);
            results.add(c);
        }

        //Log.d(LOG_TAG, results.toString());
        return results;
    }

    public void startCaregivers(View view) {
        startCaregivers();
    }

    private void startCaregivers() {
        caregiversPage = new Intent(this, CaregiversPage.class);
        startActivity(caregiversPage);
    }
}
