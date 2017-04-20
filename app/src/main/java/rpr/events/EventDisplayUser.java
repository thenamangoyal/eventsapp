package rpr.events;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
    Button bookmark;

    UserSessionManager session;
    private static String usertype_id;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display_user);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        final TextView nametv = (TextView) findViewById(R.id.tvname);
        final TextView datetv = (TextView) findViewById(R.id.tvDate);
        final TextView timetv = (TextView) findViewById(R.id.tvTime);
        final TextView venuetv = (TextView) findViewById(R.id.tvVenue);
        final TextView organisertv = (TextView) findViewById(R.id.tvOrganiser);
        final TextView detailstv = (TextView) findViewById(R.id.tvdetails);
        bookmark = (Button) findViewById(R.id.tvBookmark);

        event_id = intent.getStringExtra("event_id");

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);


        StringRequest eventRequest= new StringRequest(Request.Method.POST, getResources().getString(R.string.event_display_url),
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
                                Date date = new Date();
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                nametv.setText(name);
                                detailstv.setText(details);
                                datetv.setText(new SimpleDateFormat("dd MMM, yyyy").format(date));
                                timetv.setText(new SimpleDateFormat("hh:mm aa").format(date));
                                organisertv.setText("Organised By: " + obj.getString("creator"));
                                venuetv.setText(venue);
                                Button addcal = (Button) findViewById(R.id.tvAddCalender);
                                final Date finalDate = date;
                                addcal.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_INSERT);
                                        intent.setType("vnd.android.cursor.item/event");

                                        Calendar beginCal = Calendar.getInstance();


                                        //long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

                                        // intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
                                        // intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                                        intent.putExtra(CalendarContract.Events.TITLE, name);
                                        intent.putExtra(CalendarContract.Events.DESCRIPTION,  details);
                                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, venue);

                                        long startMillis = 0;
                                        Calendar beginTime = Calendar.getInstance();
                                        beginTime.setTime(finalDate);

                                        startMillis = beginTime.getTimeInMillis();
                                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);

                                        // intent.putExtra(Events.RRULE, "FREQ=YEARLY");

                                        startActivity(intent);
                                    }
                                });


                                checkbookmark();
                                bookmark.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        toggleBookmark();

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

    private void checkbookmark() {
        bookmark.setEnabled(false);
        StringRequest bookmarkcheckRequest= new StringRequest(Request.Method.POST, getResources().getString(R.string.getBookmark_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                bookmark.setEnabled(true);
                                bookmark.setText("Un-Bookmark");

                            } else {
                                bookmark.setEnabled(true);
                                bookmark.setText("Bookmark");
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
                params.put("user_id", usertype_id);
                return params;
            }

        };
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        queue2.add(bookmarkcheckRequest);
    }

    private void toggleBookmark() {
        bookmark.setEnabled(false);
        StringRequest togglebookmarkRequest= new StringRequest(Request.Method.POST, getResources().getString(R.string.toggleBookmark_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                bookmark.setEnabled(true);
                                checkbookmark();

                            } else {
                                bookmark.setEnabled(true);
                                checkbookmark();
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
                params.put("user_id", usertype_id);
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(togglebookmarkRequest);
    }
}
