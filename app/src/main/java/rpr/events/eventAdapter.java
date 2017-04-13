package rpr.events;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by naman on 13-04-2017.
 */

public class eventAdapter extends RecyclerView.Adapter<eventAdapter.ViewHolder>  {
    private Context context;
    private List<eventItem> event_data;

    public eventAdapter(Context context, List<eventItem> event_data) {
        this.context = context;
        this.event_data = event_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.name.setText(event_data.get(position).getName());
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event_data.get(position).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.time.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date));
        holder.venue.setText(event_data.get(position).getVenue());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDisplayUser.class);
                intent.putExtra("event_id", event_data.get(position).getEvent_id() + "");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return event_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView name;
        public TextView time;
        public TextView venue;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            venue = (TextView) itemView.findViewById(R.id.venue);

        }
    }
}
