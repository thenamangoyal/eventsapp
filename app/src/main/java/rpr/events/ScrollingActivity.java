package rpr.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String TAG_EVENT_ID = "event_id";
        final String TAG_NAME = "name";
        final String TAG_TIME = "time";
        final String TAG_VENUE = "venue";
        final String TAG_DETAILS = "details";
        Intent intent = getIntent();

        String event_id = intent.getStringExtra(TAG_EVENT_ID);
        String name = intent.getStringExtra(TAG_NAME);
        String time = intent.getStringExtra(TAG_TIME);
        String venue = intent.getStringExtra(TAG_VENUE);
        String details = intent.getStringExtra(TAG_DETAILS);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
