package com.example.marc.rmcuffv1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marc.rmcuffv1.Caregiver.CaregiverList;
import com.example.marc.rmcuffv1.Caregiver.CaregiversPage;
import com.example.marc.rmcuffv1.Caregiver.PrimaryCaregiver;
import com.example.marc.rmcuffv1.Preferences.ComplexPreferences;
import com.example.marc.rmcuffv1.Preferences.ObjectPreference;
import com.example.marc.rmcuffv1.PushPull.CustomHandler;
import com.example.marc.rmcuffv1.Settings.Reading.ReadingList;
import com.example.marc.rmcuffv1.Settings.Schedule.NewSchedulePage;
import com.example.marc.rmcuffv1.Settings.Schedule.ScheduleList;
import com.google.gson.Gson;
import com.pushbots.push.Pushbots;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private PrimaryCaregiver pcg ;
    private Intent caregiversPage ;

    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;

    private Gson GSON = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        // Set up Pushbots instance for push notification system
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(CustomHandler.class);

        // Create/Get phone data file
        objectPreference =  (ObjectPreference) this.getApplication() ;
        objectPreference.createNewComplexFile("data") ;
        complexPreferences = objectPreference.getComplexPreference() ;

        CaregiverList caregiverResults = null ;
        ReadingList readings = null;

        if( complexPreferences != null )
        {
            // Grab data from phone data
            caregiverResults = complexPreferences.getObject("caregiverList", CaregiverList.class) ;
            readings = complexPreferences.getObject("readingList", ReadingList.class ) ;
            pcg = complexPreferences.getObject("pcg", PrimaryCaregiver.class) ;
        }

        if (pcg == null) // If no primary caregiver registered, register the user
            register() ;
        if( readings == null)
            readings = new ReadingList() ;
        if (caregiverResults == null)
            caregiverResults = new CaregiverList() ;

        // If user is registered
        if (pcg != null)
        {
            // Set the pcg attributes
            pcg.setSecondaryCaregivers(caregiverResults) ;
            pcg.getPatient().setReadings(readings) ;

            // Save pcg to device memory
            complexPreferences.putObject("pcg", pcg) ;
            complexPreferences.commit() ;

            // use phone number as unique key for the user on pushbots
            Pushbots.sharedInstance().setAlias(pcg.getPrimaryCaregiverID()) ;

            // Update UI
            updateUIFields() ;
        }

    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        // When app is opened after standby

        // Restore pcg object
        pcg = complexPreferences.getObject("pcg", PrimaryCaregiver.class ) ;

        // If not registered
        if(pcg == null)
            register() ;

        // Set pushbots data again
        Pushbots.sharedInstance().setAlias(pcg.getPrimaryCaregiverID()) ;

        // Update UIfields again
        updateUIFields() ;
    }

    // Call to register a user
    private void register() {

        // First load the registration splash page, this will then lead to the registration page
        Intent registerSplash = new Intent(this, RegistrationSplash.class);
        startActivity(registerSplash);

        // Close this activity
        finish() ;
    }

    public void clearAllData(View view) {
        clearAllData();
    }

    private void clearAllData() {
        // clear all data stored on phone, used for testing purposes

        CaregiverList caregiverResults = new CaregiverList();
        ReadingList readings = new ReadingList();

        complexPreferences.removeObject("caregiverList");
        complexPreferences.commit();

        complexPreferences.removeObject("readingList");
        complexPreferences.commit();

        complexPreferences.removeObject("pcg");
        complexPreferences.commit();

        pcg = null;

        // User must now register again
        register();
    }

    private void updateUIFields()
    {
        // Imports
        TextView pcgWelcome = (TextView) findViewById(R.id.pcgNameField) ;
        TextView patientName = (TextView) findViewById(R.id.patientNameField) ;
        TextView patientPhone = (TextView) findViewById(R.id.patientPhoneField) ;

        // Set up list adapters and lists
        ArrayAdapter<String> readingsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        ListView readingsList = (ListView) findViewById(R.id.readingsInfoList);
        readingsList.setAdapter(readingsAdapter);

        ArrayAdapter<String> scheduleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        ListView scheduleList = (ListView) findViewById(R.id.scheduleInfoList);
        scheduleList.setAdapter(scheduleAdapter);

        ArrayAdapter<String> caregiverAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);


        // Set textFields
        pcgWelcome.setText("Welcome, " + pcg.getPrimaryCaregiverName() + "!") ;
        patientName.setText(pcg.getPatient().getName());
        patientPhone.setText(pcg.getPatient().getPatientID()) ;

        ReadingList readings = pcg.getPatient().getReadings() ;
        if(readings.size() == 0 )
            readingsAdapter.add("No Readings have been taken ..") ;

        for (int i = 0; i < readings.size(); i++)
        {
            readingsAdapter.add(readings.get(i).toString());
        }

        ScheduleList schedule = pcg.getPatient().getScheduled() ;
        if(schedule.size() == 0 )
            scheduleAdapter.add("No Readings have been scheduled ..") ;

        for (int i = 0; i < schedule.size(); i++)
        {
            scheduleAdapter.add(schedule.get(i).toString());
        }
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
            clearAllData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setNewSchedule(View view) { schedulePost() ; }

    public void schedulePost()
    {
        // Take user to schedule page
        Intent schedulePage = new Intent(this, NewSchedulePage.class);
        startActivity(schedulePage);
    }

    public void startCaregivers(View view) {
        startCaregivers();
    }

    private void startCaregivers() {
        // takes user to the secondary caregiver list
        caregiversPage = new Intent(this, CaregiversPage.class);
        startActivity(caregiversPage);
    }

    public void makeCall(View view) { makePhoneCall(); }

    private void makePhoneCall() {
        // Call shortcut button

        //TelephonyManager tm= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        PackageManager pm = getPackageManager();

        // if the user has no calling functionality, warn them
        if (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
            //has no Telephony features.
            System.out.println("Can't call") ;
            Toast.makeText(getApplicationContext(), "Calling Unavailable", Toast.LENGTH_LONG).show();
        }
        else
        {
            // Can make calls
            try
            {
                // start call intent using patient phone number
                Uri number = Uri.parse( "tel:" + pcg.getPatient().getPatientID() ) ;
                Intent intent = new Intent(Intent.ACTION_CALL, number);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Could not make call " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    // Disable back button by not calling super
    @Override
    public void onBackPressed() {
        finish();
    }
}
