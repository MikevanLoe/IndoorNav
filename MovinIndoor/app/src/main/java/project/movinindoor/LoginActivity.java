package project.movinindoor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import project.movinindoor.Hashing.md5;
import project.movinindoor.Readers.HttpJson;
import project.movinindoor.Readers.HttpJsonLogin;


public class LoginActivity extends Activity {

    Button btnLogin;
    EditText textEmail;
    EditText textPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
}
