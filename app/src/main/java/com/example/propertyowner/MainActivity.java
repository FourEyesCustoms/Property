package com.example.propertyowner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.propertyowner.Authentication.LoginActivity;
import com.example.propertyowner.Authentication.RegisterActivity;
import com.example.propertyowner.Models.User;
import com.example.propertyowner.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //widgets
    private Button loginBtn;
    private Button registerBtn;
    private ProgressDialog progressDialog;

    //Vars
    private String parentDbName = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: New session started");

        Paper.init(this);

        loginBtn=findViewById(R.id.login_btn);
        registerBtn=findViewById(R.id.register_btn);
        progressDialog = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Login button clicked...");
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Register Button clicked");
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey=Paper.book().read(Prevalent.userPasswordKey);
        if (userPhoneKey!=null&&userPasswordKey!=null){
            if (!TextUtils.isEmpty(userPhoneKey)&&!TextUtils.isEmpty(userPasswordKey)){

                allowAccess(userPhoneKey,userPasswordKey);
                progressDialog.setTitle("Already logged in");
                progressDialog.setMessage("Please wait a moment");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

    }

    private void allowAccess(final String phone, final String password) {
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(phone).exists()){

                    User usersData=dataSnapshot.child(parentDbName).child(phone).getValue(User.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            toastMessage("Logged in successfully");
                            progressDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            toastMessage("Wrong password");
                        }
                    }
                }
                else{
                    toastMessage("The phone number: "+phone +"Is not registered");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
