package marc.example.com.rmcuffv1_patient.PushPull;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

import com.google.gson.Gson;
import com.pushbots.push.PBNotificationIntent;
import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import marc.example.com.rmcuffv1_patient.Preferences.ComplexPreferences;
import marc.example.com.rmcuffv1_patient.Preferences.ObjectPreference;
import marc.example.com.rmcuffv1_patient.Settings.Schedules.Schedule;
import marc.example.com.rmcuffv1_patient.Settings.Schedules.ScheduleList;

public class CustomHandler extends BroadcastReceiver {
    private static final String TAG = "customHandler";
    private ObjectPreference objectPreference;
    private ComplexPreferences complexPreferences;
    private ScheduleList scheduleList;
    private Gson GSON = new Gson();

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        objectPreference = (ObjectPreference) context.getApplicationContext();
        objectPreference.createNewComplexFile("data");
        complexPreferences = objectPreference.getComplexPreference();

        if (complexPreferences != null) {
            scheduleList = complexPreferences.getObject("scheduleList", ScheduleList.class);
        }

        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
            //Check for Pushbots Instance
            Pushbots pushInstance = Pushbots.sharedInstance();
            if (!pushInstance.isInitialized()) {
                Log.w(TAG, "Initializing Pushbots.");
                Pushbots.sharedInstance().init(context.getApplicationContext());
            }

            //Clear Notification array
            if (PBNotificationIntent.notificationsArray != null) {
                PBNotificationIntent.notificationsArray = null;
            }

            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_OPEN);
            assert PushdataOpen != null;
            final int message = Log.w(TAG, "User clicked notification with Message: " + PushdataOpen.get("message"));

            //Report Opened Push Notification to Pushbots
            if (Pushbots.sharedInstance().isAnalyticsEnabled()) {
                Pushbots.sharedInstance().reportPushOpened((String) PushdataOpen.get("PUSHANALYTICS"));
            }

            //Start lanuch Activity
            String packageName = context.getPackageName();
            Intent resultIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            resultIntent.putExtras(intent.getBundleExtra("pushData"));
            Pushbots.sharedInstance().startActivity(resultIntent);

            // Handle Push Message when received
        } else if (action.equals(PBConstants.EVENT_MSG_RECEIVE)) {
            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_RECEIVE);
            if (PushdataOpen != null) {
                Log.w(TAG, "User Received notification with Message: " + PushdataOpen.get("message"));
            }

            if (PushdataOpen != null && PushdataOpen.containsKey("schedule")) {
                Schedule newSchedule = GSON.fromJson((String) PushdataOpen.get("schedule"), Schedule.class);

                if (scheduleList == null) {
                    // No scheduleList exists yet
                    // Create new ArrayList
                    scheduleList = new ScheduleList();
                }

                scheduleList.add(0, newSchedule);
                System.out.println(scheduleList.get(0).getDate());

                // UNCOMMENT THIS ONCE YOU WANT TO START SAVING
                complexPreferences.putObject("scheduleList", scheduleList);
                complexPreferences.commit();


                // Save Schedule to calendar event
                long startMillis = 0;
                long endMillis = 0;

                Calendar beginTime = Calendar.getInstance();
                beginTime.setTime(newSchedule.getDate());

                Calendar endTime = Calendar.getInstance();
                Date endDate = newSchedule.getDate();
                endDate.setMinutes(endDate.getMinutes() + 5);
                endTime.setTime(endDate);
                //endMillis = endTime.getTimeInMillis();

                // set up calendar intent, using all the date data received
                Intent i = new Intent(Intent.ACTION_INSERT)
                        .setData(Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(Events.TITLE, "BP Reading")
                        .putExtra(Events.DESCRIPTION, "Take your blood pressure");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }
}