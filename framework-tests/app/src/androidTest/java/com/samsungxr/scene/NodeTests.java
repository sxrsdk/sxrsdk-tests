package com.samsungxr.scene;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRBehavior;
import com.samsungxr.SXRBillboard;
import com.samsungxr.SXRComponentGroup;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTransform;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRCylinderNode;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.nodes.SXRTextViewNode;
import com.samsungxr.sdktests.R;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class NodeTests
{
    private static final String TAG = NodeTests.class.getSimpleName();
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRNode mRoot;
    private SXRMaterial mBlueMtl;
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
    public void setUp() throws Exception
    {
        SXRTestableActivity activity = ActivityRule.getActivity();
        mTestUtils = new SXRTestUtils(activity);
        mTestUtils.waitForOnInit();
        mWaiter = new Waiter();
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        mWaiter.assertNotNull(scene);

        mBlueMtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        mBlueMtl.setDiffuseColor(0, 0, 1, 1);
        mRoot = scene.getRoot();

        mWaiter.assertNotNull(mRoot);
    }

    @Test
    public void canClearEmptyScene() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();

        SXRTextViewNode text = new SXRTextViewNode(ctx, "Hello");
        scene.removeAllNodes();
    }

    @Test
    public void canClearSceneWithStuff() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode sphere = new SXRNode(ctx);
        SXRTextViewNode text = new SXRTextViewNode(ctx, "Hello");

        text.getTransform().setPosition(-1, 0, -2);
        sphere.getTransform().setPosition(1, 0, -2);

        final SXRNode background = new SXRCubeNode(ctx, false);
        background.getTransform().setScale(10, 10, 10);
        background.setName("background");
        scene.addNode(background);

        mRoot.addChildObject(sphere);
        mRoot.addChildObject(text);
        text.setName("text");
        sphere.setName("sphere");
        mTestUtils.waitForSceneRendering();
        scene.clear();
        mWaiter.assertNull(scene.getNodeByName("background"));
        mWaiter.assertNull(scene.getNodeByName("sphere"));
        mWaiter.assertNull(scene.getNodeByName("text"));
    }

    @Test
    public void canDisplaySpheres() throws Exception
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode sphere1 = new SXRSphereNode(ctx, true, mBlueMtl);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);
        ctx.getEventReceiver().addListener(texHandler);

        final SXRTexture tex = ctx.getAssetLoader().loadCubemapTexture(new SXRAndroidResource(ctx, R.raw.beach));
        final SXRMaterial cubeMapMtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Cubemap.ID);
        cubeMapMtl.setMainTexture(tex);

        SXRNode sphere2 = new SXRSphereNode(ctx, false, cubeMapMtl);

        sphere1.getTransform().setPosition(0, 0, -4);
        sphere2.getTransform().setScale(20, 20, 20);
        sphere1.setName("sphere1");
        sphere2.setName("sphere2");
        mTestUtils.waitForAssetLoad();
        mRoot.addChildObject(sphere1);
        scene.addNode(sphere2);
        mTestUtils.waitForXFrames(2);
        mWaiter.assertNotNull(scene.getNodeByName("sphere2"));
        mWaiter.assertNotNull(scene.getNodeByName("sphere1"));
        mTestUtils.screenShot(getClass().getSimpleName(), "canDisplaySpheres", mWaiter, mDoCompare);
    }

    @Test
    public void canDisplayCubes() throws Exception
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode cube1 = new SXRCubeNode(ctx, true, mBlueMtl);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);
        ctx.getEventReceiver().addListener(texHandler);

        final SXRTexture tex = ctx.getAssetLoader().loadCubemapTexture(new SXRAndroidResource(ctx, R.raw.beach));
        final SXRMaterial cubeMapMtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Cubemap.ID);
        cubeMapMtl.setMainTexture(tex);
        SXRNode cube2 = new SXRCubeNode(ctx, false, cubeMapMtl);

        cube1.getTransform().setPosition(0, 0, -4);
        cube1.setName("cube1");
        cube2.getTransform().setScale(20, 20, 20);
        cube2.setName("cube2");
        mTestUtils.waitForAssetLoad();
        mRoot.addChildObject(cube1);
        scene.addNode(cube2);
        mTestUtils.waitForXFrames(2);
        mWaiter.assertNotNull(scene.getNodeByName("cube2"));
        mWaiter.assertNotNull(scene.getNodeByName("cube1"));
        mTestUtils.screenShot(getClass().getSimpleName(), "canDisplayCubes", mWaiter, mDoCompare);
    }


    @Test
    public void canDisplayCylinders() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);
        ctx.getEventReceiver().addListener(texHandler);
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.color_sphere));
        SXRNode cylinder1 = new SXRCylinderNode(ctx, true, mBlueMtl);
        SXRMaterial mtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Texture.ID);
        SXRNode cylinder2 = new SXRCylinderNode(ctx, false, mtl);

        mtl.setTexture("u_texture", tex);
        cylinder1.getTransform().setPosition(0, 0, -4);
        cylinder1.setName("cylinder1");
        cylinder2.getTransform().setScale(10, 10, 10);
        cylinder2.setName("cylinder2");
        mTestUtils.waitForAssetLoad();
        mRoot.addChildObject(cylinder1);
        scene.addNode(cylinder2);
        mTestUtils.waitForXFrames(2);
        mWaiter.assertNotNull(scene.getNodeByName("cylinder1"));
        mWaiter.assertNotNull(scene.getNodeByName("cylinder2"));
        mTestUtils.screenShot(getClass().getSimpleName(), "canDisplayCylinders", mWaiter, mDoCompare);
    }


    @Test
    public void canDisplayNonTextured() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRMaterial redMtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMaterial greenMtl = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRNode cylinder1 = new SXRCylinderNode(ctx, true, redMtl);
        SXRNode cylinder2 = new SXRCylinderNode(ctx, false, greenMtl);

        redMtl.setDiffuseColor(1, 0, 0, 1);
        greenMtl.setDiffuseColor(0, 1, 0, 1);
        cylinder1.getTransform().setPosition(0, 0, -4);
        cylinder1.setName("cylinder1");
        cylinder2.getTransform().setScale(10, 10, 10);
        cylinder2.setName("cylinder2");
        mRoot.addChildObject(cylinder1);
        scene.addNode(cylinder2);
        mTestUtils.waitForXFrames(20);
        mWaiter.assertNotNull(scene.getNodeByName("cylinder1"));
        mWaiter.assertNotNull(scene.getNodeByName("cylinder2"));
        mTestUtils.screenShot(getClass().getSimpleName(), "canDisplayNonTextured", mWaiter, mDoCompare);
    }

    @Test
    public void attachBillboard() throws TimeoutException {

        SXRContext ctx = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex));

        SXRNode quadObj1 = new SXRNode(ctx, 1.0f, 1.0f, tex);
        quadObj1.getTransform().setPosition(0.8f, 0, -2);
        quadObj1.getTransform().setRotationByAxis(30, 1,1,1);
        quadObj1.attachComponent(new SXRBillboard(ctx));
        scene.getMainCameraRig().addChildObject(quadObj1);

        SXRNode quadObj2 = new SXRNode(ctx, 1.0f, 1.0f, tex);
        quadObj2.getTransform().setPosition(-1.0f, 0.8f, -2);
        quadObj2.getTransform().setRotationByAxis(-45, 0,1,0);
        quadObj2.attachComponent(new SXRBillboard(ctx, new Vector3f(0, 1, 0)));
        scene.getMainCameraRig().addChildObject(quadObj2);

        SXRNode quadObj3 = new SXRNode(ctx, 1.1f, 1.1f, tex);
        quadObj3.getTransform().setPosition(-0.5f, -0.8f, -1.4f);
        quadObj3.getTransform().setRotationByAxis(-45, 1,0,0);
        quadObj3.attachComponent(new SXRBillboard(ctx, new Vector3f(0, 1, -1)));
        scene.getMainCameraRig().addChildObject(quadObj3);
        mTestUtils.waitForAssetLoad();
        mTestUtils.waitForXFrames(10);
        mTestUtils.screenShot(getClass().getSimpleName(), "testBillboards", mWaiter, mDoCompare);
    }


    @Test
    public void attachBillboardCameraOffset() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex));

        scene.getMainCameraRig().getTransform().setPosition(0.5f, 1.0f, -0.4f);

        SXRNode quadObj1 = new SXRNode(ctx, 0.8f, 0.8f, tex);
        quadObj1.getTransform().setPosition(0.8f, 1.0f, -3);
        quadObj1.attachComponent(new SXRBillboard(ctx));

        SXRNode quadObj2 = new SXRNode(ctx, 0.8f, 0.8f, tex);
        quadObj2.getTransform().setPosition(-0.8f, -1.0f, -3);
        quadObj2.attachComponent(new SXRBillboard(ctx));

        SXRNode quadObj3 = new SXRNode(ctx, 0.8f, 0.8f, tex);
        quadObj3.getTransform().setPosition(0.8f, -1.0f, -3);
        quadObj3.attachComponent(new SXRBillboard(ctx));

        SXRNode quadObj4 = new SXRNode(ctx, 0.8f, 0.8f, tex);
        quadObj4.getTransform().setPosition(-0.8f, 1.0f, -3);
        quadObj4.attachComponent(new SXRBillboard(ctx));

        SXRNode quadObj5 = new SXRNode(ctx, 0.8f, 0.8f, tex);
        quadObj5.getTransform().setPosition(-1.5f, 0.0f, -3);
        quadObj5.attachComponent(new SXRBillboard(ctx));

        SXRNode quadObj6 = new SXRNode(ctx, 0.8f, 0.8f, tex);
        quadObj6.getTransform().setPosition(1.5f, 0.0f, -3);
        quadObj6.attachComponent(new SXRBillboard(ctx));
        mTestUtils.waitForAssetLoad();

        mRoot.addChildObject(quadObj1);
        mRoot.addChildObject(quadObj2);
        mRoot.addChildObject(quadObj3);
        mRoot.addChildObject(quadObj4);
        mRoot.addChildObject(quadObj5);
        mRoot.addChildObject(quadObj6);

        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "testBillboardsCamOffset", mWaiter, mDoCompare);
    }

    @Test
    public void testBillboardOwnersScale() throws TimeoutException {

        SXRContext ctx = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);
        final float epsilon = 0.00001f;

        ctx.getEventReceiver().addListener(texHandler);
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.drawable.colortex));
        SXRNode quadObj1 = new SXRNode(ctx, 1.0f, 1.0f, tex);
        SXRTransform quadTrans1 = quadObj1.getTransform();

        quadObj1.setName("quadObj1");
        quadTrans1.setPosition(0.8f, 0, -2);
        quadTrans1.setRotationByAxis(30, 1,1,1);
        quadTrans1.setScale(5,6,7);
        quadObj1.attachComponent(new SXRBillboard(ctx));

        SXRNode quadObj2 = new SXRNode(ctx, 1.0f, 1.0f, tex);
        SXRTransform quadTrans2 = quadObj2.getTransform();

        quadObj2.setName("quadObj2");
        quadTrans2.setPosition(-1.0f, 0.8f, -2);
        quadTrans2.setRotationByAxis(-45, 0,1,0);
        quadTrans2.setScale(0.5f,0.6f,0.7f);
        quadObj2.attachComponent(new SXRBillboard(ctx, new Vector3f(0, 1, 0)));

        SXRNode quadObj3 = new SXRNode(ctx, 1.1f, 1.1f, tex);
        SXRTransform quadTrans3 = quadObj3.getTransform();

        quadObj3.setName("quadObj3");
        quadTrans3.setPosition(-0.5f, -0.8f, -1.4f);
        quadTrans3.setRotationByAxis(-45, 1,0,0);
        quadTrans3.setScale(1,1,1);
        quadObj3.attachComponent(new SXRBillboard(ctx, new Vector3f(0, 1, -1)));

        scene.addNode(quadObj1);
        scene.addNode(quadObj2);
        scene.addNode(quadObj3);
        mTestUtils.waitForAssetLoad();

        mTestUtils.waitForXFrames(2);

        float xs = quadTrans1.getScaleX();
        float ys = quadTrans1.getScaleY();
        float zs = quadTrans1.getScaleZ();

        mWaiter.assertTrue(Math.abs(5 - xs) < epsilon);
        mWaiter.assertTrue(Math.abs(6 - ys) < epsilon);
        mWaiter.assertTrue(Math.abs(7 - zs) < epsilon);

        xs = quadTrans2.getScaleX();
        ys = quadTrans2.getScaleY();
        zs = quadTrans2.getScaleZ();
        mWaiter.assertTrue(Math.abs(0.5f - xs) < epsilon);
        mWaiter.assertTrue(Math.abs(0.6f - ys) < epsilon);
        mWaiter.assertTrue(Math.abs(0.7f - zs) < epsilon);

        xs = quadTrans3.getScaleX();
        ys = quadTrans3.getScaleY();
        zs = quadTrans3.getScaleZ();
        mWaiter.assertTrue(Math.abs(1 - xs) < epsilon);
        mWaiter.assertTrue(Math.abs(1 - ys) < epsilon);
        mWaiter.assertTrue(Math.abs(1 - zs) < epsilon);
   }

    private final static int MAX_COMPONENTS_IN_GROUP = 5;
    private final class TestBehavior extends SXRBehavior {
        public TestBehavior(SXRContext sxrContext) {
            super(sxrContext);
        }
        @Override
        public void onDrawFrame(float frameTime) {
            mTestComponentGroupLatch.countDown();
            stopListening();
        }
    }
    private final CountDownLatch mTestComponentGroupLatch = new CountDownLatch(MAX_COMPONENTS_IN_GROUP);

    @Test
    public void testComponentGroup() throws InterruptedException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        final SXRScene scene = mTestUtils.getMainScene();

        SXRComponentGroup<SXRBehavior> group = new SXRComponentGroup<>(ctx, SXRBehavior.getComponentType());
        final SXRNode so = new SXRNode(ctx);
        for (int i = 0; i < MAX_COMPONENTS_IN_GROUP; ++i) {
            group.addChildComponent(new TestBehavior(ctx));
        }
        so.attachComponent(group);
        scene.addNode(so);
        mWaiter.assertTrue(mTestComponentGroupLatch.await(10, TimeUnit.SECONDS));

        group = (SXRComponentGroup<SXRBehavior>)so.getComponent(SXRBehavior.getComponentType());
        mWaiter.assertTrue(MAX_COMPONENTS_IN_GROUP == group.getSize());
    }
}
