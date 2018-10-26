package com.samsungxr.tester;

import android.graphics.Color;
import android.opengl.GLES30;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class StencilTests
{
    private static final String TAG = StencilTests.class.getSimpleName();
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
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
    }

    @Test
    public void testBasic() throws TimeoutException, IOException {
        SXRScene scene = mTestUtils.getMainScene();

        SXRCameraRig mainCameraRig = scene.getMainCameraRig();
        mainCameraRig.getLeftCamera().setBackgroundColor(Color.WHITE);
        mainCameraRig.getRightCamera().setBackgroundColor(Color.WHITE);

        SXRNode testObject1 = makeTestObject();
        testObject1.getTransform().setPosition(1, 0, -3);
        scene.addNode(testObject1);

        SXRNode testObject2 = makeTestObject();
        testObject2.getTransform().setPosition(-1, 0, -3);
        scene.addNode(testObject2);

        SXRNode testObject3 = makeTestObject();
        testObject3.getTransform().setPosition(0, 1, -3);
        scene.addNode(testObject3);

        SXRNode testObject4 = makeTestObject();
        testObject4.getTransform().setPosition(0, -1, -3);
        scene.addNode(testObject4);

        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "testBasicReference", mWaiter, mDoCompare);
    }

    SXRNode makeTestObject() throws IOException {
        final SXRContext sxrContext = mTestUtils.getSxrContext();
        SXRNode parent = new SXRNode(sxrContext);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 2);

        sxrContext.getEventReceiver().addListener(texHandler);

        SXRTexture texture = sxrContext.getAssetLoader().loadTexture(new SXRAndroidResource(sxrContext, R.drawable.white_texture));
        SXRNode sphere = new SXRSphereNode(sxrContext, true);
        sphere.getRenderData().getMaterial().setMainTexture(texture);

        sphere.getRenderData()
                .setRenderingOrder(SXRRenderData.SXRRenderingOrder.STENCIL)
                .setStencilTest(true)
                .setStencilFunc(GLES30.GL_ALWAYS, 1, 0xFF)
                .setStencilOp(GLES30.GL_KEEP, GLES30.GL_KEEP, GLES30.GL_REPLACE)
                .setStencilMask(0xFF);
        sphere.getTransform().setScale(0.5f, 0.5f, 0.5f);

        parent.addChildObject(sphere);

        SXRTexture quad = sxrContext.getAssetLoader().loadTexture(new SXRAndroidResource(sxrContext, "StencilTests/GearVR.jpg"));
        SXRNode background = new SXRNode(sxrContext, 1.2f, 0.7f, quad);
        background.getTransform().setScale(2,2,2);

        background.getRenderData()
                .setStencilTest(true)
                .setStencilFunc(GLES30.GL_EQUAL, 1, 0xFF)
                .setStencilMask(0x00);
        parent.addChildObject(background);
        mTestUtils.waitForAssetLoad();
        return parent;
    }

}
