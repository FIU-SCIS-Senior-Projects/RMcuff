package marc.example.com.rmcuffv1_patient;

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

        System.out.println("XXX WORKS");

        if (complexPreferences != null) {
            System.out.println("XXX2 WORKS2NEW");
            scheduleList = complexPreferences.getObject("scheduleList", ScheduleList.class);
        }

        Log.d(TAG, "action=" + action);
        System.out.println("$$$$$$$$$$$$$");

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
            System.out.println("@@@@@@@@@@@@@@@2");

            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_RECEIVE);
            if (PushdataOpen != null) {
                Log.w(TAG, "#######User Received notification with Message: " + PushdataOpen.get("message"));
            }
            //System.out.println(PushdataOpen.toString()) ;

            if (PushdataOpen != null && PushdataOpen.containsKey("schedule")) {
                System.out.println("%%%%%%% " + PushdataOpen.get("schedule"));

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

                // Schedule has been saved, now set the alarm
                /*
                Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()) ;

                cal.setTime(newSchedule.getDate()) ;
                System.out.println("+++ " + cal.getTime()) ;

                Intent i = new Intent(context, MainActivity.class) ;
                PendingIntent pendingIntent = PendingIntent.getService(context, 0, i, 0) ;

                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE) ;
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


                System.out.println("Alarm Set") ;
                System.out.println("********") ;
                */
                // Get a handler that can be used to post to the main thread

                //Handler mainHandler = new Handler(context.getMainLooper()) ;
                //Runnable myRunnable = new Runnable() {
                //@Override
                //public void run()
                //{
                /*
                        long startMillis = 0 ;
                        long endMillis = 0;

                        Calendar beginTime = Calendar.getInstance() ;
                        beginTime.setTime(s.getDate()) ;
                        startMillis = beginTime.getTimeInMillis() ;

                        Calendar endTime = Calendar.getInstance();
                        Date endDate = s.getDate() ;
                        endDate.setMinutes(endDate.getMinutes() + 5) ;
                        endTime.setTime(endDate);
                        endMillis = endTime.getTimeInMillis();


                        long calID = 1;
                        ContentResolver cr = context.getContentResolver();
                        ContentValues values = new ContentValues();
                        values.put(Events.DTSTART, startMillis);
                        values.put(Events.DTEND, endMillis);
                        values.put(Events.TITLE, "BP Reading");
                        values.put(Events.DESCRIPTION, "Please open the RMCuff App to take your scheduled reading :)");

                        values.put(Events.CALENDAR_ID, calID);

                        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
                        //values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                        Uri uri = cr.insert(Events.CONTENT_URI, values);

                        long eventID = Long.parseLong(uri.getLastPathSegment());
                        */
                //Calendar beginTime = Calendar.getInstance();
                // beginTime.set(2016, 0, 19, 7, 30);
                //Calendar endTime = Calendar.getInstance();
                //endTime.set(2016, 0, 19, 8, 30);

                long startMillis = 0;
                long endMillis = 0;

                Calendar beginTime = Calendar.getInstance();
                beginTime.setTime(newSchedule.getDate());
                //startMillis = beginTime.getTimeInMillis() ;

                Calendar endTime = Calendar.getInstance();
                Date endDate = newSchedule.getDate();
                endDate.setMinutes(endDate.getMinutes() + 5);
                endTime.setTime(endDate);
                //endMillis = endTime.getTimeInMillis();

                Intent i = new Intent(Intent.ACTION_INSERT)
                        .setData(Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(Events.TITLE, "BP Reading")
                        .putExtra(Events.DESCRIPTION, "Take your blood pressure");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                //Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);

                //values.put(CalendarContract.Reminders.MINUTES, 2);
                //values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                // } // This is your code
                //};
                //mainHandler.post(myRunnable);
            }
        }
    }
}