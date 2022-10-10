package com.example.youthconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewEventsActivities extends AppCompatActivity {
    RecyclerView rview;
    eventAdapter adapter;
    DatabaseReference eventDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events_activities);
        eventDB = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("events");
        rview = findViewById(R.id.recycler1);
        rview.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Events> options = new FirebaseRecyclerOptions.Builder<Events>().setQuery(eventDB, Events.class).build();
        adapter = new eventAdapter(options);
        rview.setAdapter(adapter);
    }
    @Override protected void onStart(){
        super.onStart();
        adapter.startListening();
    }
    @Override protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}