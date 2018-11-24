package com.example.skett.abndp9inventoryapp2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skett.abndp9inventoryapp2.data.InventoryContract;

/**
 * {@link KicksCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of kicks data as its data source. This adapter knows
 * how to create list items for each row of kicks data in the {@link Cursor}.
 */

public class KicksCursorAdapter extends CursorAdapter {


    /**
     * Constructs a new {@link KicksCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public KicksCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);

    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout

        final Button buyButton = view.findViewById(R.id.sale);
        TextView prodNameTextView = view.findViewById(R.id.prodname_textview);
        TextView priceTextView = view.findViewById(R.id.price_textview);
        final TextView quantityTextView = view.findViewById(R.id.quantity_textview);
        final Button editButton = view.findViewById(R.id.editor_edit_button);

        // Find the columns of kicks attributes that we're interested in
        final int kicksIDIndex = cursor.getColumnIndex(InventoryContract.KicksEntry._ID);
        int pNameColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry.COLUMN_PRODUCT_NAME);
        int pPriceColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry.COLUMN_PRICE);
        int pQuantityColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry.COLUMN_QUANTITY);

        // Read the kicks attributes from the Cursor for the current shoe
        final int kicksID = cursor.getInt(kicksIDIndex);
        String kicksName = cursor.getString(pNameColumnIndex);
        String kicksPrice = cursor.getString(pPriceColumnIndex);
        final int kicksQuantity = cursor.getInt(pQuantityColumnIndex);

        // Update the TextViews with the attributes for the current shoe
        prodNameTextView.setText(kicksName);
        priceTextView.setText(kicksPrice);
        quantityTextView.setText(Integer.toString(kicksQuantity));


        final String kID = cursor.getString(cursor.getColumnIndex(InventoryContract.KicksEntry._ID));
        buyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (kicksQuantity > 1) {
                    Uri currentUri = ContentUris.withAppendedId(InventoryContract.KicksEntry.CONTENT_URI, Long.parseLong(kID));
                    ContentValues kValues = new ContentValues();
                    kValues.put(InventoryContract.KicksEntry.COLUMN_QUANTITY, kicksQuantity - 1);
                    context.getContentResolver().update(currentUri, kValues, null, null);
                    Toast.makeText(context, R.string.sold, Toast.LENGTH_SHORT).show();
                    swapCursor(cursor);
                }else {
                        Toast.makeText(context, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
                    }

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(view.getContext(), AddKicksActivity.class);
                        Uri currentKickUri = ContentUris.withAppendedId(InventoryContract.KicksEntry.CONTENT_URI, kicksID);
                        intent.setData(currentKickUri);
                        context.startActivity(intent);
                    }
                });

            }
        });




    }}
