package marc.example.com.rmcuffv1_patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Davidb on 10/8/15.
 */
public class Device
{
    private String deviceID ;                   /* the ID of the device being used */
    private String connectivityState ;          /* the connectivity state the device is in */


    public Device(String deviceID)
    {
        this.deviceID = deviceID ;
        this.connectivityState = null ;
    }

    public boolean connectDevice()
    {
        setConnectivityState("CONNECTED")  ; // If device connected properly
        return true ;

        // later on add false condition
    }

    public Reading runDevice()
    {
        if ( getConnectivityState().equals("CONNECTED") ) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //get current date time with Date()
            Date date = new Date();

            System.out.println(dateFormat.format(date));
            Reading reading = new Reading(89.1, 30.2, date);
            return reading;
        }

        return null ;
    }

    // Get methods

    public String getDeviceID() {
        return deviceID;
    }

    public String getConnectivityState() {
        return connectivityState;
    }

    // Set methods

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setConnectivityState(String connectivityState) {
        this.connectivityState = connectivityState;
    }




}
