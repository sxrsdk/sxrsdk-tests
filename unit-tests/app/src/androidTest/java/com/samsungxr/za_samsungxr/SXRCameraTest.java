package com.samsungxr.za_samsungxr;

import android.graphics.Color;
import android.util.Log;

import com.samsungxr.CustomPostEffectShaderManager;
import com.samsungxr.SXRCamera;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRPostEffect;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRTransform;
import com.samsungxr.animation.SXRAnimationTest;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

/**
 * @author Marcos Gorll
 */
public class SXRCameraTest extends ActivityInstrumentationSXRf {


    private final String TAG = SXRCameraTest.class.getSimpleName();

    private SXRCamera sxrCameraR;
    private SXRCamera sxrCameraL;

    private final float rl = 2.0f;
    private final float gl = 3.0f;
    private final float bl = 4.0f;
    private final float al = 5.0f;

    private final float rr = 2.1f;
    private final float gr = 3.1f;
    private final float br = 4.1f;
    private final float ar = 5.1f;


    /**
     * SXRCamera - Test if getOwnerObject is a SXRSceneObject object
     */
    public void testGetOwnerObject() {
        Log.d(TAG, "iniciando testgetOwnerObject");
        SXRCamera sxrCamera = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();

        SXRCameraRig sxrCamera2 = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        //assertEquals(SXRSceneObject.class.getName(), sxrCamera.getOwnerObject().getClass().getName());
    }

    /**
     * SXRCamera - Test if getRenderMask is a number
     */
    public void testGetRenderMask() {
        Log.d(TAG, "iniciando testgetRenderMask");

        init();
        SXRCamera sxrCamera = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        assertFalse(Float.isNaN(sxrCamera.getRenderMask()));
    }

    /**
     * SXRCamera - Test if setRenderMask is a number
     */
    public void testSetRenderMask() {
        Log.d(TAG, "iniciando testsetRenderMask");

        init();
        SXRCamera sxrCamera = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        int renderMask = sxrCamera.getRenderMask();
        sxrCamera.setRenderMask(SXRRenderData.SXRRenderMaskBit.Left);
        try {
            assertEquals(SXRRenderData.SXRRenderMaskBit.Left, sxrCamera.getRenderMask());
        } finally {
            sxrCamera.setRenderMask(renderMask);
        }
    }

    /**
     * SXRCamera - Test addPostEffect
     */
    public void testAddPostEffect() throws InterruptedException {
        Log.d(TAG, "testaddPostEffect");

        final CustomPostEffectShaderManager shaderManager
                = SXRAnimationTest.makeCustomPostEffectShaderManager(TestDefaultSXRViewManager.mSXRContext);
        SXRPostEffect postEffect = new SXRPostEffect(TestDefaultSXRViewManager.mSXRContext, shaderManager.getShaderId());
        postEffect.setVec3("ratio_r", 0.393f, 0.769f, 0.189f);
        postEffect.setVec3("ratio_g", 0.349f, 0.686f, 0.168f);
        postEffect.setVec3("ratio_b", 0.272f, 0.534f, 0.131f);

        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera().addPostEffect(postEffect);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getRightCamera().addPostEffect(postEffect);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera().removePostEffect(postEffect);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getRightCamera().removePostEffect(postEffect);
    }

    /**
     * SXRCamera - Test removePostEffect
     */
    public void testRemovePostEffectt() throws InterruptedException {
        Log.d(TAG, "testremovePostEffectt");

        final CustomPostEffectShaderManager shaderManager
                = SXRAnimationTest.makeCustomPostEffectShaderManager(TestDefaultSXRViewManager.mSXRContext);
        SXRPostEffect postEffect = new SXRPostEffect(TestDefaultSXRViewManager.mSXRContext, shaderManager.getShaderId());
        postEffect.setVec3("ratio_r", 0.393f, 0.769f, 0.189f);
        postEffect.setVec3("ratio_g", 0.349f, 0.686f, 0.168f);
        postEffect.setVec3("ratio_b", 0.272f, 0.534f, 0.131f);

        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera().addPostEffect(postEffect);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getRightCamera().addPostEffect(postEffect);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera().removePostEffect(postEffect);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getRightCamera().removePostEffect(postEffect);
    }

    /**
     * SXRCamera - Test setBackgroundColor
     */
    public void testSetBackgroundColor() {
        Log.d(TAG, "testsetBackgroundColor");

        init();

        SXRCamera sxrCameraL = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        SXRCamera sxrCameraR = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getRightCamera();

        sxrCameraL.setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        sxrCameraR.setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);

        assertEquals(1.0f, sxrCameraL.getBackgroundColorR(), 0.01f);
        assertEquals(1.0f, sxrCameraL.getBackgroundColorG(), 0.01f);
        assertEquals(1.0f, sxrCameraL.getBackgroundColorB(), 0.01f);
        assertEquals(1.0f, sxrCameraL.getBackgroundColorA(), 0.01f);

