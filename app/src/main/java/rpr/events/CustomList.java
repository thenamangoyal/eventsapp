package rpr.events;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Vishal Singh on 29-03-2017.
 */
public class CustomList extends ArrayAdapter<String> {
    private String[] event_id;
    private String[] name;
    private String[] time;
    private String[] venue;
    private String[] details;
    private Activity context;

    public CustomList(Activity context, String[] event_id, String[] name, String[] time, String[] venue, String[] details) {
        super(context, R.layout.activity_event_display_user, event_id);
        this.context = context;
        this.event_id = event_id;
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.details = details;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_event_display_user, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.name);
        TextView textViewDetails = (TextView) listViewItem.findViewById(R.id.details);
        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.time);
        TextView textViewVenue = (TextView) listViewItem.findViewById(R.id.venue);

        textViewName.setText(name[position]);
        textViewDetails.setText(details[position]);
        textViewTime.setText(time[position]);
        textViewVenue.setText(venue[position]);

        return listViewItem;
    }
}
