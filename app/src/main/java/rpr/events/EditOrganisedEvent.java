package rpr.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditOrganisedEvent extends AppCompatActivity {

    Context context;
    UserSessionManager session;
    private TextView txtName;
    private TextView txtVenue;
    private TextView txtDetails;
    private TextView txtDate;
    private TextView txtTime;
    private Spinner categorySpinner;
    private Spinner usertypeSpinner;
    Button btntimepicker, btndatepicker;

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

    private static String user_id;
    RequestQueue queue;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_edit_organised_event);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session = new UserSessionManager(this);

        session = new UserSessionManager(getApplicationContext());
        if(session.checkLogin())
            finish();
        HashMap<String, String> user = session.getUserDetails();
        queue = Volley.newRequestQueue(context);
        user_id = user.get(UserSessionManager.KEY_USER_ID);

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

        txtName = (TextView) findViewById(R.id.event_name);
        txtDate = (TextView) findViewById(R.id.in_date);
        txtTime = (TextView) findViewById(R.id.in_time);
        txtVenue = (TextView) findViewById(R.id.venue);
        txtDetails = (TextView) findViewById(R.id.details);
        txtName.setText(name);
        txtVenue.setText(venue);
        txtDetails.setText(details);
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtDate.setText(DateFormat.getDateInstance().format(date));
        txtTime.setText(new SimpleDateFormat("hh:mm aa").format(date));

        final String[] selectedDate = new String[1];
        selectedDate[0] = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();

        final String[] selectedTime = new String[1];
        selectedTime[0] = new SimpleDateFormat("HH:mm:ss").format(date).toString();

        final Calendar c = Calendar.getInstance();
        c.setTime(date);


        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        usertypeSpinner = (Spinner) findViewById(R.id.usertype_spinner);

        btndatepicker = (Button) findViewById(R.id.btn_date);
        btntimepicker = (Button) findViewById(R.id.btn_time);
        getusertype();
        getcategory();
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Event Name");
                final EditText input = new EditText(context);
                input.setText(txtName.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtName.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }});
        txtVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Venue");
                final EditText input = new EditText(context);
                input.setText(txtVenue.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtVenue.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }});
        txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Details");
                final EditText input = new EditText(context);
                input.setText(txtDetails.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtDetails.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }});

        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.YEAR, year);
                                    cal.set(Calendar.MONTH, monthOfYear);
                                    cal.set(Calendar.DATE, dayOfMonth);
                                    Date selected = cal.getTime();
                                    txtDate.setText(DateFormat.getDateInstance().format(selected));



                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);



                                    formatter = new SimpleDateFormat("yyyy-MM-dd");

                                    selectedDate[0] = formatter.format(date).toString();


                                } catch (Exception ex) {

                                }


                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dd.show();
            }
        });

        btntimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog td = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    cal.set(Calendar.MINUTE, minute);
                                    cal.set(Calendar.SECOND, 0);
                                    Date selected = cal.getTime();

                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    txtTime.setText(new SimpleDateFormat("hh:mm aa").format(selected));



                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                    java.sql.Time timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    selectedTime[0] = String.valueOf(timeValue);

                                } catch (Exception ex) {
                                }
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        android.text.format.DateFormat.is24HourFormat(context)
                );
                td.show();
            }
        });


        final Button bUpdate = (Button) findViewById(R.id.bUpdate);

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = txtName.getText().toString().trim();
                venue = txtVenue.getText().toString().trim();
                category_id = categorySpinner.getSelectedItemPosition() + 1;
                usertype_id = usertypeSpinner.getSelectedItemPosition();
                details = txtDetails.getText().toString().trim();
                time = selectedDate[0] + " " + selectedTime[0];
                if (name.equals("")){
                    Toast.makeText(context, "Please specify Name", Toast.LENGTH_SHORT).show();
                }
                else if (venue.equals("")){
                    Toast.makeText(context, "Please specify Venue", Toast.LENGTH_SHORT).show();
                }
                else if (details.equals("")){
                    Toast.makeText(context, "Please specify Details", Toast.LENGTH_SHORT).show();
                }
                else{

                    StringRequest registerRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.updateEvent_url),
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {

                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");

                                        if (success) {


                                            Toast.makeText(context, "Event " + name + " was updated Successfully ", Toast.LENGTH_SHORT).show();

                                            onBackPressed();


                                        } else {

                                            Toast.makeText(context, "Event update Failed", Toast.LENGTH_SHORT).show();

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
                            params.put("name", name);
                            params.put("user_id",user_id);
                            params.put("event_id",event_id+"");
                            params.put("category_id", category_id+"");
                            params.put("usertype_id", usertype_id+"");
                            params.put("venue",venue);
                            params.put("time",time);
                            params.put("details",details);


                            return params;
                        }

                    };
                    queue.add(registerRequest);
                }
            }
        });

        final Button bDelete = (Button) findViewById(R.id.bDelete);

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                    StringRequest registerRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.deleteEvent_url),
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {

                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");

                                        if (success) {


                                            Toast.makeText(context, "Event " + name + " was deleted ", Toast.LENGTH_SHORT).show();

                                            onBackPressed();


                                        } else {

                                            Toast.makeText(context, "Event deletion Failed", Toast.LENGTH_SHORT).show();

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
                            params.put("user_id",user_id);
                            params.put("event_id",event_id+"");


                            return params;
                        }

                    };
                    queue.add(registerRequest);

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null){
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    private void getusertype(){
        //Creating a string request


        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.usertype_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            jsonResponse = new JSONObject(response);
                            JSONArray result = jsonResponse.getJSONArray("usertype");
                            ArrayList<String> usertypes = new ArrayList<String>();
                            for(int i=0;i<result.length();i++){

                                //Getting json object
                                JSONObject obj = result.getJSONObject(i);

                                //Adding the name of the student to array list
                                usertypes.add(obj.getString("name"));

                            }
                            usertypeSpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, usertypes));
                            usertypeSpinner.setSelection(usertype_id);
                            //Calling method getStudents to get the students from the JSON Array
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        //Adding request to the queue
        queue.add(usertypeRequest);

    }
    private void getcategory(){
        //Creating a string request


        StringRequest categoryRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.categoryRegister_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            jsonResponse = new JSONObject(response);
                            JSONArray result = jsonResponse.getJSONArray("category");
                            ArrayList<String> categorys = new ArrayList<String>();
                            for(int i=0;i<result.length();i++){

                                //Getting json object
                                JSONObject obj = result.getJSONObject(i);

                                //Adding the name of the student to array list
                                categorys.add(obj.getString("name"));

                            }
                            categorySpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, categorys));
                            categorySpinner.setSelection(category_id-1);
                            //Calling method getStudents to get the students from the JSON Array
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue


        //Adding request to the queue
        queue.add(categoryRequest);

    }

}
