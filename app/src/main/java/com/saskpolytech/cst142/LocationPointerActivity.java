package com.saskpolytech.cst142;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * This class provides the functionality for selecting a location and viewing which direciton
 * it is in as well as a reading on how far the location is away from you.
 *
 * This is the main activity of this app, it doesn't really do much in the android vd but if you
 * put it on a phone you can see the pointer move towards the currently selected location.
 *
 * Location permissions must be granted on the device.
 *
 * @author Dylan Sies (cst142)
 */
public class LocationPointerActivity extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemSelectedListener
{

    //Helper Attributes
    private LocationDBHelper db;
    private LocationHelper locHelp;
    private Cursor cursor;

    private Location currentDestination;
    private boolean sentNotification = false;

    //Sensor Attributes
    private SensorManager mSensorManager;
    private Sensor sAccelerometer;
    private Sensor sMagnetometer;

    public static float[] mAccelerometer = null;
    public static float[] mGeomagnetic = null;

    //Control Attributes
    private Spinner spnLocations;
    private ImageView imgPointer;
    private TextView tvDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_pointer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize helper classes for this activity
        this.db = new LocationDBHelper(this);
        this.locHelp = new LocationHelper(this);

        getControls();

        refreshData();

        //Set the listener for the spinner
        spnLocations.setOnItemSelectedListener(this);

        //Create a sensor manager and assign the attributes to the correct sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager != null)
        {
            sAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }


    @Override
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


    @Override
    protected void onResume() {
        super.onResume();
        //Register the sensors so they can start working
        mSensorManager.registerListener(this, sAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, sMagnetometer, SensorManager.SENSOR_DELAY_GAME);

        //Refresh the spinner
        refreshData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //Unregister the sensors when they are not being used to save resources/battery
        mSensorManager.unregisterListener(this, sAccelerometer);
        mSensorManager.unregisterListener(this, sMagnetometer);
    }


    /**
     * This method will assign the control attributes to the correct controls on the layout
     */
    private void getControls()
    {
        this.spnLocations = (Spinner) findViewById(R.id.spnLocations);
        this.imgPointer = (ImageView) findViewById(R.id.imgPointer);
        this.tvDistance = (TextView) findViewById(R.id.tvDistance);
    }


    /**
     * This method will be called when the Configure Locations button is pressed on the layout.
     * @param v - The current button being pressed
     */
    public void onConfigureLocations(View v)
    {
        Intent intent = new Intent(this, ConfigureLocationsActivity.class);
        startActivity(intent);
    }


    /**
     * This method will refresh the data inside the spinner based on all of the location objects in
     * The database
     */
    private void refreshData()
    {
        db.open();

        cursor = db.getAllLocationsAsCursor();

        //Show the name column
        String[] cols = new String[] {db.NAME};
        int[] views = new int[] {android.R.id.text1};

        //use predefined layout to display all of the locaations
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, cols, views);

        //Set the adapter to the spinner
        this.spnLocations.setAdapter(adapter);

        db.close();
    }


    /**
     * This method will be called whenever a registered device sensor has changed
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        //This method gets called for both sensors so we have to get the values for which ever one
        //called this method
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            mAccelerometer = sensorEvent.values;
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            mGeomagnetic = sensorEvent.values;
        }

        if (mAccelerometer != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, mAccelerometer, mGeomagnetic)) {
                float fOrientation[] = new float[3];
                SensorManager.getOrientation(R, fOrientation);
                // We have the azimuth pitch and roll now so we can convert from Radians to Degrees
                // We only need the azimuth which is the angle the device is pointing from magnetic north
                float azimuth = (float) (180 * fOrientation[0] / Math.PI);

                //Create a location object with your current location which will allow us to get the
                //bearing and distance to our destination
                Location yourLocation = new Location("");
                yourLocation.setLatitude(locHelp.latestLat);
                yourLocation.setLongitude(locHelp.latestLong);

                float nAngle = 0f;
                float fDistance = 0f;

                if (currentDestination != null)
                {
                    //Get the angle and distance from the location you are standing to the destination
                    nAngle = yourLocation.bearingTo(this.currentDestination);
                    fDistance = yourLocation.distanceTo(this.currentDestination);
                }

                //Refresh the distance textview
                this.tvDistance.setText(String.format("%.2f",fDistance) + " metres");

                //If you are within range of your location you are traveling to send a notification to
                //alert the user
                if ((LocUtils.distanceWithinRange(0.5f, 50, fDistance)) && !this.sentNotification && currentDestination != null)
                {
                    //Send notification to alert the user they are coming close to their destination
                    sendNearbyNotification();
                    this.sentNotification = true;
                }

                //Rotate the imageview with an animation based off of the angle we have
                RotateAnimation aRotate = new RotateAnimation(nAngle - azimuth, nAngle - azimuth,
                                          Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                aRotate.setDuration(200);
                aRotate.setFillAfter(true);
                //Start the animation
                imgPointer.startAnimation(aRotate);
            }
        }
    }


    /**
     * This method is required for a sensorEventListener however I do not use it.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }


    /*
     * Will be called when an item from the spinner is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        //Set our destination to the newly selected location on the spinner
        Location destLocation = new Location("");
        destLocation.setLatitude(Double.parseDouble(cursor.getString(2)));
        destLocation.setLongitude(Double.parseDouble(cursor.getString(3)));

        this.currentDestination = destLocation;
        //Set the sent notification to false so we can get another one for our new location.
        this.sentNotification = false;
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
    }


    /**
     * This method will send a notification to the user alerting them that they are nearby their
     * destination.
     */
    private void sendNearbyNotification()
    {
        //Get the notification manager
        NotificationManager notifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n;

        //What happens when the user taps on the notification
        Intent intent = new Intent(LocationPointerActivity.this, LocationPointerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(LocationPointerActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        // Check if oreo or higher because they use channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //Set up channel
            String channelNumber = "1111";
            CharSequence channelName = "channel1111";
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel channel = new NotificationChannel(channelNumber, channelName,  importance);
            //Put channel into the manager
            notifyMgr.createNotificationChannel(channel);

            //Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelNumber);

            builder.setChannelId(channelNumber).setSmallIcon(R.mipmap.ic_launcher_round).setTicker("Ticker Message").setContentTitle("You have arrived")
                    .setContentText("You are nearby " + cursor.getString(1)).setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent);

            //Assign the built notification
            n = builder.build();
        }
        else
        {
            //Older than oreo so no channels
            //Build the notification
            Notification.Builder builder = new Notification.Builder(this);

            builder.setSmallIcon(R.mipmap.ic_launcher_round).setTicker("Ticker Message").setContentTitle("You have arrived")
                    .setContentText("You are nearby " + cursor.getString(1)).setAutoCancel(true).setContentIntent(pendingIntent);

            //Assign the built notification
            n = builder.build();
        }

        // show the notification
        notifyMgr.notify(cursor.getInt(0), n);
    }


}

