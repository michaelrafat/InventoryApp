package com.example.android.inventoryapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.inventoryapp.Data.InventoryContract;
import com.example.android.inventoryapp.Data.InventoryDbHelper;

import java.util.ArrayList;

/**
 * Created by miche on 7/6/2017.
 */

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    Cursor cursor;
    InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(getContext());


    public ProductAdapter(@NonNull Context context, ArrayList<Product> productsList) {
        super(context, 0, productsList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Product product = getItem(position);
        ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();

            holder.productName = (TextView) convertView.findViewById(R.id.product_name_lv);

            holder.productPrice = (TextView) convertView.findViewById(R.id.product_price_lv);

            holder.productQuantity = (TextView) convertView.findViewById(R.id.product_quantity_lv);

            holder.productImage = (ImageView) convertView.findViewById(R.id.image_listView);

            holder.saleProduct = (Button) convertView.findViewById(R.id.sale_button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));

        cursor = inventoryDbHelper.selectInventory(position + 1);
        if (cursor.moveToFirst()) {
            byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_IMAGE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            holder.productImage.setImageBitmap(bitmap);
        }

        holder.saleProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity;
                cursor = inventoryDbHelper.selectInventory(position + 1);

                if (cursor.moveToFirst()) {

                    quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY));

                    if (quantity > 0) {

                        quantity--;
                        inventoryDbHelper.updateInventory(quantity, position + 1);

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        context.startActivity(intent);

                        Toast.makeText(getContext(), "1 Product Sold!", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "No product Found!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }


    static class ViewHolder {

        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        Button saleProduct;
        ImageView productImage;
    }
}