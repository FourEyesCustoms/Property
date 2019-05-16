package com.example.propertyowner.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.propertyowner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    //Widgets
    private Button registerBtn;
    private EditText userName;
    private EditText phoneNumber;
    private EditText newPassword;
    private EditText confirmPassword;

    //Vars
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        hideSoftInputKeyboard();

        registerBtn=findViewById(R.id.register_button);
        userName=findViewById(R.id.register_user_name);
        phoneNumber=findViewById(R.id.register_phone_number);
        newPassword=findViewById(R.id.register_password);
        confirmPassword=findViewById(R.id.confirm_register_password);

        progressDialog=new ProgressDialog(this);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Register button clicked");
                hideSoftInputKeyboard();
                createAccount();
            }
        });
    }

    private void createAccount(){
        Log.d(TAG, "createAccount: Create New Account");
        String registerName= userName.getText().toString();
        String registerPhoneNumber=phoneNumber.getText().toString();
        String registerPassword=newPassword.getText().toString();
        String confirmRegisterPassword=confirmPassword.getText().toString();

        if (TextUtils.isEmpty(registerName)){
            toastMessage("User Name field cannot be empty");
        }
        else if (TextUtils.isEmpty(registerPhoneNumber)){
            toastMessage("Phone number field cannot be empty");
        }
        else if (TextUtils.isEmpty(registerPassword)){
            toastMessage("Please Enter your password");
        }
        else if (TextUtils.isEmpty(confirmRegisterPassword)){
            toastMessage("Confirm your password");
        }
        else {
            progressDialog.setTitle("Create Account");
            progressDialog.setMessage("Registering Your Account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            validatePhone(registerName,registerPhoneNumber,registerPassword);

        }
    }

    private void validatePhone(final String registerName, final String phone, final String registerPassword){
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("users").child(phone).exists())){

                    HashMap<String ,Object> userMap = new HashMap<>();
                    userMap.put("phone",phone);
                    userMap.put("name",registerName);
                    userMap.put("password",registerPassword);

                    rootRef.child("users").child(phone).updateChildren(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        toastMessage("Account Created successfully");
                                        progressDialog.dismiss();
                                       /* Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent); */
                                       finish();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        String message= Objects.requireNonNull(task.getException()).getMessage();
                                        toastMessage("Error: "+message);
                                    }
                                }
                            });
                }
                else {
                    toastMessage("This Phone number already exists");
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
    private void hideSoftInputKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
