package com.example.android.inventoryapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.inventoryapp.Data.InventoryDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.BindException;

public class InventoryAdder extends AppCompatActivity {

    private Button addInventoryButton;
    private EditText productNameET, productPriceET, productQuantityET;
    private InventoryDbHelper inventoryDbHelper;
    private ImageView productImageAdder;
    private FloatingActionButton backButton;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);

        inventoryDbHelper = new InventoryDbHelper(this);

        productNameET = (EditText) findViewById(R.id.name_editText);
        productPriceET = (EditText) findViewById(R.id.price_editText);
        productQuantityET = (EditText) findViewById(R.id.quantity_editText);

        addInventoryButton = (Button) findViewById(R.id.add_inventory_button);
        productImageAdder = (ImageView) findViewById(R.id.product_image_adder);

        backButton = (FloatingActionButton) findViewById(R.id.adder_back_fab_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        Button addImageButton = (Button) findViewById(R.id.add_image);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        addInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = null;
                String price = null;
                String quantity = null;

                name = productNameET.getText().toString().trim();
                price = productPriceET.getText().toString().trim();
                quantity = productQuantityET.getText().toString().trim();


                if ((name.length() != 0 && name != null) && (price.length() != 0 && price != null) && quantity != null && quantity.length() != 0 && image != null) {

                    if (inventoryDbHelper.insertInventory(name, price, quantity, image)) {
                        Toast.makeText(getApplicationContext(), "Inventory Added !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InventoryAdder.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Inventory does not added!", Toast.LENGTH_LONG).show();
                        productNameET.setError("Required");
                        productQuantityET.setError("Required");
                        productPriceET.setError("Required");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Required fields missed!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            try {
                Bundle bundle = data.getExtras();
                Bitmap imageBitmap = (Bitmap) bundle.get("data");

                productImageAdder.setImageBitmap(imageBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

                image = stream.toByteArray();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }
}