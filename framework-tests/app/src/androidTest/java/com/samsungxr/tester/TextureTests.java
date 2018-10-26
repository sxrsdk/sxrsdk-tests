package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRImage;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRShader;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.SXRTransform;
import com.samsungxr.SXRVertexBuffer;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.nodes.SXRTextViewNode;
import com.samsungxr.utility.Log;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;


@RunWith(AndroidJUnit4.class)
public class TextureTests
{
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRNode mRoot;
    private boolean mDoCompare = true;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @After
    public void tearDown()
    {
        SXRScene scene = mTestUtils.getMainScene();
        if (scene != null)
        {
            scene.clear();
        }
    }

    @Before
    public void setUp() throws TimeoutException
    {
        SXRTestableActivity activity = ActivityRule.getActivity();
        mTestUtils = new SXRTestUtils(activity);
        mTestUtils.waitForOnInit();
        mWaiter = new Waiter();

        SXRScene scene = mTestUtils.getMainScene();
        mWaiter.assertNotNull(scene);
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
    }

    public void centerModel(SXRNode model)
    {
        SXRNode.BoundingVolume bv = model.getBoundingVolume();
        float sf = 1 / bv.radius;
        model.getTransform().setScale(sf, sf, sf);
        bv = model.getBoundingVolume();
        model.getTransform().setPosition(-bv.center.x, -bv.center.y, -bv.center.z - 1.5f * bv.radius);
    }

    public void repeatTexcoords(SXRMesh mesh)
    {
        float[] texcoords = mesh.getTexCoords();

        for (int i = 0; i < texcoords.length; i++)
        {
            texcoords[i] *= 2.0f;
        }
        mesh.setFloatArray("a_texcoord1", texcoords);
    }

    private SXRNode makeObject(SXRContext ctx, float w, float h)
    {
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode sceneObj = new SXRNode(ctx, w, h, "float3 a_position float2 a_texcoord", mtl);
        SXRRenderData rd = sceneObj.getRenderData();

        mtl.setDiffuseColor(0, 1, 0, 0.5f);
        rd.setAlphaBlend(true);
        rd.setRenderingOrder(SXRRenderData.SXRRenderingOrder.TRANSPARENT);
        rd.setCullFace(SXRRenderPass.SXRCullFaceEnum.None);
        rd.setDepthTest(false);
        rd.setAlphaToCoverage(true);
        return sceneObj;
    }

    @Test
    public void testAlphaToCoverage() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRCameraRig rig = ctx.getMainScene().getMainCameraRig();
        SXRNode middle = makeObject(ctx, 3, 3);
        SXRNode bottom = makeObject(ctx, 2, 2);
        SXRNode top = makeObject(ctx, 2, 2);

