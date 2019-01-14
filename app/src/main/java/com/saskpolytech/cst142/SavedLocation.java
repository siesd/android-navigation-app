package com.saskpolytech.cst142;

import java.io.Serializable;


/**
 * This class is the custom location object that will be written to a database. It will hold a
 * name, latitude, and longitude. It is serializable so it can be passed through a bundle.
 * @author Dylan Sies (cst142)
 */
public class SavedLocation implements Serializable
{

    //Attributes
    public long id;
    public String sName;
    public String sLatitude;
    public String sLongitude;


    //Constructor
    public SavedLocation(String name, String latitude, String longitude)
    {
        this.id = -1;
        this.sName = name;
        this.sLatitude = latitude;
        this.sLongitude = longitude;
    }


    //Constructor with id
    public SavedLocation(long id, String name, String latitude, String longitude)
    {
        this(name,latitude,longitude);
        this.id = id;
    }


}
