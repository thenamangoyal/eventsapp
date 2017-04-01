package rpr.events;

/**
 * Created by Vishal Singh on 02-04-2017.
 */

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
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
        import android.database.sqlite.SQLiteDatabase;


public class Primary extends Fragment {

    private SQLiteDatabase db;

    private static Context context = null;
    private static final String TAG = Menu3.class.getSimpleName();
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DETAILS = "details";

    private static ProgressDialog pDialog;
    private ListView lv;

    // URL to get events JSON
    private static String ListURL = "http://10.1.1.19/~2015csb1021/php/ListAll.php";

    ArrayList<HashMap<String, String>> eventList;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        context = getActivity();
        return inflater.inflate(R.layout.primary_layout, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 1");
//        ProgressDialog pDialog;
//        ListView lv;

        // URL to get events JSON
        //String ListURL = "http://10.1.1.19/~2015csb1021/php/ListAll.php";

        eventList = new ArrayList<>();

        lv = (ListView) getView().findViewById(R.id.listView);

        new Primary.Getevents().execute();

    }


    private class Getevents extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Primary.context);
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

//                    Log.e("OnCreateView", "6");
                    // Getting JSON Array node
                    JSONArray events = jsonObj.getJSONArray("events");

                    //                  Log.e("OnCreateView", "6");
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context.getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context.getApplicationContext(),
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
                    Primary.context, eventList,
                    R.layout.event_list_item, new String[]{TAG_NAME, TAG_TIME, TAG_VENUE},
                    new int[]{R.id.name, R.id.time, R.id.venue});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
                    String event_id = (String) map.get(TAG_EVENT_ID);
                    String name = (String) map.get(TAG_NAME);
                    String time = (String) map.get(TAG_TIME);
                    String venue = (String) map.get(TAG_VENUE);
                    String details = (String) map.get(TAG_DETAILS);
                    Intent intent = new Intent(Primary.context, ScrollingActivity.class);
                    intent.putExtra(TAG_EVENT_ID, event_id);
                    intent.putExtra(TAG_NAME, name);
                    intent.putExtra(TAG_TIME, time);
                    intent.putExtra(TAG_VENUE, venue);
                    intent.putExtra(TAG_DETAILS, details);
                    Primary.context.startActivity(intent);

                }
            });
        }

    }


}