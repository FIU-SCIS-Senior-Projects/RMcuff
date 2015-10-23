package com.example.marc.rmcuffv1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.util.Date;

public class NewSchedulePage  extends Activity {

    PrimaryCaregiver pcg = null ;
    private Gson GSON = new Gson();

    private ObjectPreference pcgObjectPreference;
    private ComplexPreferences pcgComplexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule_page);


        pcgObjectPreference = (ObjectPreference) this.getApplication();
        pcgObjectPreference.createNewComplexFile("primaryCaregiver") ;
        pcgComplexPreferences = pcgObjectPreference.getComplexPreference() ;

        if (pcgComplexPreferences != null)
        {
            System.out.println("$$$$$$*********\n");
            pcg = pcgComplexPreferences.getObject("pcg", PrimaryCaregiver.class) ;
        }

    }

    public void sendSchedule(View view)
    {
        DatePicker dp = (DatePicker) findViewById(R.id.datePicker) ;
        TimePicker tp = (TimePicker) findViewById(R.id.timePicker) ;

        if( dp != null && tp != null && pcg != null)
        {
            Date date = new Date(dp.getYear() - 1900, dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute()) ;

            // Send Date
            schedulePost(date) ;
        }
    }

    public void schedulePost(Date date)
    {
        // Get Phone Signal and Wifi Status
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the phone is connected somehow
        if (networkInfo != null && networkInfo.isConnected())
        {
            // fetch data
            System.out.println("Connected") ;
            Schedule toSchedule = new Schedule(date) ;
            String toSend = GSON.toJson(toSchedule, Schedule.class) ;

            Post post = new Post() ;
            post.execute(pcg.getPatient().getPatientID(), toSend) ;
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
        getMenuInflater().inflate(R.menu.menu_new_schedule_page, menu);
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
