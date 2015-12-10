# RMcuff

RMcuff is a new system for joint effort tracking of blood pressure between a patient, a primary caregiver and secondary caregivers has been constructed in software and outlined in detail in the writing that follows. The mobility of cellular devices, Android devices in this case, is leveraged to provide reliable remote monitoring of patient blood pressure activity. The software works in combination with a bluetooth operated blood pressure cuff that is currently in development.

# Installation:
DavidBaez.com/RMCaregiver.apk for the caregiver application.

DavidBaez.com/RMPatient.apk for the patient application.

<a href="http://www.youtube.com/watch?feature=player_embedded&v=2Y7lIfIoHTM
" target="_blank"><img src="http://img.youtube.com/vi/2Y7lIfIoHTM/0.jpg" 
alt="Install" width="240" height="180" border="10" /></a>

Both applications need to be downloaded from the android device that it will be installed on as the app is currently not in the appstore.
Once both applications are downloaded you will have the ability to install the respective application.

# Introduction
In the introduction that follows, the system will be dissected in depth in terms of what it is like prior to a software solution or the intended software solution and what the system will become after the intended software solution is set in place. The section is the first phase of planning required to implement a new system. This section will describe the current system in place and the new system to be built in detail. In addition, this section will enumerate the steps, in units of User Stories, required to achieve the intended system.

# Current System
Since the advent of wearable technology, health applications have become extremely popular and have flooded mobile device application stores. Most of these health applications are geared around tracking fitness statistics such as distance traveled and calories burned for adults and young adults. The feature complexity and user interfaces of many of these applications are geared towards a younger audience who grew up with and are savvy of, technology. Applications for tracking blood pressure in older adults such as iHealth exist, but they fail to remain minimal and simple enough for the people who desperately need the service, the elderly, to use easily. They also lack functionality for the people who care after the blood pressure cuff user to track results and set blood pressure schedules. As an example, iHealth keeps track of blood pressure, weight, calories consumed and burned, sleep, and so on. It provides diary and goal tracking features. The excess of functionality leads to use complexity and user interface complexity. Leaving elderly users with nowhere to turn. The features are meant for self tracking, which isn’t very ideal for elderly individuals who lack the self motivation required for the mundane task of tracking blood pressure, and require the help of a related primary caregiver to stay on top of things. 

In the current system, a primary caregiver takes the responsibility of tracking the blood pressure of a patient, usually a loved one, over the phone with a few occasional personal visits. Typically they would tell the patient to take their blood pressure at a certain time, and just have to take their word for it. The patient and caregiver largely communicate over the phone, and a patient is able to lie about reading outcomes, to longer support negative habits. It requires a lot of responsibility on the patient side and a lot of trust on the primary caregiver side.

# Purpose of New System
The new system has one primary purpose, and that is accessibility for elderly, less tech savvy adults. The big difference, is that this new system gives the primary caregiver the power to schedule and track readings to motivate the patient remotely via their cell phone. It is a minimized application that provides blood pressure monitoring and only features directly related to that, thus avoiding overcomplexity. Less functionality also means a clean user interface, so users could easily and quickly navigate the application with little to no instruction. The new system will send the patient’s primary caregiver blood pressure reports via a push notification to a separate primary caregiver application. The primary caregiver can then send secondary caregivers text message logs of the patient’s blood pressure. Text messages make the transfer of information immediate and more likely to be read quickly. No need for secondary caregivers to download an app, and no email reports that rarely get read, just simplicity. The point is to do one thing very well. 

# Code Directory
* RMCuff/
  * Server-Code/
  	* PushBots.class.php 	-- Push bot API code
  	* push.php -- Push bot API push methods
  * RMCuff.V1/
    * app/
      * src/
        * main/
          * rmcuffv1/
            - MainActivity.java 	-- Main activity page
	        - RegistrationPage.java 	-- Registration activity
            - RegistrationSplash.java -- Splash screen
            * Caregiver/  -- Contains information regarding the caregiver class structure
              - Caregiver.java 	-- Caregiver class
	          - CaregiverList.java 	-- Caregiver list class
	          - CaregiversPage.java 	-- Activity to add, delete, modify caregivers
	          - CustomArrayAdapter.java 	-- Array adapter for displaying caregivers
	          - NewCaregiverPage.java 	-- Activity for registering new secondary caregiver
	          - PrimaryCaregiver.java -- Primary caregiver class
            * Preferences/
              - ComplexPreferences.java  -- Containts methods for manipulating our serialized preferences
	          - ObjectPreference.java  --  GSON method of serializing classes and objects
            * PushPull/
              - CustomHandler.java 	-- Handler for managing pushbot API
              - Post.java  -- Part of the pushbot API
            * Settings/
              - Patient/
              - MyPatient.java  --  Patient class
            * Reading/
              - Reading.java  -- Reading class
              - ReadingList.java -- Reading list for displaying readings to app
            * Schedule/
	          - NewSchedulePage.java 	-- Activity for setting new schedule
	          - Schedule.java 	-- Schedule class
	          - ScheduleList.java  -- Schedule list for displaying schedules to app
 * RMCuff.V1_Patient/
   * app/
       * src/
         * main/
           * rmcuffv1/
              - MainActivity.java 	-- Main activity page
              - RegistrationPage.java 	-- Registration activity
              - RegistrationSplash.java -- Splash screen
              - Settings.Java  -- Opens a settings page for the patient (NOT FUNCTIONING)
              * BluetoothTools/
              	- BluetoothUtils.java 	-- Contains utilities for bluetooth device identification
              	- Const.java 	-- Containts bluetooth connection constants
              	- DeviceConnector.java 	-- Main handler for the bluetooth communication
              	- DeviceData.java 	-- Device data class
              	- Utils.java -- More utils for bluetooth communication
              * Preferences/
                - ComplexPreferences.java  -- Containts methods for manipulating our serialized preferences
  	            - ObjectPreference.java  --  GSON method of serializing classes and objects
              * PushPull/
                - CustomHandler.java 	-- Handler for managing pushbot API
                - Post.java  -- Part of the pushbot API
              * Settings/
                - Patient/
                - MyPatient.java  --  Patient class
              * Reading/
                - Reading.java  -- Reading class
                - ReadingList.java -- Reading list for displaying readings to app
              * Schedule/
  	            - Schedule.java 	-- Schedule class
  	            - ScheduleList.java  -- Schedule list for displaying schedules to app
