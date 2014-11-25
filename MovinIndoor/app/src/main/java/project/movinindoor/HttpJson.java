package project.movinindoor;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thomas on 24-11-2014.
 */

public class HttpJson extends AsyncTask<String, String, JSONArray> {

    public JSONArray doInBackground(String... url){
        JSONArray json = null;

        try {
            JSONArray jsonobj = new JSONArray();
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(url[0]);
            StringEntity se = new StringEntity(jsonobj.toString());
            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            httppostreq.setEntity(se);
            HttpResponse httpresponse = httpclient.execute(httppostreq);

            String responseText = null;
            responseText = EntityUtils.toString(httpresponse.getEntity());
            json = new JSONArray(responseText);
            Log.e("log_tag", "Geen error: " + json);

            int id;
            String description;
            int floor;
            int priority;
            String status;
            String building;

            for (int i = 0; i < json.length(); i++) {
                JSONObject row = json.getJSONObject(i);
                id = row.getInt("ID");
                description = row.getString("Description");
                floor = row.getInt("Floor");
                priority = row.getInt("Priority");
                status = row.getString("Status");
                building = row.getString("Building");

                Log.e("log_tag", "ID: " + id);
                Log.e("log_tag", "Description: " + description);
            }
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error: " + e.toString());
        }
        return json;
    }

    protected void onPreExecute(){

    }

    protected void onProgressUpdate(){

    }

    protected void onPostExecute(){

    }
}
