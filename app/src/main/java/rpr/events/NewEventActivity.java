package rpr.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewEventActivity extends AppCompatActivity {

    String[] categoryArray;
    String[] visibleArray;
    static int hour, min;

    TextView txtdate, txttime;
    Button btntimepicker, btndatepicker;

    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        final Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String username = intent.getStringExtra("username");
        final int user_id = intent.getIntExtra("user_id",-1);
        Resources res = getResources();

        categoryArray = res.getStringArray(R.array.category);

        final Spinner spinnerCategory = (Spinner) findViewById(R.id.category);

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,categoryArray);
        spinnerCategory.setAdapter(adapterCategory);

        visibleArray = res.getStringArray(R.array.visible);

        final Spinner spinnerVisible = (Spinner) findViewById(R.id.visible);

        ArrayAdapter<String> adapterVisible = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,visibleArray);
        spinnerVisible.setAdapter(adapterVisible);

        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        txtdate = (TextView) findViewById(R.id.in_date);
        txttime = (TextView) findViewById(R.id.in_time);

        btndatepicker = (Button) findViewById(R.id.btn_date);
        btntimepicker = (Button) findViewById(R.id.btn_time);

        final String[] datetime = new String[2];

        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(NewEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);

                                    txtdate.setText(formatter.format(date).toString());

                                    formatter = new SimpleDateFormat("yyyy-MM-dd");

                                    datetime[0] = formatter.format(date).toString();

                                } catch (Exception ex) {

                                }


                            }
                        }, year, month, day);
                dd.show();
            }
        });
        btntimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog td = new TimePickerDialog(NewEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    txttime.setText(String.valueOf(timeValue));
                                    datetime[1] = String.valueOf(timeValue);
                                } catch (Exception ex) {
                                    txttime.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(NewEventActivity.this)
                );
                td.show();
            }
        });

        final EditText etEventName = (EditText) findViewById(R.id.event_name);
        final EditText etVenue = (EditText) findViewById(R.id.venue);
        final EditText etDetails = (EditText) findViewById(R.id.details);

        final Button bAddNew = (Button) findViewById(R.id.bAdd);

        bAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String event_name = etEventName.getText().toString();
                final String venue = etVenue.getText().toString();
                final int category = spinnerCategory.getSelectedItemPosition();
                final int visible = spinnerVisible.getSelectedItemPosition();
                final String details = etDetails.getText().toString();
                String time = datetime[0].toString()+" "+ datetime[1].toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(NewEventActivity.this, UserAreaActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("username", username);
                                NewEventActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewEventActivity.this);
                                builder.setMessage("Add Event Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                NewEventRequest request = new NewEventRequest(event_name, user_id, category, visible, venue, time, details , responseListener);
                RequestQueue queue = Volley.newRequestQueue(NewEventActivity.this);
                queue.add(request);
            }
        });
    }
}
