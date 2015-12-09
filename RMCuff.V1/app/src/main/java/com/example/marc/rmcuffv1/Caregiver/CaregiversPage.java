package com.example.marc.rmcuffv1.Caregiver;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marc.rmcuffv1.Preferences.ComplexPreferences;
import com.example.marc.rmcuffv1.Preferences.ObjectPreference;
import com.example.marc.rmcuffv1.R;
import com.example.marc.rmcuffv1.Settings.Reading.Reading;

public class CaregiversPage extends Activity {

    private static final String LOG_TAG = CaregiversPage.class.getSimpleName();
    PrimaryCaregiver pcg;
    private Intent newCaregiver;
    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;
    private ListView caregiverListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_caregivers_page);

        // load phone data
        objectPreference = (ObjectPreference) this.getApplication();
        objectPreference.createNewComplexFile("data");
        complexPreferences = objectPreference.getComplexPreference();

        // link the list from layout
        caregiverListView = (ListView) findViewById(R.id.caregiverListView);

        // set up pcg object
        pcg = complexPreferences.getObject("pcg", PrimaryCaregiver.class) ;

        registerForContextMenu(caregiverListView);

        // populate the secondary cg list
        populateList();
    }

    public void sendTextMessage(View view) {
        sendTextMessage();
    }

    private void sendTextMessage() {

        // Send text message log

        // if a user is registered and there are readings available from the patient
        if (pcg != null && pcg.getPatient().getReadings().size() != 0)
        {

            String message = "Patient: " + pcg.getPatient().getName() + "\n" ;

            // for each reading the patient has taken
            for (Reading r : pcg.getPatient().getReadings().getReadingList())
            {
                // add the reading to the message that will be submitted
                message += r.toString() + "\n" ;
            }

            // Get phone sms manager
            SmsManager msgManager = SmsManager.getDefault();

            // NEW
            try {
                // grab the caregiver list from phone data
                CaregiverList caregiverResults = complexPreferences.getObject("caregiverList", CaregiverList.class);

                // if the list is not empty
                if (!caregiverResults.getCaregiverList().isEmpty()) {

                    // for each secondary caregiver
                    for (Caregiver cg : caregiverResults.getCaregiverList()) {
                        if (cg.getNotify()) // if the notify box is checked
                        {
                            // send them a copy of the text report
                            msgManager.sendTextMessage(cg.getPhoneNum(), null, message, null, null);
                        }
                    }
                    System.out.println("Message Sent!");
                    Toast.makeText(getApplicationContext(), "Message Sent!",
                            Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("CaregiverList is empty..");
                    Toast.makeText(getApplicationContext(), "Empty caregiver List",
                            Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException e) {
                Log.d(LOG_TAG, "EMPTY LIST");
                Toast.makeText(getApplicationContext(), "Empty caregiver list",
                        Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "You have no readings to report",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_caregivers_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clearList) {
            //complexPreferences.wipePreferences(this, "caregivers");
            //populateList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.caregiverListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Object o;
        Caregiver fullObject;

        switch(item.getItemId()) {
            // user selected the delete option
            case R.id.delete:
                o = caregiverListView.getItemAtPosition(info.position);
                fullObject = (Caregiver) o;

                // remove the object selected
                CaregiverList caregiverResults = complexPreferences.getObject("caregiverList", CaregiverList.class) ;
                caregiverResults.remove(info.position) ;

                // save new list to phone data
                complexPreferences.putObject("caregiverList", caregiverResults);
                complexPreferences.commit();

                Log.d(LOG_TAG, "DELETING: " + " " + fullObject.getFirstName());

                // reload the list in UI
                populateList();

                return true;
            // User selected the edit option
            case R.id.edit:
                o = caregiverListView.getItemAtPosition(info.position);
                fullObject = (Caregiver) o;

                fullObject.setUserID(info.position);

                // send them to the edit page
                Intent mIntent = new Intent(this, NewCaregiverPage.class);
                Bundle mBundle = new Bundle();

                mBundle.putSerializable("editCaregiver", fullObject);
                mIntent.putExtras(mBundle);

                Log.d(LOG_TAG, "STARTING NEW CAREGIVER PAGE");

                startActivity(mIntent);

                Log.d(LOG_TAG, "EDITING: " + " " + fullObject.getFirstName());
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        populateList();
    }

    public void addCaregiver(View view) {
        addCaregiver();
    }

    private void addCaregiver() {
        newCaregiver = new Intent(this, NewCaregiverPage.class);

        Log.d(LOG_TAG, "STARTING NEW CAREGIVER PAGE");

        startActivity(newCaregiver);
    }

    private void populateList() {
        Log.d(LOG_TAG, "POPULATING LIST");

        //Log.d(LOG_TAG, caregiverListView.toString());
        try {
            final CaregiverList caregiverResults = complexPreferences.getObject("caregiverList", CaregiverList.class) ;

            caregiverListView.setAdapter(new CustomArrayAdapter(this, caregiverResults.getCaregiverList()));

            caregiverListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = caregiverListView.getItemAtPosition(position);
                    Caregiver fullObject = (Caregiver) o;

                    fullObject.setNotify(!fullObject.getNotify());

                    caregiverResults.replace(position, fullObject) ;
                    //caregiverResults.add(position, fullObject) ;

                    //complexPreferences.putObject(String.valueOf(position), fullObject);
                    complexPreferences.putObject("caregiverList", caregiverResults);
                    complexPreferences.commit();

                    Log.d(LOG_TAG, "CHOSE: " + " " + fullObject.getFirstName());

                    populateList();
                }
            });
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, "EMPTY LIST");
        }
    }

    // Disable back button by not calling super
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
