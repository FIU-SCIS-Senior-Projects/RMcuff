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

        objectPreference = (ObjectPreference) this.getApplication();
        //objectPreference.createNewComplexFile("caregivers");
        objectPreference.createNewComplexFile("data");

        complexPreferences = objectPreference.getComplexPreference();
        caregiverListView = (ListView) findViewById(R.id.caregiverListView);

        pcg = complexPreferences.getObject("pcg", PrimaryCaregiver.class) ;

        registerForContextMenu(caregiverListView);
        populateList();
    }

    public void sendTextMessage(View view) {
        sendTextMessage();
    }

    private void sendTextMessage() {

        if (pcg != null && pcg.getPatient().getReadings().size() != 0)
        {

            String message = "Patient: " + pcg.getPatient().getName() + "\n" ;

            for (Reading r : pcg.getPatient().getReadings().getReadingList())
            {
                message += r.toString() + "\n" ;
            }

            SmsManager msgManager = SmsManager.getDefault();

            // NEW
            try {
                CaregiverList caregiverResults = complexPreferences.getObject("caregiverList", CaregiverList.class);

                if (!caregiverResults.getCaregiverList().isEmpty()) {
                    for (Caregiver cg : caregiverResults.getCaregiverList()) {
                        if (cg.getNotify()) // if the notify box is checked
                        {
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
            case R.id.delete:
                o = caregiverListView.getItemAtPosition(info.position);
                fullObject = (Caregiver) o;

                CaregiverList caregiverResults = complexPreferences.getObject("caregiverList", CaregiverList.class) ;
                caregiverResults.remove(info.position) ;

                //complexPreferences.removeObject(String.valueOf(info.position));
                complexPreferences.putObject("caregiverList", caregiverResults);
                complexPreferences.commit();

                Log.d(LOG_TAG, "DELETING: " + " " + fullObject.getFirstName());

                populateList();

                return true;
            case R.id.edit:
                o = caregiverListView.getItemAtPosition(info.position);
                fullObject = (Caregiver) o;

                fullObject.setUserID(info.position);
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

    /*
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
    */
}
