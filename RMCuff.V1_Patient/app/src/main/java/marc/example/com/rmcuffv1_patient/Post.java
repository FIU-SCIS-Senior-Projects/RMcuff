package marc.example.com.rmcuffv1_patient;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post extends AsyncTask<String, Void, Void> {
    private static final String DEBUG_TAG = "[POST_CLASS]";

    public Post() {

    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            postReading(params[0], params[1]);
        } catch (IOException e) {
            System.out.println("Unable to retrieve web page. URL may be invalid.");
        }

        return null;
    }

    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public void postReading(String phoneNum, String readingData) throws IOException {
        String myurl = "http://www.davidbaez.com/push.php";
        System.out.println("hey");
        InputStream is = null;
        DataOutputStream out = null;
        HttpURLConnection conn = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();

            String USER_AGENT = "Mozilla/5.0";
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Encoding", "*/*");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setDoOutput(true);

            // For Posting
            String urlParameters = "pushMode=toCaregiver" + "&" + "phoneNum=" + phoneNum + "&" + "reading=" + readingData;
            System.out.println("****************** " + urlParameters);

            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(urlParameters);
            out.flush();
            out.close();

            // Starts the query
            //conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            //return contentAsString;
            System.out.println(contentAsString);

            //conn.disconnect() ;
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}