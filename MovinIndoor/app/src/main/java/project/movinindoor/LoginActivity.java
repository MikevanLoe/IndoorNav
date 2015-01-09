package project.movinindoor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.movinindoor.Hashing.md5;
import project.movinindoor.Readers.HttpJsonLogin;


public class LoginActivity extends Activity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    Button btnLogin;
    EditText textEmail;
    EditText textPassword;

    Intent Loginintent;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "607567241847";

    int loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Loginintent = new Intent(this, MapsActivity.class);

        prefs = getSharedPreferences("Login", MODE_PRIVATE);
        editor = prefs.edit();

        if(!checkConnection())
        {
            Toast toastcon = Toast.makeText(getApplicationContext(), "You need an internet connection for this app.", Toast.LENGTH_LONG);
            toastcon.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        getActionBar().hide();

        textEmail = (EditText)findViewById(R.id.etUserName);
        textPassword = (EditText)findViewById(R.id.etPass);
        btnLogin = (Button)findViewById(R.id.btnSingIn);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnSignIn(View view)
    {
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();
        boolean error = false;

        if(email.equals("") && password.equals(""))
        {
            Toast toastemailpass = Toast.makeText(getApplicationContext(), "Your email and or password cannot be empty.", Toast.LENGTH_SHORT);
            toastemailpass.show();
            error = true;

        }
        if(!isValidEmail(email))
        {
            Toast toastvalidemail = Toast.makeText(getApplicationContext(), "Your email is invalid.", Toast.LENGTH_SHORT);
            toastvalidemail.show();
            error = true;
        }
        if(email.length() > 254 )
        {
            Toast toastvalidemaill = Toast.makeText(getApplicationContext(), "Your email cant be that long.", Toast.LENGTH_SHORT);
            toastvalidemaill.show();
            error = true;
        }
        if(password.length() > 254)
        {
            Toast toastvalidemail = Toast.makeText(getApplicationContext(), "Your password is too long.", Toast.LENGTH_SHORT);
            toastvalidemail.show();
            error = true;
        }
        if(error == false)
        {
            String salt = "ofzhWAF1VsnkqVmCrD6V";

            String hashedpassword = md5.md5(textPassword.getText().toString() + salt);

            try {
                int userinfo = new HttpJsonLogin().execute("http://movin.nvrstt.nl/mobilelogin.php", textEmail.getText().toString(), hashedpassword).get();

                if(userinfo < 0)
                {
                    Toast toastinvalidinfo = Toast.makeText(getApplicationContext(), "Invalid login credentials", Toast.LENGTH_LONG);
                    toastinvalidinfo.show();
                }
                else
                {
                    Toast toastloggedin = Toast.makeText(getApplicationContext(), "Login succesful", Toast.LENGTH_LONG);
                    toastloggedin.show();

                    getRegId(userinfo);
                    Log.d("beforeid", ""+userinfo);
                    editor.putInt("LoggedIn", 1);
                    editor.putInt("UserID", userinfo);
                    editor.commit();

                    this.loggedIn = prefs.getInt("LoggedIn", -1);

                    Intent Loginintent = new Intent(this, MapsActivity.class);
                    startActivity(Loginintent);
                    finish();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();

            }
        }

        /*
        Toast toast = Toast.makeText(getApplicationContext(), "email: " + email + " Password " + password, Toast.LENGTH_SHORT);
        toast.show();
        Intent Loginintent = new Intent(this, MapsActivity.class);
        startActivity(Loginintent);
        finish(); */
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean checkConnection()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getRegId(final int userid) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    try {
                        regid = gcm.register(PROJECT_NUMBER);
                        Log.d("regid", regid);

                    } catch (NullPointerException e) {
                    }
                    msg = "Device registered, registration ID=" + regid;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://movin.nvrstt.nl/registrateid.php");

                    try {
                        // Add your data
                        List<NameValuePair> nameValuePairs = new ArrayList<>();
                        nameValuePairs.add(new BasicNameValuePair("registrationid", regid));
                        nameValuePairs.add(new BasicNameValuePair("userid", "" + userid));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        // Execute HTTP Post Request
                        HttpResponse response = httpclient.execute(httppost);

                    } catch (ClientProtocolException e) {
                    } catch (IOException e) {
                    }

                    // AsyncTask<String, String, String> registrationid = PostRequest.execute("http://movin.nvrstt.nl/registrateid.php", "registrationid", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }

                return msg;
            }


        }.execute(null, null, null);
    }
}