package com.samsungxr.tester;


import android.graphics.Color;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRComponent;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRExternalScene;
import com.samsungxr.SXRLight;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRPointLight;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRResourceVolume;
import com.samsungxr.SXRImportSettings;
import com.samsungxr.SXRShaderId;
import com.samsungxr.SXRSpotLight;
import com.samsungxr.SXRVertexBuffer;
import com.samsungxr.animation.SXRAnimator;
import com.samsungxr.animation.SXRRepeatMode;
import com.samsungxr.nodes.SXRCubeNode;

import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class AssetImportTests
{
    private static final String TAG = AssetImportTests.class.getSimpleName();
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRNode mRoot;
    private SXRNode mBackground;
    private boolean mDoCompare = true;
    private AssetEventHandler mHandler;

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

        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();

        mWaiter.assertNotNull(scene);
        mBackground = new SXRCubeNode(ctx, false, new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID));
        mBackground.getTransform().setScale(10, 10, 10);
        mBackground.setName("background");
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
        mHandler = new AssetEventHandler(scene, mWaiter, mTestUtils, getClass().getSimpleName());
    }

    @Test
    public void canLoadModel() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;
        String baseName = "astro_boy.dae";
        String filePath = "jassimp/astro_boy.dae";
        //String baseName = "TRex_NoGround.fbx";
        //String filePath = SXRTestUtils.GITHUB_URL + "jassimp/trex/TRex_NoGround.fbx";
        EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.NO_ANIMATION));
        ctx.getEventReceiver().addListener(mHandler);
        mHandler.dontAddToScene();
        try
        {
            model = ctx.getAssetLoader().loadModel(filePath, settings, true, null);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.checkAssetLoaded(null, 4);
        mWaiter.assertNull(scene.getNodeByName(baseName));
        mHandler.checkAssetErrors(0, 0);
        mHandler.centerModel(model, scene.getMainCameraRig().getTransform());
        scene.addNode(model);
        mWaiter.assertNotNull(scene.getNodeByName(baseName));
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot("AssetImportTests", "canLoadModel", mWaiter, mDoCompare);
    }

    @Test
    public void canLoadModelWithHandler() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;
        String filePath = "jassimp/astro_boy.dae";

        try
        {
            model = ctx.getAssetLoader().loadModel(filePath, mHandler);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.checkAssetLoaded(null, 4);
        mWaiter.assertTrue(model.getChildrenCount() > 0);
        mHandler.checkAssetErrors(0, 0);
        mHandler.centerModel(model, scene.getMainCameraRig().getTransform());
        scene.addNode(model);
        mWaiter.assertNotNull(scene.getNodeByName("astro_boy.dae"));
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot("AssetImportTests", "canLoadModelWithHandler", mWaiter, mDoCompare);
    }

    @Test
    public void canLoadModelWithCustomIO() throws TimeoutException
    {
        class ResourceLoader extends SXRResourceVolume
        {
            class Resource extends SXRAndroidResource
            {
                public Resource(SXRContext ctx, String path) throws IOException
                {
                    super(ctx, path);
                }

                public synchronized void openStream() throws IOException
                {
                    // do some special stuff here
                    super.openStream();
                }
            }
            public int ResourcesLoaded = 0;

            public ResourceLoader(SXRContext ctx, String fileName)
            {
                super(ctx, fileName);
            }

            public SXRAndroidResource openResource(String filePath) throws IOException
            {
                ++ResourcesLoaded;
                Resource resource = new Resource(sxrContext, getFullPath(defaultPath, filePath));
                return super.addResource(resource);
            }
        };
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = new SXRNode(ctx);
        ResourceLoader volume = new ResourceLoader(ctx, "jassimp/astro_boy.dae");
        EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.NO_ANIMATION));

        mHandler.dontAddToScene();
        ctx.getAssetLoader().loadModel(volume, model, settings, false, mHandler);
        mTestUtils.waitForAssetLoad();
        mWaiter.assertEquals(5, volume.ResourcesLoaded);
        mHandler.checkAssetLoaded(null, 4);
        mWaiter.assertNull(scene.getNodeByName("astro_boy.dae"));
        mWaiter.assertTrue(model.getChildrenCount() > 0);
        mHandler.checkAssetErrors(0, 0);
        mHandler.centerModel(model, scene.getMainCameraRig().getTransform());
        scene.addNode(model);
        mWaiter.assertNotNull(scene.getNodeByName("astro_boy.dae"));
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot("AssetImportTests", "canLoadModelWithCustomIO", mWaiter, mDoCompare);
    }


    @Test
    public void canLoadModelInScene() throws TimeoutException
    {
        mHandler.loadTestModel("jassimp/astro_boy.dae", 4, 0, "canLoadModelInScene");
    }

    @Test
    public void canLoadExternalScene() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRExternalScene sceneLoader = new SXRExternalScene(ctx, "jassimp/astro_boy.dae", true);
        SXRNode model = new SXRNode(ctx);

        ctx.getEventReceiver().addListener(mHandler);
        model.attachComponent(sceneLoader);
        mHandler.dontAddToScene();
        sceneLoader.load(scene);
        mWaiter.assertNotNull(model);
        mTestUtils.waitForAssetLoad();
        mHandler.centerModel(model, scene.getMainCameraRig().getTransform());
        mHandler.checkAssetLoaded("astro_boy.dae", 4);
        mHandler.checkAssetErrors(0, 0);
        mTestUtils.waitForXFrames(5);
        mTestUtils.screenShot("AssetImportTests", "canLoadExternalScene", mWaiter, mDoCompare);
    }


    class MeshVisitorNoLights implements SXRNode.ComponentVisitor
    {
        public boolean visit(SXRComponent comp)
        {
            if (comp.getClass().isAssignableFrom(SXRRenderData.class))
            {
                SXRRenderData rdata = (SXRRenderData) comp;
                SXRMesh mesh = rdata.getMesh();
                if (mesh != null)
                {
                    SXRVertexBuffer vbuf = mesh.getVertexBuffer();
                    mWaiter.assertNotNull(vbuf);
                    mWaiter.assertTrue(vbuf.hasAttribute("a_position"));
                    mWaiter.assertFalse(vbuf.hasAttribute("a_normal"));
                    mWaiter.assertTrue(vbuf.hasAttribute("a_texcoord"));
                }
            }
            else if (comp.getClass().isAssignableFrom(SXRLight.class))
            {
                mWaiter.fail("Light sources are present and should not be");
            }
            return true;
        }
    }

    class MeshVisitorNoTexture implements SXRNode.ComponentVisitor
    {
        public boolean visit(SXRComponent comp)
        {
            SXRRenderData rdata = (SXRRenderData) comp;
            SXRMesh mesh = rdata.getMesh();
            if (mesh != null)
            {
                SXRVertexBuffer vbuf = mesh.getVertexBuffer();
                mWaiter.assertNotNull(vbuf);
                mWaiter.assertTrue(vbuf.hasAttribute("a_position"));
                mWaiter.assertTrue(vbuf.hasAttribute("a_normal"));
                mWaiter.assertFalse(vbuf.hasAttribute("a_texcoord"));
            }
            return true;
        }
    }

    @Test
    public void canLoadModelWithoutTextures() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.NO_TEXTURING));
            model = ctx.getAssetLoader().loadModel("jassimp/astro_boy.dae", settings, true, (SXRScene) null);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.checkAssetLoaded(null, 0);
        mHandler.checkAssetErrors(0, 0);
        model.forAllComponents(new MeshVisitorNoTexture(), SXRRenderData.getComponentType());
    }

    @Test
    public void canX3DLoadModelWithoutTextures() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.NO_TEXTURING));
            model = ctx.getAssetLoader().loadModel(SXRTestUtils.GITHUB_URL + "x3d/general/twoplanesobjects.x3d", settings, true, (SXRScene) null);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.checkAssetLoaded(null, 0);
        mHandler.checkAssetErrors(0, 0);
        model.forAllComponents(new MeshVisitorNoTexture(), SXRRenderData.getComponentType());
    }

    @Test
    public void canLoadModelWithoutLights() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;
        SXRPointLight light = new SXRPointLight(ctx);
        SXRNode lightObj = new SXRNode(ctx);
        SXRCameraRig rig = scene.getMainCameraRig();

        rig.getLeftCamera().setBackgroundColor(1, 1, 0.5f, 1.0f);
        rig.getRightCamera().setBackgroundColor(1, 1, 0.5f, 1.0f);
        rig.getCenterCamera().setBackgroundColor(1, 1, 0.5f, 1.0f);
        ctx.getEventReceiver().addListener(mHandler);
        lightObj.attachComponent(light);
        try
        {
            EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.NO_LIGHTING));
            model = ctx.getAssetLoader().loadModel( "gear_vr_controller.obj", settings, true, (SXRScene) scene);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.checkAssetLoaded(null, 1);
        mHandler.checkAssetErrors(0, 0);
        model.getTransform().setPosition(0, 0, -0.2f);
        model.forAllComponents(new MeshVisitorNoLights());
    }

    @Test
    public void canLoadX3DModelWithoutLights() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.NO_LIGHTING));
            model = ctx.getAssetLoader().loadModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/pointlightmultilights.x3d", settings, true, (SXRScene) null);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.checkAssetLoaded(null, 4);
        mHandler.checkAssetErrors(0, 0);
        model.forAllComponents(new MeshVisitorNoLights());
    }

    @Test
    public void PLYVertexColors() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;
        String modelName = "man128.ply";

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            model = ctx.getAssetLoader().loadModel("jassimp/" + modelName, (SXRScene) null);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.checkAssetLoaded(null, 0);
        mHandler.centerModel(model, scene.getMainCameraRig().getTransform());
        mHandler.checkAssetErrors(0, 0);
        List<SXRRenderData> rdatas = model.getAllComponents(SXRRenderData.getComponentType());
        SXRMaterial vertexColorMtl = new SXRMaterial(ctx, new SXRShaderId(VertexColorShader.class));
        for (SXRRenderData rdata : rdatas)
        {
            rdata.setMaterial(vertexColorMtl);
        }
        scene.addNode(model);
        mWaiter.assertNotNull(scene.getNodeByName(modelName));
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot("AssetImportTests", "PLYVertexColors", mWaiter, mDoCompare);
    }

    @Test
    public void jassimpTrees3DS() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/trees/trees9.3ds", 8, 0, "jassimpTrees3DS");
    }

    @Test
    public void jassimpBenchCollada() throws TimeoutException
    {
        mHandler.loadTestModel("jassimp/bench.dae", 0, 0, "jassimpBenchCollada");
    }

    @Test
    public void jassimpHippoOBJ() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/hippo/hippo.obj", 1, 0, "jassimpHippoOBJ");
    }

    @Test
    public void jassimpDeerOBJ() throws TimeoutException
    {
        SXRAndroidResource res = new SXRAndroidResource(mTestUtils.getSxrContext(), R.raw.deerobj);
        mHandler.loadTestModel(res, 1, 0, "jassimpDeerOBJ");
    }

    @Test
    public void jassimpBearOBJ() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/animals/bear-obj.obj", 3, 0, "jassimpBearOBJ");
    }

    @Test
    public void jassimpWolfOBJ() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/animals/wolf-obj.obj", 3, 0, "jassimpWolfOBJ");
    }

    @Test
    public void jassimpSkinningTREX() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/trex/TRex_NoGround.fbx", 1, 0, "jassimpSkinningTREX");
    }

    @Test
    public void jassimpGlossWaterBottleGLTF() throws TimeoutException
    {
        SXRNode light1 = createLight(mTestUtils.getSxrContext(), 1, 1, 1, 1.8f);
        SXRNode light2 = createLight(mTestUtils.getSxrContext(), 1, 1, 1, -0.8f);
        mRoot.addChildObject(light1);
        mRoot.addChildObject(light2);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "/jassimp/gltf/WaterBottle/WaterBottle.gltf", 6, 0, "jassimpGlossWaterBottleGLTF");
    }

    @Test
    public void jassimpTelephonePBRGLTF() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight pointLight = new SXRPointLight(ctx);
        SXRCameraRig rig = scene.getMainCameraRig();
        rig.getCenterCamera().setBackgroundColor(Color.LTGRAY);
        rig.getLeftCamera().setBackgroundColor(Color.LTGRAY);
        rig.getRightCamera().setBackgroundColor(Color.LTGRAY);

        pointLight.setDiffuseIntensity(0.8f, 0.8f, 08f, 1.0f);
        pointLight.setSpecularIntensity(0.8f, 0.8f, 08f, 1.0f);
        lightObj.attachComponent(pointLight);
        lightObj.getTransform().setPosition(-1.0f, 1.0f, 0);
        scene.addNode(lightObj);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/gltf/Telephone/Telephone.gltf", 4, 0, "jassimpTelephonePBRGLTF");
    }

    @Test
    public void jassimpCowPBRGLTF() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight pointLight = new SXRPointLight(ctx);
        SXRCameraRig rig = scene.getMainCameraRig();
        rig.getCenterCamera().setBackgroundColor(Color.LTGRAY);
        rig.getLeftCamera().setBackgroundColor(Color.LTGRAY);
        rig.getRightCamera().setBackgroundColor(Color.LTGRAY);

        pointLight.setDiffuseIntensity(0.8f, 0.8f, 08f, 1.0f);
        pointLight.setSpecularIntensity(0.8f, 0.8f, 08f, 1.0f);
        lightObj.attachComponent(pointLight);
        lightObj.getTransform().setPosition(-1.0f, 1.0f, 0);
        scene.addNode(lightObj);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/gltf/cow/cow.gltf", 1, 0, "jassimpCowPBRGLTF");
    }

    @Test
    public void jassimpEngineBinaryGLTF() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/gltf/2CylinderEngine-glTF-Binary/2CylinderEngine.glb", 0, 0, "jassimpEngineBinaryGLTF");
    }

    @Test
    public void jassimpBoxEmbeddedGLTF() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/gltf/BoxTextured-glTF-Embedded/BoxTextured.gltf", 1, 0, "jassimpBoxEmbeddedGLTF");
    }

    @Test
    public void jassimpLoadError() throws TimeoutException
    {
        try
        {
            mTestUtils.getSxrContext().getAssetLoader().loadModel("missingmodel.obj");
        }
        catch (IOException ex)
        {
            mWaiter.assertTrue(ex.getMessage().contains("FileNotFoundException"));
            return;
        }
        catch (Exception ex)
        {
            mWaiter.fail(ex);
        }
    }

    @Test
    public void x3dTeapotTorus() throws TimeoutException
    {
        mHandler.loadTestModel("x3d/teapottorusdirlights.x3d", 2, 0, "x3dTeapotTorus");
    }

    @Test
    public void x3dGenerateNormals() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/generate_normals/teapotandtorusnonormals.x3d", 2, 0, "x3dGenerateNormals");
    }

    @Test
    public void x3dOpacity() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/general/opacitytest01.x3d", 2, 0, "x3dOpacity");
    }

    @Test
    public void x3dEmissive() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/general/emissivecolor.x3d", 0, 0, "x3dEmissive");
    }

    @Test
    public void x3dHierarchy() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/general/twoplaneswithchildren.x3d", 5, 0, "x3dHierarchy");
    }

    @Test
    public void x3dLoadError() throws TimeoutException
    {
        try
        {
            mTestUtils.getSxrContext().getAssetLoader().loadModel("missingmodel.x3d");
        }
        catch (IOException ex)
        {
            mWaiter.assertTrue(ex.getMessage().contains("FileNotFoundException"));
        }
        catch (Exception ex)
        {
            mWaiter.fail(ex);
        }

        try
        {
            mTestUtils.getSxrContext().getAssetLoader().loadModel("http://blah.blah/missingmodel.x3d");
        }
        catch (IOException ex)
        {
            mWaiter.assertTrue(ex.getMessage().contains("UnknownHostException"));
        }
        catch (Exception ex)
        {
            mWaiter.fail(ex);
        }
    }

    private SXRNode createLight(SXRContext context, float r, float g, float b, float y)
    {
        SXRNode lightNode = new SXRNode(context);
        SXRSpotLight light = new SXRSpotLight(context);

        lightNode.attachLight(light);
        lightNode.getTransform().setPosition(0, y, 3);
        light.setAmbientIntensity(0.3f * r, 0.3f * g, 0.3f * b, 1);
        light.setDiffuseIntensity(r, g, b, 1);
        light.setSpecularIntensity(r, g, b, 1);
        light.setInnerConeAngle(8);
        light.setOuterConeAngle(12);
        return lightNode;
    }


}
