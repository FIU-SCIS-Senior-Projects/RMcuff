package com.example.marc.rmcuffv1.PushPull;

/**
 * Created by Davidb on 10/22/15.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.marc.rmcuffv1.Preferences.ComplexPreferences;
import com.example.marc.rmcuffv1.Preferences.ObjectPreference;
import com.example.marc.rmcuffv1.Settings.Reading.Reading;
import com.example.marc.rmcuffv1.Settings.Reading.ReadingList;
import com.google.gson.Gson;
import com.pushbots.push.PBNotificationIntent;
import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;

import java.util.HashMap;


/**
 * Created by Davidb on 10/19/15.
 */
public class CustomHandler extends BroadcastReceiver
{
    private static final String TAG = "customHandler";

    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;

    private ReadingList readingList ;
    private Gson GSON = new Gson();

    @Override
    public void onReceive(Context context, Intent intent)
    {

        String action = intent.getAction();
        objectPreference = (ObjectPreference) context.getApplicationContext() ;
        objectPreference.createNewComplexFile("data");
        complexPreferences = objectPreference.getComplexPreference();

        if( complexPreferences != null) {
            readingList = complexPreferences.getObject("readingList", ReadingList.class) ;
        }

        Log.d(TAG, "action=" + action);

        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
            //Check for Pushbots Instance
            Pushbots pushInstance = Pushbots.sharedInstance();
            if(!pushInstance.isInitialized()){
                Log.w(TAG, "Initializing Pushbots.");
                Pushbots.sharedInstance().init(context.getApplicationContext());
            }

            //Clear Notification array
            if(PBNotificationIntent.notificationsArray != null){
                PBNotificationIntent.notificationsArray = null;
            }

            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_OPEN);
            Log.w(TAG, "User clicked notification with Message: " + PushdataOpen.get("message"));

            //Report Opened Push Notification to Pushbots
            if(Pushbots.sharedInstance().isAnalyticsEnabled()){
                Pushbots.sharedInstance().reportPushOpened( (String) PushdataOpen.get("PUSHANALYTICS"));
            }

            //Start lanuch Activity
            String packageName = context.getPackageName();
            Intent resultIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

            resultIntent.putExtras(intent.getBundleExtra("pushData"));
            Pushbots.sharedInstance().startActivity(resultIntent);

            // Handle Push Message when received
        }else if(action.equals(PBConstants.EVENT_MSG_RECEIVE)){

            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_RECEIVE);
            Log.w(TAG, "User Received notification with Message: " + PushdataOpen.get("message")) ;
            //System.out.println(PushdataOpen.toString()) ;

            if( PushdataOpen != null && PushdataOpen.containsKey("reading"))
            {

                Reading newReading = GSON.fromJson((String)PushdataOpen.get("reading"), Reading.class) ;

                if (readingList == null)
                {
                    // No scheduleList exists yet
                    // Create new ArrayList
                    readingList = new ReadingList() ;
                }

                readingList.add(0, newReading) ;
                System.out.println(readingList.get(0).getDate()) ;

                // UNCOMMENT THIS ONCE YOU WANT TO START SAVING
                complexPreferences.putObject("readingList", readingList);
                complexPreferences.commit();
            }
        }
    }
}
