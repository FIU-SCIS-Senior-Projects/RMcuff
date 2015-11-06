package marc.example.com.rmcuffv1_patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RegistrationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
    }

    public void submitForm(View view)
    {
        EditText yourName = (EditText) findViewById(R.id.yourName) ;
        EditText yourPhone = (EditText) findViewById(R.id.yourPhone) ;
        EditText theirName = (EditText) findViewById(R.id.theirName) ;
        EditText theirPhone = (EditText) findViewById(R.id.theirPhone) ;

        System.out.println("Name:" + yourName.getText() + "\n" + "#:" + yourPhone.getText() + "\n" + "their name:" + theirName.getText() + "\n" + "their #:" + theirPhone.getText()) ;

        boolean readyToRegister = true ;

        if (empty(yourName.getText().toString()))
        {
            readyToRegister = false ;
            yourName.setError("Please Enter your Name");
        }
        if (empty(yourPhone.getText().toString()) || invalid(yourPhone.getText().toString()))
        {
            readyToRegister = false ;
            yourPhone.setError("Please enter a valid Phone Number (10 digits)(No special Characters)");
        }
        if (empty(theirName.getText().toString()))
        {
            readyToRegister = false ;
            theirName.setError("Please Enter Care Provider's Name") ;
        }
        if (empty(theirPhone.getText().toString()) || invalid(theirPhone.getText().toString()))
        {
            readyToRegister = false ;
            theirPhone.setError("Please enter a valid Phone Number (10 digits)(No special Characters)");
        }

        if(readyToRegister)
        {
            // Save Data and exit this page
        }
    }
    public boolean invalid(String phoneNum)
    {
        if(phoneNum.length() != 10 || !TextUtils.isDigitsOnly(phoneNum)) 
        {
            return true ;
        }
        return false ;
    }
    public boolean empty(String s)
    {
        if (TextUtils.isEmpty(s))
        {
            return true ;
        }
        return false ;
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
