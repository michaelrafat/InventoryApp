package com.example.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.Data.InventoryContract;
import com.example.android.inventoryapp.Data.InventoryDbHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class InventoryDetails extends AppCompatActivity {

    private TextView productNameTV, productPriceTV, productQuantityTV, deleteDialogTV;

    private Button increaseQuantityButton, decreaseQuantityButton, updateInventoryButton,
            deleteInventoryButton, deleteDialogNoButton, deleteDialogYesButton,
            orderButton;

    private ImageView productImage;

    private FloatingActionButton backButton;

    private InventoryDbHelper inventoryDbHelper;
    private MainActivity mainActivity = new MainActivity();
    private SQLiteDatabase db;
    private static int quantity;
    private static String orderMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details);

        inventoryDbHelper = new InventoryDbHelper(this);
        db = inventoryDbHelper.getWritableDatabase();
        Cursor cursor = null;

        productNameTV = (TextView) findViewById(R.id.name_details);
        productPriceTV = (TextView) findViewById(R.id.price_details);
        productQuantityTV = (TextView) findViewById(R.id.quantity_details);
        productImage = (ImageView) findViewById(R.id.product_picture);

        deleteDialogTV = (TextView) findViewById(R.id.delete_dialog_textView);
        deleteDialogNoButton = (Button) findViewById(R.id.delete_option_no);
        deleteDialogYesButton = (Button) findViewById(R.id.delete_option_yes);

        orderButton = (Button) findViewById(R.id.order_button);

        backButton = (FloatingActionButton) findViewById(R.id.details_back_fab_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"michealrafat@yahoo.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order Product");
                emailIntent.putExtra(Intent.EXTRA_TEXT, orderMessage);
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));

                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });

        try {

            cursor = inventoryDbHelper.selectInventory(mainActivity.productID);

            if (cursor.getCount() != 0) {
                cursor.moveToFirst();

                productNameTV.setText(cursor.getString(1));
                productPriceTV.setText(cursor.getString(2) + "$");
                productQuantityTV.setText(cursor.getString(3));

                byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_IMAGE));
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                productImage.setImageBitmap(bitmap);

                orderMessage = "Product Order : " + "\n" +
                        "Product Name : " + cursor.getString(1) + "\n"
                        + "Product Price : " + cursor.getString(2) + "$" + "\n"
                        + "Quantity : " + cursor.getString(3);

                quantity = cursor.getInt(3);
            }

        } catch (CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }

        increaseQuantityButton = (Button) findViewById(R.id.increase_inventory_button);
        decreaseQuantityButton = (Button) findViewById(R.id.decrease_inventory_button);

        updateInventoryButton = (Button) findViewById(R.id.update_inventory_button);
        deleteInventoryButton = (Button) findViewById(R.id.delete_inventory_button);

        updateInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inventoryDbHelper.updateInventory(quantity, mainActivity.productID);

                Toast.makeText(getApplicationContext(), "Product Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InventoryDetails.this, MainActivity.class);
                startActivity(intent);

            }
        });

        deleteInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDialogTV.setVisibility(View.VISIBLE);
                deleteDialogNoButton.setVisibility(View.VISIBLE);
                deleteDialogYesButton.setVisibility(View.VISIBLE);

                deleteDialogYesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteDialogTV.setVisibility(View.GONE);
                        deleteDialogNoButton.setVisibility(View.GONE);
                        deleteDialogYesButton.setVisibility(View.GONE);

                        quantity = 0;

                        inventoryDbHelper.deleteInventory(mainActivity.productID);

                        Toast.makeText(getApplicationContext(), "Product Deleted!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InventoryDetails.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                deleteDialogNoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteDialogTV.setVisibility(View.GONE);
                        deleteDialogNoButton.setVisibility(View.GONE);
                        deleteDialogYesButton.setVisibility(View.GONE);
                    }
                });
            }
        });

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity++;
                productQuantityTV.setText(String.valueOf(quantity));
            }
        });

        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity > 0) {

                    quantity--;
                    productQuantityTV.setText(String.valueOf(quantity));
                } else {
                    Toast.makeText(getApplicationContext(), "You have no quantity for this product!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}