package rpr.events;

/**
 * Created by Vishal Singh on 31-03-2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu1 extends Fragment {


    private Context context = null;

    public SQLiteDatabase db;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.tab_layout,null);

        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("List Events");


        context = getActivity();

        createDatabase();
        getcategory();



        return x;

    }

    protected void createDatabase(){
        db=getActivity().openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS category (category_id INTEGER NOT NULL, name VARCHAR NOT NULL);");
    }

    protected void insertIntoDB(String category_id, String name){
        String query = "INSERT INTO category (category_id,name) VALUES('"+category_id+"', '"+name+"');";
        db.execSQL(query);
    }



    private void getcategory(){
        //Creating a string request


        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.category_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                JSONArray result = jsonResponse.getJSONArray("category");
                                ArrayList<String> categories = new ArrayList<>();

                                db.execSQL("DELETE FROM category");
                                for(int i=0;i<result.length();i++) {

                                    //Getting json object
                                    JSONObject obj = result.getJSONObject(i);

                                    //Adding the name of the student to array list
                                    categories.add(obj.getString("name"));
                                    insertIntoDB(obj.getString("category_id"), obj.getString("name"));

                                }

                                db.close();
                                viewPager.setAdapter(new dynamicAdapter(getChildFragmentManager(),categories));

                                tabLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tabLayout.setupWithViewPager(viewPager);
                                    }
                                });
                            }
                            else{

                                use_old_category();
                            }




                            //Calling method getStudents to get the students from the JSON Array
                        } catch (JSONException e) {
                            use_old_category();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Couldn't connect to internet",Toast.LENGTH_SHORT).show();

                        use_old_category();
                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(usertypeRequest);

    }

    private void use_old_category(){
        String query = "SELECT * FROM category ORDER BY category_id ASC";
        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        if (recordSet.getCount() == 0){
            db.execSQL("DELETE FROM category");
            insertIntoDB("0", "All");
            insertIntoDB("1", "Cultural");
            insertIntoDB("2", "Technical");
            insertIntoDB("3", "Sports");
            insertIntoDB("4", "Seminar");

            recordSet = db.rawQuery(query, null);
        }
        //Move to the first row in your results
        recordSet.moveToFirst();
        ArrayList<String> categories = new ArrayList<>();
        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {


            categories.add(recordSet.getString(recordSet.getColumnIndex("name")));
            recordSet.moveToNext();

        }
        recordSet.close();
        db.close();

        viewPager.setAdapter(new dynamicAdapter(getChildFragmentManager(),categories));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

    }

    class dynamicAdapter extends FragmentPagerAdapter{

        ArrayList<String> pass_category;

        public dynamicAdapter(FragmentManager fm, ArrayList<String> category) {
            super(fm);
            this.pass_category = category;


        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = new Primary();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt("category_id", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {

            return pass_category.size();

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            return  pass_category.get(position);
        }
    }

}