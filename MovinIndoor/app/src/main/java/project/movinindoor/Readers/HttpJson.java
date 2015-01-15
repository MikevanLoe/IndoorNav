package project.movinindoor.Readers;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

/**
 * Created by Thomas on 24-11-2014.
 */

public class HttpJson extends AsyncTask<String, String, JSONArray> {

    private JSONArray json = null;

    public JSONArray doInBackground(String... url) {

        try {
            JSONArray jsonobj = new JSONArray();

            // Setup connection
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(url[0]);
            StringEntity se = new StringEntity(jsonobj.toString());
            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httppostreq.setEntity(se);
            HttpResponse httpresponse = httpclient.execute(httppostreq);

            // Retrieve json information
            String responseText = null;
            responseText = EntityUtils.toString(httpresponse.getEntity());
            json = new JSONArray(responseText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    protected void onPreExecute() {

    }
}