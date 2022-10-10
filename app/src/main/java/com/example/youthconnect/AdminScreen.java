package com.example.youthconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AdminScreen extends AppCompatActivity {
    public String user, admin;
    public String eventID, eventName, activities;
    public Button btnAdd, btnUpdate, btnDelete, btnModify, btnAddEvent, btnUpdateEvent, btnDeleteEvent, btnUpdateAdmin;
    public EditText edtUser, edtAdmin;
    public EditText edtEventName, edtActivities, edtDeleteID, edtEventID, edtUpdateEventName, edtUpdateActivities;
    public DatabaseReference studentDb, eventsDb, eventRef;
    public Intent intent;
    public AlertDialog dialog;
    public AlertDialog.Builder builder;
    public View viewAdd, viewUpdate, viewDelete, viewModify;
    public Events event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);
        btnAdd = (Button) findViewById(R.id.btnAddEvent);
        btnUpdate = (Button) findViewById(R.id.btnUpdateEvent);
        btnDelete = (Button) findViewById(R.id.btnDeleteEvent);
        btnModify = (Button) findViewById(R.id.btnUpdateType);
        studentDb = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("student_info");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddPopup();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUpdatePopup();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDeletePopup();
            }
        });
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createModifyPopup();
            }
        });
    }
    public void createAddPopup () {
        builder = new AlertDialog.Builder(this);
        final View addScreen = getLayoutInflater().inflate(R.layout.add_event, null);
        edtEventName = (EditText) addScreen.findViewById(R.id.edtAddEventName);
        edtActivities = (EditText) addScreen.findViewById(R.id.edtAddActivities);
        btnAddEvent = (Button) addScreen.findViewById(R.id.btnAddEvents);
        builder.setView(addScreen);
        dialog = builder.create();
        dialog.show();
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                int randID = rand.nextInt(1000);
                eventID = "EV" + randID;
                eventName = edtEventName.getText().toString();
                activities = edtActivities.getText().toString();
                eventsDb = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("events");
                boolean emptyFields = validateAddFields(eventName, activities);
                if (emptyFields){
                    Toast.makeText(getApplicationContext(), "Please fill in the fields", Toast.LENGTH_SHORT).show();
                } else{
                    addEventsActivities(eventID, eventName, activities);
                    Toast.makeText(getApplicationContext(), "You have succesfully added an event", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
    }
    public void createUpdatePopup(){
        builder = new AlertDialog.Builder(this);
        final View updateScreen = getLayoutInflater().inflate(R.layout.update_event, null);
        edtEventID = (EditText) updateScreen.findViewById(R.id.edtEventID);
        edtUpdateEventName = (EditText) updateScreen.findViewById(R.id.edtUpdateEventName);
        edtUpdateActivities = (EditText) updateScreen.findViewById(R.id.edtUpdateActivities);
        btnUpdateEvent = (Button) updateScreen.findViewById(R.id.btnUpdateEvents);
        builder.setView(updateScreen);
        dialog = builder.create();
        dialog.show();
        btnUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventID = edtEventID.getText().toString();
                eventName = edtUpdateEventName.getText().toString();
                activities = edtUpdateActivities.getText().toString();
                eventsDb = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("events");
                boolean emptyFields = validateUpdateFields(eventID, eventName, activities);
                if (emptyFields){
                    Toast.makeText(getApplicationContext(), "Please fill in the fields", Toast.LENGTH_SHORT).show();
                } else{
                    updateEventsActivities(eventID, eventName, activities);
                    Toast.makeText(getApplicationContext(), "You have succesfully updated an event", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
    }
    public void createDeletePopup (){
        builder = new AlertDialog.Builder(this);
        final View deleteScreen  = getLayoutInflater().inflate(R.layout.delete_event, null);
        edtDeleteID = (EditText) deleteScreen.findViewById(R.id.edtDeleteID);
        btnDeleteEvent = (Button) deleteScreen.findViewById(R.id.btnDeleteEvents);
        builder.setView(deleteScreen);
        dialog = builder.create();
        dialog.show();
        btnDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventID = edtDeleteID.getText().toString();
                eventsDb = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("events");
                boolean emptyFields = validateDeleteFields(eventID);
                if (emptyFields){
                    Toast.makeText(getApplicationContext(), "Please fill in the fields", Toast.LENGTH_SHORT).show();
                } else {
                    deleteEventsActivities(eventID);
                    Toast.makeText(getApplicationContext(), "You have succesfully deleted an event!", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
    }
    public void createModifyPopup (){
        builder = new AlertDialog.Builder(this);
        final View modifyScreen = getLayoutInflater().inflate(R.layout.change_user, null);
        edtUser = (EditText) modifyScreen.findViewById(R.id.edtUser);
        edtAdmin = (EditText) modifyScreen.findViewById(R.id.edtAdminStatus);
        btnUpdateAdmin = (Button) modifyScreen.findViewById(R.id.btnModifyUser);
        builder.setView(modifyScreen);
        dialog = builder.create();
        dialog.show();
        btnUpdateAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = edtUser.getText().toString();
                admin = edtAdmin.getText().toString();
                studentDb = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("student_info");
                boolean emptyFields = validateModifyFields(user, admin);
                if (emptyFields){
                    Toast.makeText(getApplicationContext(), "Please fill in the fields", Toast.LENGTH_SHORT).show();
                } else {
                    modifyUser(user, admin);
                    Toast.makeText(getApplicationContext(), "You have succesfully modified a user's status!", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
    }
    public void addEventsActivities(String id, String name, String act){
        eventsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(id)){
                    Toast.makeText(getApplicationContext(),"Event already added!", Toast.LENGTH_SHORT).show();
                    edtEventName.setText("");
                    edtActivities.setText("");
                } else {
                    eventsDb.child(id).child("eventName").setValue(name);
                    eventsDb.child(id).child("activities").setValue(act);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Cannot add event", error.toException());
            }
        });
    }
    public void updateEventsActivities (String id, String name, String act){
        eventsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(id)){
                    eventsDb.child(id).child("activities").setValue(act);
                    eventsDb.child(id).child("eventName").setValue(name);
                } else{
                    Toast.makeText(getApplicationContext(),"Event doesn't exist!", Toast.LENGTH_SHORT).show();
                    edtEventID.setText("");
                    edtUpdateEventName.setText("");
                    edtUpdateActivities.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Cannot update event", error.toException());
            }
        });
    }
    public void deleteEventsActivities (String id){
        eventsDb.child(id).removeValue();
    }
    public void modifyUser(String user, String admin){
        studentDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)){
                    studentDb.child(user).child("admin").setValue(admin);
                }
                else{
                    Toast.makeText(getApplicationContext(),"User doesn't exist", Toast.LENGTH_SHORT).show();
                    edtUser.setText("");
                    edtAdmin.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Cannot update admin status", error.toException());
            }
        });
    }
    public Boolean validateAddFields (String name, String act){
        return (name.equals("") || act.equals(""));
    }
    public Boolean validateUpdateFields (String id, String name, String act){
        return (id.equals("") || name.equals("") || act.equals(""));
    }
    public Boolean validateDeleteFields (String id){
        return (id.equals(""));
    }
    public Boolean validateModifyFields (String user, String admin){
        return (user.equals("") || admin.equals(""));
    }
}