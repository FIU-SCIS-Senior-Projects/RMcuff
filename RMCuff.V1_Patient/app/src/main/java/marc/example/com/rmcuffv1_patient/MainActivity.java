package marc.example.com.rmcuffv1_patient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.pushbots.push.Pushbots;

import java.lang.ref.WeakReference;
import java.util.Date;

import marc.example.com.rmcuffv1_patient.BluetoothTools.DeviceConnector;
import marc.example.com.rmcuffv1_patient.BluetoothTools.DeviceData;
import marc.example.com.rmcuffv1_patient.BluetoothTools.Utils;
import marc.example.com.rmcuffv1_patient.Preferences.ComplexPreferences;
import marc.example.com.rmcuffv1_patient.Preferences.ObjectPreference;
import marc.example.com.rmcuffv1_patient.PushPull.CustomHandler;
import marc.example.com.rmcuffv1_patient.PushPull.Post;
import marc.example.com.rmcuffv1_patient.Settings.Patients.Patient;
import marc.example.com.rmcuffv1_patient.Settings.Readings.Reading;
import marc.example.com.rmcuffv1_patient.Settings.Readings.ReadingList;
import marc.example.com.rmcuffv1_patient.Settings.Schedules.ScheduleList;

public class MainActivity extends AppCompatActivity {
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static BluetoothResponseHandler mHandler;
    private static String address = "20:15:07:27:55:76";

    //TextView testTextView;

    private Patient patient;
    private Gson GSON = new Gson();
    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;
    private BluetoothAdapter mBluetoothAdapter = null;
    private DeviceConnector connector;
    private int retryCounter = 0, btRetry = 0;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up pushbots push notification system instance
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(CustomHandler.class);

        // get phone data
        objectPreference = (ObjectPreference) this.getApplication();
        objectPreference.createNewComplexFile("data");
        complexPreferences = objectPreference.getComplexPreference();

        ScheduleList schedule = null;
        ReadingList readings = null;
        patient = null;

        // if phone data found
        if (complexPreferences != null) {
            // set objects from phone data
            schedule = complexPreferences.getObject("scheduleList", ScheduleList.class);
            readings = complexPreferences.getObject("readingList", ReadingList.class);
            patient = complexPreferences.getObject("patient", Patient.class);
        }

        if (patient == null) // if not registered yet
            register();
        if (schedule == null)
            schedule = new ScheduleList();
        if (readings == null)
            readings = new ReadingList();


        // If registered, store all the latest data into the object
        if (patient != null) {
            patient.setReadings(readings);
            patient.setSchedule(schedule);
            complexPreferences.putObject("patient", patient);
            complexPreferences.commit();

            // set up unique push notification key using phone number
            Pushbots.sharedInstance().setAlias(patient.getPatientID());

            // update UI
            updateUIFields();
        }

        // set up google api, for bluetooth
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // set up bluetooth handler
        if (mHandler == null) mHandler = new BluetoothResponseHandler(this);
        else mHandler.setTarget(this);

        // set up bluetooth
        checkBT();
        connectBt();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // when app is opened from pause

        // set up objects again
        patient = complexPreferences.getObject("patient", Patient.class);

        if (patient == null) // if not registered
            register();

        // Set pushbots data again
        Pushbots.sharedInstance().setAlias(patient.getPatientID());

        updateUIFields();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (mHandler == null) mHandler = new BluetoothResponseHandler(this);
        else mHandler.setTarget(this);

