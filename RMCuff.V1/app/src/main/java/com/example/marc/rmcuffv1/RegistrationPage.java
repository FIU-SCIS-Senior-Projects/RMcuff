package com.example.marc.rmcuffv1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.marc.rmcuffv1.Caregiver.CaregiverList;
import com.example.marc.rmcuffv1.Caregiver.PrimaryCaregiver;
import com.example.marc.rmcuffv1.Preferences.ComplexPreferences;
import com.example.marc.rmcuffv1.Preferences.ObjectPreference;
import com.example.marc.rmcuffv1.Settings.Patient.MyPatient;
import com.example.marc.rmcuffv1.Settings.Reading.ReadingList;
import com.example.marc.rmcuffv1.Settings.Schedule.ScheduleList;

public class RegistrationPage extends AppCompatActivity {

    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        // Get phone data
        objectPreference = (ObjectPreference) this.getApplication();
        objectPreference.createNewComplexFile("data");
        complexPreferences = objectPreference.getComplexPreference();
    }

    public void submitForm(View view)
    {
        // Get text fields
        EditText yourName = (EditText) findViewById(R.id.yourName) ;
        EditText yourPhone = (EditText) findViewById(R.id.yourPhone) ;
        EditText theirName = (EditText) findViewById(R.id.theirName) ;
        EditText theirPhone = (EditText) findViewById(R.id.theirPhone) ;

        // grab input info
        String cgName = yourName.getText().toString() ;
        String pName = theirName.getText().toString() ;
        String cgNum = yourPhone.getText().toString() ;
        String pNum = theirPhone.getText().toString() ;

        System.out.println("Name:" + yourName.getText() + "\n" + "#:" + yourPhone.getText() + "\n" + "their name:" + theirName.getText() + "\n" + "their #:" + theirPhone.getText()) ;

        boolean readyToRegister = true ;

        // Check if fields are valid
        if (empty(cgName))
        {
            readyToRegister = false ;
            theirName.setError("Please Enter Care Provider's Name") ;
        }
        if (empty(cgNum) || invalid(cgNum))
        {
            readyToRegister = false ;
            theirPhone.setError("Please enter a valid Phone Number (10 digits)(No special Characters)");
        }
        if (empty(pName))
        {
            readyToRegister = false ;
            yourName.setError("Please Enter your Name");
        }
        if (empty(pNum) || invalid(pNum))
        {
            readyToRegister = false ;
            yourPhone.setError("Please enter a valid Phone Number (10 digits)(No special Characters)");
        }

        // if valid input
        if(readyToRegister)
        {
            // Set up registered objects and store them to device memory
            MyPatient patient = new MyPatient(pNum, pName,new ReadingList(), new ScheduleList()) ;
            PrimaryCaregiver pcg = new PrimaryCaregiver(cgNum, cgName, patient, new CaregiverList() ) ;
            complexPreferences.putObject("pcg", pcg) ;
            complexPreferences.commit() ;
            registrationComplete() ;
        }
    }

    public void registrationComplete()
    {
        // Take user to the main activity
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);

        // Terminate this activity
        finish() ;
    }

    // check if phone is digits only and 10 char length
    public boolean invalid(String phoneNum)
    {
        return phoneNum.length() != 10 || !TextUtils.isDigitsOnly(phoneNum);
    }

    // Check if empty input
    public boolean empty(String s)
    {
        return TextUtils.isEmpty(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_registration_page, menu);
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

    // Disable back button by not calling super
    @Override
    public void onBackPressed() {
        finish();
    }
}
