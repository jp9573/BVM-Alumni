package jaypatel.co.in.bvmalumni.Suggestions;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import jaypatel.co.in.bvmalumni.Info;
import jaypatel.co.in.bvmalumni.News.NewsAdapter;
import jaypatel.co.in.bvmalumni.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Suggestions extends Fragment {

    Button send;
    EditText suggestionTxt;
    Context context;

    public Suggestions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_suggestions, container, false);

        send = (Button) v.findViewById(R.id.sendSuggestion);
        suggestionTxt = (EditText) v.findViewById(R.id.suggestion);
        context = getContext();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = suggestionTxt.getText().toString().trim();
                if(msg.length() > 0) {
                    Info info = new Info(context);
                    String email = info.dataBase.getCurrentEmail();
                    String name = info.dataBase.getCurrentUserName();
                    String mobile = info.dataBase.getMobile();
                    SendData obj = new SendData();
                    obj.execute(msg,email,name,mobile);
                }else {
                    //Info.showToast(context, "Please write a suggestion first!");
                    suggestionTxt.setError("Please write a suggestion first");
                }
            }
        });

        return v;
    }

    class SendData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog = progressDialog.show(getActivity(), getString(R.string.PleaseWait), "Saving your valuable suggestion", true, false);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String data = params[0];
                String strEmail = params[1];
                String strName = params[2];
                String strMobile = params[3];

                URL url = new URL(Info.url + "InsertSuggestion.php?data="+data+"&email=" + strEmail + "&name=" + strName +
                        "&mobile=" + strMobile);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Done")) {
                suggestionTxt.setText("");
                Info.showSnackBar(getView(), "Suggestion successfully saved! Thank you.");
            }else {
                Info.showToast(context, "Something going wrong");
            }
            if((this.progressDialog != null) && this.progressDialog.isShowing())
                this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }
}
