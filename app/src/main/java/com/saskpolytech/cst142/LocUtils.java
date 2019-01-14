package com.saskpolytech.cst142;


/**
 * This method contains tools that will be used for calculating things pertaining to locations.
 * @author Dylan Sies (cst142)
 */
public class LocUtils
{

    /**
     * This method will check a passed in number if it is a valid latitude value
     * @return - true if it is a valid latitude
     */
    public static boolean checkValidLat(float fVal)
    {
        //from -90 to 90
        return (fVal <= 90 && fVal >= -90);
    }


    /**
     * This method will check a passed in number if it is a valid longitude value
     * @return - true if it is a valid longitude
     */
    public static boolean checkValidLong(float fVal)
    {
        //From -180 to 180
        return (fVal <= 180 && fVal >= -180);
    }


    public static boolean distanceWithinRange(float lowerBound, float upperBound, float distance)
    {
        return (distance <= upperBound && distance >= lowerBound);
    }


}
