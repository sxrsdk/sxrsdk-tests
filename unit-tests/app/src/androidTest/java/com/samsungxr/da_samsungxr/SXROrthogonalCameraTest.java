package com.samsungxr.da_samsungxr;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.SXROrthogonalCamera;

/**
 * Created by j.elidelson on 6/10/2015.
 */
public class SXROrthogonalCameraTest extends ActivityInstrumentationSXRf {

    public SXROrthogonalCameraTest() {
        super(SXRTestActivity.class);
    }

    public void testConstructor(){
        assertNotNull(TestDefaultSXRViewManager.mSXRContext);
        SXROrthogonalCamera sxrOrthogonalCamera = new SXROrthogonalCamera(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(sxrOrthogonalCamera);
    }

    public void testSetGetLeftClippingDistance(){

        SXROrthogonalCamera sxrOrthogonalCamera = new SXROrthogonalCamera(TestDefaultSXRViewManager.mSXRContext);
        sxrOrthogonalCamera.setLeftClippingDistance(-1.0f);
        assertEquals(-1.0f,sxrOrthogonalCamera.getLeftClippingDistance());
    }

    public void testSetGetRighttClippingDistance(){

        SXROrthogonalCamera sxrOrthogonalCamera = new SXROrthogonalCamera(TestDefaultSXRViewManager.mSXRContext);
        sxrOrthogonalCamera.setRightClippingDistance(1.0f);
        assertEquals(1.0f,sxrOrthogonalCamera.getRightClippingDistance());
    }

    public void testSetGetBottomClippingDistance(){

        SXROrthogonalCamera sxrOrthogonalCamera = new SXROrthogonalCamera(TestDefaultSXRViewManager.mSXRContext);
        sxrOrthogonalCamera.setBottomClippingDistance(1.0f);
        assertEquals(1.0f,sxrOrthogonalCamera.getBottomClippingDistance());
    }

    public void testSetGetTopClippingDistance(){

        SXROrthogonalCamera sxrOrthogonalCamera = new SXROrthogonalCamera(TestDefaultSXRViewManager.mSXRContext);
        sxrOrthogonalCamera.setTopClippingDistance(1.0f);
        assertEquals(1.0f,sxrOrthogonalCamera.getTopClippingDistance());
    }

    public void testSetGetNearClippingDistance(){

        SXROrthogonalCamera sxrOrthogonalCamera = new SXROrthogonalCamera(TestDefaultSXRViewManager.mSXRContext);
        sxrOrthogonalCamera.setNearClippingDistance(1.0f);
        assertEquals(1.0f,sxrOrthogonalCamera.getNearClippingDistance());
    }

    public void testSetGetFarClippingDistance(){

        SXROrthogonalCamera sxrOrthogonalCamera = new SXROrthogonalCamera(TestDefaultSXRViewManager.mSXRContext);
        sxrOrthogonalCamera.setFarClippingDistance(1.0f);
        assertEquals(1.0f,sxrOrthogonalCamera.getFarClippingDistance());
    }
}
