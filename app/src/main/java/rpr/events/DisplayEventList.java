package rpr.events;

/**
 * Created by Vishal Singh on 02-04-2017.
 */

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.database.Cursor;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;



public class DisplayEventList extends Fragment {


    public SQLiteDatabase db;
    UserSessionManager session;

    private static Context context = null;
    private static final String TAG = UserProfile.class.getSimpleName();
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DETAILS = "details";
    private static String usertype_id;

    private static ProgressDialog pDialog;
    private RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private eventAdapter adapter;

    private int loadlimit;
    boolean loading;
    boolean error_load;
    private int category_id;

    // URL to get events JSON

    ArrayList<eventItem> eventList;

    protected void createDatabase(){
        db=getActivity().openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Events"+category_id+" (event_id INTEGER NOT NULL, category_id INTEGER NOT NULL, user_id INTEGER NOT NULL, usertype_id INTEGER NOT NULL, name VARCHAR NOT NULL, details VARCHAR NOT NULL, time TIMESTAMP NOT NULL,venue VARCHAR NOT NULL  );");
    }

    protected void insertIntoDB(String Event_id, String Category_id, String User_id, String Usertype_id, String Name, String Details, String Time, String Venue){

        String query = "INSERT INTO Events"+category_id+" (event_id,category_id,user_id,usertype_id,name, details, time, venue) VALUES('"+Event_id+"','"+Category_id+"','"+User_id+"','"+Usertype_id+"', '"+Name+"', '"+Details+"', '"+Time+"', '"+Venue+"');";

        db.execSQL(query);
    }
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        context = getActivity();

        session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);
        category_id =getArguments().getInt("category_id", 0);

        createDatabase();
        return inflater.inflate(R.layout.display_event_list_layout, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventList = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        loadlimit = 0;
        loading = false;
        error_load = false;
        load_data_from_server(loadlimit++);

        gridLayoutManager = new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new eventAdapter(context,eventList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), null));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(!error_load && !loading && gridLayoutManager.findLastCompletelyVisibleItemPosition() == eventList.size()-1){
                    loading = true;
                    load_data_from_server(loadlimit++);
                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        db.close();
    }


    private void load_data_from_server(final int id) {
        final ProgressDialog dialog = new ProgressDialog(context);

        StringRequest eventRequest= new StringRequest(Request.Method.POST, getResources().getString(R.string.listAll_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                JSONArray array = jsonResponse.getJSONArray("events");
                                db.execSQL("DELETE FROM Events"+category_id);
                                for (int i=0; i<array.length(); i++){

                                    JSONObject c = array.getJSONObject(i);

                                    eventItem data = new eventItem(c.getInt("event_id"),c.getInt("category_id"),c.getInt("user_id"),c.getInt("usertype_id"),c.getString("name"),
                                            c.getString("details"),c.getString("time"),c.getString("venue"));

                                    insertIntoDB(c.getString("event_id"),c.getString("category_id"),c.getString("user_id"),c.getString("usertype_id"),c.getString("name"), c.getString("details"),c.getString("time"),c.getString("venue"));

                                    eventList.add(data);

                                }

                                adapter.notifyDataSetChanged();
                                loading = false;

                            } else {
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
                if (!error_load){
                    error_load = true;
                    use_old_data();
                }


            }
        }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("limit", id+"");
                params.put("usertype_id", usertype_id+"");
                params.put("category_id", category_id+"");
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(eventRequest);


    }

    private void use_old_data(){
        String query = "SELECT * FROM Events"+category_id;
        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        //Move to the first row in your results
        recordSet.moveToFirst();


        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {

            eventItem data = new eventItem(recordSet.getInt(recordSet.getColumnIndex("event_id")),recordSet.getInt(recordSet.getColumnIndex("category_id")),recordSet.getInt(recordSet.getColumnIndex("user_id")),recordSet.getInt(recordSet.getColumnIndex("usertype_id")),recordSet.getString(recordSet.getColumnIndex("name")),
                    recordSet.getString(recordSet.getColumnIndex("details")),recordSet.getString(recordSet.getColumnIndex("time")),recordSet.getString(recordSet.getColumnIndex("venue")));


            // adding event to event list
            eventList.add(data);


            recordSet.moveToNext();

        }
        recordSet.close();
        adapter.notifyDataSetChanged();
    }




}