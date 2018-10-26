package com.samsungxr.da_samsungxr;

import com.samsungxr.SXRContext;
import com.samsungxr.SXREyePointeeHolder;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRMeshCollider;
import com.samsungxr.SXRMeshEyePointee;
import com.samsungxr.SXRNode;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.tests.R;
import com.samsungxr.misc.ColorShader;
import com.samsungxr.utils.UtilResource;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.BoundsValues;

import java.security.InvalidParameterException;

/**
 * Created by d.alipio@samsunsg.com; on 1/21/15.
 */
public class SXRMeshTest extends ActivityInstrumentationSXRf {


    private static final float UNPICKED_COLOR_R = 0.7f;
    private static final float UNPICKED_COLOR_G = 0.7f;
    private static final float UNPICKED_COLOR_B = 0.7f;
    private static final float UNPICKED_COLOR_A = 1.0f;
    private SXRNode mNode;

    public SXRMeshTest() {
        super(SXRTestActivity.class);
    }

    /**
     * Valid create mesh factory.
     */
    public void testCreateMeshFactory() {
        SXRContext sxrContext = TestDefaultSXRViewManager.mSXRContext;
        assertNotNull(sxrContext.createQuad(11, 22));
    }

    public void testTheVerticesPositionOfVertices() {
        SXRContext sxrContext = TestDefaultSXRViewManager.mSXRContext;
        SXRMesh mesh = new SXRMesh(sxrContext);
        float vertices[] = {-0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f, 0.5f, -0.5f, 0.0f};
        mesh.setVertices(vertices);
        assertEquals(mesh.getVertices()[0], vertices[0]);
        assertEquals(mesh.getVertices()[1], vertices[1]);
        assertEquals(mesh.getVertices()[2], vertices[2]);
        assertEquals(mesh.getVertices()[3], vertices[3]);
        assertEquals(mesh.getVertices()[4], vertices[4]);
        assertEquals(mesh.getVertices()[5], vertices[5]);
        assertEquals(mesh.getVertices()[6], vertices[6]);
        assertEquals(mesh.getVertices()[7], vertices[7]);
        assertEquals(mesh.getVertices()[8], vertices[8]);
        assertEquals(mesh.getVertices()[9], vertices[9]);
        assertEquals(mesh.getVertices()[10], vertices[10]);
        assertEquals(mesh.getVertices()[11], vertices[11]);
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

    public void testAttachCollider() {
        mNode = getColorBoard(1.0f, 1.0f);
        mNode.getTransform().setPosition(0.0f, 3.0f, -5.0f);
        SXRNode object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(0.0f, 3.0f, -5.0f);
        SXRContext sxrContext = TestDefaultSXRViewManager.mSXRContext;
        SXRMeshCollider collider = new SXRMeshCollider(sxrContext, mNode.getRenderData().getMesh());
        mNode.attachComponent(collider);
    }


    public void testGetOwnerObjectScene() {
        mNode = getColorBoard(1.0f, 1.0f);
        mNode.getTransform().setPosition(0.0f, 3.0f, -5.0f);
        SXRNode object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(0.0f, 3.0f, -5.0f);
        assertNotNull(mNode);
    }


    public void testGetNormalSXRMesh() {
        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
        assertNotNull(sxrMesh.getNormals());
    }


    /**
     * Try set object empty in @sxrMesh.setNormals
     */
    public void testRetrievePositionEmptyArrayInNormalsMesh() {
        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));

