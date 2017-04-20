package rpr.events;

/**
 * Created by Vishal Singh on 31-03-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserProfile extends Fragment {


    UserSessionManager session;
    private Context context = null;
    public String name;
    public String usertype_id;
    public String usertype;
    public String email;
    public TextView user_name;
    public TextView user_email;
    public TextView user_type;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Manage Profile");
        context = getActivity();
        session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(UserSessionManager.KEY_NAME);
        email = user.get(UserSessionManager.KEY_EMAIL);
        usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);
        usertype = user.get(UserSessionManager.KEY_USERTYPE);
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_name = (TextView) getView().findViewById(R.id.user_name);
        user_email = (TextView) getView().findViewById(R.id.user_email);
        user_type = (TextView) getView().findViewById(R.id.user_type);
        user_name.setText(name);
        user_email.setText(email);
        user_type.setText(usertype);

        getView().findViewById(R.id.bchangepass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), changePassword.class);
                startActivity(intent);



            }
        });

    }
}