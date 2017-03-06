package rpr.events;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Name", Toast.LENGTH_SHORT).show();
                }
                else if(etUsername.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Username", Toast.LENGTH_SHORT).show();
                }
                else if(etPassword.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Password", Toast.LENGTH_SHORT).show();
                }
                else if (etAge.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify Age", Toast.LENGTH_SHORT).show();
                }
                else{
                    final String name = etName.getText().toString();
                    final String username = etUsername.getText().toString();
                    final String password = etPassword.getText().toString();
                    final int age = Integer.parseInt(etAge.getText().toString());
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    Toast.makeText(getApplicationContext(), "Register Successfull " + username, Toast.LENGTH_SHORT).show();
                                    RegisterActivity.this.startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_SHORT).show();
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                                    builder.setMessage("Register Failed")
//                                            .setNegativeButton("Retry", null)
//                                            .create()
//                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(name, username, age, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}
