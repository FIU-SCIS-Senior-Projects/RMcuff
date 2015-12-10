<?php
echo "YES" ;

require_once('PushBots.class.php') ;

$phoneNum = $_POST['phoneNum'] ;
$pushMode = $_POST['pushMode'] ;

if ($phoneNum && $pushMode)
{
    // push from caregiver to patient
    // Use patient app ID and secret since sending to them
    if($pushMode === 'toPatient') 
    {
        $scheduleData = $_POST['schedule'] ;
        
        $pb = new PushBots();

        $appID = '561bec6c177959df6d8b4569' ;
        $appSecret = '39f91bedbe10b3b4293e8119f4e188fd' ;

        $pb->App($appID, $appSecret) ;
        $pb->Alias($phoneNum) ;

        $pb->Alert("Your Caregiver Scheduled a BP Reading!") ;
        $pb->Platform("1") ;
        
        // Sending actual Schedule Data
        if($scheduleData)
        {
            $customfields= array("schedule" => $scheduleData);
            $pb->Payload($customfields) ;
        }
        // $customfields= array("schedule" => "SCHEDULE_TEXT_DATA");
        // $pb->Payload($customfields);
        
        $pb->Push();
    }
    // push from patient to caregiver
    // Use caregiver app ID and secret since sending to them
    elseif($pushMode === 'toCaregiver')
    {
        $readingData = $_POST['reading'] ;
        
        $pb = new PushBots();

        $appID = '561bed9e177959566f8b4567' ;
        $appSecret = '17dd35aa71364267e35c4287592ede70' ;

        $pb->App($appID, $appSecret) ;
        $pb->Alias($phoneNum) ;

        $pb->Alert("Your Patient Just took a reading!") ;
        $pb->Platform("1") ;
        
        // Sending actual Reading Data
        if($readingData)
        {
            $customfields= array("reading" => $readingData);
            $pb->Payload($customfields);
        }
        // $customfields= array("reading" => "READING_TEXT_DATA");
        // $pb->Payload($customfields);
        
        $pb->Push();
    }
    
    
}

?>
