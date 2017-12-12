package jaypatel.co.in.bvmalumni.Folder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jaypatel.co.in.bvmalumni.Gallery.MainActivityGallery;
import jaypatel.co.in.bvmalumni.MainActivity;
import jaypatel.co.in.bvmalumni.R;

public class FolderMainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "https://firebasestorage.googleapis.com/v0/b/bvm-alumni.appspot.com/o/folders.json?alt=media&token=7ffa3b34-2135-48cc-8031-b61d27861a10";

    ArrayList<HashMap<String, String>> folerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_main);
        setTitle("Gallery");
        folerList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list_gallery);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(FolderMainActivity.this, ""+i, Toast.LENGTH_SHORT).show();
                HashMap folderSelect = (HashMap) adapterView.getItemAtPosition(i);
                String linksGot = folderSelect.get("links").toString();
                Intent intent = new Intent(FolderMainActivity.this,MainActivityGallery.class);
                intent.putExtra("links",linksGot);
                startActivity(intent);
            }
        });

        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call_old
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FolderMainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            jsonStr.replaceAll("\n","");
            jsonStr.replaceAll(" ","");
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray folders = jsonObj.getJSONArray("folders");

                    // looping through All Contacts
                    for (int i = 0; i < folders.length(); i++) {
                        JSONObject c = folders.getJSONObject(i);

                        String id = c.getString("id");
                        String details = c.getString("details");
                        String links = c.getString("links");

                        // tmp hash map for single contact
                        HashMap<String, String> folder = new HashMap<>();

                        // adding each child node to HashMap key => value
                        folder.put("id", id);
                        folder.put("details", details);
                        folder.put("links", links);

                        // adding contact to contact list
                        folerList.add(folder);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    FolderMainActivity.this, folerList,
                    R.layout.list_item_folder, new String[]{"id", "details"}, new int[]{R.id.name_folder, R.id.details});
            lv.setAdapter(adapter);
            lv.setDividerHeight(0);
        }

    }
}

