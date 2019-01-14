package com.saskpolytech.cst142;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * This class is used to edit a location object that is passed to it through a bundle.
 * It lets you edit the name, latitude, and longitude.
 * @author Dylan Sies (cst142)
 */
public class EditLocationActivity extends AppCompatActivity
{

    //Helper attributes
    private LocationDBHelper db;
    private LocationHelper locHelp;

    //Key value for the passed in savedlocation
    public static final String EXTRA_SAVEDLOCATION = "savedlocation";
    private SavedLocation obSavedLocation;

    //Control attributes
    private EditText etName;
    private EditText etLat;
    private EditText etLon;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize the helper attributes
        this.db = new LocationDBHelper(this);
        this.locHelp = new LocationHelper(this);

        //Go in the bundle and grab the savedlocation object
        Bundle passedData = getIntent().getExtras();
        this.obSavedLocation = (SavedLocation) passedData.getSerializable(EXTRA_SAVEDLOCATION);

        getControls();

        //Set all the text from the object passed in from the bundle
        setTitle("Editing " + this.obSavedLocation.sName);
        etName.setText(this.obSavedLocation.sName);
        etLat.setText(this.obSavedLocation.sLatitude);
        etLon.setText(this.obSavedLocation.sLongitude);

    }


    protected void onStart()
    {
        super.onStart();
        //Start listening for location
        locHelp.mGoogleApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //Stop listening for location
        locHelp.mGoogleApiClient.disconnect();
    }


    /**
     * This method will be called when the save button is pressed. It will first look for valid input
     * and then update the location object in the database and return to the previous activity.
     * @param v
     */
    public void onSaveButtonPressed(View v)
    {
        db.open();

        //Check that fields are not blank
        if (etName.getText().toString().matches("") || etLat.getText().toString().matches("") || etLon.getText().toString().matches(""))
        {
            //At least on of the fields is blank
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Check for valid lat and lng
            if (LocUtils.checkValidLat(Float.parseFloat(etLat.getText().toString())) && LocUtils.checkValidLong(Float.parseFloat(etLon.getText().toString())))
            {
                //Valid latitude and longitude so we can update the location in the database
                db.updateLocation(new SavedLocation(this.obSavedLocation.id, this.etName.getText().toString(),
                        this.etLat.getText().toString(), this.etLon.getText().toString()));

                //Exit this activity
                finish();
            }
            else
            {
                Toast.makeText(this, "Enter a valid Latitude and Longitude", Toast.LENGTH_SHORT).show();
            }
        }

        db.close();
    }


    /**
     * This method will assign the appropriate attributes to the correct controls on the layout
     */
    private void getControls()
    {
        this.etName = (EditText) findViewById(R.id.etName);
        this.etLat = (EditText) findViewById(R.id.etLatitude);
        this.etLon = (EditText) findViewById(R.id.etLongitude);
    }


    /**
     * This method will be called when the getCurrentLocation button is pressed, it will fill
     * in the two edit texts with the current longitude and latitude of your device.
     * @param v the button being pressed
     */
    public void onGetCurrentLocation(View v)
    {
        etLat.setText(locHelp.latestLat + "");
        etLon.setText(locHelp.latestLong + "");
    }


}
