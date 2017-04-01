package rpr.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vishal Singh on 29-03-2017.
 */
public class ParseJSON {
    public static String[] event_id;
    public static String[] name;
    public static String[] time;
    public static String[] venue;
    public static String[] details;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TIME = "time";
    public static final String KEY_VENUE = "venue";
    public static final String KEY_DETAILS = "details";

    private JSONArray events = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            events = jsonObject.getJSONArray(JSON_ARRAY);

            event_id = new String[events.length()];
            name = new String[events.length()];
            time = new String[events.length()];
            details = new String[events.length()];
            venue = new String[events.length()];

            for(int i=0;i<events.length();i++){
                JSONObject jo = events.getJSONObject(i);
                event_id[i] = jo.getString(KEY_ID);
                name[i] = jo.getString(KEY_NAME);
                time[i] = jo.getString(KEY_TIME);
                details[i] = jo.getString(KEY_DETAILS);
                venue[i] = jo.getString(KEY_VENUE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