        //checkBT();
        //connectBt();
    }

    public void clearAllData(View view) {
        clearAllData();
    }

    private void clearAllData() {
        // clear phone data for app
        complexPreferences.removeObject("patient");
        complexPreferences.commit();

        complexPreferences.removeObject("readingList");
        complexPreferences.commit();

        complexPreferences.removeObject("scheduleList");
        complexPreferences.commit();

        patient = null;
        register(); // user must now register again
    }

    public void updateUIFields() {
        // link layout fields
        TextView patientWelcome = (TextView) findViewById(R.id.patientNameField);
        TextView pcgName = (TextView) findViewById(R.id.pcgNameField);
        TextView pcgPhone = (TextView) findViewById(R.id.pcgPhoneField);

        // set up array adapters and lists
        ArrayAdapter<String> readingsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        ListView readingsList = (ListView) findViewById(R.id.readingsInfoList);
        readingsList.setAdapter(readingsAdapter);

        ArrayAdapter<String> scheduleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        ListView scheduleList = (ListView) findViewById(R.id.scheduleInfoList);
        scheduleList.setAdapter(scheduleAdapter);

        // Input user data into layout
        patientWelcome.setText("Welcome, " + patient.getName() + "!");
        pcgName.setText(patient.getPrimaryCaregiverName());
        pcgPhone.setText(patient.getPrimaryCaregiverID());

        if (patient.getSchedule().size() > 0) {
            scheduleAdapter.add(patient.getSchedule().get(0).toString());
        } else {
            scheduleAdapter.add("No Readings have been scheduled ..");
        }

        ReadingList readings = patient.getReadings();
        if (readings.size() == 0)
            readingsAdapter.add("No Readings have been taken ..");
        for (int i = 0; i < readings.size(); i++) {
            readingsAdapter.add(readings.get(i).toString());
        }
    }

    private void register() {
    // send user to registration page
        Intent registerSplash = new Intent(this, RegistrationSplash.class);
        startActivity(registerSplash);
        finish() ;
    }

    public void makeCall(View view) {
        makePhoneCall();
    }

    public void makePhoneCall() {
        // phone call shortcut button pressed
        PackageManager pm = getPackageManager();

        // if the user has no calling feature
        if (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Toast.makeText(getApplicationContext(), "Calling Unavailable", Toast.LENGTH_LONG).show();
        } else {
            // user can make a call
            try {
                // make a phone call intent to cg phone number
                Uri number = Uri.parse("tel:" + patient.getPrimaryCaregiverID());
                Intent intent = new Intent(Intent.ACTION_CALL, number);
                try {
                    startActivity(intent);
                } catch (SecurityException e) {

                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Could not make call " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void takeReading(View view) {
        readingPostStart();
    }

    private void readingPostStart() {
        // get connectivity info
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // check if bluetooth is available
        if (networkInfo != null && networkInfo.isConnected()) {
            if (connector.getState() == 2) {
                bluetoothReadingPost(); // take a reading, user is connected to bt device
            } else {
                Toast.makeText(getApplicationContext(), "Could not connect to Bluetooth\nPlease try again.", Toast.LENGTH_SHORT).show();

                checkBT();
                connectBt();
                /*if (btRetry++ < 1) {
                    checkBT();
                    connectBt();
                } else {
                    btRetry = 0;
                    retryCounter = 11;
                }

                if (retryCounter++ < 11) {
                    readingPostStart();
                } else {
                    retryCounter = 0;
                }
                */
            }
        } else {
            System.out.println("No Connection");
        }
    }

    private void readingPostEndBLUGEN(String msg) {
        Log.d(LOG_TAG, msg);

        // string array for response
        String[] bluResponseSplit = msg.split("\\.");

        // sys and dia blood pressure reading
        int rSys = Integer.parseInt(bluResponseSplit[0]);
        int rDia = Integer.parseInt(bluResponseSplit[1].trim());

        // create new reading object and prepare to send to CG
        Reading toReading = new Reading(rSys, rDia, new Date());
        String toSend = GSON.toJson(toReading, Reading.class);

        // save reading to phone data
        ReadingList rl = patient.getReadings();
        rl.add(0, toReading);
        patient.setReadings(rl);
        complexPreferences.putObject("readingList", patient.getReadings());
        complexPreferences.commit();

        // update UI
        updateUIFields();

        // submit reading to CG
        Post post = new Post();
        post.execute(patient.getPrimaryCaregiverID(), toSend);
    }

    private void bluetoothReadingPost() {
        //testTextView.setText("");
        sendMessage("0");
    }

    private void checkBT() {
        // get BT adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if not enabled
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth Disabled", Toast.LENGTH_SHORT).show();

            // request enable
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Null", Toast.LENGTH_SHORT).show();
        }
    }

    public void connectBt() {
        // attempt connection to device
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.d(LOG_TAG, "Connecting to ... " + device);
        // stop trying to discover new devices
        mBluetoothAdapter.cancelDiscovery();
        stopConnection();

        // set up connection
        DeviceData data = new DeviceData(device, "HC-06");
        connector = new DeviceConnector(data, mHandler);
        connector.connect();
    }

    private void sendMessage(String data) {
        // if no data to send, return
        if (data.isEmpty()) return;
        byte[] command = data.getBytes();
        if (isConnected()) {
            connector.write(command); // send data
        }
    }

    private boolean isConnected() {
        // return if device is connected
        return (connector != null) && (connector.getState() == DeviceConnector.STATE_CONNECTED);
    }

    private void stopConnection() {
        // stop the connection
        if (connector != null) {
            connector.stop();
            connector = null;
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

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://marc.example.com.rmcuffv1_patient/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://marc.example.com.rmcuffv1_patient/http/host/path")
        );

        stopConnection();

        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    void appendLog(String message, boolean outgoing) {

        StringBuilder msg = new StringBuilder();

        msg.append(message);
        if (outgoing) msg.append('\n');
        //testTextView.append(msg);

        Log.d(LOG_TAG, "TEST MESSAGE: " + msg);
    }

    private class BluetoothResponseHandler extends Handler {
        // set up handler for response from BT device
        private WeakReference<MainActivity> mActivity;

        public BluetoothResponseHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        public void setTarget(MainActivity target) {
            mActivity.clear();
            mActivity = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    // track state changes in device
                    case MESSAGE_STATE_CHANGE:

                        Utils.log("MESSAGE_STATE_CHANGE: " + msg.arg1);
                        switch (msg.arg1) {
                            case DeviceConnector.STATE_CONNECTED:
                                break;
                            case DeviceConnector.STATE_CONNECTING:
                                break;
                            case DeviceConnector.STATE_NONE:
                                break;
                        }
                        break;

                    // If message recv
                    case MESSAGE_READ:
                        final String readMessage = (String) msg.obj;
                        if (readMessage != null) {
                            activity.appendLog(readMessage, false);
                            readingPostEndBLUGEN(readMessage.toString());
                        }
                        break;

                    case MESSAGE_DEVICE_NAME:
                        // stub
                        break;

                    case MESSAGE_WRITE:
                        // stub
                        break;

                    case MESSAGE_TOAST:
                        // stub
                        break;
                }
            }
        }
    }

    // Disable back button by not calling super
    @Override
    public void onBackPressed() {
        finish();
    }
}