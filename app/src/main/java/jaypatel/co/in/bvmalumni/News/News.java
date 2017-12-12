package jaypatel.co.in.bvmalumni.News;


import android.app.ProgressDialog;
import android.content.Context;
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
public class News extends Fragment {

    ArrayList<String> heading, detail;
    Context context;
    RecyclerView rv;

    public News() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        setRetainInstance(true);
        context = getContext();
        rv = (RecyclerView) rootView.findViewById(R.id.news_recycler_view);
        rv.setHasFixedSize(true);

        if(savedInstanceState == null) {
            if(Info.isNetworkAvailable(getContext())) {
                heading = new ArrayList<>();
                detail = new ArrayList<>();
                LoadData ld = new LoadData();
                ld.execute();
            }else {
                Info.showNetworkFailuireToast(getContext());
            }
        }else {
            heading = savedInstanceState.getStringArrayList("heading");
            detail = savedInstanceState.getStringArrayList("detail");
            NewsAdapter adapter = new NewsAdapter(heading, detail, context);
            rv.setAdapter(adapter);
        }

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("heading", heading);
        outState.putStringArrayList("detail", detail);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    class LoadData extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog = progressDialog.show(getActivity(), getString(R.string.PleaseWait), getString(R.string.WorkInProgress), true, false);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(Info.url + "LoadNews.php");
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
            String jsonData = s;
            ArrayList<String> h, d;
            h = new ArrayList<>();
            d = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray ja = jsonObject.getJSONArray("News");
                int i =0;
                while(i < ja.length()){
                    JSONObject jo = ja.getJSONObject(i);
                    String headLine = jo.getString("HeadLine");
                    String Detail = jo.getString("Detail");
                    h.add(headLine);
                    d.add(Detail);
                    i++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            heading = h;
            detail = d;
            if(heading.size() == 0) {
                heading.add("No news found");
                detail.add(" ");
            }

            if((this.progressDialog != null) && this.progressDialog.isShowing())
                this.progressDialog.dismiss();
            this.progressDialog = null;
            NewsAdapter adapter = new NewsAdapter(heading, detail, context);
            rv.setAdapter(adapter);

        }
    }

}