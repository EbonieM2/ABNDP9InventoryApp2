package com.example.skett.abndp9inventoryapp2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.skett.abndp9inventoryapp2.data.InventoryContract;

import static com.example.skett.abndp9inventoryapp2.data.InventoryContract.KicksEntry.CONTENT_URI;


/**
 * Displays the list of items in stock that were entered and stored in the app.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int KICKS_LOADER = 0;

    KicksCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup FAB to open AddKicksActivity
        FloatingActionButton flab = findViewById(R.id.flab);
        flab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddKicksActivity.class);
                startActivity(intent);
            }
        });

        //Find the ListView which will be populated with the kicks data
        ListView kicksListView = findViewById(R.id.list);

        //Find and set empty view on the ListView so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        kicksListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of kicks data in the Cursor.
        mCursorAdapter = new KicksCursorAdapter(this, null);
        kicksListView.setAdapter(mCursorAdapter);

        //Setup item click listener
        kicksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                //Create a new intent to go to the AddKicksActivity
                Intent intent = new Intent(MainActivity.this, AddKicksActivity.class);

                //Form the content URI that represents the specific shoe that was clicked on,
                // by appending the "id" (passed as input to this method) onto the CONTENT_URI.
                Uri currentKicksUri = ContentUris.withAppendedId(CONTENT_URI, id);

                //Set the URI on the data field of the intent
                intent.setData(currentKicksUri);

                //Launch the EditorActivity to display the data for the current pet.
                startActivity(intent);
            }
        });
        //Start the Loader
        android.support.v4.app.LoaderManager.getInstance(this).initLoader(KICKS_LOADER, null, this).forceLoad();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Define a projection that specifies the columns from the table we care about.
        String[] projection = {InventoryContract.KicksEntry._ID, InventoryContract.KicksEntry.COLUMN_PRODUCT_NAME, InventoryContract.KicksEntry.COLUMN_PRICE, InventoryContract.KicksEntry.COLUMN_QUANTITY};

        //This loader will execute the ContentProvider's query method on a background thread
        return new android.support.v4.content.CursorLoader(this, CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //Update the KicksCursorAdapter with the new cursor containing updated Kicks data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    /**
     * Helper method to delete all kicks in the database.
     */
    private void deleteAllKicks() {
        int rowsDeleted = getContentResolver().delete(CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from kicks database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu options from the res/menu/inventory_menu.xml file.
        //This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.inventory_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //User clicked on a menu option in the overflow menu
        switch (item.getItemId()) {
            //Respond to a click on the "delete all entries" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_shoes);
        builder.setPositiveButton(R.string.editor_delete_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the shoe.
                deleteAllKicks();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the shoe.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}




