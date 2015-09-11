package com.example.marc.rmcuffv1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.Date ;

public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    Patient patient ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Caregiver [] caregivers = { new Caregiver("Person", "7863158886", true) } ;
        patient = new Patient("David", new Date(), caregivers, new Reading[10], null, new Device("XYZ007") ) ;
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
        patient.beginReading() ; // for Testing // will later go on Start Reading button
        patient.alertCaregivers() ;
        TextView v = (TextView) findViewById(R.id.homePageStatus);
        v.setText("Message Sent!");
    }
}
