package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRSpotLight;
import com.samsungxr.scene_objects.SXRCubeSceneObject;
import com.samsungxr.scene_objects.SXRSphereSceneObject;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class ShadowTests
{
    private static final String TAG = ShadowTests.class.getSimpleName();
    private static final int WAIT_FRAMES = 8;
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRSceneObject mRoot;
    private SXRSceneObject mSphere;
    private SXRSceneObject mCube;
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

        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial blue = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMaterial red = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRSceneObject background;

        mWaiter.assertNotNull(scene);
        scene.getMainCameraRig().setFarClippingDistance(20.0f);
        background = makeBackground(ctx);
        blue.setDiffuseColor(0, 0, 1, 1);
        red.setDiffuseColor(0.8f, 0, 0, 1);
        red.setSpecularColor(0.6f, 0.3f, 0.6f, 1);
        red.setSpecularExponent(10.0f);
        mSphere = new SXRSphereSceneObject(ctx, true, red);
        mSphere.getTransform().setPosition(0, 0.5f, -3);
        mSphere.setName("sphere");
        mCube = new SXRCubeSceneObject(ctx, true, blue);
        mCube.getTransform().setPosition(0, -0.5f, -3);
        mCube.setName("cube");
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
        scene.addSceneObject(background);
    }

    void setupShadow(SXRDirectLight light, SXRSceneObject owner)
    {
        owner.attachComponent(light);
        light.setShadowRange(0.1f, 25.0f);
    }

    void setupShadow(SXRSpotLight light, SXRSceneObject owner)
    {
        owner.attachComponent(light);
        light.setShadowRange(0.1f, 25.0f);
    }

    @Test
    public void spotLightAtCornerCastsShadow() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj = new SXRSceneObject(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        setupShadow(light, lightObj);
        lightObj.getTransform().rotateByAxis(-45, 1, 0, 0);
        lightObj.getTransform().rotateByAxis(35, 0, 1, 0);
        lightObj.getTransform().setPosition(3, 3, 0);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightAtCornerCastsShadow", mWaiter, mDoCompare);
    }

    @Test
    public void spotLightAtFrontCastsShadow() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj = new SXRSceneObject(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        setupShadow(light, lightObj);
        lightObj.getTransform().rotateByAxis(-45, 1, 0, 0);
        lightObj.getTransform().setPosition(0, 3, 0);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightAtFrontCastsShadow", mWaiter, mDoCompare);
    }


    @Test
    public void spotLightAtSideCastsShadow() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj = new SXRSceneObject(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        setupShadow(light, lightObj);
        lightObj.getTransform().rotateByAxis(-90, 0, 1, 0);
        lightObj.getTransform().setPosition(-3, 0, -3);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightAtSideCastsShadow", mWaiter, mDoCompare);
    }


    @Test
    public void spotLightAtTopCastsShadow() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj = new SXRSceneObject(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        setupShadow(light, lightObj);
        lightObj.getTransform().rotateByAxis(-90, 1, 0, 0);
        lightObj.getTransform().setPosition(0, 3, -3);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightAtTopCastsShadow", mWaiter, mDoCompare);
    }

    @Test
    public void directLightAtCornerCastsShadow() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj = new SXRSceneObject(ctx);
        SXRDirectLight light = new SXRDirectLight(ctx);

        setupShadow(light, lightObj);
        lightObj.getTransform().rotateByAxis(-45, 1, 0, 0);
        lightObj.getTransform().rotateByAxis(35, 0, 1, 0);
        lightObj.getTransform().setPosition(3, 3, 3);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "directLightAtCornerCastsShadow", mWaiter, mDoCompare);
    }


    @Test
    public void directLightAtFrontCastsShadow() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj = new SXRSceneObject(ctx);
        SXRDirectLight light = new SXRDirectLight(ctx);

        setupShadow(light, lightObj);
        lightObj.getTransform().rotateByAxis(-45, 1, 0, 0);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "directLightAtFrontCastsShadow", mWaiter, mDoCompare);
    }

    @Test
    public void directLightAtSideCastsShadow() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj = new SXRSceneObject(ctx);
        SXRDirectLight light = new SXRDirectLight(ctx);

        setupShadow(light, lightObj);
        lightObj.getTransform().rotateByAxis(-90, 0, 1, 0);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "directLightAtSideCastsShadow", mWaiter, mDoCompare);
    }

    @Test
    public void directLightAtTopCastsShadow() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj = new SXRSceneObject(ctx);
        SXRDirectLight light = new SXRDirectLight(ctx);

        setupShadow(light, lightObj);
        lightObj.getTransform().rotateByAxis(-90, 1, 0, 0);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "directLightAtTopCastsShadow", mWaiter, mDoCompare);
    }

    @Test
    public void twoLightsCastShadows() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj1 = new SXRSceneObject(ctx);
        SXRSceneObject lightObj2 = new SXRSceneObject(ctx);
        SXRDirectLight light1 = new SXRDirectLight(ctx);
        SXRSpotLight light2 = new SXRSpotLight(ctx);

        setupShadow(light1, lightObj1);
        lightObj1.getTransform().rotateByAxis(-90, 1, 0, 0);
        setupShadow(light2, lightObj2);
        lightObj2.getTransform().rotateByAxis(-90, 0, 1, 0);
        lightObj2.getTransform().setPosition(-3, 0, -3);
        light2.setInnerConeAngle(30.0f);
        light2.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj1);
        mRoot.addChildObject(lightObj2);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "twoLightsCastShadows", mWaiter, mDoCompare);
    }


    @Test
    public void threeLightsCastShadows() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRSceneObject lightObj1 = new SXRSceneObject(ctx);
        SXRSceneObject lightObj2 = new SXRSceneObject(ctx);
        SXRSceneObject lightObj3 = new SXRSceneObject(ctx);
        SXRDirectLight light1 = new SXRDirectLight(ctx);
        SXRSpotLight light2 = new SXRSpotLight(ctx);
        SXRSpotLight light3 = new SXRSpotLight(ctx);

        setupShadow(light1, lightObj1);
        lightObj1.getTransform().rotateByAxis(-90, 1, 0, 0);
        setupShadow(light2, lightObj2);
        lightObj2.getTransform().rotateByAxis(-90, 0, 1, 0);
        lightObj2.getTransform().setPosition(-3, 0, -3);
        light2.setInnerConeAngle(30.0f);
        light2.setOuterConeAngle(45.0f);
        setupShadow(light3, lightObj3);
        lightObj3.getTransform().rotateByAxis(-45, 0, 1, 0);
        lightObj3.getTransform().setPosition(0, 3, 0);
        light3.setInnerConeAngle(30.0f);
        light3.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj1);
        mRoot.addChildObject(lightObj2);
        mRoot.addChildObject(lightObj3);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(WAIT_FRAMES);
        mTestUtils.screenShot(getClass().getSimpleName(), "threeLightsCastShadows", mWaiter, mDoCompare);
    }

    SXRSceneObject makeBackground(SXRContext ctx)
    {
        SXRMaterial leftmtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMaterial rightmtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMaterial floormtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMaterial backmtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRSceneObject rightside =  new SXRSceneObject(ctx, 4.0f, 4.0f);
        SXRSceneObject leftside =  new SXRSceneObject(ctx, 4.0f, 4.0f);
        SXRSceneObject floor =  new SXRSceneObject(ctx, 4.0f, 4.0f);
        SXRSceneObject back = new SXRSceneObject(ctx, 4.0f, 4.0f);
        SXRSceneObject background = new SXRSceneObject(ctx);

        backmtl.setAmbientColor(0.3f, 0.3f, 0.3f, 1.0f);
        backmtl.setDiffuseColor(0.7f, 0.7f, 0.7f, 1.0f);
        rightmtl.setAmbientColor(0.4f, 0.4f, 0.2f, 1.0f);
        rightmtl.setDiffuseColor(0.7f, 0.7f, 0.3f, 1.0f);
        leftmtl.setAmbientColor(0.2f, 0.4f, 0.4f, 1.0f);
        leftmtl.setDiffuseColor(0.4f, 0.7f, 0.7f, 1.0f);
        floormtl.setAmbientColor(0.4f, 0.2f, 0.4f, 1.0f);
        floormtl.setDiffuseColor(0.7f, 0.4f, 0.7f, 1.0f);

        floor.getRenderData().setMaterial(floormtl);
        floor.getTransform().rotateByAxis(-90, 1, 0, 0);
        floor.getTransform().setPosition(0, -2.0f, -2.0f);
        floor.getRenderData().setCastShadows(false);

        rightside.getRenderData().setMaterial(rightmtl);
        rightside.getRenderData().setCastShadows(false);
        rightside.getTransform().rotateByAxis(90, 0, 1, 0);
        rightside.getTransform().setPosition(-2.0f, 0.0f, -2.0f);

        leftside.getRenderData().setMaterial(leftmtl);
        leftside.getRenderData().setCastShadows(false);
        leftside.getTransform().rotateByAxis(-90, 0, 1, 0);
        leftside.getTransform().setPosition(2.0f, 0.0f, -2.0f);

        back.getRenderData().setMaterial(backmtl);
        back.getRenderData().setCastShadows(false);
        back.getTransform().setPosition(0.0f, 0.0f, -4.0f);

        background.addChildObject(floor);
        background.addChildObject(rightside);
        background.addChildObject(leftside);
        background.addChildObject(back);
        return background;
    }
}