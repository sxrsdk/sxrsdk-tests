package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRTexture;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class CullTests
{
    private SXRTestUtils sxrTestUtils;
    private Waiter mWaiter;
    private SXRSceneObject mRoot;
    private boolean mDoCompare = true;

    public CullTests() {
        super();
    }

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new
            ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);


    @After
    public void tearDown()
    {
        SXRScene scene = sxrTestUtils.getMainScene();
        if (scene != null)
        {
            scene.clear();
        }
    }

    @Before
    public void setUp() throws TimeoutException {
        sxrTestUtils = new SXRTestUtils(ActivityRule.getActivity());
        mWaiter = new Waiter();
        sxrTestUtils.waitForOnInit();
        SXRContext ctx  = sxrTestUtils.getSxrContext();
        SXRScene mainScene = sxrTestUtils.getMainScene();
        SXRSceneObject cube = new SXRSceneObject(ctx);
        TextureEventHandler texHandler = new TextureEventHandler(sxrTestUtils, 4);

        ctx.getEventReceiver().addListener(texHandler);

        SXRTexture tempTex1 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.redtex));
        SXRSceneObject quad1 = new SXRSceneObject(ctx, 4, 4, tempTex1);
        quad1.getTransform().setPosition(0.0f, 0.0f, 2.0f);
        cube.addChildObject(quad1);

        SXRTexture tempTex2 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.bluetex));
        SXRSceneObject quad2 = new SXRSceneObject(ctx, 4, 4, tempTex2);
        quad2.getTransform().setPosition(0.0f, 0.0f, -2.0f);
        cube.addChildObject(quad2);

        SXRTexture tempTex3 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.yellowtex));
        SXRSceneObject quad3 = new SXRSceneObject(ctx, 4, 4, tempTex3);
        quad3.getTransform().setPosition(2.0f, 0.0f, 0.0f);
        quad3.getTransform().setRotationByAxis(90, 0, 1.0f, 0.0f);
        cube.addChildObject(quad3);

        SXRTexture tempTex4 = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.greentex));
        SXRSceneObject quad4 = new SXRSceneObject(ctx, 4, 4, tempTex4);
        quad4.getTransform().setPosition(-2.0f, 0.0f, 0.0f);
        quad4.getTransform().setRotationByAxis(-90, 0, 1.0f, 0.0f);
        cube.addChildObject(quad4);

        cube.getTransform().setRotationByAxis(45, 1, 0, 0);
        cube.getTransform().setPosition(0, -0.8f, -8.0f);
        sxrTestUtils.waitForAssetLoad();
        mainScene.getMainCameraRig().addChildObject(cube);
        mRoot = cube;
    }

    private void testCull(int id)
    {
        //quad1
        mRoot.getChildByIndex(0).getRenderData().setCullFace(SXRRenderPass.SXRCullFaceEnum.fromInt(id));
        //quad2
        mRoot.getChildByIndex(1).getRenderData().setCullFace(SXRRenderPass.SXRCullFaceEnum.fromInt(id));
        //quad3
        mRoot.getChildByIndex(2).getRenderData().setCullFace(SXRRenderPass.SXRCullFaceEnum.fromInt(id));
        //quad4
        mRoot.getChildByIndex(3).getRenderData().setCullFace(SXRRenderPass.SXRCullFaceEnum.fromInt(id));
    }


    @Test
    public void frontFaceCullTest() throws TimeoutException {
        testCull(0);
        sxrTestUtils.waitForXFrames(10);
        sxrTestUtils.screenShot(getClass().getSimpleName(), "testFrontFaceCull", mWaiter, mDoCompare);
    }

    @Test
    public void backFaceCullTest() throws TimeoutException {
        testCull(1);
        sxrTestUtils.waitForXFrames(10);
        sxrTestUtils.screenShot(getClass().getSimpleName(), "testBackFaceCull", mWaiter, mDoCompare);

    }

    @Test
    public void noneFaceCullTest() throws TimeoutException {
        testCull(2);
        sxrTestUtils.waitForXFrames(10);
        sxrTestUtils.screenShot(getClass().getSimpleName(), "testNoneFaceCull", mWaiter, mDoCompare);
    }
}
