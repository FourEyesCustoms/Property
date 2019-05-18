package com.example.propertyowner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;

public class AddNewProductActivity extends AppCompatActivity {


    private static final String TAG = "AddNewProductActivity";

    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        categoryName= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("category")).toString();

        toastMessage(categoryName + " has been clicked");

    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
