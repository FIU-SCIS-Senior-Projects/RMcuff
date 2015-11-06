package marc.example.com.rmcuffv1_patient;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pushbots.push.Pushbots;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Patient patient ;
    private Gson GSON = new Gson();

    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //register() ;

        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(CustomHandler.class);

        System.out.println("XXX4 WORKS4") ;
        objectPreference = (ObjectPreference) this.getApplication();
        objectPreference.createNewComplexFile("data");
        complexPreferences = objectPreference.getComplexPreference();

        ScheduleList schedule = null ;
        ReadingList readings = null ;

        if( complexPreferences != null )
        {
            //complexPreferences.putObject("scheduleList", new ScheduleList()) ;
            //complexPreferences.putObject("readingList", new ReadingList()) ;
            //complexPreferences.commit() ;
            System.out.println("XXX5 WORKS5") ;
            schedule = complexPreferences.getObject("scheduleList", ScheduleList.class ) ;
            readings = complexPreferences.getObject("readingList", ReadingList.class ) ;
        }

        if(schedule == null)
            schedule = new ScheduleList() ;
        if(readings == null)
            readings = new ReadingList() ;


        // LOAD ALL STORED DATA
        patient = new Patient("7864445555", "7863158886", "Luke", new Date(), readings, schedule, new Device("0")) ;

        Pushbots.sharedInstance().setAlias(patient.getPatientID()) ;

        // Update UI
        updateUIFields() ;
    }

    public void updateUIFields()
    {
        // Imports
        TextView patientWelcome = (TextView) findViewById(R.id.patientNameField) ;
        TextView pcgName = (TextView) findViewById(R.id.pcgNameField) ;
        TextView pcgPhone = (TextView) findViewById(R.id.pcgPhoneField) ;

        ArrayAdapter<String> readingsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        ListView readingsList = (ListView) findViewById(R.id.readingsInfoList);
        readingsList.setAdapter(readingsAdapter);

        ArrayAdapter<String> scheduleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        ListView scheduleList = (ListView) findViewById(R.id.scheduleInfoList);
        scheduleList.setAdapter(scheduleAdapter);

        // Set textFields
        patientWelcome.setText("Welcome, " + patient.getName() + "!") ;
        pcgName.setText("David Baez");
        pcgPhone.setText(patient.getPrimaryCaregiverID()) ;

        if(patient.getSchedule().size() > 0)
        {
            System.out.println("&&&&" + patient.getSchedule().size()) ;
            scheduleAdapter.add(patient.getSchedule().get(0).toString());
        }
        else
        {
            scheduleAdapter.add("No readings have been scheduled ...");
        }

        ReadingList readings = patient.getReadings() ;
        if(readings.size() == 0 )
            readingsAdapter.add("No readings have been taken ..") ;

        for (int i = 0; i < 3; i++)
        {
            if(readings.size() > i)
            {
                readingsAdapter.add(readings.get(i).toString()) ;
            }
        }

    }

    private void register()
    {
        Intent registerPage = new Intent(this, RegistrationPage.class);
        startActivity(registerPage);
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
                Uri number = Uri.parse( "tel:" + patient.getPrimaryCaregiverID() ) ;
                Intent intent = new Intent(Intent.ACTION_CALL, number);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Could not make call " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }



    public void takeReading(View view) { readingPost(); }

    public void readingPost()
    {
        // Get Phone Signal and Wifi Status
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the phone is connected somehow
        if (networkInfo != null && networkInfo.isConnected())
        {
            // fetch data
            System.out.println("Connected") ;

            Random r = new Random();

            int rSys =  r.nextInt(20) + 100 ; // between 100 and 120
            int rDia = r.nextInt(20) + 80 ; // between 80 and 100

            Reading toReading = new Reading(rSys, rDia, new Date());
            String toSend = GSON.toJson(toReading, Reading.class) ;

            patient.getReadings().add(0, toReading) ;
            complexPreferences.putObject("readingList", patient.getReadings()) ;
            updateUIFields() ;

            Post post = new Post() ;
            post.execute(patient.getPrimaryCaregiverID(), toSend) ;
        }
        else
        {
            // display error
            System.out.println("No Connection") ;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
