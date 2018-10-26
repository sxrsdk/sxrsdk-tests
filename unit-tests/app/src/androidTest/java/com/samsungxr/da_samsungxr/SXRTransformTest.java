package com.samsungxr.da_samsungxr;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.misc.ColorShader;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.BoundsValues;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRNode;

/**
 * Created by santhyago on 2/27/15.
 */
public class SXRTransformTest extends ActivityInstrumentationSXRf {

    private static final float UNPICKED_COLOR_R = 0.7f;
    private static final float UNPICKED_COLOR_G = 0.7f;
    private static final float UNPICKED_COLOR_B = 0.7f;
    private static final float UNPICKED_COLOR_A = 1.0f;

    private SXRNode mNode;

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
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().rotateByAxisWithPivot(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));

        mNode.getTransform().rotateByAxisWithPivot(
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
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setRotationByAxis(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        mNode.getTransform().setRotationByAxis(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testSetRotation() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setRotation(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));

        mNode.getTransform().setRotation(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testRotate() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        SXRNode mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("RenderDataScript is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("LeftNode is null.", mNode);
        mNode.getTransform().rotate(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        mNode.getTransform().rotate(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testRotateByAxis() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        SXRNode mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("is null.", mNode);
        mNode.getTransform().rotateByAxis(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        mNode.getTransform().rotateByAxis(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testSetScale() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setScale(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mNode.getTransform().getScaleX());
        mNode.getTransform().setScale(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mNode.getTransform().getScaleY());
    }

    public void testTranslate() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().translate(
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        mNode.getTransform().translate(
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
    }

    public void testSetGetPositionX() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setPositionX(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mNode.getTransform().getPositionX());
        mNode.getTransform().setPositionX(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mNode.getTransform().getPositionX());
    }

    public void testSetGetPositionY() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setPositionY(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mNode.getTransform().getPositionY());
        mNode.getTransform().setPositionY(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mNode.getTransform().getPositionY());
    }

    public void testSetGetPositionZ() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setPositionZ(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mNode.getTransform().getPositionZ());
        mNode.getTransform().setPositionZ(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mNode.getTransform().getPositionZ());
    }

    public void testSetGetScaleX() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setScaleX(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mNode.getTransform().getScaleX());
        mNode.getTransform().setScaleX(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mNode.getTransform().getScaleX());
    }

    public void testSetGetScaleY() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setScaleY(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mNode.getTransform().getScaleY());
        mNode.getTransform().setScaleY(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mNode.getTransform().getScaleY());
    }

    public void testSetScaleZ() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setScaleZ(BoundsValues.getFloatList().get(0));
        assertEquals(BoundsValues.getFloatList().get(0), mNode.getTransform().getScaleZ());
        mNode.getTransform().setScaleZ(BoundsValues.getFloatList().get(1));
        assertEquals(BoundsValues.getFloatList().get(1), mNode.getTransform().getScaleZ());
    }

    public void ignoretestGetRotationYaw() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        float yaw = mNode.getTransform().getRotationYaw();
        boolean result = (yaw >= 0) && (yaw <= 360);
        assertTrue(result);
        mNode.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
        yaw = mNode.getTransform().getRotationYaw();
        result = (yaw >= 0) && (yaw <= 360);
        assertTrue(result);
    }

    public void testGetRotationPitch() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        float pitch = mNode.getTransform().getRotationPitch();
        boolean result = (pitch >= 0) && (pitch <= 360);
        assertTrue("Pitch 1: " + pitch, result);
        mNode.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));

        pitch = mNode.getTransform().getRotationPitch();
        result = (pitch >= 0) && (pitch <= 360);
        assertTrue("Pitch 2: " + pitch, result);
    }

    public void testGetRotationRoll() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        mNode.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        float roll = mNode.getTransform().getRotationRoll();
        boolean result = (roll >= 0) && (roll <= 360);
        assertTrue(result);
        mNode.getTransform().setRotation(1.0f,
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
        roll = mNode.getTransform().getRotationRoll();
        result = (roll >= 0) && (roll <= 360);
        assertTrue(result);
    }

    public void testGetModelMatrix() {
        //TestDefaultSXRViewManager sxrViewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(sxrViewManager, "sxr.xml");
        mNode = getColorBoard(1.0f, 1.0f);
        assertNotNull("TestDefaultSXRViewManager is null.", TestDefaultSXRViewManager.mSXRContext);
        assertNotNull("SXRNode is null.", mNode);
        assertEquals(16, mNode.getTransform().getModelMatrix().length);
    }

    private SXRNode getColorBoard(float width, float height) {

        SXRContext sxrContext = TestDefaultSXRViewManager.mSXRContext;
        ColorShader mColorShader = new ColorShader(sxrContext);
        SXRMaterial material = new SXRMaterial(sxrContext, mColorShader.getShaderId());
        material.setVec4(ColorShader.COLOR_KEY, UNPICKED_COLOR_R, UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);
        SXRNode board = new SXRNode(sxrContext, width, height);
        board.getRenderData().setMaterial(material);

        return board;
    }
}
