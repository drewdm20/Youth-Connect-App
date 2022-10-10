package com.example.youthconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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

public class LoginScreen extends AppCompatActivity {
    public EditText edtUsername, edtPassword;
    public Button btnLogin;
    public Intent intent;
    public DatabaseReference studentDb;
    public String getPassword, getUserType, uName, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        edtUsername = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtUserPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentDb = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("student_info");
                uName = edtUsername.getText().toString();
                pass = edtPassword.getText().toString();
                boolean emptyFields = validateFields(uName, pass);
                if (emptyFields){
                    Toast.makeText(LoginScreen.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
                } else{
                    validateUser(uName, pass);
                }
            }
        });
    }
    public Boolean validateFields(String username, String password){
        return (username.equals("")) || (password.equals(""));
    }
    public void validateUser(String username, String password){
        studentDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(username)){
                    getPassword = snapshot.child(username).child("password").getValue(String.class);
                    getUserType = snapshot.child(username).child("admin").getValue(String.class);
                    if(getPassword.equals(pass)){
                        if (getUserType.equals("admin")){
                            Toast.makeText(getApplicationContext(),"Welcome admin!", Toast.LENGTH_SHORT).show();
                            goToAdminScreen();
                        }else {
                            Toast.makeText(getApplicationContext(),"Welcome affiliate!", Toast.LENGTH_SHORT).show();
                            goToAffiliateScreen();
                        }
                    } else{
                        Toast.makeText(getApplicationContext(),"Incorrect username and/or password! Please try again", Toast.LENGTH_SHORT).show();
                        edtUsername.setText("");
                        edtPassword.setText("");
                    }
                } else {
                    Toast.makeText(LoginScreen.this,"Incorrect username and/or password! Please try again", Toast.LENGTH_SHORT).show();
                    edtUsername.setText("");
                    edtPassword.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value", error.toException());
            }
        });
    }
    public void goToAdminScreen(){
        startActivity(intent = new Intent(getApplicationContext(), AdminScreen.class));
        finish();
    }
    public void goToAffiliateScreen(){
        startActivity(intent = new Intent(getApplicationContext(), AffiliateScreen.class));
        finish();
    }
}