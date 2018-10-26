package com.samsungxr.da_samsungxr;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.misc.ColorShader;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.BoundsValues;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRSceneObject;

/**
 * Created by santhyago on 2/27/15.
 */
public class SXRTransformTest extends ActivityInstrumentationSXRf {

    private static final float UNPICKED_COLOR_R = 0.7f;
    private static final float UNPICKED_COLOR_G = 0.7f;
    private static final float UNPICKED_COLOR_B = 0.7f;
    private static final float UNPICKED_COLOR_A = 1.0f;

    private SXRSceneObject mSceneObject;

    public SXRTransformTest() {
        super(SXRTestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        SXRContext sxrContext = TestDefaultSXRViewManager.mSXRContext;
        assertNotNull(sxrContext);
    }

    public void testRotateByAxisWithPivot() {
        //(float, float, float, float, float, float, float)
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().rotateByAxisWithPivot(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));

        mSceneObject.getTransform().rotateByAxisWithPivot(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testSetRotationByAxis() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setRotationByAxis(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        mSceneObject.getTransform().setRotationByAxis(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testSetRotation() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setRotation(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));

        mSceneObject.getTransform().setRotation(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testRotate() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        SXRSceneObject mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("RenderDataScript is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("LeftSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().rotate(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        mSceneObject.getTransform().rotate(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testRotateByAxis() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        SXRSceneObject mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("is null.", mSceneObject);
        mSceneObject.getTransform().rotateByAxis(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        mSceneObject.getTransform().rotateByAxis(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testSetScale() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setScale(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mSceneObject.getTransform().getScaleX());
        mSceneObject.getTransform().setScale(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mSceneObject.getTransform().getScaleY());
    }

    public void testTranslate() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().translate(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        mSceneObject.getTransform().translate(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testSetGetPositionX() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setPositionX(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mSceneObject.getTransform().getPositionX());
        mSceneObject.getTransform().setPositionX(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mSceneObject.getTransform().getPositionX());
    }

    public void testSetGetPositionY() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setPositionY(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mSceneObject.getTransform().getPositionY());
        mSceneObject.getTransform().setPositionY(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mSceneObject.getTransform().getPositionY());
    }

    public void testSetGetPositionZ() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setPositionZ(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mSceneObject.getTransform().getPositionZ());
        mSceneObject.getTransform().setPositionZ(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mSceneObject.getTransform().getPositionZ());
    }

    public void testSetGetScaleX() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setScaleX(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mSceneObject.getTransform().getScaleX());
        mSceneObject.getTransform().setScaleX(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mSceneObject.getTransform().getScaleX());
    }

    public void testSetGetScaleY() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setScaleY(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mSceneObject.getTransform().getScaleY());
        mSceneObject.getTransform().setScaleY(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mSceneObject.getTransform().getScaleY());
    }

    public void testSetScaleZ() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setScaleZ(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mSceneObject.getTransform().getScaleZ());
        mSceneObject.getTransform().setScaleZ(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mSceneObject.getTransform().getScaleZ());
    }

    public void ignoretestGetRotationYaw() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        float yaw = mSceneObject.getTransform().getRotationYaw();
        boolean result = (yaw >= 0) && (yaw <= 360);
        assertTrue(result);
        mSceneObject.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
        yaw = mSceneObject.getTransform().getRotationYaw();
        result = (yaw >= 0) && (yaw <= 360);
        assertTrue(result);
    }

    public void testGetRotationPitch() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        float pitch = mSceneObject.getTransform().getRotationPitch();
        boolean result = (pitch >= 0) && (pitch <= 360);
        assertTrue("Pitch 1: " + pitch, result);
        mSceneObject.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));

        pitch = mSceneObject.getTransform().getRotationPitch();
        result = (pitch >= 0) && (pitch <= 360);
        assertTrue("Pitch 2: " + pitch, result);
    }

    public void testGetRotationRoll() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        mSceneObject.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        float roll = mSceneObject.getTransform().getRotationRoll();
        boolean result = (roll >= 0) && (roll <= 360);
        assertTrue(result);
        mSceneObject.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
        roll = mSceneObject.getTransform().getRotationRoll();
        result = (roll >= 0) && (roll <= 360);
        assertTrue(result);
    }

    public void testGetModelMatrix() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mSceneObject = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRSceneObject is null.", mSceneObject);
        assertEquals(16, mSceneObject.getTransform().getModelMatrix().length);
    }

    private SXRSceneObject getColorBoard(float width, float height) {

        SXRContext sxrContext = TestDefaultSXRViewManager.mSXRContext;
        ColorShader mColorShader = new ColorShader(sxrContext);
        SXRMaterial material = new SXRMaterial(sxrContext, mColorShader.getShaderId());
        material.setVec4(ColorShader.COLOR_KEY, UNPICKED_COLOR_R, UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);
        SXRSceneObject board = new SXRSceneObject(sxrContext, width, height);
        board.getRenderData().setMaterial(material);

        return board;
    }
}
