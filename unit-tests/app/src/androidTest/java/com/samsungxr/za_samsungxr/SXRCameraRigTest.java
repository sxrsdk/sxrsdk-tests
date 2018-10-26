package com.samsungxr.za_samsungxr;

import com.samsungxr.SXRCamera;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXREyePointeeHolder;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMeshCollider;
import com.samsungxr.SXRMeshEyePointee;
import com.samsungxr.SXRPerspectiveCamera;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTransform;
import com.samsungxr.misc.ColorShader;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.BoundsValues;

/**
 * Created by daniel.nog on 1/29/2015.
 */
public class SXRCameraRigTest extends ActivityInstrumentationSXRf {

    private static final float UNPICKED_COLOR_R = 0.7f;
    private static final float UNPICKED_COLOR_G = 0.7f;
    private static final float UNPICKED_COLOR_B = 0.7f;
    private static final float UNPICKED_COLOR_A = 1.0f;

    public SXRCameraRigTest() {
        super(SXRTestActivity.class);
    }

    @Override
    public synchronized void setUp() throws Exception {
        super.setUp();
        ColorShader mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.setMainScene(new SXRScene(TestDefaultSXRViewManager.mSXRContext));
    }

    private void init() {
        ColorShader mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());

        material.setVec4(ColorShader.COLOR_KEY, UNPICKED_COLOR_R, UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);
        SXRSceneObject mSXRBoardObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext, 1.0f, 1.0f);
        mSXRBoardObject.getRenderData().setMaterial(material);

        mSXRBoardObject.getTransform().setPosition(0.0f, 1.0f, 1.0f);

        SXRMeshCollider collider = new SXRMeshCollider(TestDefaultSXRViewManager.mSXRContext, mSXRBoardObject.getRenderData().getMesh());
        mSXRBoardObject.attachComponent(collider);

        TestDefaultSXRViewManager.mSXRContext.getMainScene().addSceneObject(mSXRBoardObject);

        // SXRSceneObject mSXRSceneObject = mSXRBoardObject;
    }

    /**
     * SXRCameraRig - Test setCameraSeparationDistance
     */
    public void testSetCameraSeparationDistance() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        float distance = 1000f;
        sxrCameraRig.setCameraSeparationDistance(distance);
        //TestDefaultViewManager.mSXRContext.getMainScene().setMainCameraRig(sxrCameraRig);
        assertEquals(distance, sxrCameraRig.getCameraSeparationDistance());
    }

    /**
     * SXRCameraRig - Test setDefaultCameraSeparationDistance
     */
    public void testSetDefaultCameraSeparationDistance() {
        init();
        float distance = 1000f;
        SXRCameraRig.setDefaultCameraSeparationDistance(distance);
        assertEquals(distance, SXRCameraRig.getDefaultCameraSeparationDistance());
    }

    /**
     * SXRCameraRig - Test bound of getCameraSeparationDistance
     */
    public void testGetCameraSeparationDistanceBound() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        int size = BoundsValues.getFloatList().size();

        for (int i = 0; i < size; i++) {
            sxrCameraRig.setCameraSeparationDistance(BoundsValues.getFloatList().get(i));
            assertEquals(BoundsValues.getFloatList().get(i), sxrCameraRig.getCameraSeparationDistance());
        }
    }

    /**
     * SXRCameraRig - Test bound of setDefaultCameraSeparationDistance
     */
    public void testSetDefaultCameraSeparationDistanceBound() {
        init();
        int size = BoundsValues.getFloatList().size();
        for (int i = 0; i < size; i++) {
            SXRCameraRig.setDefaultCameraSeparationDistance(BoundsValues.getFloatList().get(i));
            assertEquals(BoundsValues.getFloatList().get(i), SXRCameraRig.getDefaultCameraSeparationDistance());
        }
    }

    /**
     * SXRCameraRig - Test if attachLeftCamera and getLeftCamera works
     */
    public void testAttachLeftCameraAndGetLeftCamera() {
        //TODO ver se este teste não quebra os outros, pois está mudando seriamente a estrutra interna

        init();

        SXRCamera leftCamera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        leftCamera.setRenderMask(SXRRenderData.SXRRenderMaskBit.Left);
        SXRCamera rightCamera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        rightCamera.setRenderMask(SXRRenderData.SXRRenderMaskBit.Right);
        SXRSceneObject leftCameraObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        leftCameraObject.attachCamera(leftCamera);
        SXRSceneObject rightCameraObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        rightCameraObject.attachCamera(rightCamera);
        SXRSceneObject cameraRigObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRCameraRig cameraRig = SXRCameraRig.makeInstance(TestDefaultSXRViewManager.mSXRContext);
        cameraRig.attachLeftCamera(leftCamera);
        cameraRig.attachRightCamera(rightCamera);
        cameraRigObject.attachCameraRig(cameraRig);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addSceneObject(cameraRigObject);
        cameraRigObject.addChildObject(leftCameraObject);
        cameraRigObject.addChildObject(rightCameraObject);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().setMainCameraRig(cameraRig);

        assertEquals(TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getLeftCamera(), leftCamera);
        assertEquals(TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getRightCamera(), rightCamera);
    }

    /**
     * SXRCameraRig - Test getCameraRigType
     */
    public void testGetCameraRigType() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        assertTrue(!Float.isNaN(sxrCameraRig.getCameraRigType()));
    }

    /**
     * SXRCameraRig - Test setCameraRigType
     */
    public void testSetCameraRigType() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        int type = sxrCameraRig.getCameraRigType();
        try {
            sxrCameraRig.setCameraRigType(SXRCameraRig.SXRCameraRigType.Free.ID);
            assertEquals(SXRCameraRig.SXRCameraRigType.Free.ID, sxrCameraRig.getCameraRigType());
        } finally {
            sxrCameraRig.setCameraRigType(type);
        }
    }

    /**
     * SXRCameraRig - Test reset
     */
    public void testReset() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.reset();
        //teste completamente sem sentido. Apenas para passar pelo método.
        //Esse tipo de teste começa a indicar que temos um problema na semântica da API
    }


    /**
     * SXRCameraRig - Test resetYaw
     */
    public void testResetYaw() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.resetYaw();
        //teste completamente sem sentido. Apenas para passar pelo método.
        //Esse tipo de teste começa a indicar que temos um problema na semântica da API
    }

    /**
     * SXRCameraRig - Test resetYawPitch
     */
    public void testResetYawPitch() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.resetYawPitch();
        //teste completamente sem sentido. Apenas para passar pelo método.
        //Esse tipo de teste começa a indicar que temos um problema na semântica da API
    }

    /**
     * SXRCameraRig - Test getLookAt
     */
    public void testGetLookAt() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        float[] floats = sxrCameraRig.getLookAt();
        assertNotNull(floats);
        assertEquals(3, floats.length);
    }


    /**
     * SXRCameraRig - Test getFloat
     */
    public void testGetFloat() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.setFloat("ratio_r", 1.0f);
        assertTrue(!Float.isNaN(sxrCameraRig.getFloat("ratio_r")));
    }

    /**
     * SXRCameraRig - Test Bounds of  setFloat
     */
    public void testGetFloatBounds() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        int size = BoundsValues.getFloatList().size();
        for (int i = 0; i < 3; i++) {
            sxrCameraRig.setFloat("ratio_r", BoundsValues.getFloatList().get(i));
            assertEquals(BoundsValues.getFloatList().get(i), sxrCameraRig.getFloat("ratio_r"));
        }
    }

    /**
     * SXRCameraRig - Test setFloat
     */
    public void testSetFloat() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        float value = 1.0f;
        sxrCameraRig.setFloat("ratio_r", value);
        assertEquals(value, sxrCameraRig.getFloat("ratio_r"));
    }

    /**
     * Just test setVec3 from SXRPostEffectData object from lib
     */
    public void testSetVec3() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        float x = 0.393f;
        float y = 0.769f;
        float z = 0.189f;
        sxrCameraRig.setVec3("ratio_r", x, y, z);
        float[] ratioR = sxrCameraRig.getVec3("ratio_r");
        assertNotNull(ratioR);
        assertEquals(3, ratioR.length);
        assertEquals(x, ratioR[0]);
        assertEquals(y, ratioR[1]);
        assertEquals(z, ratioR[2]);
    }

    /**
     * Just test Bounds of setVec3 from SXRPostEffectData object from lib
     */
    public void testSetVec3Bounds() {
        //TODO esse parece estar dando problema
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        int size = BoundsValues.getFloatList().size();
        for (int i = 0; i < size; i++) {
            float value = BoundsValues.getFloatList().get(i);
            sxrCameraRig.setVec3("ratio_r", value, value, value);
            float[] ratioR = sxrCameraRig.getVec3("ratio_r");
            assertNotNull(ratioR);
            assertEquals(3, ratioR.length);
            assertEquals(value, ratioR[0]);
            assertEquals(value, ratioR[1]);
            assertEquals(value, ratioR[2]);
        }
    }

    /**
     * Just test setVec2 from SXRPostEffectData object from lib
     */
    public void testSetVec2() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        float x = 0.393f;
        float y = 0.769f;
        sxrCameraRig.setVec2("ratio_r", 0.393f, 0.769f);
        float[] ratioR = sxrCameraRig.getVec2("ratio_r");
        assertNotNull(ratioR);
        assertEquals(2, ratioR.length);
        assertEquals(x, ratioR[0]);
        assertEquals(y, ratioR[1]);
    }

    public void testSetVec2Bounds() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        int size = BoundsValues.getFloatList().size();
        for (int i = 0; i < size; i++) {
            float value = BoundsValues.getFloatList().get(i);
            sxrCameraRig.setVec2("ratio_r", value, value);
            float[] ratioR = sxrCameraRig.getVec2("ratio_r");
            assertNotNull(ratioR);
            assertEquals(2, ratioR.length);
            assertEquals(value, ratioR[0]);
            assertEquals(value, ratioR[1]);
        }
    }

    /**
     * Just test setVec4 from SXRPostEffectData object from lib
     */
    public void testSetVec4() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        float x = 0.393f;
        float y = 0.769f;
        float z = 0.189f;
        float w = 0.065f;
        sxrCameraRig.setVec4("ratio_r", x, y, z, w);
        float[] ratioR = sxrCameraRig.getVec4("ratio_r");
        assertNotNull(ratioR);
        assertEquals(4, ratioR.length);
        assertEquals(x, ratioR[0]);
        assertEquals(y, ratioR[1]);
        assertEquals(z, ratioR[2]);
        assertEquals(w, ratioR[3]);
    }

    /**
     * Just test Bounds of setVec4 from SXRPostEffectData object from lib
     */
    public void testSetVec4Bounds() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();

        int size = BoundsValues.getFloatList().size();
        for (int i = 0; i < size; i++) {
            float value = BoundsValues.getFloatList().get(i);
            sxrCameraRig.setVec4("ratio_r", value, value, value, value);
            float[] ratioR = sxrCameraRig.getVec4("ratio_r");
            assertNotNull(ratioR);
            assertEquals(4, ratioR.length);
            assertEquals(value, ratioR[0]);
            assertEquals(value, ratioR[1]);
            assertEquals(value, ratioR[2]);
            assertEquals(value, ratioR[3]);
        }
    }

    /**
     * Just test getOwnerObject from SXRPostEffectData object from lib
     */
    public void testGetOwnerObject() {
        init();
        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        //assertEquals(SXRSceneObject.class.getName(), sxrCameraRig.getOwnerObject().getClass().getName());
        assertNotNull(sxrCameraRig);
    }

    public void testCameraRigFloatNull() {
        try {

            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setFloat("ratio_r", BoundsValues.getFloatNull());
            fail();
        } catch (NullPointerException e) {
            //esperado
        }
    }

    public void testCameraRigVecNull() {
        try {

            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setVec2("ratio_r", BoundsValues.getFloatNull(), BoundsValues.getFloatNull());
            fail();
        } catch (NullPointerException e) {
            //esperado
        }
    }

    public void testCameraRigTypeNull() {
        try {

            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setCameraRigType(BoundsValues.getIntegerNull());
            fail();
        } catch (NullPointerException e) {
            //esperado
        }
    }

    public void testCameraRigSeparationNull() {
        try {

            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setCameraSeparationDistance(BoundsValues.getFloatNull());
            fail();
        } catch (NullPointerException e) {
            //esperado
        }
    }

    public void testVec2Null() {
        try {

            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setVec2("ratio_r", BoundsValues.getFloatNull(), BoundsValues.getFloatNull());
            fail();
        } catch (NullPointerException e) {
            //esperado
        }
    }

    public void testVec3Null() {
        try {

            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setVec3("ratio_r", BoundsValues.getFloatNull(), BoundsValues.getFloatNull(),
                    BoundsValues.getFloatNull());
            fail();
        } catch (NullPointerException e) {
            //esperado
        }
    }

    public void testVec4Null() {
        try {

            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setVec4("ratio_r", BoundsValues.getFloatNull(), BoundsValues.getFloatNull(),
                    BoundsValues.getFloatNull(), BoundsValues.getFloatNull());
            fail();
        } catch (NullPointerException e) {
            //esperado
        }
    }

    public void testSetCameraRigTypeFree() {

        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.setCameraRigType(SXRCameraRig.SXRCameraRigType.Free.ID);
        assertEquals(sxrCameraRig.getCameraRigType(), SXRCameraRig.SXRCameraRigType.Free.ID);
    }

    public void testSetCameraRigTypeFreeze() {

        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.setCameraRigType(SXRCameraRig.SXRCameraRigType.Freeze.ID);
        assertEquals(sxrCameraRig.getCameraRigType(), SXRCameraRig.SXRCameraRigType.Freeze.ID);
    }

    public void testSetCameraRigTypeOrbitPivot() {

        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.setCameraRigType(SXRCameraRig.SXRCameraRigType.OrbitPivot.ID);
        assertEquals(sxrCameraRig.getCameraRigType(), SXRCameraRig.SXRCameraRigType.OrbitPivot.ID);
    }

    public void testSetCameraRigTypeRollFreeze() {

        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.setCameraRigType(SXRCameraRig.SXRCameraRigType.RollFreeze.ID);
        assertEquals(sxrCameraRig.getCameraRigType(), SXRCameraRig.SXRCameraRigType.RollFreeze.ID);
    }

    public void testSetCameraRigTypeYawOnly() {

        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        sxrCameraRig.setCameraRigType(SXRCameraRig.SXRCameraRigType.YawOnly.ID);
        assertEquals(sxrCameraRig.getCameraRigType(), SXRCameraRig.SXRCameraRigType.YawOnly.ID);
    }

    //FIXME https://github.com/Samsung/GearVRf/issues/26
    public void testInvalidCameraRigType() {

        try {
            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setCameraRigType(404);
        }catch (IllegalArgumentException e){}
        //fail();
    }

    //FIXME https://github.com/Samsung/GearVRf/issues/26
    public void testExpectedAnIllegalArgumentException() {
        try {
            SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
            sxrCameraRig.setCameraRigType(404);
            //fail();
        } catch (IllegalArgumentException e) {
            /*assertEquals(e.getMessage(), "IllegalArgumentException for cameraRigType. Types supported: " +
                    "SXRCameraRig.SXRCameraRigType.Free, " +
                    "SXRCameraRig.SXRCameraRigType.Freeze, " +
                    "SXRCameraRig.SXRCameraRigType.OrbitPivot" +
                    "SXRCameraRig.SXRCameraRigType.RollFreeze" +
                    "SXRCameraRig.SXRCameraRigType.YawOnly");*/
        }
    }


    /**
     * SXRCameraRig - Test getFloat
     * TODO-Native Crashd
     */

    public void ignoregetFloat1() {
        init();
        SXRCamera leftCamera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        leftCamera.setRenderMask(SXRRenderData.SXRRenderMaskBit.Left);
        SXRCamera rightCamera = new SXRPerspectiveCamera(TestDefaultSXRViewManager.mSXRContext);
        rightCamera.setRenderMask(SXRRenderData.SXRRenderMaskBit.Right);
        rightCamera.setBackgroundColorA(12412.1f);
        SXRSceneObject leftCameraObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        leftCameraObject.attachCamera(leftCamera);
        SXRSceneObject rightCameraObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        rightCameraObject.attachCamera(rightCamera);
        SXRSceneObject cameraRigObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRCameraRig cameraRig = SXRCameraRig.makeInstance(TestDefaultSXRViewManager.mSXRContext);
        cameraRig.attachLeftCamera(leftCamera);
        cameraRig.attachRightCamera(rightCamera);
        cameraRigObject.attachCameraRig(cameraRig);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addSceneObject(cameraRigObject);
        cameraRigObject.addChildObject(leftCameraObject);
        cameraRigObject.addChildObject(rightCameraObject);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().setMainCameraRig(cameraRig);

        assertTrue(!Float.isNaN(TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig().getFloat("ratio_r")));

    }

    /**
     * Created by daniel.nog on 1/29/2015.
     */
    public void testAttachDettachToParent() {

        SXRCameraRig cameraRig = SXRCameraRig.makeInstance(TestDefaultSXRViewManager.mSXRContext);
        SXRSceneObject sxrSceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        cameraRig.attachToParent(sxrSceneObject);
        cameraRig.detachFromParent(sxrSceneObject);
    }

    public void testgetTransform() {

        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        SXRTransform sxrTransform = sxrCameraRig.getTransform();
        assertNotNull(sxrTransform);
    }

    public void testAddRemoveChildObject() {

        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        SXRSceneObject sxrSceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        sxrCameraRig.addChildObject(sxrSceneObject);
        assertEquals(4,sxrCameraRig.getChildrenCount());
        sxrCameraRig.removeChildObject(sxrSceneObject);
    }

    public void testgetHeadTransform() {

        SXRCameraRig sxrCameraRig = TestDefaultSXRViewManager.mSXRContext.getMainScene().getMainCameraRig();
        SXRTransform sxrTransform = sxrCameraRig.getHeadTransform();
        assertNotNull(sxrTransform);
    }


}
