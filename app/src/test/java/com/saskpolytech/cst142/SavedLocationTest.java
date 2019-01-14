package com.saskpolytech.cst142;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is a test class that will test that SavedLocation Objects are created properly
 */
public class SavedLocationTest {


    @Test
    public void checkSavedLocationConstructor1()
    {
        SavedLocation testLocation = new SavedLocation("name", "32", "50");

        assertTrue(testLocation.id == -1);
        assertTrue(testLocation.sName.equals("name"));
        assertTrue(testLocation.sLatitude.equals("32"));
        assertTrue(testLocation.sLongitude.equals("50"));
    }

    @Test
    public void checkSavedLocationConstructor2()
    {
       SavedLocation testLocation = new SavedLocation(20, "sask", "15", "20");

       assertTrue(testLocation.id == 20);
       assertTrue(testLocation.sName.equals("sask"));
       assertTrue(testLocation.sLatitude.equals("15"));
       assertTrue(testLocation.sLongitude.equals("20"));
    }

}