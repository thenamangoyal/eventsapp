package rpr.events;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EventDisplayUser extends AppCompatActivity {

    String event_id;
    String name;
    String time;
    String venue;
    String details;
    int category_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display_user);
        final String TAG_EVENT_ID = "event_id";
        final String TAG_NAME = "name";
        final String TAG_TIME = "time";
        final String TAG_VENUE = "venue";
        final String TAG_DETAILS = "details";
        Intent intent = getIntent();
        final TextView nametv = (TextView) findViewById(R.id.name);
        final TextView timetv = (TextView) findViewById(R.id.time);
        final TextView venuetv = (TextView) findViewById(R.id.venue);
        final TextView detailstv = (TextView) findViewById(R.id.details);

        event_id = intent.getStringExtra(TAG_EVENT_ID);

        String url = "http://10.1.1.19/~2015csb1021/event/getEvent.php";

        StringRequest eventRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                JSONArray result = jsonResponse.getJSONArray("events");
                                JSONObject obj = result.getJSONObject(0);
                                name = obj.getString("name");
                                venue = obj.getString("venue");
                                details = obj.getString("details");
                                time = obj.getString("time");
                                category_id = obj.getInt("category_id");
                                nametv.setText(name);
                                detailstv.setText(details);
                                timetv.setText(time);
                                venuetv.setText(venue);
                                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_INSERT);
                                        intent.setType("vnd.android.cursor.item/event");
                                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                        Date date = new Date();
                                        try {
                                            date = format.parse(time);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Calendar beginCal = Calendar.getInstance();
                                        beginCal.setTime(date);


                                        //long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

                                        //intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
                                        // intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
                                        // intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                                        intent.putExtra(CalendarContract.Events.TITLE, name);
                                        intent.putExtra(CalendarContract.Events.DESCRIPTION,  details);
                                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, venue);
                                        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal.getTimeInMillis());
                                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, beginCal.getTimeInMillis()+60*60*1000);

                                        // intent.putExtra(Events.RRULE, "FREQ=YEARLY");

                                        startActivity(intent);
                                    }
                                });

                            } else {

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id);

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(eventRequest);



    }
}
