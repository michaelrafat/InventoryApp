package com.example.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.Data.InventoryDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Product> productsList = new ArrayList<>();
    private InventoryDbHelper inventoryDbHelper;
    private FloatingActionButton addFAB;
    private ListView listView;
    private TextView appDescriptionTV;
    public static int productID;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inventoryDbHelper = new InventoryDbHelper(this);
        db = inventoryDbHelper.getWritableDatabase();

        addFAB = (FloatingActionButton) findViewById(R.id.add_fab_button);
        listView = (ListView) findViewById(R.id.list_view);
        appDescriptionTV = (TextView) findViewById(R.id.app_description);

        addInventoriesToListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                productID = position + 1;

                Intent intent = new Intent(MainActivity.this, InventoryDetails.class);
                startActivity(intent);
            }
        });

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InventoryAdder.class);
                startActivity(intent);
            }
        });
    }

    public void addInventoriesToListView() {

        Cursor data = inventoryDbHelper.selectData();
        Product product;

        if (data.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Empty Database !", Toast.LENGTH_LONG).show();
        } else {
            appDescriptionTV.setVisibility(View.GONE);
            while (data.moveToNext()) {
                product = new Product(data.getString(1), data.getString(2)+"$", Integer.parseInt(data.getString(3)));
                productsList.add(product);
                ProductAdapter productAdapter = new ProductAdapter(this, productsList);
                listView.setAdapter(productAdapter);
            }
        }
    }
}