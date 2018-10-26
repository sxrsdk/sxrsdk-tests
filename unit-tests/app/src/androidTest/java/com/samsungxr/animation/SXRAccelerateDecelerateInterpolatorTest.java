package com.samsungxr.animation;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRTestActivity;

import com.samsungxr.SXRPerspectiveCamera;

import java.security.InvalidParameterException;

//import org.junit.Test;

/**
 * Created by diego on 2/26/15.
 */
public class SXRAccelerateDecelerateInterpolatorTest extends ActivityInstrumentationSXRf {

    private SXRAccelerateDecelerateInterpolator interpolator;

    public SXRAccelerateDecelerateInterpolatorTest() {
        super(SXRTestActivity.class);
    }

    public void testGetInstance() {
        assertEquals(SXRPerspectiveCamera.getDefaultFovY(), 90.0f, 0.0f);
        SXRAccelerateDecelerateInterpolator interpolator = SXRAccelerateDecelerateInterpolator.getInstance();
        assertNotNull(interpolator);
    }

    public void testMapRatio() {
        SXRAccelerateDecelerateInterpolator interpolator = SXRAccelerateDecelerateInterpolator.getInstance();
        float result=interpolator.mapRatio(0.5f);
        assertEquals(result, 0.5f);
    }

    public void testMapRatioNegative() {
        try {
            SXRAccelerateDecelerateInterpolator interpolator = SXRAccelerateDecelerateInterpolator.getInstance();
            assertEquals(interpolator.mapRatio(-0.5f), -0.5f);
            //fail();
        }catch (InvalidParameterException e) {
            assertNotNull(e.getMessage(), "ratio - the current time ratio, >= 0 and <= 1");
        }
    }

    public void testMapRatioEqualThanOne() {
        SXRAccelerateDecelerateInterpolator interpolator = SXRAccelerateDecelerateInterpolator.getInstance();
        assertEquals(interpolator.mapRatio(1.0f), 1.0f);
    }

    public void testMapRatioGreaterThanOne() {
        try {
            SXRAccelerateDecelerateInterpolator interpolator = SXRAccelerateDecelerateInterpolator.getInstance();
            assertEquals(interpolator.mapRatio(2.0f), 2.0f);
            //fail();
        } catch (InvalidParameterException e) {
            assertNotNull(e.getMessage(), "ratio - the current time ratio, >= 0 and <= 1");
        }
    }

    public void testMapRatioNaN() {
        SXRAccelerateDecelerateInterpolator interpolator = SXRAccelerateDecelerateInterpolator.getInstance();
        assertEquals(interpolator.mapRatio(Float.NaN), Float.NaN);
    }

    public void ignoretestMapRatioPositiveInfinity() {
        SXRAccelerateDecelerateInterpolator interpolator = SXRAccelerateDecelerateInterpolator.getInstance();
        assertEquals(interpolator.mapRatio(Float.POSITIVE_INFINITY), Float.POSITIVE_INFINITY,0.0f);
        //assertEquals(interpolator.mapRatio(Float.POSITIVE_INFINITY), Float.NaN,0.0f);
    }

    public void testMapRatioNegativeInfinity() {
        SXRAccelerateDecelerateInterpolator interpolator = SXRAccelerateDecelerateInterpolator.getInstance();
        //assertEquals(interpolator.mapRatio(Float.NEGATIVE_INFINITY), Float.NEGATIVE_INFINITY,0.0f);
        assertEquals(interpolator.mapRatio(Float.NEGATIVE_INFINITY), Float.NaN,0.0f);
    }


}
