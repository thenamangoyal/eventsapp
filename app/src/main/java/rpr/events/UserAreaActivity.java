package rpr.events;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAreaActivity extends AppCompatActivity {

    private static final String TAG = UserAreaActivity.class.getSimpleName();
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DETAILS = "details";

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get events JSON
    private static String ListURL = "http://10.1.1.19/~2015csb1021/php/ListAll.php";

    ArrayList<HashMap<String, String>> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final TextView welcomMessage = (TextView) findViewById(R.id.tvWelcomMsg);
        final Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String username = intent.getStringExtra("username");
        final int user_id = intent.getIntExtra("user_id",-1);
        int age = intent.getIntExtra("age", -1);

        String message = name + " welcome.";
        welcomMessage.setText(message);
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.addNewFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent addEventIndent = new Intent(UserAreaActivity.this, NewEventActivity.class);
                addEventIndent.putExtra("name", name);
                addEventIndent.putExtra("user_id", user_id);
                addEventIndent.putExtra("username", username);
                UserAreaActivity.this.startActivity(addEventIndent);
            }
        });

        eventList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listView);

        new Getevents().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class Getevents extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UserAreaActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(ListURL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray events = jsonObj.getJSONArray("events");

                    // looping through All events
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject c = events.getJSONObject(i);

                        String event_id = c.getString(TAG_EVENT_ID);
                        String name = c.getString(TAG_NAME);
                        String time = c.getString(TAG_TIME);
                        String venue = c.getString(TAG_VENUE);
                        String details = c.getString(TAG_DETAILS);

                        // tmp hash map for single event
                        HashMap<String, String> event = new HashMap<>();

                        // adding each child node to HashMap key => value
                        event.put(TAG_EVENT_ID, event_id);
                        event.put(TAG_NAME, name);
                        event.put(TAG_TIME, time);
                        event.put(TAG_VENUE, venue);
                        event.put(TAG_DETAILS, details);

                        // adding event to event list
                        eventList.add(event);
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
                    UserAreaActivity.this, eventList,
                    R.layout.event_list_item, new String[]{TAG_NAME, TAG_TIME, TAG_VENUE},
                    new int[]{R.id.name, R.id.time, R.id.venue});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    HashMap<String,String> map =(HashMap<String,String>)parent.getItemAtPosition(position);
                    String event_id  = (String)map.get(TAG_EVENT_ID);
                    String name = (String)map.get(TAG_NAME);
                    String time = (String)map.get(TAG_TIME);
                    String venue = (String)map.get(TAG_VENUE);
                    String details = (String)map.get(TAG_DETAILS);
                    Intent intent = new Intent(UserAreaActivity.this, EventDisplayUser.class);
                    intent.putExtra(TAG_EVENT_ID, event_id);
                    intent.putExtra(TAG_NAME, name);
                    intent.putExtra(TAG_TIME, time);
                    intent.putExtra(TAG_VENUE, venue);
                    intent.putExtra(TAG_DETAILS, details);
                    UserAreaActivity.this.startActivity(intent);
                }
            });
        }

    }
}
