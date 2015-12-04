package com.example.marc.rmcuffv1;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pushbots.push.Pushbots;

import java.util.ArrayList;
import java.util.Date ;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    //MyPatient patient ;
    private PrimaryCaregiver pcg ;
    private Intent caregiversPage;

    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;

    private Gson GSON = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(CustomHandler.class);

        // Get all Object preferences
        objectPreference =  (ObjectPreference) this.getApplication() ;
        objectPreference.createNewComplexFile("data") ;

        complexPreferences = objectPreference.getComplexPreference() ;

        CaregiverList caregiverResults = null ;
        ReadingList readings = null;

        if( complexPreferences != null )
        {
            //readingComplexPreferences.putObject("readingList", new ReadingList());
            //readingComplexPreferences.commit() ;

            System.out.println("XXX5 WORKS5") ;
            caregiverResults = complexPreferences.getObject("caregiverList", CaregiverList.class) ;
            readings = complexPreferences.getObject("readingList", ReadingList.class ) ;
            pcg = complexPreferences.getObject("pcg", PrimaryCaregiver.class) ;
        }

        if (pcg == null)
            register() ;
        if( readings == null)
            readings = new ReadingList() ;
        if (caregiverResults == null)
            caregiverResults = new CaregiverList() ;


        // MyPatient patient = new MyPatient("7864445555", "Luke Skywalkwer", readings, new ScheduleList()) ;
        // pcg = new PrimaryCaregiver("7863158886", patient, caregiverResults) ;


        //Dynamic Load


        if (pcg != null)
        {
            pcg.setSecondaryCaregivers(caregiverResults) ;
            pcg.getPatient().setReadings(readings) ;
            complexPreferences.putObject("pcg", pcg) ;
            complexPreferences.commit() ;


            Pushbots.sharedInstance().setAlias(pcg.getPrimaryCaregiverID()) ;
            // Update UI
            updateUIFields() ;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        System.out.println("+++++ Restart") ;

        pcg = complexPreferences.getObject("pcg", PrimaryCaregiver.class ) ;

        if(pcg == null)
            register() ;

        // Set pushbots data again
        Pushbots.sharedInstance().setAlias(pcg.getPrimaryCaregiverID()) ;

        updateUIFields() ;
    }

    private void register()
    {
        //Intent registerPage = new Intent(this, RegistrationPage.class);
        //startActivity(registerPage);
        Intent registerSplash = new Intent(this, RegistrationSplash.class);
        startActivity(registerSplash);
    }

    public void clearAllData(View view)
    {
        CaregiverList caregiverResults = new CaregiverList() ;
        ReadingList readings = new ReadingList() ;

        complexPreferences.removeObject("caregiverList");
        complexPreferences.commit();

        complexPreferences.removeObject("readingList");
        complexPreferences.commit();

        complexPreferences.removeObject("pcg");
        complexPreferences.commit();

        pcg = null ;
        register() ;
    }

    public void updateUIFields()
    {
        // Imports
        TextView pcgWelcome = (TextView) findViewById(R.id.pcgNameField) ;
        TextView patientName = (TextView) findViewById(R.id.patientNameField) ;
        TextView patientPhone = (TextView) findViewById(R.id.patientPhoneField) ;

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

        for (int i = 0; i < 3; i++)
        {
            if(readings.size() > i)
            {
                readingsAdapter.add(readings.get(i).toString()) ;
            }
        }

        scheduleAdapter.add("No Readings have been scheduled ..") ;
        //scheduleAdapter.add("Time 2") ;
        //scheduleAdapter.add("Time 3") ;


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

    public void setNewSchedule(View view) { schedulePost() ; }

    public void schedulePost()
    {

        Intent schedulePage = new Intent(this, NewSchedulePage.class);
        startActivity(schedulePage);

        /*
        // Get Phone Signal and Wifi Status
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the phone is connected somehow
        if (networkInfo != null && networkInfo.isConnected())
        {
            // fetch data
            System.out.println("Connected") ;
            Schedule toSchedule = new Schedule(new Date()) ;
            String toSend = GSON.toJson(toSchedule, Schedule.class) ;

            Post post = new Post() ;
            post.execute(pcg.getPatient().getPatientID(), toSend) ;
        }
        else
        {
            // display error
            System.out.println("No Connection") ;
        }
        */
    }

    /*
    private ArrayList<Caregiver> GetSearchResultsFromPreferences() {
        ArrayList<Caregiver> results = new ArrayList<>();
        int count;

        count = secCaregiverComplexPreferences.getCount();

        for (int i = 0; i < count; i++) {
            Caregiver c = secCaregiverComplexPreferences.getObject(String.valueOf(i), Caregiver.class);
            results.add(c);
        }

        //Log.d(LOG_TAG, results.toString());
        return results;
    }
    */

    public void startCaregivers(View view) {
        startCaregivers();
    }

    private void startCaregivers() {
        caregiversPage = new Intent(this, CaregiversPage.class);
        startActivity(caregiversPage);
    }

    public void makeCall(View view) { makePhoneCall(); }

    public void makePhoneCall()
    {
        System.out.println("##########7") ;

        //TelephonyManager tm= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        PackageManager pm = getPackageManager();

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
                Uri number = Uri.parse( "tel:" + pcg.getPatient().getPatientID() ) ;
                Intent intent = new Intent(Intent.ACTION_CALL, number);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Could not make call " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
