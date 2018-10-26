package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRPointLight;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRSpotLight;
import com.samsungxr.SXRTexture;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;

import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class LightTests
{
    private static final String TAG = LightTests.class.getSimpleName();
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRNode mRoot;
    private SXRNode mSphere;
    private SXRNode mCube;
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
        SXRMaterial white = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMaterial blue = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMaterial check = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRTexture checker = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.checker));
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);
        SXRNode background = new SXRCubeNode(ctx, false, white);

        ctx.getEventReceiver().addListener(texHandler);
        mWaiter.assertNotNull(scene);
        mWaiter.assertNotNull(checker);
        check.setTexture("diffuseTexture", checker);
        background.getTransform().setScale(10, 10, 10);
        blue.setDiffuseColor(0, 0, 1, 1);
        mSphere = new SXRSphereNode(ctx, true, blue);
        mSphere.getTransform().setPosition(0, 0, -2);
        mCube = new SXRCubeNode(ctx, true, check, new Vector3f(6, 3, 1));
        mCube.getTransform().setPosition(-1, 0, -4);
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
        mTestUtils.waitForAssetLoad();
        ctx.getEventReceiver().removeListener(texHandler);
        mRoot.addChildObject(background);
    }

    @Test
    public void pointLightAtFrontIlluminates() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight light = new SXRPointLight(ctx);

        lightObj.attachComponent(light);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "pointLightAtFrontIlluminates", mWaiter, mDoCompare);
    }

    @Test
    public void canDisableLightInRenderData() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight light = new SXRPointLight(ctx);

        lightObj.attachComponent(light);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mSphere.getRenderData().disableLight();
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "canDisableLightInRenderData", mWaiter, mDoCompare);
    }

    @Test
    public void pointLightIlluminatesInColor() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight light = new SXRPointLight(ctx);

        light.setDiffuseIntensity(0, 0, 1, 1);
        lightObj.attachComponent(light);
        mRoot.addChildObject(lightObj);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(),"pointLightIlluminatesInColor", mWaiter, mDoCompare);
    }

    @Test
    public void pointLightAtFrontAttenuates() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight light = new SXRPointLight(ctx);

        light.setAttenuation(0, 1, 0);
        lightObj.attachComponent(light);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "pointLightAtFrontAttenuates", mWaiter, mDoCompare);
    }

    @Test
    public void pointLightAtCornerIlluminates() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight light = new SXRPointLight(ctx);

        lightObj.getTransform().setPosition(-3, 0, 3);
        lightObj.attachComponent(light);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "pointLightAtCornerIlluminates", mWaiter, mDoCompare);
    }


    @Test
    public void pointLightHasSpecularReflection() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight light = new SXRPointLight(ctx);

        lightObj.getTransform().setPosition(-3, 0, 3);
        lightObj.attachComponent(light);
        mSphere.getRenderData().getMaterial().setSpecularColor(0.8f, 0.8f, 0.8f, 1.0f);
        mSphere.getRenderData().getMaterial().setSpecularExponent(8.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "pointLightHasSpecularReflection", mWaiter, mDoCompare);
    }

    @Test
    public void spotLightAtFrontIlluminates() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        lightObj.attachComponent(light);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightAtFrontIlluminates", mWaiter, mDoCompare);
   }

    @Test
    public void spotLightIlluminatesInColor() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        light.setDiffuseIntensity(1, 0, 0, 1);
        lightObj.attachComponent(light);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightIlluminatesInColor", mWaiter, mDoCompare);
    }

    @Test
    public void spotLightAtCornerIlluminates() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        lightObj.attachComponent(light);
        lightObj.getTransform().rotateByAxis(-45, 0, 1, 0);
        lightObj.getTransform().setPosition(-3, 0, 3);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightAtCornerIlluminates", mWaiter, mDoCompare);
    }

    @Test
    public void spotLightHasSpecularReflection() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        lightObj.attachComponent(light);
        lightObj.getTransform().rotateByAxis(-45, 0, 1, 0);
        lightObj.getTransform().setPosition(-3, 0, 3);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mSphere.getRenderData().getMaterial().setSpecularColor(0.8f, 0.8f, 0.8f, 1.0f);
        mSphere.getRenderData().getMaterial().setSpecularExponent(8.0f);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightHasSpecularReflection", mWaiter, mDoCompare);
    }

    @Test
    public void spotLightAtFrontAttenuates() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRSpotLight light = new SXRSpotLight(ctx);

        light.setAttenuation(0, 1, 0);
        lightObj.attachComponent(light);
        light.setInnerConeAngle(30.0f);
        light.setOuterConeAngle(45.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "spotLightAtFrontAttenuates", mWaiter, mDoCompare);
    }

    @Test
    public void directLightRotatedIlluminates() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRDirectLight light = new SXRDirectLight(ctx);

        lightObj.getTransform().rotateByAxis(-45, 0, 1, 0);
        lightObj.attachComponent(light);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "directLightRotatedIlluminates", mWaiter, mDoCompare);
    }


    @Test
    public void directLightIlluminatesInColor() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRDirectLight light = new SXRDirectLight(ctx);

        light.setAmbientIntensity(0.3f, 0.1f, 0.1f, 1);
        light.setDiffuseIntensity(0.3f, 0.3f, 0.6f, 1);
        lightObj.getTransform().rotateByAxis(-45, 0, 1, 0);
        lightObj.attachComponent(light);
        mRoot.addChildObject(lightObj);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "directLightIlluminatesInColor", mWaiter, mDoCompare);
    }

    @Test
    public void directLightHasSpecularReflection() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj = new SXRNode(ctx);
        SXRDirectLight light = new SXRDirectLight(ctx);

        lightObj.getTransform().rotateByAxis(-45, 0, 1, 0);
        lightObj.attachComponent(light);
        mSphere.getRenderData().getMaterial().setSpecularColor(0.8f, 0.8f, 0.8f, 1.0f);
        mSphere.getRenderData().getMaterial().setSpecularExponent(8.0f);
        mRoot.addChildObject(lightObj);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "directLightHasSpecularReflection", mWaiter, mDoCompare);
    }


    @Test
    public void directAndPointLightsIlluminate() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj1 = new SXRNode(ctx);
        SXRDirectLight light1 = new SXRDirectLight(ctx);
        SXRNode lightObj2 = new SXRNode(ctx);
        SXRPointLight light2 = new SXRPointLight(ctx);

        light1.setDiffuseIntensity(1, 0, 0.5f, 1);
        light2.setDiffuseIntensity(0, 0.2f, 0.5f, 1);
        lightObj1.getTransform().rotateByAxis(-90, 1, 0, 0);
        lightObj2.getTransform().setPositionZ(4);
        lightObj1.attachComponent(light1);
        lightObj2.attachComponent(light2);
        mSphere.getRenderData().getMaterial().setDiffuseColor(1, 1, 1, 1);
        mRoot.addChildObject(lightObj1);
        mRoot.addChildObject(lightObj2);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "directAndPointLightsIlluminate", mWaiter, mDoCompare);
    }

    @Test
    public void canDisableLight() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj1 = new SXRNode(ctx);
        SXRDirectLight light1 = new SXRDirectLight(ctx);
        SXRNode lightObj2 = new SXRNode(ctx);
        SXRPointLight light2 = new SXRPointLight(ctx);

        light1.setDiffuseIntensity(1, 0, 0.5f, 1);
        light2.setDiffuseIntensity(0, 0.2f, 0.5f, 1);
        lightObj1.getTransform().rotateByAxis(-90, 1, 0, 0);
        lightObj2.getTransform().setPositionZ(4);
        lightObj1.attachComponent(light1);
        lightObj2.attachComponent(light2);
        mSphere.getRenderData().getMaterial().setDiffuseColor(1, 1, 1, 1);
        mRoot.addChildObject(lightObj1);
        mRoot.addChildObject(lightObj2);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        light2.setEnable(false);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "canDisableLight", mWaiter, mDoCompare);
    }

    @Test
    public void canEnableLight() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj1 = new SXRNode(ctx);
        SXRDirectLight light1 = new SXRDirectLight(ctx);
        SXRNode lightObj2 = new SXRNode(ctx);
        SXRPointLight light2 = new SXRPointLight(ctx);

        light1.setEnable(false);
        light1.setDiffuseIntensity(1, 0, 0.5f, 1);
        light2.setDiffuseIntensity(0, 0.2f, 0.5f, 1);
        lightObj1.getTransform().rotateByAxis(-90, 1, 0, 0);
        lightObj2.getTransform().setPositionZ(4);
        lightObj1.attachComponent(light1);
        lightObj2.attachComponent(light2);
        mSphere.getRenderData().getMaterial().setDiffuseColor(1, 1, 1, 1);
        mRoot.addChildObject(lightObj1);
        mRoot.addChildObject(lightObj2);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        light1.setEnable(true);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "directAndPointLightsIlluminate", mWaiter, mDoCompare);
    }

    @Test
    public void twoSpotLightsIlluminate() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode lightObj1 = new SXRNode(ctx);
        SXRSpotLight light1 = new SXRSpotLight(ctx);
        SXRNode lightObj2 = new SXRNode(ctx);
        SXRSpotLight light2 = new SXRSpotLight(ctx);

        light1.setInnerConeAngle(20.0f);
        light1.setOuterConeAngle(30.0f);
        light1.setDiffuseIntensity(1.0f, 0.3f, 0.3f, 1.0f);
        light2.setInnerConeAngle(10.0f);
        light2.setOuterConeAngle(20.0f);
        light2.setDiffuseIntensity(0.3f, 1.0f, 0.3f, 1.0f);
        lightObj1.getTransform().rotateByAxis(-45, 0, 1, 0);
        lightObj1.getTransform().setPosition(-1, 0, 1);
        lightObj2.getTransform().rotateByAxis(45, 0, 1, 0);
        lightObj2.getTransform().setPosition(2, 0, 2);
        lightObj1.attachComponent(light1);
        lightObj2.attachComponent(light2);
        mSphere.getRenderData().getMaterial().setDiffuseColor(0.8f, 0.8f, 0.8f, 1.0f);
        mSphere.getRenderData().getMaterial().setSpecularColor(0.8f, 0.8f, 0.8f, 1.0f);
        mSphere.getRenderData().getMaterial().setSpecularExponent(8.0f);
        mRoot.addChildObject(lightObj1);
        mRoot.addChildObject(lightObj2);
        mRoot.addChildObject(mCube);
        mRoot.addChildObject(mSphere);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "twoSpotLightsIlluminate", mWaiter, mDoCompare);
    }
}
