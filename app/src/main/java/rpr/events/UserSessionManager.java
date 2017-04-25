package rpr.events;


import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class UserSessionManager {

        // Shared Preferences reference
        SharedPreferences pref;

        // Editor reference for Shared preferences
        Editor editor;

        // Context
        Context _context;

        // Shared pref mode
        int PRIVATE_MODE = 0;

        // Sharedpref file name
        private static final String PREFER_NAME = "eventsApp";

        // All Shared Preferences Keys
        private static final String IS_USER_LOGIN = "IsUserLoggedIn";

        // User name (make variable public to access from outside)
        public static final String KEY_NAME = "name";

        // Email address (make variable public to access from outside)
        public static final String KEY_EMAIL = "email";

        public static final String KEY_USER_ID = "user_id";

        public static final String KEY_USERTYPE = "usertype";
        public static final String KEY_USERTYPE_ID = "usertype_id";
        public static final String KEY_USERTYPES = "usertypes";
        public static final String KEY_CREATED = "created";

        // Constructor
        public UserSessionManager(Context context){
            this._context = context;
            pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

        //Create login session
        public void createUserLoginSession(String name, String email, int user_id, int usertype_id, String usertype, int usertypes, String created){
            // Storing login value as TRUE
            editor.putBoolean(IS_USER_LOGIN, true);

            // Storing name in pref
            editor.putString(KEY_NAME, name);

            // Storing email in pref
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_USER_ID, user_id+"");
            editor.putString(KEY_USERTYPE_ID, usertype_id+"");
            editor.putString(KEY_USERTYPE, usertype);
            editor.putString(KEY_USERTYPES, usertypes+"");
            editor.putString(KEY_CREATED, created);


            // commit changes
            editor.commit();
        }

        /**
         * Check login method will check user login status
         * If false it will redirect user to login page
         * Else do anything
         * */
        public boolean checkLogin(){
            // Check login status
            if(!this.isUserLoggedIn()){

                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(_context, LoginActivity.class);

                // Closing all the Activities from stack
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                _context.startActivity(i);

                return true;
            }
            return false;
        }



        /**
         * Get stored session data
         * */
        public HashMap<String, String> getUserDetails(){

            //Use hashmap to store user credentials
            HashMap<String, String> user = new HashMap<String, String>();

            // user name
            user.put(KEY_NAME, pref.getString(KEY_NAME, null));

            // user email id
            user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

            user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

            user.put(KEY_USERTYPE_ID, pref.getString(KEY_USERTYPE_ID, null));
            user.put(KEY_USERTYPE, pref.getString(KEY_USERTYPE, null));
            user.put(KEY_USERTYPES, pref.getString(KEY_USERTYPES, null));
            user.put(KEY_CREATED, pref.getString(KEY_CREATED, null));

            // return user
            return user;
        }

        /**
         * Clear session details
         * */
        public void logoutUser(){

            // Clearing all user data from Shared Preferences
            editor.clear();
            editor.commit();

            // After logout redirect user to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }


        // Check for login
        public boolean isUserLoggedIn(){
            return pref.getBoolean(IS_USER_LOGIN, false);
        }

}
