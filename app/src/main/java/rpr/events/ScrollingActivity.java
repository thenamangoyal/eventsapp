package rpr.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.util.Calendar;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.Timer;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ScrollingActivity extends AppCompatActivity {
    String name;
    String details;
    String time;
    String venue;
    int category_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        final String TAG_EVENT_ID = "event_id";
        final String TAG_NAME = "name";
        final String TAG_TIME = "time";
        final String TAG_VENUE = "venue";
        final String TAG_DETAILS = "details";


        final TextView tvdetails = (TextView) findViewById(R.id.details) ;
        final TextView tvvenue = (TextView) findViewById(R.id.venue) ;
        final TextView tvtime = (TextView) findViewById(R.id.time) ;


        Intent intent = getIntent();
        final String event_id = intent.getStringExtra(TAG_EVENT_ID);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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
                                toolbar.setTitle(name);
                                tvdetails.setText(details);
                                tvtime.setText(time);
                                tvvenue.setText(venue);

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

                intent.putExtra(Events.TITLE, name);
                intent.putExtra(Events.DESCRIPTION,  details);
                intent.putExtra(Events.EVENT_LOCATION, venue);
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, beginCal.getTimeInMillis()+60*60*1000);

                // intent.putExtra(Events.RRULE, "FREQ=YEARLY");

                startActivity(intent);
            }
        });
    }
}
