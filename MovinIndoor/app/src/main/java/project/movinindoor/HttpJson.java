package project.movinindoor;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Thomas on 24-11-2014.
 */

public class HttpJson {

    InputStream is;

    public void getHttpJson(){

        String result = "";

        // http post
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://movin.nvrstt.nl/defectsjson.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            this.is = entity.getContent();
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error in http connection " +e.toString());
        }

        // convert response to string
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        // parse json data
        try
        {
            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Log.i("log_tag", "id: " + json_data.getInt("ID") +
                       ", Description: " + json_data.getString("Buiding") +
                       ", Floor: " + json_data.getInt("Floor") +
                       ", Priority: " + json_data.getInt("Priority") +
                       ", Status: " + json_data.getString("Status")
                );
            }

        }
        catch(JSONException e)
        {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }
}
