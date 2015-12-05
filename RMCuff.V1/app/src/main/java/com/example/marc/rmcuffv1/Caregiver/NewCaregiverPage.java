package com.example.marc.rmcuffv1.Caregiver;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.marc.rmcuffv1.Preferences.ComplexPreferences;
import com.example.marc.rmcuffv1.Preferences.ObjectPreference;
import com.example.marc.rmcuffv1.R;

public class NewCaregiverPage extends Activity {

    private static final String LOG_TAG = NewCaregiverPage.class.getSimpleName();

    private Caregiver caregiver = new Caregiver();
    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;

    private EditText firstName, lastName, emailAddress, phoneNumber;
    private Button confirmButton;
    private boolean edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_caregiver_page);

        objectPreference = (ObjectPreference) this.getApplication();
        //objectPreference.createNewComplexFile("caregivers");
        objectPreference.createNewComplexFile("data");

        complexPreferences = objectPreference.getComplexPreference();

        if (getIntent().getSerializableExtra("editCaregiver") != null){
            caregiver = (Caregiver) getIntent().getSerializableExtra("editCaregiver");

            confirmButton = (Button) findViewById(R.id.addCaregiverButton);
            confirmButton.setText("EDIT");
            edit = true;

            editCaregiver(caregiver);
        } else {
            caregiver = new Caregiver();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_new_caregiver, menu);
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

    private void editCaregiver(Caregiver caregiver) {
        firstName = (EditText) findViewById(R.id.editText_firstName);
        lastName = (EditText) findViewById(R.id.editText_lastName);
        emailAddress = (EditText) findViewById(R.id.editText_email);
        phoneNumber = (EditText) findViewById(R.id.editText_phone);

        try {
            firstName.setText(caregiver.getFirstName());
            lastName.setText(caregiver.getLastName());
            emailAddress.setText(caregiver.getEmailAddress());
            phoneNumber.setText(caregiver.getPhoneNum());

            Log.d(LOG_TAG, "LOADED: " + caregiver.toString());
        } catch (Exception e) {
            setCaregiverStatus("Error saving caregiver!");

            Log.e(LOG_TAG, "LOAD ERROR: " + e);
        }
    }

    public void createNewCaregiver(View view) {
        createNewCaregiver();
        finish();
    }

    private void createNewCaregiver() {
        firstName = (EditText) findViewById(R.id.editText_firstName);
        lastName = (EditText) findViewById(R.id.editText_lastName);
        emailAddress = (EditText) findViewById(R.id.editText_email);
        phoneNumber = (EditText) findViewById(R.id.editText_phone);

        try {
            caregiver.setFirstName(firstName.getText().toString());
            caregiver.setLastName(lastName.getText().toString());
            caregiver.setEmailAddress(emailAddress.getText().toString());
            caregiver.setPhoneNum(phoneNumber.getText().toString());

            if (edit) {
                saveToComplexPreferences();

                setCaregiverStatus("Caregiver Edited!");

                Log.d(LOG_TAG, "EDITED: " + caregiver.toString());

                super.onBackPressed();
            } else {
                //updateComplexPreferencesCount();
                saveToComplexPreferences();

                setCaregiverStatus("Caregiver Added!");

                Log.d(LOG_TAG, "SAVED: " + caregiver.toString());
            }
        } catch (Exception e) {
            setCaregiverStatus("Error Saving Caregiver!");

            Log.e(LOG_TAG, "SAVE ERROR: " + e);
        }

        clearNewCaregiverField();
    }

    private void clearNewCaregiverField() {
        firstName = (EditText) findViewById(R.id.editText_firstName);
        lastName = (EditText) findViewById(R.id.editText_lastName);
        emailAddress = (EditText) findViewById(R.id.editText_email);
        phoneNumber = (EditText) findViewById(R.id.editText_phone);

        firstName.setText("");
        lastName.setText("");
        emailAddress.setText("");
        phoneNumber.setText("");
    }

    private void setCaregiverStatus(String status) {
        TextView v = (TextView) findViewById(R.id.addCaregiverStatus);

        v.setText(status);
    }

    /*
    private void updateComplexPreferencesCount() {
        if (complexPreferences != null) {
            caregiver.setUserID(complexPreferences.getCount());
            complexPreferences.setCount(complexPreferences.getCount() + 1);

            Log.d(LOG_TAG, "UPDATING USER COUNT.  NEW COUNT: " + caregiver.getUserID());
        } else {
            Log.d(LOG_TAG, "ERROR WRITING USER COUNT");
        }
    }
    */

    private void saveToComplexPreferences() {
        if (complexPreferences != null) {
            CaregiverList cgl = complexPreferences.getObject("caregiverList", CaregiverList.class) ;
            if (cgl == null) cgl = new CaregiverList() ;

            if (!edit)
                cgl.add(0, caregiver);
            else
                cgl.replace(caregiver.getUserID(), caregiver);

            //complexPreferences.putObject(String.valueOf(caregiver.getUserID()), caregiver);
            complexPreferences.putObject("caregiverList", cgl);
            complexPreferences.commit();

            Log.d(LOG_TAG, "WRITING TO COMPLEXPREF: " + (caregiver));

            caregiver = new Caregiver();
        } else {
            Log.d(LOG_TAG, "ERROR WRITTING COMPLEXPREF");
        }
    }

    // Disable back button by not calling super
    @Override
    public void onBackPressed() {
        finish();
    }
}
