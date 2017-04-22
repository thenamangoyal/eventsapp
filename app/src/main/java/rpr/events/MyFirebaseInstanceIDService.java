package rpr.events;


/**
 * Created by naman on 22-04-2017.
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.firebase_pref),MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        edit.putString(getString(R.string.firebase_token),token);
        edit.commit();


        UserSessionManager session = new UserSessionManager(getApplicationContext());
        if (session.isUserLoggedIn()) {
            HashMap<String, String> user = session.getUserDetails();
            final String user_id = user.get(UserSessionManager.KEY_USER_ID);
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
                    params.put("user_id", user_id);
                    params.put("token", token);


                    return params;
                }

            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(fcmRequest);
        }
    }
}