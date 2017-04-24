package rpr.events;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Spinner usertypeSpinner;
    private Button bRegister;
    private Context context = null;

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        queue = Volley.newRequestQueue(context);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        usertypeSpinner = (Spinner) findViewById(R.id.usertype_spinner);
        bRegister = (Button) findViewById(R.id.bRegister);
        getusertype();

        TextView loginLink = (TextView) findViewById(R.id.tvSignin) ;
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);

                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RegisterActivity.this.startActivity(loginIntent);

                finish();


            }
        });

        etName.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etName.getText().toString().length() == 0){
                    etName.setError("Please enter name");
                }
                else{
                    etName.setError(null);
                }

            }
        });

        etEmail.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                boolean iitrpr_email = etEmail.getText().toString().matches("[a-zA-Z0-9._-]+@iitrpr\\.ac\\.in");
                if (!iitrpr_email){
                    etEmail.setError("Invalid Email");
                }
                else{
                    etEmail.setError(null);
                }

            }
        });



        etPassword.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etPassword.getText().toString().length() < 8){
                    etPassword.setError("Atleast 8 characters");
                }
                else{
                    etPassword.setError(null);
                }

            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Name", Toast.LENGTH_SHORT).show();
                }
                else if(etEmail.getText().toString().trim().equals("") || !(etEmail.getText().toString().trim().matches("[a-zA-Z0-9._-]+@iitrpr\\.ac\\.in"))){
                    Toast.makeText(getApplicationContext(), "Please specify valid Email", Toast.LENGTH_SHORT).show();
                }
                else if(etPassword.getText().toString().length() < 8){
                    Toast.makeText(getApplicationContext(), "Please specify Password of atleast 8 characters", Toast.LENGTH_SHORT).show();
                }
                else{
                    final String name = etName.getText().toString().trim();
                    final String email = etEmail.getText().toString().trim();
                    final int usertype_id = usertypeSpinner.getSelectedItemPosition()+1;
                    final String password = etPassword.getText().toString();



                        StringRequest registerRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.Register_url),
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {

                                        try{
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");

                                            if (success) {


                                                Toast.makeText(getApplicationContext(), "Register Successful " + email, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                // Add new Flag to start new Activity
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                getApplicationContext().startActivity(intent);
                                                finish();

                                            } else {

                                                Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_SHORT).show();

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

                                Toast.makeText(getApplicationContext(), "Couldn't connect to internet",Toast.LENGTH_SHORT).show();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("password", password);
                                params.put("name", name);
                                params.put("usertype_id", usertype_id+"");

                                return params;
                            }

                        };
                        queue.add(registerRequest);



                }
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


        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.usertypeRegister_url),
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

            //Adding request to the queue
            queue.add(usertypeRequest);

    }
}
