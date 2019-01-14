package com.saskpolytech.cst142;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class is the Activity for creating a new location object in the database. It has fields
 * For the user to enter their own data and also a button to get the devices current location.
 * @author Dylan Sies (cst142)
 */
public class NewLocationActivity extends AppCompatActivity
{

    //Helper Attributes
    private LocationDBHelper db;
    private LocationHelper locHelp;

    //Control Attributes
    private EditText etName;
    private EditText etLongitude;
    private EditText etLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set the activity title
        setTitle("New Location");

        // Show back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize helpers
        this.db = new LocationDBHelper(this);
        this.locHelp = new LocationHelper(this);

        getControls();
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
     * This method is called on create and will get all controls from the layout and assign them
     * to the private attributes of this class
     */
    private void getControls()
    {
        this.etName = findViewById(R.id.etName);
        this.etLongitude = findViewById(R.id.etLongitude);
        this.etLatitude = findViewById(R.id.etLatitude);
    }


    /**
     * This method will be called when the save location button is pressed. It will create a
     * SavedLocation from the edit Texts and write it to the database.
     * @param v The button being pressed
     */
    public void onAddLocation(View v)
    {
        db.open();

            //Check that fields are not blank
            if (etName.getText().toString().matches("") || etLatitude.getText().toString().matches("") || etLongitude.getText().toString().matches(""))
            {
                //At least on of the fields is blank
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Check for valid lat and lng
                if (LocUtils.checkValidLat(Float.parseFloat(etLatitude.getText().toString())) && LocUtils.checkValidLong(Float.parseFloat(etLongitude.getText().toString())))
                {
                    //Valid latitude and longitude so create to location
                    SavedLocation savedLocation = new SavedLocation(etName.getText().toString(), etLatitude.getText().toString(), etLongitude.getText().toString());

                    //Write location in the database
                    db.createLocation(savedLocation);

                    //Close this activity
                    finish();
                }
                else
                {
                    //Let the user know that the latitude or longitude is not valid
                    Toast.makeText(this, "Enter a valid Latitude and Longitude", Toast.LENGTH_SHORT).show();
                }
            }
            
        db.close();
    }


    /**
     * This method will be called when the getCurrentLocation button is pressed, it will fill
     * in the two edit texts with the current longitude and latitude of your device.
     * @param v the button being pressed
     */
    public void onGetCurrentLocation(View v)
    {
        etLatitude.setText(locHelp.latestLat + "");
        etLongitude.setText(locHelp.latestLong + "");
    }


}
