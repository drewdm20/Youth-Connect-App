package com.example.youthconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RegisterScreen extends AppCompatActivity {
    public EditText edtUsername, edtName, edtSurname, edtPassword;
    public CheckBox chkAdmin;
    public Button btnRegister;
    public Intent intent;
    public Student student;
    public DatabaseReference studentDb;
    public String uName, name, surname, password, admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtName = (EditText) findViewById(R.id.edtName);
        edtSurname = (EditText) findViewById(R.id.edtSurname);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        chkAdmin = (CheckBox) findViewById(R.id.chkAdmin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uName = edtUsername.getText().toString();
                name = edtName.getText().toString();
                surname = edtSurname.getText().toString();
                password = edtPassword.getText().toString();
                admin = (chkAdmin.isChecked()) ? "admin": "affiliate";
                student = new Student(admin, name, password, surname);
                studentDb = FirebaseDatabase.getInstance().getReference().child("youth_connect_db").child("student_info");
                boolean emptyFields = validateFields(uName, admin, name, password, surname);
                if (emptyFields){
                    Toast.makeText(getApplicationContext(), " Please fill in the fields!", Toast.LENGTH_SHORT).show();
                } else{

                    registerNewUser(uName, admin, name, password, surname);
                }

            }
        });
    }
    public void registerNewUser ( String username, String admin, String name, String password, String surname){
        studentDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)){
                    Toast.makeText(getApplicationContext(),"Username already taken!", Toast.LENGTH_SHORT).show();
                    edtUsername.setText("");
                } else {
                    student.setAdmin(admin);
                    student.setName(name);
                    student.setPassword(password);
                    student.setsName(surname);
                    studentDb.child(username).setValue(student);
                    Toast.makeText(getApplicationContext(), "You have successfully been registered. Please login", Toast.LENGTH_SHORT).show();
                    goToLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Cannot register user", error.toException());
            }
        });
    }
    public Boolean validateFields(String username, String password, String admin, String name, String surname){
        return (username.equals("")) || (password.equals("") || admin.equals("") || name.equals("") || surname.equals(""));
    }
    public void goToLogin () {
        startActivity(intent = new Intent(getApplicationContext(), LoginScreen.class));
        finish();
    }
}