        assertEquals(1.0f, sxrCameraR.getBackgroundColorR(), 0.01f);
        assertEquals(1.0f, sxrCameraR.getBackgroundColorG(), 0.01f);
        assertEquals(1.0f, sxrCameraR.getBackgroundColorB(), 0.01f);
        assertEquals(1.0f, sxrCameraR.getBackgroundColorA(), 0.01f);
    }

    /**
     * SXRCamera - Test getBackgroundColorR
     */
    public void testGetBackgroundColorR() {
        Log.d(TAG, "testgetBackgroundColorR");

        init();

        assertEquals(rl, sxrCameraL.getBackgroundColorR(), 0.01f);
        assertEquals(rr, sxrCameraR.getBackgroundColorR(), 0.01f);
    }

    /**
     * SXRCamera - Test getBackgroundColorG
     */
    public void testGetBackgroundColorG() {
        Log.d(TAG, "testgetBackgroundColorG");

        init();

        assertEquals(gl, sxrCameraL.getBackgroundColorG(), 0.01f);
        assertEquals(gr, sxrCameraR.getBackgroundColorG(), 0.01f);
    }

    /**
     * SXRCamera - Test getBackgroundColorB
     */
    public void testGetBackgroundColorB() {
        Log.d(TAG, "testgetBackgroundColorB");

        init();

        assertEquals(bl, sxrCameraL.getBackgroundColorB(), 0.01f);
        assertEquals(br, sxrCameraR.getBackgroundColorB(), 0.01f);
    }

    /**
     * SXRCamera - Test getBackgroundColorA
     */
    public void testGetBackgroundColorA() {

        Log.d(TAG, "testgetBackgroundColorA");
        init();

        assertEquals(al, sxrCameraL.getBackgroundColorA(), 0.01f);
        assertEquals(ar, sxrCameraR.getBackgroundColorA(), 0.01f);
    }


    /**
     * SXRCamera - Test setBackgroundColorR
     */
    public void testSetBackgroundColorR() {
        Log.d(TAG, "testsetBackgroundColorR");

        init();
        sxrCameraL.setBackgroundColorR(1.123f);
        sxrCameraR.setBackgroundColorR(3.211f);

        assertEquals(1.123f, sxrCameraL.getBackgroundColorR(), 0.01f);
        assertEquals(3.211f, sxrCameraR.getBackgroundColorR(), 0.01f);
    }

    /**
     * SXRCamera - Test setBackgroundColorG
     */
    public void testSetBackgroundColorG() {
        Log.d(TAG, "testsetBackgroundColorG");

        init();
        sxrCameraL.setBackgroundColorG(1.123f);
        sxrCameraR.setBackgroundColorG(3.211f);

        assertEquals(1.123f, sxrCameraL.getBackgroundColorG(), 0.01f);
        assertEquals(3.211f, sxrCameraR.getBackgroundColorG(), 0.01f);
    }

    /**
     * SXRCamera - Test setBackgroundColorB
     */
    public void testSetBackgroundColorB() {
        Log.d(TAG, "testsetBackgroundColorB");

        init();

        sxrCameraL.setBackgroundColorB(1.123f);
        sxrCameraR.setBackgroundColorB(3.211f);

        assertEquals(1.123f, sxrCameraL.getBackgroundColorB(), 0.01f);
        assertEquals(3.211f, sxrCameraR.getBackgroundColorB(), 0.01f);
    }


    /**
     * SXRCamera - Test setBackgroundColorA
     */
    public void testSetBackgroundColorA() {
        Log.d(TAG, "testsetBackgroundColorA");

        init();

        sxrCameraL.setBackgroundColorA(1.123f);
        sxrCameraR.setBackgroundColorA(3.211f);

        assertEquals(1.123f, sxrCameraL.getBackgroundColorA(), 0.01f);
        assertEquals(3.211f, sxrCameraR.getBackgroundColorA(), 0.01f);
    }

    public void testSetBackgroundCameraLeftInvalid() {

        sxrCameraL = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        try {
            sxrCameraL.setBackgroundColorA(new Float("s"));
            fail();
        } catch (NumberFormatException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testSetBackgroundCameraRightInvalid() {

        sxrCameraR = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        try {
            sxrCameraR.setBackgroundColorA(new Float("s"));
            fail();
        } catch (NumberFormatException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void init() {
        sxrCameraL = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        sxrCameraR = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getRightCamera();
        //setBackgroundColor(float r, float g, float b, float a)
        sxrCameraL.setBackgroundColor(rl, gl, bl, al);
        sxrCameraR.setBackgroundColor(rr, gr, br, ar);
    }

    /**
     * SXRCamera - Test if getOwnerObject is a SXRSceneObject object
     */
    public void testBackGroundColor() {
        SXRCamera sxrCamera = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        sxrCamera.setBackgroundColor(Color.YELLOW);
        assertEquals(Color.YELLOW, sxrCamera.getBackgroundColor());
        //assertEquals(SXRSceneObject.class.getName(), sxrCamera.getOwnerObject().getClass().getName());
    }

    public void testgetTransform() {
        SXRCamera sxrCamera = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        SXRTransform sxrTransform = sxrCamera.getTransform();
        assertNotNull(sxrTransform);
    }


    public void testAttachDetachTransfor() {
        SXRCamera sxrCamera = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera();
        SXRSceneObject sxrSceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        sxrCamera.addChildObject(sxrSceneObject);
        assertEquals(1,sxrCamera.getChildrenCount());
        sxrCamera.removeChildObject(sxrSceneObject);
    }

    public void testOthers() { //by Elidelson Carvalho on 10/01/2015
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.setCameraRigType(SXRCameraRig.SXRCameraRigType.RollFreeze.ID);
        assertEquals(sxrCameraRig.getCameraRigType(), SXRCameraRig.SXRCameraRigType.RollFreeze.ID);
    }

}

