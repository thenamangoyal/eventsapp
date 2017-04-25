package rpr.events;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class changePassword extends AppCompatActivity {

    EditText oldpass;
    EditText newpass;
    EditText newpass2;
    Button changepass;
    UserSessionManager session;
    RequestQueue queue;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session = new UserSessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(UserSessionManager.KEY_EMAIL);
        oldpass = (EditText) findViewById(R.id.etoldPassword);
        newpass = (EditText) findViewById(R.id.etnewPassword);
        newpass2 = (EditText) findViewById(R.id.etnewPassword2);
        changepass = (Button) findViewById(R.id.bchangePassword);
        queue = Volley.newRequestQueue(getApplicationContext());

        newpass.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (newpass.getText().toString().length() < 8){
                    newpass.setError("Atleast 8 characters");
                }
                else{
                    newpass.setError(null);
                }
                if (newpass.getText().toString().equals(newpass2.getText().toString())){
                    newpass2.setError(null);
                }
                else{
                    newpass2.setError("Passwords don't match");
                }

            }
        });

        newpass2.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (newpass.getText().toString().equals(newpass2.getText().toString())){
                    newpass2.setError(null);
                }
                else{
                    newpass2.setError("Passwords don't match");
                }

            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(oldpass.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please specify old Password", Toast.LENGTH_SHORT).show();
                }
                else if (newpass.getText().toString().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Please specify new Password of atleast 8 characters", Toast.LENGTH_SHORT).show();
                }
                else if (!newpass2.getText().toString().equals(newpass.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "New password don't match", Toast.LENGTH_SHORT).show();
                }
                else {
                        final String oldPassword = oldpass.getText().toString();
                        final String newPassword = newpass.getText().toString();

                        StringRequest changepasswordRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.changePassword_url),
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        try{
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {

                                                Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            } else {

                                                Toast.makeText(getApplicationContext(), "Password couldn't be changed", Toast.LENGTH_SHORT).show();

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
                                Snackbar.make(v, "Couldn't connect to internet",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("oldPassword", oldPassword);
                                params.put("newPassword", newPassword);

                                return params;
                            }

                        };

                        queue.add(changepasswordRequest);

                }

            }
        });


    }
}
