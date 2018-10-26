package com.samsungxr.za_samsungxr;

import com.samsungxr.SXRCustomCamera;
import com.samsungxr.SXRRenderData;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

/**
 * Created by m.gorll on 2/27/2015.
 */
public class SXRCustomCameraTest extends ActivityInstrumentationSXRf {




    //FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/22
    public void ignoreCreateCustomCamera() {
        SXRCustomCamera leftCustomCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        leftCustomCamera.setProjectionMatrix(2f, 0f, 1f, 0f,
                0f, 2f, 1f, 0f,
                0f, 0f, -1f, -2f,
                0f, 0f, -1f, 0f);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().attachLeftCamera(leftCustomCamera);
    }

    //FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/32
    public void ignoreCreateCustomCameraRight() {

        SXRCustomCamera rightCustomCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        rightCustomCamera.setProjectionMatrix(2f, 0f, 1f, 0f,
                0f, 2f, 1f, 0f,
                0f, 0f, -1f, -2f,
                0f, 0f, -1f, 0f);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().attachRightCamera(rightCustomCamera);
    }

    public void testSetBackgroundColorInvalid() {

        SXRCustomCamera leftCustomCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        leftCustomCamera.setProjectionMatrix(2f, 0f, 1f, 0f,
                0f, 2f, 1f, 0f,
                0f, 0f, -1f, -2f,
                0f, 0f, -1f, 0f);
        try {
            leftCustomCamera.setBackgroundColor(new Integer("aaa"));
            fail();
        } catch (NumberFormatException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testSetRenderMask() {

        SXRCustomCamera leftCustomCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        leftCustomCamera.setProjectionMatrix(2f, 0f, 1f, 0f,
                0f, 2f, 1f, 0f,
                0f, 0f, -1f, -2f,
                0f, 0f, -1f, 0f);
        leftCustomCamera.setRenderMask(SXRRenderData.SXRRenderMaskBit.Left);
        assertEquals(leftCustomCamera.getRenderMask(), SXRRenderData.SXRRenderMaskBit.Left);
    }

    //FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/33
    public void ignoreSetRenderMaskInvalid() {

        SXRCustomCamera leftCustomCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        leftCustomCamera.setProjectionMatrix(2f, 0f, 1f, 0f,
                0f, 2f, 1f, 0f,
                0f, 0f, -1f, -2f,
                0f, 0f, -1f, 0f);

        leftCustomCamera.setRenderMask(365);
        fail();
    }

    public void testSetBackgroundA() {
        SXRCustomCamera customCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        customCamera.setBackgroundColorA(0f);
        assertEquals(customCamera.getBackgroundColorA(), 0f);
    }

    public void testSetBackgroundR() {
        SXRCustomCamera customCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        customCamera.setBackgroundColorA(0f);
        assertEquals(customCamera.getBackgroundColorR(), 0f);
    }

    public void testSetBackgroundG() {
        SXRCustomCamera customCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        customCamera.setBackgroundColorA(0f);
        assertEquals(customCamera.getBackgroundColorG(), 0f);
    }

    public void testSetBackgroundB() {
        SXRCustomCamera customCamera = new SXRCustomCamera(TestDefaultSXRViewManager.mSXRContext);
        customCamera.setBackgroundColorA(0f);
        assertEquals(customCamera.getBackgroundColorB(), 0f);
    }
}
