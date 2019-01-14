package com.saskpolytech.cst142;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * This class provides the functionality viewing the list of all your locations as well as
 * providing options to create, edit and delete a location in the list.
 *
 * @author Dylan Sies (cst142)
 */
public class ConfigureLocationsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    //Attributes
    private LocationDBHelper db;
    private ArrayList<SavedLocation> locationList;
    private SavedLocation currentSelectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set the Title of the activity
        setTitle("Configure Locations");

        // Show back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize database helper
        this.db = new LocationDBHelper(this);
    }


    protected void onStart()
    {
        super.onStart();
        //Fill the listview
        refreshData();
    }


    /**
     * This method will be called when the New button is pressed on the layout.
     * It will open another activity which will let us add a location.
     * @param v - The current button being pressed
     */
    public void onNewLocation(View v)
    {
        Intent intent = new Intent(this, NewLocationActivity.class);
        startActivity(intent);
    }


    /**
     * This method will be called when the Delete button is pressed on the layout.
     * It will delete the currently selected location from the list.
     * @param v - The current button being pressed
     */
    public void onDeleteLocation(View v)
    {

        if (currentSelectedItem != null)
        {
            db.open();
            //Delete the location from the database
            db.deleteLocation(this.currentSelectedItem);
            db.close();

            //Refresh the list view and reset the currently selected item
            this.currentSelectedItem = null;
            refreshData();
        }
        else
        {
            //Alert the user that they have no item selected
            Toast.makeText(this, "Please select an item to delete", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method will be called when the Edit button is press on the layout.
     * It will open a new activity which will allow us to edit the currently selected location.
     * @param v - The current button being pressed
     */
    public void onEditLocation(View v)
    {
        if (currentSelectedItem != null)
        {
            //Go tho the edit activity and pass the SavedLocation object
            Intent intent = new Intent(this, EditLocationActivity.class);
            intent.putExtra(EditLocationActivity.EXTRA_SAVEDLOCATION, this.currentSelectedItem);
            startActivity(intent);
        }
        else
        {
            //Alert the user that they have no item selected to edit
            Toast.makeText(this, "Please select an item to edit", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method will get all of the SavedLocations an populate the listView with them
     */
    private void refreshData()
    {
        db.open();
        //Get list view control
        ListView lvLocations = findViewById(R.id.lvLocationList);

        lvLocations.setOnItemClickListener(this);

        //Retreive a list of objects from the database
        this.locationList = db.getAllLocations();

        //give the list the the custom location adapter
        LocationAdapter adapter = new LocationAdapter(this, this.locationList);

        lvLocations.setAdapter(adapter);

        db.close();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Set the currently selected item attribute
        this.currentSelectedItem = this.locationList.get(position);
    }


}
