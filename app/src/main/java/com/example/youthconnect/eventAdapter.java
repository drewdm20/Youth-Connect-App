package com.example.youthconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class eventAdapter extends FirebaseRecyclerAdapter<Events, eventAdapter.eventsViewHolder> {

    public eventAdapter (@NonNull FirebaseRecyclerOptions<Events> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull eventsViewHolder holder, int position, @NonNull Events model){
        holder.eventName.setText(model.getEventName());
        holder.activities.setText(model.getActivities());
    }

    @NonNull
    @Override
    public eventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false);
        return new eventAdapter.eventsViewHolder(view);
    }

    class eventsViewHolder extends RecyclerView.ViewHolder{
        TextView eventName, activities;
        public eventsViewHolder(@NonNull View itemView){
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            activities = itemView.findViewById(R.id.activities);
        }
    }
}
