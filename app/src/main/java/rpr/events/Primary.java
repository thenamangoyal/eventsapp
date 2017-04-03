package rpr.events;

/**
 * Created by Vishal Singh on 02-04-2017.
 */

        import android.app.ProgressDialog;
        import android.database.sqlite.SQLiteOpenHelper;
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
        import android.database.Cursor;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;



public class Primary extends Fragment {

    public SQLiteDatabase db;

    UserSessionManager session;

    private static Context context = null;
    private static final String TAG = Menu3.class.getSimpleName();
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DETAILS = "details";
    private static String usertype_id;

    private static ProgressDialog pDialog;
    private ListView lv;

    // URL to get events JSON
    private static String ListURL = "http://10.1.1.19/~2015csb1021/event/listAll.php";

    ArrayList<HashMap<String, String>> eventList;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        context = getActivity();
        createDatabase();
        session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);
        return inflater.inflate(R.layout.primary_layout, container, false);

    }
    protected void createDatabase(){
        db=getActivity().openOrCreateDatabase("EventDB0", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Events(event_id INTEGER NOT NULL, name VARCHAR NOT NULL,venue VARCHAR NOT NULL, time TIMESTAMP NOT NULL, details VARCHAR NOT NULL  );");
    }

    protected void insertIntoDB(String Event_id, String Name, String Time, String Venue, String Details){
        String query = "INSERT INTO Events(event_id,name,venue, time, details) VALUES('"+Event_id+"', '"+Name+"', '"+Time+"', '"+Venue+"', '"+Details+"');";
        db.execSQL(query);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            String jsonStr = sh.makeServiceCall(ListURL+"?usertype_id="+usertype_id);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    if (jsonObj.getBoolean("success") == true) {
                        JSONArray events = jsonObj.getJSONArray("events");


                        db.execSQL("DELETE FROM Events");
                        for (int i = 0; i < events.length(); i++) {
                            JSONObject c = events.getJSONObject(i);

                            String event_id = c.getString(TAG_EVENT_ID);
                            String name = c.getString(TAG_NAME);
                            String time = c.getString(TAG_TIME);
                            String venue = c.getString(TAG_VENUE);
                            String details = c.getString(TAG_DETAILS);

                            insertIntoDB(event_id, name, time, venue, details);

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
                        db.close();
                    }
                    else{
                        Toast.makeText(context.getApplicationContext(),
                                "No events found",
                                Toast.LENGTH_LONG)
                                .show();
                    }

                } catch (final JSONException e) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context.getApplicationContext(),
                                "No Network",
                                Toast.LENGTH_LONG)
                                .show();

                    }
                });



                String query = "SELECT * FROM Events WHERE 1";
                //Cursor points to a location in your results
                Cursor recordSet = db.rawQuery(query, null);
                //Move to the first row in your results
                recordSet.moveToFirst();


                //Position after the last row means the end of the results
                while (!recordSet.isAfterLast()) {


                    String event_id = recordSet.getString(recordSet.getColumnIndex("event_id"));
                    String name = recordSet.getString(recordSet.getColumnIndex("name"));
                    String time = recordSet.getString(recordSet.getColumnIndex("time"));
                    String venue = recordSet.getString(recordSet.getColumnIndex("venue"));
                    String details = recordSet.getString(recordSet.getColumnIndex("details"));


                    HashMap<String, String> event = new HashMap<>();

                    // adding each child node to HashMap key => value
                    event.put(TAG_EVENT_ID, event_id);
                    event.put(TAG_NAME, name);
                    event.put(TAG_TIME, time);
                    event.put(TAG_VENUE, venue);
                    event.put(TAG_DETAILS, details);

                    // adding event to event list
                    eventList.add(event);


                    recordSet.moveToNext();

                }

                db.close();



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
                    Intent intent = new Intent(Primary.context, EventDisplayUser.class);
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