package rpr.events;

/**
 * Created by Vishal Singh on 31-03-2017.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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


public class Menu2 extends Fragment {

    @Nullable
    private static Context context = null;
    private static TextView txtDate;
    private static TextView txtTime;
    private static Spinner categorySpinner;
    private static Spinner usertypeSpinner;
    Button btntimepicker, btndatepicker;

    UserSessionManager session;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        context = getActivity();
        session = new UserSessionManager(context);
        return inflater.inflate(R.layout.fragment_menu_2, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Organise Event");
        categorySpinner = (Spinner) getView().findViewById(R.id.category_spinner);
        usertypeSpinner = (Spinner) getView().findViewById(R.id.usertype_spinner);
        txtDate = (TextView) getView().findViewById(R.id.in_date);
        txtTime = (TextView) getView().findViewById(R.id.in_time);

        btndatepicker = (Button) getView().findViewById(R.id.btn_date);
        btntimepicker = (Button) getView().findViewById(R.id.btn_time);
        getusertype();
        getcategory();

        final Calendar c = Calendar.getInstance();

        txtDate.setText(DateFormat.getDateInstance().format(new Date()));
        txtTime.setText(DateFormat.getTimeInstance().format(new Date()));

        final String[] selectedDate = new String[1];
        SimpleDateFormat formatterdefault = new SimpleDateFormat("dd/MM/yyyy");
        String dateInStringdeafult = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        Date datedefault = null;
        try {
            datedefault = formatterdefault.parse(dateInStringdeafult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatterdefault = new SimpleDateFormat("yyyy-MM-dd");

        selectedDate[0] = formatterdefault.format(datedefault).toString();
        final String[] selectedTime = new String[1];
        String dtStartdefault = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(c.get(Calendar.MINUTE));
        SimpleDateFormat formatdefault = new SimpleDateFormat("HH:mm");

        java.sql.Time timeValuedefault = null;
        try {
            timeValuedefault = new java.sql.Time(formatdefault.parse(dtStartdefault).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        selectedTime[0] = String.valueOf(timeValuedefault);

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
                                    txtTime.setText(DateFormat.getTimeInstance().format(selected));

                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
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

        final EditText etEventName = (EditText) getView().findViewById(R.id.event_name);
        final EditText etVenue = (EditText) getView().findViewById(R.id.venue);
        final EditText etDetails = (EditText) getView().findViewById(R.id.details);

        final Button bAddNew = (Button) getView().findViewById(R.id.bAdd);

        bAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String event_name = etEventName.getText().toString().trim();
                final String venue = etVenue.getText().toString().trim();
                final int category = categorySpinner.getSelectedItemPosition() + 1;
                final int usertype = usertypeSpinner.getSelectedItemPosition();
                final String details = etDetails.getText().toString().trim();
                final String time = selectedDate[0] + " " + selectedTime[0];
                HashMap<String, String> user = session.getUserDetails();

                // get name
                final String user_id = user.get(UserSessionManager.KEY_USER_ID);
                if (event_name.equals("")){
                    Toast.makeText(context, "Please specify Name", Toast.LENGTH_SHORT).show();
                }
                else if (venue.equals("")){
                    Toast.makeText(context, "Please specify Venue", Toast.LENGTH_SHORT).show();
                }
                else if (details.equals("")){
                    Toast.makeText(context, "Please specify Details", Toast.LENGTH_SHORT).show();
                }
                else{
                    String url = "http://10.1.1.19/~2015csb1021/event/addEvent.php";

                    StringRequest registerRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {

                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");

                                        if (success) {


                                            Toast.makeText(context, "Event " + event_name + " was added Successfully ", Toast.LENGTH_SHORT).show();
                                            FragmentManager fragmentManager2 = getFragmentManager();
                                            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                                            fragmentTransaction2.replace(R.id.content_frame, new Menu1());
                                            fragmentTransaction2.commit();

                                        } else {

                                            Toast.makeText(context, "Event adding Failed", Toast.LENGTH_SHORT).show();

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
                            params.put("event_name", event_name);
                            params.put("user_id",user_id);
                            params.put("category_id", category+"");
                            params.put("usertype_id", usertype+"");
                            params.put("venue",venue);
                            params.put("time",time);
                            params.put("details",details);


                            return params;
                        }

                    };
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(registerRequest);
                }
            }
        });


    }
    private void getusertype(){
        //Creating a string request

        String url = "http://10.1.1.19/~2015csb1021/event/usertype.php";

        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, url,
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(usertypeRequest);

    }
    private void getcategory(){
        //Creating a string request

        String url = "http://10.1.1.19/~2015csb1021/event/category.php";

        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, url,
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(usertypeRequest);

    }
}