        try {
            sxrMesh.setNormals(BoundsValues.getArrayFloatEmpty());
            fail();

        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }

    }

    /**
     * Try set object empty in @sxrMesh.setFloatVector
     */
    public void testRetrievePositionEmptyArrayInVector() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));

        try {
            sxrMesh.setFloatVector("ratio_r", BoundsValues.getArrayFloatEmpty());
            fail();
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Try set object empty in @sxrMesh.setTexCoords
     */
    public void testRetrievePositionEmptyArrayInTextCoords() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));

        try {
            sxrMesh.setTexCoords(BoundsValues.getArrayFloatEmpty());
            fail();
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Try set object empty in @sxrMesh.setVec2Vector
     * TODO-Native Crash
     */

    public void ignoreRetrievePositionEmptyArrayInVec2() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));

        try {
            sxrMesh.setVec2Vector("ratio_", BoundsValues.getArrayFloatEmpty());
            assertNotNull(sxrMesh.getVec2Vector("ratio_r")[0]);
        } catch (Exception e) {
            fail();
        }

    }

    /**
     * Try set object empty in @sxrMesh.setVec3Vector
     * TODO-Native Crash
     */
    public void ignoreRetrievePositionEmptyArrayInVec3() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));

        try {
            sxrMesh.setVec3Vector("ratio_", BoundsValues.getArrayFloatEmpty());
            assertNull(sxrMesh.getVec3Vector("ratio_r")[0]);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Try set object empty in @sxrMesh.setVec4Vector
     */
    public void ignoreRetrievePositionEmptyArrayInVec4() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));

        try {
            sxrMesh.setVec4Vector("ratio_", BoundsValues.getArrayFloatEmpty());
            assertNotNull(sxrMesh.getVec4Vector("ratio_r")[0]);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Try set object empty in @sxrMesh.setVertices
     * TODO-Native Crashd
     */
    public void ignoreRetrievePositionEmptyArrayInVertices() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));

        try {
            sxrMesh.setVertices(BoundsValues.getArrayFloatEmpty());
            assertNotNull(sxrMesh.getVec4Vector("ratio_r")[0]);
        } catch (Exception e) {
            fail();
        }

    }

    /**
     * Create a array with six position in vec4.
     * In documentation we have:
     * <p/>
     * Bind an array of three-component float vectors to the shader attribute key.
     */
    public void testCreateArrayForVec4With6Position() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));
        float[] vec4 = {-0.5f, 0.5f, 0.0f, -0.5f, 0.5f, 0.0f};

        try {
            sxrMesh.setVec4Vector("ratio_r", vec4);
            assertNotNull(sxrMesh.getVec4Vector("ratio_r"));
            fail();
        } catch (Exception e) {
            //assertEquals(e.getMessage(), "setVec4Vector method support only three position array");
        }
    }


    /**
     * Create a array with six position in vec3.
     * In documentation we have:
     * <p/>
     * <p/>
     * Bind an array of three-component float vectors to the shader attribute key.
     */
    public void ignoreCreateArrayForVec3With6Position() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));
        float[] vec3 = {-0.5f, 0.5f, 0.0f, -0.5f, 0.5f, 0.0f};

        try {
            sxrMesh.setVec3Vector("ratio_r", vec3);
            assertNotNull(sxrMesh.getVec4Vector("ratio_r"));
            fail();
        } catch (InvalidParameterException e) {
            assertEquals(e.getMessage(), "setVec3Vector method support only three position array");
        }
    }

    /**
     * Create a array with six position in vec2.
     * In documentation we have:
     * <p/>
     * Bind an array of two-component float vectors to the shader attribute key.
     */
    public void ignoreCreateArrayForVec2With6Position() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));
        float[] vec2 = {-0.5f, 0.5f, 0.0f, -0.5f, 0.5f, 0.0f};

        try {
            sxrMesh.setVec3Vector("ratio_r", vec2);
            assertNotNull(sxrMesh.getVec4Vector("ratio_r"));
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "setVec2Vector method support only three position array");
        }
    }

    /**
     * Created by Elidelson on 9/02/15.
     */
    public void testGetSetTexCoords() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));
        float[] coords = {0.5f, 0.5f, 0.0f, 0.5f};
        sxrMesh.setTexCoords(coords);
        assertEquals(4,sxrMesh.getTexCoords().length);
        float[] coords2 = {0.5f, 0.5f, 0.0f, 0.5f,0.5f};
        try {
            sxrMesh.setTexCoords(coords2);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
     }

    public void testGetSetFloatVector() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
        float[] coords = new float[80];
        for (int i = 0; i < 80; i++) coords[i] = 0.1f;
        sxrMesh.setFloatVector("test", coords);
        assertEquals(80, sxrMesh.getFloatVector("test").length);

        float[] coords2 = new float[100];
        for (int i = 0; i < 100; i++) coords2[i] = 0.1f;
        try{
           sxrMesh.setFloatVector("test", coords2);
           assertEquals(100, sxrMesh.getFloatVector("test").length);
           fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

    public void testGetSetVec2Vector() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
        float[] coords = new float[160];
        for (int i = 0; i < 160; i++) coords[i] = 0.1f;
        sxrMesh.setVec2Vector("test", coords);
        assertEquals(160, sxrMesh.getVec2Vector("test").length);

        float[] coords2 = new float[100];
        for (int i = 0; i < 100; i++) coords2[i] = 0.1f;
        try{
            sxrMesh.setVec2Vector("test", coords2);
            assertEquals(100, sxrMesh.getVec2Vector("test").length);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }


    public void testGetSetVec3Vector() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
        float[] coords = new float[240];
        for (int i = 0; i < 240; i++) coords[i] = 0.1f;
        sxrMesh.setVec3Vector("test", coords);
        assertEquals(240, sxrMesh.getVec3Vector("test").length);

        float[] coords2 = new float[100];
        for (int i = 0; i < 100; i++) coords2[i] = 0.1f;
        try{
            sxrMesh.setVec3Vector("test", coords2);
            assertEquals(100, sxrMesh.getVec3Vector("test").length);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

    public void testGetSetVec4Vector() {

        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
        float[] coords = new float[320];
        for (int i = 0; i < 320; i++) coords[i] = 0.1f;
        sxrMesh.setVec4Vector("test", coords);
        assertEquals(320, sxrMesh.getVec4Vector("test").length);

        float[] coords2 = new float[100];
        for (int i = 0; i < 100; i++) coords2[i] = 0.1f;
        try{
            sxrMesh.setVec4Vector("test", coords2);
            assertEquals(100, sxrMesh.getVec4Vector("test").length);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

    public void testGetBoundingBox(){
        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
        SXRMesh sxrMesh2 = sxrMesh.getBoundingBox();
        assertNotNull(sxrMesh2);
    }

    public void testEyePointeeSetMesh(){
        SXRMesh sxrMesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
        SXRMeshEyePointee sxrMeshEyePointee = new SXRMeshEyePointee(sxrMesh,true);
        SXRMeshEyePointee sxrMeshEyePointee2 = new SXRMeshEyePointee(sxrMesh,false);
        assertNotNull(sxrMeshEyePointee);
        assertNotNull(sxrMeshEyePointee2);
        sxrMeshEyePointee.setMesh(sxrMesh);
        sxrMeshEyePointee.getMesh();
        assertNotNull(sxrMeshEyePointee);
    }

}
