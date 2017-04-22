package rpr.events;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    Button bLogin;
    TextView registerLink;
    UserSessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new UserSessionManager(getApplicationContext());

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        registerLink = (TextView) findViewById(R.id.tvRegister);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);

                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LoginActivity.this.startActivity(registerIntent);

                finish();


            }
        });

        if (session.isUserLoggedIn()){




            Intent intent = new Intent(LoginActivity.this, NavBar.class);
//
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginActivity.this.startActivity(intent);
            finish();
      }



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
                if (etPassword.getText().toString().length() == 0){
                    etPassword.setError("Please enter Password");
                }
                else{
                    etPassword.setError(null);
                }

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Username", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (etPassword.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please specify Password", Toast.LENGTH_SHORT).show();
                    } else {
                        final String email = etEmail.getText().toString().trim();
                        final String password = etPassword.getText().toString();


                        StringRequest loginRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.Login_url),
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        try{
                                            final JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                FirebaseMessaging.getInstance().subscribeToTopic("0");
                                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.firebase_pref),MODE_PRIVATE);
                                                final int user_id = jsonResponse.getInt("user_id");
                                                final String token = sharedPreferences.getString(getString(R.string.firebase_token),"");
                                                StringRequest fcmRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.updateFCMtoken_url),
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {

                                                                //                        try{
                                                                //                            JSONObject jsonResponse = new JSONObject(response);
                                                                //                            boolean success = jsonResponse.getBoolean("success");
                                                                //
                                                                //                            if (success) {
                                                                //
                                                                //
                                                                //
                                                                //                            } else {
                                                                //
                                                                //
                                                                //                            }
                                                                //                        }catch (JSONException e) {
                                                                //                            e.printStackTrace();
                                                                //                        }

                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        // error

                                                    }
                                                }
                                                ) {
                                                    @Override
                                                    protected Map<String, String> getParams() {
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put("user_id", user_id+"");
                                                        params.put("token", token);


                                                        return params;
                                                    }

                                                };
                                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                                queue.add(fcmRequest);


                                                UserSessionManager session = new UserSessionManager(getApplicationContext());



                                                session.createUserLoginSession(jsonResponse.getString("name"), jsonResponse.getString("email"), jsonResponse.getInt("user_id"), jsonResponse.getInt("usertype_id"), jsonResponse.getString("usertype"), jsonResponse.getString("created"));

                                                Toast.makeText(getApplicationContext(), "Login Successful " + jsonResponse.getString("name"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, NavBar.class);

                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                // Add new Flag to start new Activity
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                LoginActivity.this.startActivity(intent);
                                                finish();

                                            } else {

                                                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

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

                                return params;
                            }

                        };
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(loginRequest);
                    }
                }

            }
        });
    
    }


}
