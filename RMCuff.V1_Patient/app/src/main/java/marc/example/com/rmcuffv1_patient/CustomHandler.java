package marc.example.com.rmcuffv1_patient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
    private ScheduleList scheduleList ;
    private Gson GSON = new Gson();

    @Override
    public void onReceive(Context context, Intent intent)
    {

        String action = intent.getAction();
        objectPreference = (ObjectPreference) context.getApplicationContext() ;
        objectPreference.createNewComplexFile("data");
        complexPreferences = objectPreference.getComplexPreference();

        System.out.println("XXX WORKS") ;


        if( complexPreferences != null) {
            System.out.println("XXX2 WORKS2NEW") ;
            scheduleList = complexPreferences.getObject("scheduleList", ScheduleList.class) ;
        }


        Log.d(TAG, "action=" + action);
        System.out.println("$$$$$$$$$$$$$") ;

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
            System.out.println("@@@@@@@@@@@@@@@2") ;

            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_RECEIVE);
            Log.w(TAG, "#######User Received notification with Message: " + PushdataOpen.get("message")) ;
            //System.out.println(PushdataOpen.toString()) ;

            if( PushdataOpen != null && PushdataOpen.containsKey("schedule"))
            {
                System.out.println("%%%%%%% " + PushdataOpen.get("schedule"));

                Schedule newSchedule = GSON.fromJson((String)PushdataOpen.get("schedule"), Schedule.class) ;

                if (scheduleList == null)
                {
                    // No scheduleList exists yet
                    // Create new ArrayList
                    scheduleList = new ScheduleList() ;
                }

                scheduleList.add(0, newSchedule) ;
                System.out.println(scheduleList.get(0).getDate()) ;

                // UNCOMMENT THIS ONCE YOU WANT TO START SAVING
                complexPreferences.putObject("scheduleList", scheduleList);
                complexPreferences.commit();
                System.out.println("XXX3 WORKS3") ;
            }
        }
    }
}
