package com.samsungxr.da_samsungxr;

import android.util.Log;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.SXRPerspectiveCamera;

/**
 * @author Marcos Gorll
 */
public class SXRPerspectiveCameraTest extends ActivityInstrumentationSXRf {

    private final String TAG = SXRPerspectiveCameraTest.class.getSimpleName();

    public SXRPerspectiveCameraTest() {
        super(SXRTestActivity.class);
    }

    public void testCreatePerspectiveCamera() {
        Log.d(TAG, "testCreatePerspectiveCamera");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        camera.getNearClippingDistance();
        assertEquals(TestDefaultSXRViewManager.mSXRContext, camera.getSXRContext());
    }


    //FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/56
    public void ignoreAttachCamera() {
        Log.d(TAG, "testAttachCamera");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().attachRightCamera(camera);
    }

    public void testGetNearClippingDistance() {
        Log.d(TAG, "testGetNearClippingDistance");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        assertEquals(0.1f, camera.getNearClippingDistance(), 0.01f);
    }

    public void ignoretestGetFarClippingDistance() {
        Log.d(TAG, "testGetNearClippingDistance");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        assertEquals(1000.0f, camera.getFarClippingDistance(), 0.01f);
    }

    public void testGetFovY() {
        Log.d(TAG, "testGetFovY");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        assertEquals(90.0f, camera.getFovY(), 0.01f);
    }

    public void testGetAspectRatio() {
        Log.d(TAG, "testGetAspectRatio");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        assertEquals(1.0f, camera.getAspectRatio(), 0.01f);
    }

    public void testGetDefaultFovY() {
        Log.d(TAG, "testGetDefaultFovY");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        assertEquals(90.0f, camera.getDefaultFovY(), 0.01f);
    }

    public void testGetDefaultAspectRatio() {
        Log.d(TAG, "testGetDefaultAspectRatio");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        assertEquals(1.0f, camera.getDefaultAspectRatio(), 0.01f);
    }

    public void testSetNearClippingDistance() {
        Log.d(TAG, "testSetNearClippingDistance");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        float distance = 1.5f;
        camera.setNearClippingDistance(distance);
        assertEquals(distance, camera.getNearClippingDistance(), 0.01f);
    }

    public void testSetFarClippingDistance() {
        Log.d(TAG, "testSetFarClippingDistance");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        float distance = 90.5f;
        camera.setFarClippingDistance(distance);
        assertEquals(distance, camera.getFarClippingDistance(), 0.01f);
    }

    public void testSetFovY() {
        Log.d(TAG, "testSetFovY");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        float value = 0.5f;
        camera.setFovY(value);
        assertEquals(value, camera.getFovY(), 0.01f);
    }

    public void testSetAspectRatio() {
        Log.d(TAG, "testSetAspectRatio");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        float value = 0.5f;
        camera.setAspectRatio(value);
        assertEquals(value, camera.getAspectRatio(), 0.01f);
    }

    public void testSetDefaultFovY() {
        Log.d(TAG, "testSetDefaultFovY");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        float value = 2.5f;
        camera.setDefaultFovY(value);
        assertEquals(value, camera.getDefaultFovY(), 0.01f);
    }

    public void testSetDefaultAspectRatio() {
        Log.d(TAG, "testSetDefaultAspectRatio");
        SXRPerspectiveCamera camera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        float value = 1.5f;
        camera.setDefaultAspectRatio(value);
        assertEquals(value, camera.getDefaultAspectRatio(), 0.01f);
    }



}

