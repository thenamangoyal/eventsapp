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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
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

    private int event_id;
    private String name;
    private String time;
    private String venue;
    private String details;
    private int usertype_id;
    private String usertype;
    private int creator_id;
    private String creator;
    private int category_id;
    private String category;

    TextView nametv;
    TextView datetv;
    TextView timetv;
    TextView venuetv;
    TextView organisertv;
    TextView detailstv;
    TextView categorytv;
    Button bookmark;
    Button addcal;

    UserSessionManager session;
    private static String user_id;


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

        final LinearLayout layout = (LinearLayout)findViewById(R.id.buttonPanel);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) findViewById(R.id.datapanel).getLayoutParams();
                mlp.setMargins(0,0,0,layout.getHeight());
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });


        session = new UserSessionManager(getApplicationContext());
        if(session.checkLogin())
            finish();
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.KEY_USER_ID);

        nametv = (TextView) findViewById(R.id.tvname);
        datetv = (TextView) findViewById(R.id.tvDate);
        timetv = (TextView) findViewById(R.id.tvTime);
        venuetv = (TextView) findViewById(R.id.tvVenue);
        organisertv = (TextView) findViewById(R.id.tvOrganiser);
        detailstv = (TextView) findViewById(R.id.tvdetails);
        categorytv = (TextView) findViewById(R.id.tvCategory);
        bookmark = (Button) findViewById(R.id.tvBookmark);
        addcal = (Button) findViewById(R.id.tvAddCalender);

        Intent intent = getIntent();
        event_id = intent.getIntExtra("event_id",0);
        name =  intent.getStringExtra("name");
        time =  intent.getStringExtra("time");
        venue =  intent.getStringExtra("venue");
        details =  intent.getStringExtra("details");
        usertype_id =  intent.getIntExtra("usertype_id",0);
        usertype =  intent.getStringExtra("usertype");
        creator_id =  intent.getIntExtra("creator_id",0);
        creator =  intent.getStringExtra("creator");
        category_id =  intent.getIntExtra("category_id",0);
        category =  intent.getStringExtra("category");
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
        organisertv.setText("Organised By: " + creator);
        venuetv.setText(venue);
        categorytv.setText(category);
        final Date finalDate = date;
        addcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");

                // intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
                // intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                // intent.putExtra(Events.RRULE, "FREQ=YEARLY");
                intent.putExtra(CalendarContract.Events.TITLE, name);
                intent.putExtra(CalendarContract.Events.DESCRIPTION,  details);
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, venue);

                Calendar beginTime = Calendar.getInstance();
                beginTime.setTime(finalDate);
                long startMillis = beginTime.getTimeInMillis();
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);

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
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
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
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(togglebookmarkRequest);
    }
}
