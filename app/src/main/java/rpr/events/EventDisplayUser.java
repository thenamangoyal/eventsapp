package rpr.events;

import android.content.Intent;
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







public class EventDisplayUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("OnCreate","3");
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

        String event_id = intent.getStringExtra(TAG_EVENT_ID);
        String name = intent.getStringExtra(TAG_NAME);
        String time = intent.getStringExtra(TAG_TIME);
        String venue = intent.getStringExtra(TAG_VENUE);
        String details = intent.getStringExtra(TAG_DETAILS);

        nametv.setText(name);
        timetv.setText(time);
        venuetv.setText(venue);
        detailstv.setText(details);
    }
}
