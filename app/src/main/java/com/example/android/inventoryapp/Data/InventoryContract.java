package com.example.android.inventoryapp.Data;

import android.provider.BaseColumns;

/**
 * Created by miche on 7/24/2017.
 */

public class InventoryContract {

    public InventoryContract() {

    }

    public static final class InventoryEntry implements BaseColumns {

        public final static String TABLE_NAME = "inventory";


        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_INVENTORY_NAME = "name";

        public final static String COLUMN_INVENTORY_PRICE = "price";

        public static final String COLUMN_INVENTORY_IMAGE = "quantity";

        public static final String COLUMN_INVENTORY_QUANTITY = "image";

    }
}