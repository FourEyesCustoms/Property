package com.example.propertyowner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";

    //Widgets
    private ImageView rentalHouses;
    private ImageView housesForSale;
    private ImageView landForSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        rentalHouses=findViewById(R.id.house_rental_id);
        housesForSale=findViewById(R.id.house_for_sale_id);
        landForSale=findViewById(R.id.land_for_sale);

        rentalHouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","rentalHouses");
                startActivity(intent);
            }
        });

        housesForSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","housesForSale");
                startActivity(intent);
            }
        });

        landForSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","landForSale");
                startActivity(intent);
            }
        });


    }
}
