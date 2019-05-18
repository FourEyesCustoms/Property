package com.example.propertyowner.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.propertyowner.CategoryActivity;
import com.example.propertyowner.HomeActivity;
import com.example.propertyowner.Models.User;
import com.example.propertyowner.Prevalent.Prevalent;
import com.example.propertyowner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    //Widgets
    private EditText loginPhoneNumber;
    private EditText loginPassword;
    private TextView forgotPassword;
    private TextView landlordPanel;
    private TextView notLandlord;
    private CheckBox rememberMe;
    private Button loginBtn;
    private ProgressDialog progressDialog;

    //Vars
    private String parentDbName = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPhoneNumber=findViewById(R.id.login_phone_number);
        loginPassword=findViewById(R.id.login_password);
        forgotPassword=findViewById(R.id.forget_password);
        loginBtn=findViewById(R.id.login_button);
        landlordPanel=findViewById(R.id.landlord_panel_link);
        notLandlord=findViewById(R.id.not_landlord);
        rememberMe=findViewById(R.id.remember_me_checkbox);
        Paper.init(this);

        progressDialog=new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser(){

        String phone=loginPhoneNumber.getText().toString();
        String password=loginPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            toastMessage("Please fill in the phone number field");
        }
        else if (TextUtils.isEmpty(password)){
            toastMessage("Please fill in the password field");
        }
        else {
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Logging in into your account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            allowAccessToAccount(phone,password);
        }
    }

    private void allowAccessToAccount(final String phone, final String password) {

        if (rememberMe.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

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
                            Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
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