        rig.getLeftCamera().setBackgroundColor(1, 1, 1, 1);
        rig.getRightCamera().setBackgroundColor(1, 1, 1, 1);
        top.getTransform().setPosition(0.5f, 0, -2);
        top.getRenderData().getMaterial().setDiffuseColor(0, 1, 0, 0.5f);
        middle.getTransform().setPosition(0, 0, -2.2f);
        middle.getRenderData().getMaterial().setDiffuseColor(0, 0, 1, 0.5f);
        bottom.getTransform().setPosition(-1, 0, -3);
        bottom.getRenderData().getMaterial().setDiffuseColor(1, 0, 0, 0.5f);
        mRoot.addChildObject(top);
        mRoot.addChildObject(middle);
        mRoot.addChildObject(bottom);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testAlphaToCoverage", mWaiter, mDoCompare);
    }


    @Test
    public void testCompressedTextureASTC() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode model = new SXRCubeNode(ctx, true, mtl);
        SXRDirectLight light = new SXRDirectLight(ctx);
        SXRNode lightObj = new SXRNode(ctx);

        light.setSpecularIntensity(0.5f, 0.5f, 0.5f, 1.0f);
        lightObj.attachComponent(light);
        scene.addNode(lightObj);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        try
        {
            SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, "sunmap.astc"));
            mtl.setTexture("diffuseTexture", tex2);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1);
        mtl.setSpecularColor(0.5f, 0.5f, 0.5f, 1.0f);
        mtl.setSpecularExponent(4.0f);
        model.getTransform().setPositionZ(-2.0f);
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testCompressedTextureASTC", mWaiter, mDoCompare);
    }

    @Test
    public void testCompressedTextureASTCUnlit() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode model = new SXRCubeNode(ctx, true, mtl);

        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        try
        {
            SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, "sunmap.astc"));
            mtl.setTexture("diffuseTexture", tex2);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1);
        mtl.setSpecularColor(0.5f, 0.5f, 0.5f, 1.0f);
        mtl.setSpecularExponent(4.0f);
        model.getTransform().setPositionZ(-2.0f);
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testCompressedTextureASTCUnlit", mWaiter, mDoCompare);
    }

    @Test
    public void testLayeredDiffuseTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial layeredMtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.PhongLayered.ID);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 2);
        ctx.getEventReceiver().addListener(texHandler);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex));
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.specularring));
        SXRNode model = new SXRCubeNode(ctx, true, layeredMtl);
        SXRDirectLight light = new SXRDirectLight(ctx);

        layeredMtl.setTexture("diffuseTexture", tex1);
        layeredMtl.setTexture("diffuseTexture1", tex2);
        layeredMtl.setInt("diffuseTexture1_blendop", 0);
        layeredMtl.setTexCoord("diffuseTexture", "a_texcoord", "diffuse_coord");
        layeredMtl.setTexCoord("diffuseTexture1", "a_texcoord", "diffuse_coord1");
        scene.getMainCameraRig().getOwnerObject().attachComponent(light);
        model.getTransform().setPositionZ(-2.0f);
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testLayeredDiffuseTexture", mWaiter, mDoCompare);
    }

    @Test
    public void testLayeredDiffuseTextureUnlit() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial layeredMtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.PhongLayered.ID);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 2);
        ctx.getEventReceiver().addListener(texHandler);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex));
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.specularring));
        SXRNode model = new SXRCubeNode(ctx, true, layeredMtl);

        layeredMtl.setTexture("diffuseTexture", tex1);
        layeredMtl.setTexture("diffuseTexture1", tex2);
        layeredMtl.setInt("diffuseTexture1_blendop", 0);
        layeredMtl.setTexCoord("diffuseTexture", "a_texcoord", "diffuse_coord");
        layeredMtl.setTexCoord("diffuseTexture1", "a_texcoord", "diffuse_coord1");
        model.getTransform().setPositionZ(-2.0f);
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testLayeredDiffuseTextureUnlit", mWaiter, mDoCompare);
    }

    @Test
    public void testMissingTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial mtl = new SXRMaterial(ctx);
        SXRNode model = new SXRCubeNode(ctx, true, mtl);

        mtl.setColor(0.7f, 0.4f, 0.6f);
        mtl.setMainTexture((SXRTexture) null);
        model.getTransform().setPositionZ(-2.0f);
        scene.addNode(model);
        mTestUtils.waitForSceneRendering();
    }

    @Test
    public void testRepeatTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode model = new SXRCubeNode(ctx, true, mtl);
        SXRDirectLight light = new SXRDirectLight(ctx);
        SXRMesh mesh = model.getRenderData().getMesh();
        SXRTextureParameters texparams = new SXRTextureParameters(ctx);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);
        ctx.getEventReceiver().addListener(texHandler);
        texparams.setWrapSType(SXRTextureParameters.TextureWrapType.GL_REPEAT);
        texparams.setWrapTType(SXRTextureParameters.TextureWrapType.GL_REPEAT);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex), texparams);
        float[] texcoords = mesh.getTexCoords();

        for (int i = 0; i < texcoords.length; i++)
        {
            texcoords[i] *= 2.0f;
        }
        mesh.setFloatArray("a_texcoord", texcoords);
        mtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("diffuseTexture", tex1);
        model.getTransform().setPositionZ(-2.0f);

        SXRNode rig = scene.getMainCameraRig().getOwnerObject();
        rig.attachComponent(light);
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testRepeatTexture", mWaiter, mDoCompare);
    }

    @Test
    public void testRepeatTextureUnlit() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode model = new SXRCubeNode(ctx, true, mtl);
        SXRMesh mesh = model.getRenderData().getMesh();
        SXRTextureParameters texparams = new SXRTextureParameters(ctx);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);
        ctx.getEventReceiver().addListener(texHandler);
        texparams.setWrapSType(SXRTextureParameters.TextureWrapType.GL_REPEAT);
        texparams.setWrapTType(SXRTextureParameters.TextureWrapType.GL_REPEAT);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex), texparams);
        float[] texcoords = mesh.getTexCoords();

        for (int i = 0; i < texcoords.length; i++)
        {
            texcoords[i] *= 2.0f;
        }
        mesh.setFloatArray("a_texcoord", texcoords);
        mtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("diffuseTexture", tex1);
        model.getTransform().setPositionZ(-2.0f);

        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testRepeatTextureUnlit", mWaiter, mDoCompare);
    }

    @Test
    public void testSpecularTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode model = new SXRCubeNode(ctx, true, mtl);
        SXRDirectLight light = new SXRDirectLight(ctx);
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.specularring));

        mtl.setDiffuseColor(0.7f, 0, 0.7f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("specularTexture", tex2);
        mtl.setTexCoord("specularTexture", "a_texcoord", "specular_coord");
        scene.getMainCameraRig().getOwnerObject().attachComponent(light);
        model.getTransform().setPositionZ(-2.0f);
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testSpecularTexture", mWaiter, mDoCompare);
    }

    @Test
    public void testRemoveTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        final SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Texture.ID);
        SXRNode model = new SXRCubeNode(ctx, true, mtl);
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.specularring));

        mtl.setDiffuseColor(0.7f, 0, 0.7f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("diffuseTexture", tex2);
        model.getTransform().setPositionZ(-2.0f);
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        ctx.runOnGlThread(new Runnable()
        {
            public void run()
            {
                mtl.setTexture("diffuseTexture", null);
            }
        });
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "testRemoveTexture", mWaiter, mDoCompare);
    }

    @Test
    public void testLayeredSpecularTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial layeredMtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.PhongLayered.ID);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 2);
        ctx.getEventReceiver().addListener(texHandler);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.wavylines));
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.specularring));
        SXRNode model = new SXRCubeNode(ctx, true, layeredMtl);
        SXRDirectLight light = new SXRDirectLight(ctx);

        layeredMtl.setDiffuseColor(0.7f, 0.2f, 0.2f, 1.0f);
        layeredMtl.setSpecularColor(1, 1, 1, 1);
        layeredMtl.setSpecularExponent(4.0f);
        layeredMtl.setTexture("specularTexture", tex1);
        layeredMtl.setTexture("specularTexture1", tex2);
        layeredMtl.setInt("specularTexture1_blendop", 0);
        layeredMtl.setTexCoord("specularTexture", "a_texcoord", "specular_coord");
        layeredMtl.setTexCoord("specularTexture1", "a_texcoord", "specular_coord1");
        scene.getMainCameraRig().getOwnerObject().attachComponent(light);
        model.getTransform().setPositionZ(-2.0f);
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testLayeredSpecularTexture", mWaiter, mDoCompare);
    }


    @Test
    public void testDiffuseSpecularTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRTextureParameters texparams = new SXRTextureParameters(ctx);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 2);
        ctx.getEventReceiver().addListener(texHandler);
        texparams.setWrapSType(SXRTextureParameters.TextureWrapType.GL_REPEAT);
        texparams.setWrapTType(SXRTextureParameters.TextureWrapType.GL_REPEAT);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex), texparams);
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.specularring));
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.PhongLayered.ID);
        SXRMesh mesh = SXRCubeNode.createCube(ctx, "float3 a_position, float2 a_texcoord, float3 a_normal, float2 a_texcoord1", true, new Vector3f(1, 1, 1));
        SXRNode model = new SXRNode(ctx, mesh, mtl);
        SXRDirectLight light = new SXRDirectLight(ctx);

        repeatTexcoords(mesh);
        model.getRenderData().setMesh(mesh);
        model.getTransform().setPositionZ(-2.0f);
        mtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("diffuseTexture", tex1);
        mtl.setTexture("specularTexture", tex2);
        mtl.setTexCoord("diffuseTexture", "a_texcoord1", "diffuse_coord");
        mtl.setTexCoord("specularTexture", "a_texcoord", "specular_coord");
        scene.getMainCameraRig().getOwnerObject().attachComponent(light);
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testDiffuseSpecularTexture", mWaiter, mDoCompare);
    }

    @Test
    public void testDiffuseSpecularTextureUnlit() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRTextureParameters texparams = new SXRTextureParameters(ctx);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 2);
        ctx.getEventReceiver().addListener(texHandler);
        texparams.setWrapSType(SXRTextureParameters.TextureWrapType.GL_REPEAT);
        texparams.setWrapTType(SXRTextureParameters.TextureWrapType.GL_REPEAT);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex), texparams);
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.specularring));
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.PhongLayered.ID);
        SXRMesh mesh = SXRCubeNode.createCube(ctx, "float3 a_position, float2 a_texcoord, float3 a_normal, float2 a_texcoord1", true, new Vector3f(1, 1, 1));
        SXRNode model = new SXRNode(ctx, mesh, mtl);

        repeatTexcoords(mesh);
        model.getRenderData().setMesh(mesh);
        model.getTransform().setPositionZ(-2.0f);
        mtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("diffuseTexture", tex1);
        mtl.setTexture("specularTexture", tex2);
        mtl.setTexCoord("diffuseTexture", "a_texcoord1", "diffuse_coord");
        mtl.setTexCoord("specularTexture", "a_texcoord", "specular_coord");
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testDiffuseSpecularTextureUnlit", mWaiter, mDoCompare);
    }

    @Test
    public void testNormalTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRTextureParameters texparams = new SXRTextureParameters(ctx);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);
        ctx.getEventReceiver().addListener(texHandler);
        texparams.setWrapSType(SXRTextureParameters.TextureWrapType.GL_REPEAT);
        texparams.setWrapTType(SXRTextureParameters.TextureWrapType.GL_REPEAT);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.rock_normal));
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode sphere = new SXRSphereNode(ctx, true, mtl, 1);
        SXRDirectLight light = new SXRDirectLight(ctx);

        mtl.setDiffuseColor(0.7f, 0.1f, 0.4f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("normalTexture", tex1);
        tex1.setTexCoord("a_texcoord", "normal_coord");
        scene.getMainCameraRig().getOwnerObject().attachComponent(light);
        sphere.getTransform().setPositionZ(-2.0f);
        mTestUtils.waitForAssetLoad();
        scene.addNode(sphere);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testNormalTexture", mWaiter, mDoCompare);
    }

    @Test
    public void testDiffuseNormalTexture() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRTextureParameters texparams = new SXRTextureParameters(ctx);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 2);
        ctx.getEventReceiver().addListener(texHandler);
        texparams.setWrapSType(SXRTextureParameters.TextureWrapType.GL_REPEAT);
        texparams.setWrapTType(SXRTextureParameters.TextureWrapType.GL_REPEAT);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex), texparams);
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.rock_normal));
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.PhongLayered.ID);
        SXRMesh mesh = SXRCubeNode.createCube(ctx, "float3 a_position, float2 a_texcoord, float3 a_normal, float2 a_texcoord1", true, new Vector3f(1, 1, 1));
        SXRDirectLight light = new SXRDirectLight(ctx);
        SXRNode model = new SXRNode(ctx, mesh, mtl);

        repeatTexcoords(mesh);
        model.getRenderData().setMesh(mesh);
        mtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("diffuseTexture", tex1);
        mtl.setTexture("normalTexture", tex2);
        tex1.setTexCoord("a_texcoord1", "diffuse_coord");
        tex2.setTexCoord("a_texcoord", "normal_coord");
        scene.getMainCameraRig().getOwnerObject().attachComponent(light);
        model.getTransform().setPositionZ(-2.0f);
        mTestUtils.waitForAssetLoad();
        scene.addNode(model);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testDiffuseNormalTexture", mWaiter, mDoCompare);
    }

    @Test
    public void testDiffuseNormalTextureSphere() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRTextureParameters texparams = new SXRTextureParameters(ctx);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 2);
        ctx.getEventReceiver().addListener(texHandler);
        texparams.setWrapSType(SXRTextureParameters.TextureWrapType.GL_REPEAT);
        texparams.setWrapTType(SXRTextureParameters.TextureWrapType.GL_REPEAT);

        SXRTexture tex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex), texparams);
        SXRTexture tex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.rock_normal));
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.PhongLayered.ID);
        SXRDirectLight light = new SXRDirectLight(ctx);
        SXRNode protoSphere = new SXRSphereNode(ctx, true, mtl);
        SXRMesh protoMesh = protoSphere.getRenderData().getMesh();
        SXRVertexBuffer newVerts = new SXRVertexBuffer(protoMesh.getVertexBuffer(),"float3 a_position, float2 a_texcoord, float3 a_normal, float2 a_texcoord1");
        SXRMesh newmesh = new SXRMesh(newVerts, protoMesh.getIndexBuffer());
        SXRNode sphere = new SXRNode(ctx, newmesh, mtl);

        repeatTexcoords(newmesh);
        mtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1);
        mtl.setSpecularColor(1, 1, 1, 1);
        mtl.setSpecularExponent(4.0f);
        mtl.setTexture("diffuseTexture", tex1);
        mtl.setTexture("normalTexture", tex2);
        tex1.setTexCoord("a_texcoord1", "diffuse_coord");
        tex2.setTexCoord("a_texcoord", "normal_coord");
        scene.getMainCameraRig().getOwnerObject().attachComponent(light);
        sphere.getTransform().setPositionZ(-2.0f);
        mTestUtils.waitForAssetLoad();
        scene.addNode(sphere);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testDiffuseNormalTextureSphere", mWaiter, mDoCompare);
    }

    @Test
    public void testLoadTextureFromResource() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRTexture texture = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex));
        SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode groundObject = new SXRCubeNode(ctx, true, material);

        material.setTexture("diffuseTexture", texture);
        groundObject.getTransform().setPositionZ(-2.0f);
        scene.addNode(groundObject);
        mTestUtils.waitForXFrames(3);
        mTestUtils.screenShot(getClass().getSimpleName(), "testLoadTextureFromResource", mWaiter, mDoCompare);
    }


    @Test
    public void testSwitchTextures() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        String[] texFiles = new String[] { "NumberOne.png", "NumberTwo.png" };
        SXRTexture[] textures = new SXRTexture[texFiles.length];
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, texFiles.length);
        int i = 0;
        ctx.getEventReceiver().addListener(texHandler);
        try
        {
            for (String texFile : texFiles)
            {
                SXRAndroidResource r = new SXRAndroidResource(ctx, texFile);
                textures[i++] = ctx.getAssetLoader().loadTexture(r);
            }
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        SXRTexture tex = new SXRTexture(ctx);
        SXRNode quad = new SXRNode(ctx, 2, 2, tex);
        quad.getTransform().setPositionZ(-4.0f);
        scene.addNode(quad);
        for (i = 0; i < 1000; ++i)
        {
            SXRTexture t = textures[i % texFiles.length];
            SXRImage image = t.getImage();
            tex.setImage(image);
            mTestUtils.waitForXFrames(1);
        }
    }

    public void checkResults(int actual, int truth)
    {
        mWaiter.assertEquals(truth, actual);
    }
}

