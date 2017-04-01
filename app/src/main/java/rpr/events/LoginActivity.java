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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button bLogin;
    TextView registerLink;

   // SessionManager session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
       // session = new SessionManager(getApplicationContext());

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        registerLink = (TextView) findViewById(R.id.tvRegister);






        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etUsername.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Username", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (etPassword.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please specify Password", Toast.LENGTH_SHORT).show();
                    } else {
                        final String username = etUsername.getText().toString().trim();
                        final String password = etPassword.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        String name = jsonResponse.getString("name");
                                        int user_id = jsonResponse.getInt("user_id");


                                        Toast.makeText(getApplicationContext(), "Login Successfull " + username, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, NavBar.class);

                                        intent.putExtra("name", name);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("username", username);

                                        LoginActivity.this.startActivity(intent);
                                        Log.e("OnCreate", "8.1");

                                    } else {

                                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                        queue.add(loginRequest);
                    }
                }
            }
        });
    
    }
}
