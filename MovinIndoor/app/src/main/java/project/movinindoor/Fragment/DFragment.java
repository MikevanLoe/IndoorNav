package project.movinindoor.Fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import project.movinindoor.R;

/**
 * Created by Davey on 17-12-2014.
 */
public class DFragment extends DialogFragment {
    private boolean m_status;
   // private dialogDoneListener mListener;

    private EditText editText;
    private String text = "";
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment, container, false);

        Button Cancel= (Button)
                rootView.findViewById(R.id.Cancel);
        Cancel.setOnClickListener(onCancel);
        Button OK= (Button) rootView.findViewById(R.id.OK);
        OK.setOnClickListener(onOK);

        editText = (EditText) rootView.findViewById(R.id.editTextComment);
        editText.setText(text);

        getDialog().setTitle("Edit comment");
        // Do something else
        return rootView;
    }

    View.OnClickListener onCancel=
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    m_status=false;
                    //mListener.onDone(m_status);
                    dismiss();
                }
            };

    View.OnClickListener onOK=
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            try {
                                HttpClient httpclient = new DefaultHttpClient();
                                URI uri = new URI("http", "movin.nvrstt.nl", "/commentdefect.php?defectid=" + id + "&comment="+editText.getText().toString(), null);
                                URL url = uri.toURL();
                                HttpGet httpget = new HttpGet(url.toString().replace("%3F", "?"));
                                Log.i("CommentURl: ", url.toString().replace("%3F", "?"));
                                HttpResponse response = httpclient.execute(httpget);
                            } catch (ClientProtocolException e) {
                                Log.i("Set Comment", "ClientProtocol");
                            } catch (MalformedURLException u) {
                                Log.i("Set Comment", "URL chrash");
                            } catch (IOException e) {
                                Log.i("Set Comment", "IOException");
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            return "";
                        }
                    }.execute(null,null,null);
                    m_status=true;
                    //mListener.onDone(m_status);
                    dismiss();
                }
            };

    public void setEditText(String text) {
        this.text = text;
    }

    public void setRepairId(String text) {
        this.id = text;
    }

    /*

    p
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (dialogDoneListener) activity;
        } catch (ClassCastException e) {
            mListener = (dialogDoneListener) new dialogDoneListener() {
                @Override
                public void onDone(boolean state) {

                }
            };
           // throw new ClassCastException(activity.toString()
           //         + " must implement dialogDoneistener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface dialogDoneListener{
        void onDone(boolean state);
    }
    */
}


