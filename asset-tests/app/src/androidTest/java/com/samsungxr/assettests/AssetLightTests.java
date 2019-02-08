package com.samsungxr.assettests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRLight;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.nodes.SXRCubeNode;

import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class AssetLightTests
{
    private static final String TAG = AssetLightTests.class.getSimpleName();
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

        SXRContext ctx = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();

        mWaiter.assertNotNull(scene);
        mBackground =
            new SXRCubeNode(ctx, false, new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID));
        mBackground.getTransform().setScale(10, 10, 10);
        mBackground.setName("background");
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
        mHandler = new AssetEventHandler(scene, mWaiter, mTestUtils, getClass().getSimpleName());
        if (!mDoCompare)
        {
            mHandler.disableImageCompare();
        }
    }

    @Test
    public void jassimpCubeDiffuseDirectional() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_diffuse_directionallight.fbx", 1, 0, "jassimpCubeDiffuseDirectional");
    }

    @Test
    public void jassimpCubeDiffusePoint() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_diffuse_pointlight.fbx", 1, 0, "jassimpCubeDiffusePoint");
    }

    @Test
    public void jassimpCubeDiffuseSpot() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_diffuse_spotlight.fbx", 1, 0, "jassimpCubeDiffuseSpot");
    }

    @Test
    public void jassimpCubeDiffuseSpotLinearDecay() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_diffuse_spotlight_linear.fbx", 1, 0, "jassimpCubeDiffuseSpotLinearDecay");
    }

    @Test
    public void jassimpCubeDiffuseSpotLinearDecay9() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_diffuse_spotlight_linear9.fbx", 1, 0, "jassimpCubeDiffuseSpotLinearDecay9");
    }

    @Test
    public void jassimpCubeNormalDiffuseAmbient() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_normal_diffuse_ambientlight.fbx", 2, 0, "jassimpCubeNormalDiffuseAmbient");
    }

    @Test
    public void jassimpCubeNormalDiffuseDirect() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_normal_diffuse_directionallight.fbx", 2, 0, "jassimpCubeNormalDiffuseDirect");
    }

    @Test
    public void jassimpCubeNormalDiffusePoint() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_normal_diffuse_pointlights.fbx", 2, 0, "jassimpCubeNormalDiffusePoint");
    }

    @Test
    public void jassimpCubeNormalDiffuseSpot() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_normal_diffuse_spotlight.fbx", 2, 0, "jassimpCubeNormalDiffuseSpot");
    }

    @Test
    public void jassimpCubeNormalDiffuseSpotLinearDecay() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_normal_diffuse_spotlight_linear_decay.fbx", 2, 0, "jassimpCubeNormalDiffuseSpotLinearDecay");
    }

    @Test
    public void jassimpCubeNormalDiffuseSpotQuadraticDecay() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_normal_diffuse_spotlight_quadratic_decay.fbx", 2, 0, "jassimpCubeNormalDiffuseSpotQuadraticDecay");
    }

    @Test
    public void jassimpCubeAmbientTexture() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_ambient_texture.fbx", 1, 0, "jassimpCubeAmbientTexture");
    }

    @Test
    public void jassimpCubeAmbientSpecularTexture() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_ambient_specular_texture.fbx", 2, 0, "jassimpCubeAmbientSpecularTexture");
    }

    @Test
    public void x3dPointLight1() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/pointlightsimple.x3d", 1, 0, "x3dPoint1");
    }

    @Test
    public void x3dPointLight2() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/pointlighttest.x3d", 2, 0, "x3dPoint2");
    }

    @Test
    public void x3dPointLightAttenuation() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/pointlightattenuationtest.x3d", 3, 0, "x3dPointLightAttenuation");
    }

    @Test
    public void x3dMultiplePoints() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/pointlightmultilights.x3d", 4, 0, "x3dMultiplePoints");
    }

    @Test
    public void x3dDirectLight() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/directionallight1.x3d", 4, 0, "x3dDirectLight");
    }

    @Test
    public void x3dSpotLight1() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/spotlighttest1.x3d", 4, 0, "x3dSpotLight1");
    }

    @Test
    public void x3dSpotLight2() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/spotlighttest2.x3d", 4, 0, "x3dSpotLight2");
    }

    @Test
    public void x3dSpotLight3() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/spotlighttest3.x3d", 4, 0, "x3dSpotLight3");
    }

    @Test
    public void x3dSpotLight4() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/lighting/spotlighttest4.x3d", 4, 0, "x3dSpotLight4");
    }

    @Test
    public void x3dGenerateNormalsPoint() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/generate_normals/nonormalswithptlights.x3d", 2, 0, "x3dGenerateNormalsPoint");
    }

    @Test
    public void x3dShininess() throws TimeoutException
    {
        mHandler.setWaitFrames(4);
        mHandler.loadTestModel("x3d/teapotandtorus_AddPtLt.x3d", 2, 0, "x3dShininess");
    }

    @Test
    public void testAddLight() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/animals/wolf-obj.obj", 3, 0, null);
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRDirectLight light1 = new SXRDirectLight(ctx);
        SXRDirectLight light2 = new SXRDirectLight(ctx);
        SXRNode topLight = new SXRNode(ctx);

        light1.setDiffuseIntensity(1, 1, 0.5f, 1);
        topLight.attachComponent(light2);
        topLight.getTransform().rotateByAxis(-90.0f, 1, 0, 0);
        topLight.getTransform().setPositionY(1.0f);
        scene.addNode(topLight);
        scene.getMainCameraRig().getHeadTransformObject().attachComponent(light1);
        mTestUtils.waitForXFrames(4);
        mTestUtils.screenShot(getClass().getSimpleName(), "testAddLight", mWaiter, mDoCompare);
    }

    @Test
    public void testRemoveLight() throws TimeoutException
    {
        SXRNode model = mHandler.loadTestModel("jassimp/astro_boy.dae", 4, 0, null);
        mTestUtils.waitForXFrames(2);
        List<SXRLight> lights = model.getAllComponents(SXRLight.getComponentType());

        for (SXRLight l : lights)
        {
            l.getOwnerObject().detachComponent(SXRLight.getComponentType());
        }
        mTestUtils.waitForXFrames(4);
        mTestUtils.screenShot(getClass().getSimpleName(), "testRemoveLight", mWaiter, mDoCompare);
    }

 /*
    @Test
    public void VRBenchmark() throws TimeoutException
    {
        SXRNode model = loadTestModel("sd:GearVRF/benchmark_assets/TestShadowNew.FBX", 2, 0, null);
        model.getTransform().rotateByAxis(90, 0, 1, 0);
        model.getTransform().setPosition(0, -0.5f, 0);
        mTestUtils.waitForXFrames(2000);
    }
    */
}
