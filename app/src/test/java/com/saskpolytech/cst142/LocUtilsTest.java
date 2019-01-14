package com.saskpolytech.cst142;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is a test class that will test all of the methods in LocUtils
 */
public class LocUtilsTest {

    @Test
    public void checkValidLatNormalCase1() {
        assertTrue(LocUtils.checkValidLat(50f));
    }

    @Test
    public void checkValidLatNormalCase2() {
        assertTrue(LocUtils.checkValidLat(-50f));
    }

    @Test
    public void checkValidLatLowerBoundaryCase1() {
        assertTrue(LocUtils.checkValidLat(-90f));
    }

    @Test
    public void checkValidLatLowerBoundaryCase2() {
        assertFalse(LocUtils.checkValidLat(-91f));
    }

    @Test
    public void checkValidLatLowerBoundaryCase3() {
        assertFalse(LocUtils.checkValidLat(-90.00011f));
    }

    @Test
    public void checkValidLatHigherBoundaryCase1() {
        assertTrue(LocUtils.checkValidLat(90f));
    }

    @Test
    public void checkValidLatHigherBoundaryCase2() {
        assertFalse(LocUtils.checkValidLat(91f));
    }

    @Test
    public void checkValidLatHigherBoundaryCase3() {
        assertFalse(LocUtils.checkValidLat(90.00011f));
    }


    @Test
    public void checkValidLongNormalCase1() {
        assertTrue(LocUtils.checkValidLong(120f));
    }

    @Test
    public void checkValidLongNormalCase2() {
        assertTrue(LocUtils.checkValidLong(-120f));
    }

    @Test
    public void checkValidLongLowerBoundaryCase1() {
        assertTrue(LocUtils.checkValidLong(-180f));
    }

    @Test
    public void checkValidLongLowerBoundaryCase2() {
        assertFalse(LocUtils.checkValidLong(-181f));
    }

    @Test
    public void checkValidLongLowerBoundaryCase3() {
        assertFalse(LocUtils.checkValidLong(-180.0001f));
    }

    @Test
    public void checkValidLongHigherBoundaryCase1() {
        assertTrue(LocUtils.checkValidLong(180f));
    }

    @Test
    public void checkValidLongHigherBoundaryCase2() {
        assertFalse(LocUtils.checkValidLong(181f));
    }

    @Test
    public void checkValidLongHigherBoundaryCase3() {
        assertFalse(LocUtils.checkValidLong(180.0011f));
    }

    @Test
    public void distanceWithinRangeNormalCase1() {
        assertTrue(LocUtils.distanceWithinRange(0, 100, 50));
    }

    @Test
    public void distanceWithinRangeNormalCase2() {
        assertTrue(LocUtils.distanceWithinRange(0, 0.5f, 0.25f));
    }

    @Test
    public void distanceWithinRangeLowerBoundCase1() {
        assertTrue(LocUtils.distanceWithinRange(5, 10, 5));
    }

    @Test
    public void distanceWithinRangeLowerBoundCase2() {
        assertFalse(LocUtils.distanceWithinRange(5, 10, 4));
    }

    @Test
    public void distanceWithinRangeLowerBoundCase3() {
        assertFalse(LocUtils.distanceWithinRange(5, 10, 4.99999f));
    }

    @Test
    public void distanceWithinRangeHigherBoundCase1() {
        assertTrue(LocUtils.distanceWithinRange(5, 10, 10));
    }

    @Test
    public void distanceWithinRangeHigherBoundCase2() {
        assertFalse(LocUtils.distanceWithinRange(5, 10, 11));
    }

    @Test
    public void distanceWithinRangeHigherBoundCase3() {
        assertFalse(LocUtils.distanceWithinRange(5, 10, 10.0001f));
    }


}