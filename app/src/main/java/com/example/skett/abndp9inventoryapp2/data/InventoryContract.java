package com.example.skett.abndp9inventoryapp2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.skett.abndp9inventoryapp2";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_KICKS = "kicks";

    /**
     * The MIME type of the {CONTENT_URI} for a list of kicks.
     */
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_KICKS;

    /**
     * The MIME type of the {CONTENT_URI} for a single shoe.
     */
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_KICKS;

    /**
     * Inner class that defines constant values for the kicks database table.
     * Each entry in the table represents a single shoe.
     */

    /*Inner class below defines the table contents of the Kicks table*/
    public static final class KicksEntry implements BaseColumns {

        /** The content URI to access the kicks data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_KICKS);


        //Table Name
        public static final String TABLE_NAME = "Kicks";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_PRODUCT_NAME = "Name";

        public static final String COLUMN_PRICE = "Price";

        public static final String COLUMN_QUANTITY = "Quantity";

        public static final String COLUMN_SUPPLIER_NAME = "SupplierName";

        public static final String COLUMN_SUPPLIER_PHONE = "SupplierPhone";
    }}
