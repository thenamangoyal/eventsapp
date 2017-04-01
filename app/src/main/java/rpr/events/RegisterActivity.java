package rpr.events;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Name", Toast.LENGTH_SHORT).show();
                }
                else if(etEmail.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Email", Toast.LENGTH_SHORT).show();
                }
                else if(etPassword.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Password", Toast.LENGTH_SHORT).show();
                }
                else if(etPassword.getText().toString().length() < 8){
                    Toast.makeText(getApplicationContext(), "Please specify Password of atleast 8 characters", Toast.LENGTH_SHORT).show();
                }
                else{
                    final String name = etName.getText().toString().trim();
                    final String email = etEmail.getText().toString().trim();
                    final int usertype_id = usertypeSpinner.getSelectedItemPosition()+1;
                    final String password = etPassword.getText().toString();

                    boolean iitrpr_email = email.matches("[a-zA-Z0-9._-]+@iitrpr\\.ac\\.in");
                    if (!iitrpr_email){
                        Toast.makeText(getApplicationContext(), "Please enter iitrpr email only", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        String url = "http://10.1.1.19/~2015csb1021/event/Register.php";

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
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(registerRequest);


                    }
                }
            }
        });
    }

    private void getusertype(){
            //Creating a string request

        String url = "http://10.1.1.19/~2015csb1021/event/usertypeRegister.php";

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
                                usertypeSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, usertypes));

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
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //Adding request to the queue
            requestQueue.add(usertypeRequest);

    }
}
