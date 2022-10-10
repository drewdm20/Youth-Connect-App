package com.example.youthconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AffiliateScreen extends AppCompatActivity {
    public AlertDialog dialog;
    public AlertDialog.Builder builder;
    public EditText edtOldUser, edtNewPassword;
    public Button btnUpdate, btnViewEvents, btnUpdateDetails;
    public Intent intent;
    public DatabaseReference studentDb;
    public String user, newPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate_screen);
        btnViewEvents = (Button) findViewById(R.id.btnViewEvents);
        btnUpdateDetails = (Button) findViewById(R.id.btnUpdateDetails);
        btnViewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToViewEvents();
            }
        });
        btnUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });
    }
    public void createPopup (){
        builder = new AlertDialog.Builder(this);
        final View updateDetails = getLayoutInflater().inflate(R.layout.activity_update_login_details, null);
        edtOldUser = (EditText) updateDetails.findViewById(R.id.edtOldUsername);
        edtNewPassword = (EditText) updateDetails.findViewById(R.id.edtNewPassword);
        btnUpdate = (Button) updateDetails.findViewById(R.id.btnUpdateUser);
        builder.setView(updateDetails);
        dialog = builder.create();
        dialog.show();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = edtOldUser.getText().toString();
                newPassword = edtNewPassword.getText().toString();
                studentDb = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("student_info");
                boolean emptyFields = validateFields(user, newPassword);
                if(emptyFields){
                    Toast.makeText(getApplicationContext(), "Please fill in the fields", Toast.LENGTH_SHORT).show();
                } else{
                    updateUser(user, newPassword);
                }
            }
        });
    }
    public void updateUser(String username, String newPassword){
        studentDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)){
                    studentDb.child(username).child("password").setValue(newPassword);
                    Toast.makeText(getApplicationContext(), "Your details have successfully been updated", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    goToLogin();
                } else{
                    Toast.makeText(getApplicationContext(), "Failed to update details. Please check username", Toast.LENGTH_SHORT).show();
                    edtOldUser.setText("");
                    edtNewPassword.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Cannot update user", error.toException());
            }
        });
    }
    public void goToViewEvents (){
        startActivity(intent = new Intent(getApplicationContext(), ViewEventsActivities.class));
    }
    public void goToLogin(){
        startActivity(intent = new Intent(getApplicationContext(), LoginScreen.class));
        finish();
    }
    public Boolean validateFields(String username, String password){
        return (username.equals("")) || (password.equals(""));
    }
}