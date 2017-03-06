package rpr.events;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by naman on 06-03-2017.
 */

public class NewEventRequest extends StringRequest {
    private static final String REQUEST_URL = "http://10.1.1.19/~2015csb1021/php/AddEvent.php";
    private Map<String, String> params;

    public NewEventRequest(String event_name, int user_id, int category, int visible, String venue, String datetime, String details , Response.Listener<String> listener) {
        super(Request.Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("event_name", event_name);
        params.put("user_id", user_id+"");
        params.put("category", category+"");
        params.put("visible", visible+"");
        params.put("venue", venue);
        params.put("time", datetime);
        params.put("details", details);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}