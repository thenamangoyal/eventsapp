package rpr.events;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {

                                                session.createUserLoginSession(jsonResponse.getString("name"), jsonResponse.getString("email"), jsonResponse.getInt("user_id"), jsonResponse.getInt("usertype_id"), jsonResponse.getString("created"));

                                                Toast.makeText(getApplicationContext(), "Login Successful " + jsonResponse.getString("name"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, NavBar.class);
//
//                                                intent.putExtra("name", jsonResponse.getString("name"));
//                                                intent.putExtra("user_id", jsonResponse.getInt("user_id"));
//                                                intent.putExtra("usertype_id", jsonResponse.getInt("usertype_id"));
//                                                intent.putExtra("email", jsonResponse.getString("email"));
//                                                intent.putExtra("created", jsonResponse.getString("created"));

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
