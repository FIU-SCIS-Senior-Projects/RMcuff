package marc.example.com.rmcuffv1_patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import marc.example.com.rmcuffv1_patient.Preferences.ComplexPreferences;
import marc.example.com.rmcuffv1_patient.Preferences.ObjectPreference;
import marc.example.com.rmcuffv1_patient.Settings.Patients.Patient;
import marc.example.com.rmcuffv1_patient.Settings.Readings.ReadingList;
import marc.example.com.rmcuffv1_patient.Settings.Schedules.ScheduleList;

public class RegistrationPage extends AppCompatActivity {

    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        objectPreference = (ObjectPreference) this.getApplication();
        objectPreference.createNewComplexFile("data");
        complexPreferences = objectPreference.getComplexPreference();
    }

    public void submitForm(View view) {
        EditText yourName = (EditText) findViewById(R.id.yourName);
        EditText yourPhone = (EditText) findViewById(R.id.yourPhone);
        EditText theirName = (EditText) findViewById(R.id.theirName);
        EditText theirPhone = (EditText) findViewById(R.id.theirPhone);

        String pName = yourName.getText().toString();
        String cgName = theirName.getText().toString();
        String pNum = yourPhone.getText().toString();
        String cgNum = theirPhone.getText().toString();

        System.out.println("Name:" + yourName.getText() + "\n" + "#:" + yourPhone.getText() + "\n" + "their name:" + theirName.getText() + "\n" + "their #:" + theirPhone.getText());

        boolean readyToRegister = true;


        if (empty(pName)) {
            readyToRegister = false;
            yourName.setError("Please Enter your Name");
        }
        if (empty(pNum) || invalid(pNum)) {
            readyToRegister = false;
            yourPhone.setError("Please enter a valid Phone Number (10 digits)(No special Characters)");
        }
        if (empty(cgName)) {
            readyToRegister = false;
            theirName.setError("Please Enter Care Provider's Name");
        }
        if (empty(cgNum) || invalid(cgNum)) {
            readyToRegister = false;
            theirPhone.setError("Please enter a valid Phone Number (10 digits)(No special Characters)");
        }

        if (readyToRegister) {
            // Save Data and exit this page
            //(String patientID, String primaryCaregiverID, String name,  ReadingList readings, ScheduleList schedule)
            Patient patient = new Patient(pNum, cgNum, pName, cgName, new ReadingList(), new ScheduleList());
            complexPreferences.putObject("patient", patient);
            complexPreferences.commit();
            registrationComplete();
        }
    }

    public void registrationComplete() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public boolean invalid(String phoneNum) {
        return phoneNum.length() != 10 || !TextUtils.isDigitsOnly(phoneNum);
    }

    public boolean empty(String s) {
        return TextUtils.isEmpty(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_page, menu);
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