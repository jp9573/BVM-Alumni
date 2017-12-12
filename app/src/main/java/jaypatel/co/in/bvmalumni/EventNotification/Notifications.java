package jaypatel.co.in.bvmalumni.EventNotification;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import jaypatel.co.in.bvmalumni.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications extends Fragment {

    ArrayList<String> message, body, detail;
    RecyclerView rv;

    public Notifications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        setRetainInstance(true);
        rv = (RecyclerView) rootView.findViewById(R.id.notification_recycler_view);
        rv.setHasFixedSize(true);

        if(savedInstanceState == null) {
            if(Info.isNetworkAvailable(getContext())) {
                message = new ArrayList<>();
                body = new ArrayList<>();
                detail = new ArrayList<>();
                LoadData ld = new LoadData();
                ld.execute();
            }else {
                Info.showNetworkFailuireToast(getContext());
            }
        }else {
            message = savedInstanceState.getStringArrayList("heading");
            body = savedInstanceState.getStringArrayList("body");
            detail = savedInstanceState.getStringArrayList("detail");
            NotificationAdapter adapter = new NotificationAdapter(message, body, detail);
            rv.setAdapter(adapter);
        }

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("heading", message);
        outState.putStringArrayList("body", body);
        outState.putStringArrayList("detail", detail);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    class LoadData extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog = progressDialog.show(getActivity(), getString(R.string.PleaseWait), "Loading Notifications!", true, false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(Info.url + "LoadNotifications.php");
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
            //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            String jsonData = s;
            ArrayList<String> h, b, d;
            h = new ArrayList<>();
            b = new ArrayList<>();
            d = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray ja = jsonObject.getJSONArray("Message");
                int i =0;
                while(i < ja.length()){
                    JSONObject jo = ja.getJSONObject(i);
                    String headLine = jo.getString("HeadLine");
                    String body = jo.getString("Body");
                    String Detail = jo.getString("Detail");
                    h.add(headLine);
                    b.add(body);
                    d.add(Detail);
                    i++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            message = h;
            body = b;
            detail = d;
            progressDialog.dismiss();
            if(message.size() == 0) {
                message.add("Upcoming Notifications will be dispay over here!");
                detail.add(" ");
                b.add(" ");
            }
            NotificationAdapter adapter = new NotificationAdapter(message, body, detail);
            rv.setAdapter(adapter);

        }
    }
}
