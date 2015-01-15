package project.movinindoor.Readers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 8-1-2015.
 */
public class HttpJsonLogin extends AsyncTask<String, String, Integer> {

    private Integer userid;

    protected Integer doInBackground(String... url) {

        try {
            // Setup connection
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(url[0]);

            //Add data for post
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", url[1]));
            nameValuePairs.add(new BasicNameValuePair("password", url[2]));
            httppostreq.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse httpresponse = httpclient.execute(httppostreq);

            // Retrieve json information
            String responseText = null;
            responseText = EntityUtils.toString(httpresponse.getEntity());

            JSONObject userIDobj = new JSONObject(responseText);

            userid = userIDobj.getInt("userid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userid;
    }

    protected void onPreExecute() {

    }
}