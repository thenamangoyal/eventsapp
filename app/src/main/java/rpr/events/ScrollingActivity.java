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
import java.util.Calendar;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract;
import java.util.*;
import java.util.Timer;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        final String TAG_EVENT_ID = "event_id";
        final String TAG_NAME = "name";
        final String TAG_TIME = "time";
        final String TAG_VENUE = "venue";
        final String TAG_DETAILS = "details";

        Intent intent = getIntent();
        final String event_id = intent.getStringExtra(TAG_EVENT_ID);
        final String name = intent.getStringExtra(TAG_NAME);
        final String time = intent.getStringExtra(TAG_TIME);
        final String venue = intent.getStringExtra(TAG_VENUE);
        final String details = intent.getStringExtra(TAG_DETAILS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(TAG_NAME);

        TextView description = (TextView) findViewById(R.id.tvcontent) ;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");

                Calendar cal = Calendar.getInstance();


                //long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

                //intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
               // intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
               // intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                intent.putExtra(Events.TITLE, name);
                intent.putExtra(Events.DESCRIPTION,  details);
                intent.putExtra(Events.EVENT_LOCATION, venue);
               // intent.putExtra(Events.RRULE, "FREQ=YEARLY");

                startActivity(intent);
            }
        });
    }
}
