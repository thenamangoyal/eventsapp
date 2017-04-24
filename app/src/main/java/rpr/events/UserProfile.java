package rpr.events;

/**
 * Created by Vishal Singh on 31-03-2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

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
    private String name;
    private String usertype_id;
    private String user_id;
    private String usertype;
    private String email;
    private TextView user_name;
    private TextView user_email;
    private TextView user_type;
//    private TextView subscriptions;
    private Button changepass;
//    AlertDialog multichoiceDialog;
//    ArrayList<Integer> subsribed = new ArrayList<>();
//    ArrayList<String> categorys = new ArrayList<>();
//    boolean[] checkedItems;
//    RequestQueue queue;


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
        user_id = user.get(UserSessionManager.KEY_USER_ID);
        usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);
        usertype = user.get(UserSessionManager.KEY_USERTYPE);
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        queue = Volley.newRequestQueue(context);
        user_name = (TextView) getView().findViewById(R.id.user_name);
        user_email = (TextView) getView().findViewById(R.id.user_email);
        user_type = (TextView) getView().findViewById(R.id.user_type);
//        subscriptions = (TextView) getView().findViewById(R.id.subscriptions);
        changepass = (Button) getView().findViewById(R.id.bchangepass);
        user_name.setText(name);
        user_email.setText(email);
        user_type.setText(usertype);

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), changePassword.class);
                startActivity(intent);



            }
        });


    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (queue != null){
//            queue.cancelAll(new RequestQueue.RequestFilter() {
//                @Override
//                public boolean apply(Request<?> request) {
//                    return true;
//                }
//            });
//        }
//    }

//    private void getcategory(){
//        //Creating a string request
//
//
//        final StringRequest categoryRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.category_url),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject jsonResponse = null;
//                        try {
//                            //Parsing the fetched Json String to JSON Object
//                            jsonResponse = new JSONObject(response);
//                            JSONArray result = jsonResponse.getJSONArray("category");
//                            categorys.clear();
//                            for(int i=0;i<result.length();i++){
//
//                                //Getting json object
//                                JSONObject obj = result.getJSONObject(i);
//
//                                //Adding the name of the student to array list
//                                categorys.add(obj.getString("name"));
//
//                            }
//                            checkedItems = new boolean[categorys.size()];  // Where we track the selected items
//                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                            // Set the dialog title
//                            builder.setTitle("Subscriptions")
//                                    // Specify the list array, the items to be selected by default (null for none),
//                                    // and the listener through which to receive callbacks when items are selected
//                                    .setMultiChoiceItems(categorys.toArray(new CharSequence[categorys.size()]), null,
//                                            new DialogInterface.OnMultiChoiceClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which,
//                                                                    boolean isChecked) {
//
//                                                    if (isChecked) {
//                                                        // If the user checked the item, add it to the selected items
//                                                        if (which == 0){
//                                                            for (int k=1; k<categorys.size(); k++){
//                                                                checkedItems[k] = true;
//                                                                multichoiceDialog.getListView().setItemChecked(k,true);
//                                                            }
//                                                        }
//                                                        checkedItems[which] = true;
//                                                        boolean all = true;
//                                                        for (int k=1; k<categorys.size(); k++){
//                                                            if (!checkedItems[k]){
//                                                                all = false;
//                                                                break;
//                                                            }
//                                                        }
//                                                        if (all){
//                                                            checkedItems[0] = true;
//                                                            multichoiceDialog.getListView().setItemChecked(0,true);
//                                                        }
//
//
//                                                    } else if (checkedItems[which]) {
//                                                        // Else, if the item is already in the array, remove it
//                                                        if (which == 0){
//                                                            for (int k=1; k<categorys.size(); k++){
//                                                                checkedItems[k] = false;
//                                                                multichoiceDialog.getListView().setItemChecked(k,false);
//                                                            }
//                                                        }
//                                                        checkedItems[which] = false;
//                                                        if (checkedItems[0]){
//                                                            checkedItems[0] = false;
//                                                            multichoiceDialog.getListView().setItemChecked(0,false);
//                                                        }
//
//                                                    }
//                                                }
//                                            })
//                                    // Set the action buttons
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            // User clicked OK, so save the mSelectedItems results somewhere
//                                            // or return them to the component that opened the dialog
//                                            for (int j=1; j< categorys.size(); j++){
//                                                Log.e("c", j + " "+ checkedItems[j]);
//                                                if(checkedItems[j]){
//                                                    FirebaseMessaging.getInstance().subscribeToTopic(usertype_id+"_"+j);
//                                                }
//                                                else {
//                                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(usertype_id+"_"+j);
//                                                }
//
//                                            }
//                                        }
//                                    })
//                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int id) {
//
//                                        }
//                                    });
//
//                            multichoiceDialog = builder.create();
//                            getsubscriptions();
//                            subscriptions.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    multichoiceDialog.show();
//                                }
//                            });
//
//                            //Calling method getStudents to get the students from the JSON Array
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//
//        //Creating a request queue
//
//
//        //Adding request to the queue
//        queue.add(categoryRequest);
//
//    }
//
//    private void getsubscriptions(){
//        //Creating a string request
//
//        StringRequest subscriptionRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.getInfo_url),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject jsonResponse = null;
//                        try {
//                            //Parsing the fetched Json String to JSON Object
//                            jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//                            subsribed.clear();
//                            if (success) {
//                                JSONArray result = jsonResponse.getJSONArray("topics");
//                                final ArrayList<String> topics = new ArrayList<String>();
//                                for (int i = 0; i < result.length(); i++) {
//
//                                    //Getting json object
//                                    JSONObject obj = result.getJSONObject(i);
//
//                                    //Adding the name of the student to array list
//                                    String topic = obj.getString("topic");
//                                    int val = topic.indexOf(new String("_"));
//                                    if (val >= 0 && topic.length() > val ){
//                                        int sub = Integer.parseInt(topic.substring(val+1));
//                                        if (checkedItems.length > sub){
//                                            checkedItems[sub] = true;
//                                            multichoiceDialog.getListView().setItemChecked(sub,true);
//                                        }
//                                    }
//                                }
//                                boolean all = true;
//                                for (int k=1; k<checkedItems.length; k++){
//                                    if (!checkedItems[k]){
//                                        all = false;
//                                        break;
//                                    }
//                                }
//                                if (all){
//                                    checkedItems[0] = true;
//                                    multichoiceDialog.getListView().setItemChecked(0,true);
//                                }
//                            }
//
//                            //Calling method getStudents to get the students from the JSON Array
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener()
//        {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error
//
//            }
//        }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("user_id", user_id+"");
//
//                return params;
//            }
//
//        };
//
//        //Creating a request queue
//
//
//        //Adding request to the queue
//        queue.add(subscriptionRequest);
//
//    }

}