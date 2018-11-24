package com.example.skett.abndp9inventoryapp2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.skett.abndp9inventoryapp2.data.InventoryContract;

public class AddKicksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the kicks data loader
     */
    private static final int EXISTING_KICKS_LOADER = 0;

    /**
     * Content URI for the existing kick (null if it's a new shoe)
     */
    private Uri mCurrentKickUri;

    /**
     * EditText field to enter shoe name
     */
    private EditText mNameEditText;
    /**
     * EditText field to enter shoe price
     */
    private EditText mPriceEditText;
    /**
     * EditText field to enter number in stock
     */
    private EditText mQuantityEditText;
    /**
     * EditText field to enter supplier name
     */
    private EditText mSuppNameEditText;
    /**
     * EditText field to enter supplier phone number
     */
    private EditText mSuppPhoneEditText;

    /**
     * Boolean flag that keeps track of whether kicks data has been edited (true) or not (false).
     */
    private Boolean mKicksHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mKicksHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mKicksHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_kicks);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new shoe or editing an existing one.
        Intent intent = getIntent();
        mCurrentKickUri = intent.getData();

        // If the intent DOES NOT contain a shoe content URI, then we know that we are
        // creating a new shoe.
        if (mCurrentKickUri == null) {
            // This is a new pet, so change the app bar to say "Add a Shoe"
            setTitle(getString(R.string.add_kicks));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a shoe that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing shoe, so change app bar to say "Edit Shoe"
            setTitle(getString(R.string.edit_shoe));

            // Initialize a loader to read the pet data from the database
            // and display the current values in the editor
            android.support.v4.app.LoaderManager.getInstance(this).initLoader(EXISTING_KICKS_LOADER, null, this).forceLoad();
        }
        //Find all relevant views that we need to read user input from
        mNameEditText = findViewById(R.id.edit_shoe_name);
        mPriceEditText = findViewById(R.id.edit_shoe_price);
        mQuantityEditText = findViewById(R.id.quantity_text_view);
        mSuppNameEditText = findViewById(R.id.edit_supplier_name);
        mSuppPhoneEditText = findViewById(R.id.edit_supplier_phone);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSuppNameEditText.setOnTouchListener(mTouchListener);
        mSuppPhoneEditText.setOnTouchListener(mTouchListener);

    };
    /**
     * Get user input from editor and save shoe into database.
     */
    private void saveKick() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        String quantityString = mQuantityEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        String snameString = mSuppNameEditText.getText().toString().trim();
        String sphoneString = mSuppPhoneEditText.getText().toString().trim();

        if (mCurrentKickUri == null) {
            if (TextUtils.isEmpty(nameString)) {
                Toast.makeText(this, getString(R.string.name_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(priceString)) {
                Toast.makeText(this, getString(R.string.price_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(quantityString)) {
                Toast.makeText(this, getString(R.string.quantity_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(snameString)) {
                Toast.makeText(this, getString(R.string.supplier_name_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(sphoneString)) {
                Toast.makeText(this, getString(R.string.phone_needed), Toast.LENGTH_SHORT).show();
                return;
            }


            //Create ContentValues object where column names are keys and shoe attributes are editor values
            ContentValues values = new ContentValues();
            values.put(InventoryContract.KicksEntry.COLUMN_PRODUCT_NAME, nameString);
            values.put(InventoryContract.KicksEntry.COLUMN_PRICE, price);
            values.put(InventoryContract.KicksEntry.COLUMN_QUANTITY, quantity);
            values.put(InventoryContract.KicksEntry.COLUMN_SUPPLIER_NAME, snameString);
            values.put(InventoryContract.KicksEntry.COLUMN_SUPPLIER_PHONE, sphoneString);


            Uri newUri = getContentResolver().insert(InventoryContract.KicksEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.shoe_saved), Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            if (TextUtils.isEmpty(nameString)) {
                Toast.makeText(this, getString(R.string.name_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(priceString)) {
                Toast.makeText(this, getString(R.string.price_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(quantityString)) {
                Toast.makeText(this, getString(R.string.quantity_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(snameString)) {
                Toast.makeText(this, getString(R.string.supplier_name_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(sphoneString)) {
                Toast.makeText(this, getString(R.string.phone_needed), Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues values = new ContentValues();
            values.put(InventoryContract.KicksEntry.COLUMN_PRODUCT_NAME, nameString);
            values.put(InventoryContract.KicksEntry.COLUMN_PRICE, price);
            values.put(InventoryContract.KicksEntry.COLUMN_QUANTITY, quantity);
            values.put(InventoryContract.KicksEntry.COLUMN_SUPPLIER_NAME, snameString);
            values.put(InventoryContract.KicksEntry.COLUMN_SUPPLIER_PHONE, sphoneString);
            // Otherwise this is an EXISTING shoe, so update the shoe with content URI: mCurrentKickUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentKickUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentKickUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.shoe_updated), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu options from res/menu/inventory_editor.xml
        getMenuInflater().inflate(R.menu.inventory_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            //Respond to a click on the "save" option
            case R.id.action_save:
                //Save shoe to database
                saveKick();
                return true;

            //Respond to a click on the "up" arrow button in the app bar
            case android.R.id.home:
                // If the shoe hasn't changed, continue with navigating up to parent activity
                // which is the {@link MainActivity}.
                if (!mKicksHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddKicksActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, navigate to parent activity.
                        NavUtils.navigateUpFromSameTask(AddKicksActivity.this);
                    }
                };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mKicksHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "Discard" button, close the current activity.
                finish();
            }
        };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        // Since the editor shows all shoe attributes, define a projection that contains
        // all columns from the kicks table
        String[] projection = {InventoryContract.KicksEntry._ID, InventoryContract.KicksEntry.COLUMN_PRODUCT_NAME, InventoryContract.KicksEntry.COLUMN_PRICE, InventoryContract.KicksEntry.COLUMN_QUANTITY, InventoryContract.KicksEntry.COLUMN_SUPPLIER_NAME, InventoryContract.KicksEntry.COLUMN_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new android.support.v4.content.CursorLoader(this,   // Parent activity context
                mCurrentKickUri,         // Query the content URI for the current shoe
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Quit early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of shoe attributes that we're interested in
            final int idColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry.COLUMN_QUANTITY);
            int snameColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry.COLUMN_SUPPLIER_NAME);
            int sphoneColumnIndex = cursor.getColumnIndex(InventoryContract.KicksEntry.COLUMN_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            final int quantity = cursor.getInt(quantityColumnIndex);
            String suppName = cursor.getString(snameColumnIndex);
            final int suppPhone = cursor.getInt(sphoneColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSuppNameEditText.setText(suppName);
            mSuppPhoneEditText.setText(Integer.toString(suppPhone));
            Button decreaseButton = findViewById(R.id.decrement_id);
            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String kQuantity = mQuantityEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(kQuantity)) {
                        Context context = getApplicationContext();
                        CharSequence text = "Please add a shoe first.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return;
                    }
                    int quantity = Integer.parseInt(kQuantity);
                    if (quantity <= 0) {
                        Context context = getApplicationContext();
                        CharSequence text = "Sorry! No negatives!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {
                        mQuantityEditText.setText(String.valueOf(quantity--));
                        ContentValues values = new ContentValues();
                        values.put(InventoryContract.KicksEntry.COLUMN_QUANTITY, quantity);
                        getContentResolver().update(mCurrentKickUri, values, null, null);
                    }
                    Button increaseButton = findViewById(R.id.increment_id);
                    increaseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String kQuantity = mQuantityEditText.getText().toString().trim();
                            if (TextUtils.isEmpty(kQuantity)) {
                                Context context = getApplicationContext();
                                CharSequence text = "Please add a shoe first";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();

                            } else {
                                int quantity = Integer.parseInt(kQuantity);
                                mQuantityEditText.setText(String.valueOf(quantity++));
                                ContentValues values = new ContentValues();
                                values.put(InventoryContract.KicksEntry.COLUMN_QUANTITY, quantity);
                                getContentResolver().update(mCurrentKickUri, values, null, null);
                            }
                        }
                    });

                    Button eDeleteButton = findViewById(R.id.editor_delete_button);
                    eDeleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDeleteConfirmationDialog();
                        }
                    });


                    Button orderButton = findViewById(R.id.order_button);
                    orderButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String phone = Integer.toString(suppPhone);
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                            startActivity(intent);
                        }

                    });
                }
            });

        }}
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSuppNameEditText.setText("");
        mSuppPhoneEditText.setText("");
    }


    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.quit_edit));
        builder.setPositiveButton(getString(R.string.discard), discardButtonClickListener);
        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_one);
        builder.setPositiveButton(R.string.editor_delete_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteKick();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteKick() {
        if (mCurrentKickUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentKickUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_successful), Toast.LENGTH_SHORT).show();

            }
        }
        finish();
    }
}