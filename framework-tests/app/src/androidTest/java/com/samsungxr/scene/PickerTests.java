package com.samsungxr.scene;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRBoundsPicker;
import com.samsungxr.SXRBoxCollider;
import com.samsungxr.SXRCollider;
import com.samsungxr.SXRColliderGroup;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRFrustumPicker;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRMeshCollider;
import com.samsungxr.SXRPicker;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRSphereCollider;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import com.samsungxr.utility.Log;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.EnumSet;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class PickerTests
{
    private static final int NUM_WAIT_FRAMES = 4;
    private SXRTestUtils sxrTestUtils;
    private SXRPicker mPicker;
    private SXRMaterial mBlue;
    private SXRMaterial mRed;
    private PickHandler mPickHandler;
    private Waiter mWaiter;

    public PickerTests() {
        super();
    }

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @After
    public void tearDown()
    {
        SXRScene scene = sxrTestUtils.getMainScene();
        if (scene != null) {
            scene.clear();
        }
    };

    @Before
    public void setUp() throws TimeoutException
    {
        sxrTestUtils = new SXRTestUtils(ActivityRule.getActivity());
        mWaiter = new Waiter();
        sxrTestUtils.waitForOnInit();
        SXRContext context = sxrTestUtils.getSxrContext();

        mBlue = new SXRMaterial(context, SXRMaterial.SXRShaderType.Phong.ID);
        mBlue.setDiffuseColor(0, 0, 1, 1);
        mRed = new SXRMaterial(context, SXRMaterial.SXRShaderType.Phong.ID);
        mRed.setDiffuseColor(1, 0, 0, 1);
        mPickHandler = new PickHandler(mWaiter);
    }

    @Test
    public void canPickBoxCollider()
    {
        Log.d("PICK", "start canPickBoxCollider");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode box = new SXRCubeNode(context, true, mBlue);
        SXRBoxCollider collider = new SXRBoxCollider(context);

        box.setName("box");
        box.getTransform().setPosition(0, 0, -2);
        collider.setHalfExtents(0.5f, 0.5f, 0.5f);
        box.attachComponent(collider);
        scene.addNode(box);
        sxrTestUtils.waitForSceneRendering();

        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("box", new Vector3f[] { new Vector3f(0, 0, 0.5f) }, null);
        Log.d("PICK", "end canPickBoxCollider");
    }

    @Test
    public void canPickSphereCollider()
    {
        Log.d("PICK", "start canPickSphereCollider");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider = new SXRSphereCollider(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider.setRadius(1.0f);
        sphere.attachComponent(collider);
        scene.addNode(sphere);
        sxrTestUtils.waitForSceneRendering();

        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        Log.d("PICK", "end canPickSphereCollider");
    }

    @Test
    public void canPickColliderGroup()
    {
        Log.d("PICK", "start canPickColliderGroup");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider = new SXRSphereCollider(context);
        SXRColliderGroup group = new SXRColliderGroup(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider.setRadius(1.0f);
        group.addCollider(collider);
        sphere.attachComponent(group);
        scene.addNode(sphere);
        String types = "";
        for (SXRCollider c : group)
        {
            types += c.getClass().getSimpleName();
        }
        mWaiter.assertEquals("SXRSphereCollider", types);
        sxrTestUtils.waitForSceneRendering();

        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        Log.d("PICK", "end canPickColliderGroup");
    }

    @Test
    public void canSendEventsToHitObjects()
    {
        Log.d("PICK", "start canSendEventsToHitObjects");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider = new SXRSphereCollider(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider.setRadius(1.0f);
        sphere.attachComponent(collider);
        scene.addNode(sphere);
        sxrTestUtils.waitForSceneRendering();

        sphere.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(scene, false);
        mPicker.setEventOptions(EnumSet.of(SXRPicker.EventOptions.SEND_TO_HIT_OBJECT, SXRPicker.EventOptions.SEND_PICK_EVENTS));
        mPicker.setEnable(true);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        Log.d("PICK", "end canSendEventsToHitObjects");
    }


    @Test
    public void canPickMultiple()
    {
        Log.d("PICK", "start canPickMultiple");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere.attachComponent(collider1);
        scene.addNode(sphere);

        SXRNode box = new SXRCubeNode(context, true, mRed);
        SXRBoxCollider collider2 = new SXRBoxCollider(context);

        box.setName("box");
        box.getTransform().setPosition(0, 0, -2);
        collider2.setHalfExtents(0.5f, 0.5f, 0.5f);
        box.attachComponent(collider2);
        scene.addNode(box);
        sxrTestUtils.waitForSceneRendering();

        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(scene, false);
        mPicker.setPickClosest(false);
        mPicker.setEnable(true);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        mPickHandler.checkHits("box", new Vector3f[] { new Vector3f(0, 0, 0.5f) }, null);
        Log.d("PICK", "end canPickMultiple");
   }

    @Test
    public void canPickBoundsCollider()
    {
        Log.d("PICK", "start canPickBoundsCollider");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRMeshCollider collider = new SXRMeshCollider(context, true);

        sphere.getTransform().setPosition(0, 0, -2);
        sphere.attachComponent(collider);
        sphere.setName("sphere");
        scene.addNode(sphere);
        sxrTestUtils.waitForSceneRendering();
        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        Log.d("PICK", "end canPickBoundsCollider");
    }

    @Test
    public void canPickMeshColliderSphere()
    {
        Log.d("PICK", "start canPickMeshColliderSphere");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRMeshCollider collider = new SXRMeshCollider(context, sphere.getRenderData().getMesh(), true);

        sphere.getTransform().setPosition(0, 0, -2);
        sphere.attachComponent(collider);
        sphere.setName("sphere");
        scene.addNode(sphere);
        sxrTestUtils.waitForSceneRendering();
        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkObject("sphere", sphere, 1, 0, NUM_WAIT_FRAMES - 1);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        mPickHandler.checkTexCoords("sphere", new Vector2f[] { new Vector2f(0.75f, 0.5f) }, null);
    }

    @Test
    public void canPickMeshColliderBox()
    {
        Log.d("PICK", "start canPickMeshColliderBox");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode cube = new SXRCubeNode(context, true, mBlue);
        SXRMeshCollider collider = new SXRMeshCollider(context, cube.getRenderData().getMesh(), true);

        cube.getTransform().setPosition(0, 0, -2);
        cube.attachComponent(collider);
        cube.setName("cube");
        scene.addNode(cube);
        sxrTestUtils.waitForSceneRendering();
        mPicker = new SXRPicker(context, scene);
        mPicker.getEventReceiver().addListener(mPickHandler);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkObject("cube", cube, 1, 0, NUM_WAIT_FRAMES - 1);
        mPickHandler.checkHits("cube", new Vector3f[] { new Vector3f(0, 0, 0.5f) }, null);
        mPickHandler.checkTexCoords("cube", new Vector2f[] { new Vector2f(0.5f, 0.5f) }, null);
        Log.d("PICK", "end canPickMeshColliderBox");
    }

    @Test
    public void canPickQuad()
    {
        Log.d("PICK", "start canPickQuad");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sceneObj = new SXRNode(context, 2.0f, 2.0f);
        SXRMeshCollider collider = new SXRMeshCollider(context, sceneObj.getRenderData().getMesh(), true);
        SXRRenderData rdata = sceneObj.getRenderData();

        sceneObj.attachCollider(collider);
        sceneObj.setName("quad");
        sceneObj.getTransform().setPositionZ(-5.0f);
        rdata.setMaterial(mBlue);
        scene.addNode(sceneObj);
        sxrTestUtils.waitForSceneRendering();

        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkObject("quad", sceneObj, 1, 0, NUM_WAIT_FRAMES - 1);
        mPickHandler.checkHits("quad", new Vector3f[] { new Vector3f(0, 0, 0) }, null);
        mPickHandler.checkTexCoords("quad", new Vector2f[] { new Vector2f(0.5f, 0.5f) }, null);
        Log.d("PICK", "end canPickQuad");
    }

    @Test
    public void canPickTriangle()
    {
        Log.d("PICK", "start canPickTriangle");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRMesh triangleMesh = new SXRMesh(context);
        float[] a = {0f, 0f, 0f, 5f, 5f, 5f, 1f, 4f, 1f};
        char indices[] = { 0, 1, 2 };
        triangleMesh.setVertices(a);
        triangleMesh.setIndices(indices);
        SXRNode sceneObjTriangle = new SXRNode(context, triangleMesh);
        SXRMeshCollider collider = new SXRMeshCollider(context, sceneObjTriangle.getRenderData().getMesh(), true);
        SXRRenderData rdata = sceneObjTriangle.getRenderData();

        sceneObjTriangle.attachCollider(collider);
        sceneObjTriangle.setName("Triangle");
        rdata.setMaterial(mBlue);
        scene.addNode(sceneObjTriangle);
        sceneObjTriangle.getTransform().setPosition(-2.0f, -4.0f, -15.0f);
        sceneObjTriangle.getTransform().setScale(5, 5, 5);
        sxrTestUtils.waitForSceneRendering();

        mPicker = new SXRPicker(scene, true);
        mPicker.getEventReceiver().addListener(mPickHandler);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkObject("Triangle", sceneObjTriangle, 1, 0, NUM_WAIT_FRAMES - 1);
        mPickHandler.checkHits("Triangle", new Vector3f[] { new Vector3f(0.4f, 0.8f, 0.4f) }, null);
        Log.d("PICK", "end canPickTriangle");
    }

    @Test
    public void testPickObjects()
    {
        Log.d("PICK", "start testPickObjects");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRNode box = new SXRCubeNode(context, true, mRed);
        SXRMeshCollider collider2 = new SXRMeshCollider(context, false);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere.attachComponent(collider1);
        scene.addNode(sphere);
        box.setName("box");
        box.getTransform().setPosition(0, 0.25f, -1);
        box.attachComponent(collider2);
        scene.addNode(sphere);
        scene.addNode(box);

        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);

        //test picking after the scene is rendered
        SXRPicker.SXRPickedObject picked[] = SXRPicker.pickObjects(scene, null, 0, 0, 0, 0, 0, -1.0f);
        Log.d("Picker", "testPickObjects");

        mWaiter.assertNotNull(picked);
        mWaiter.assertTrue(picked.length == 2);
        SXRPicker.SXRPickedObject hit1 = picked[0];
        SXRPicker.SXRPickedObject hit2 = picked[1];
        mWaiter.assertNotNull(hit1);
        mWaiter.assertEquals("box", hit1.hitObject.getName());
        mWaiter.assertEquals(0.0f, hit1.hitLocation[0]);
        mWaiter.assertEquals(-0.25f, hit1.hitLocation[1]);
        mWaiter.assertEquals(0.5f, hit1.hitLocation[2]);
        mWaiter.assertNotNull(hit2);
        mWaiter.assertEquals("sphere", hit2.hitObject.getName());
        mWaiter.assertEquals(0.0f, hit2.hitLocation[0]);
        mWaiter.assertEquals(0.0f, hit2.hitLocation[1]);
        mWaiter.assertEquals(1.0f, hit2.hitLocation[2]);
        Log.d("PICK", "end testPickObjects");
    }

    @Test
    public void canPickObjectWithRay()
    {
        Log.d("PICK", "start canPickObjectWithRay");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere1 = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);

        sphere1.setName("sphere1");
        sphere1.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere1.attachComponent(collider1);
        scene.addNode(sphere1);

        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        final SXRPicker.SXRPickedObject pickedObject = SXRPicker.pickNode(sphere1);
        mWaiter.assertEquals(1.0f, pickedObject.getHitDistance());
        Log.d("PICK", "end canPickObjectWithRay");
    }


    @Test
    public void canPickWithFrustum()
    {
        Log.d("PICK", "start canPickWithFrustum");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRNode box = new SXRCubeNode(context, true, mRed);
        SXRMeshCollider collider2 = new SXRMeshCollider(context, false);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere.attachComponent(collider1);
        scene.addNode(sphere);
        box.setName("box");
        box.getTransform().setPosition(-2, 0, -1);
        box.attachComponent(collider2);
        scene.addNode(sphere);
        scene.addNode(box);
        sxrTestUtils.waitForSceneRendering();

        SXRFrustumPicker picker = new SXRFrustumPicker(context, scene);
        picker.setFrustum(45.0f, 1.0f, 0.1f, 100.0f);
        picker.getEventReceiver().addListener(mPickHandler);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, -2) }, null);
        mPickHandler.checkNoHits("box");
        Log.d("PICK", "end canPickWithFrustum");
    }


    @Test
    public void canPickFromObject()
    {
        Log.d("PICK", "start canPickFromObject");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRNode box = new SXRCubeNode(context, true, mRed);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere.attachComponent(collider1);
        scene.addNode(sphere);
        box.setName("box");
        box.getTransform().setPosition(-2, 0, -2);
        scene.addNode(sphere);
        scene.addNode(box);
        sxrTestUtils.waitForSceneRendering();

        mPicker = new SXRPicker(context, scene);
        mPicker.setPickRay(0, 0, 0, 1, 0, 0);
        mPicker.getEventReceiver().addListener(mPickHandler);
        box.attachComponent(mPicker);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(-1, 0, 0) }, null);
        Log.d("PICK", "end canPickFromObject");
    }

    @Test
    public void canPickWithObject()
    {
        Log.d("PICK", "start canPickWithObject");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere1 = new SXRSphereNode(context, true, mBlue);
        SXRNode sphere2 = new SXRSphereNode(context, true, mRed);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRSphereCollider collider2 = new SXRSphereCollider(context);
        SXRNode box = new SXRCubeNode(context, true, mRed);
        Vector3f sphereCtr1 = new Vector3f(-2, 0, -2);
        Vector3f sphereCtr2 = new Vector3f(2, 0, -2);
        Vector3f boxCtr = new Vector3f(0, 0, -2);
        float radius1 = 2.0f;

        sphere1.setName("sphere1");
        sphere1.getTransform().setPosition(sphereCtr1.x, sphereCtr1.y, sphereCtr1.z);
        sphere1.getTransform().setScale(radius1, radius1, radius1);
        collider1.setRadius(radius1);
        sphere1.attachComponent(collider1);
        sphere2.setName("sphere2");
        sphere2.getTransform().setPosition(sphereCtr2.x, sphereCtr2.y, sphereCtr2.z);
        sphere2.attachComponent(collider2);
        box.setName("box");
        box.getTransform().setPosition(boxCtr.x, boxCtr.y, boxCtr.z);
        box.getTransform().setScale(2, 2, 2);
        scene.addNode(sphere1);
        scene.addNode(sphere2);
        scene.addNode(box);

        Vector3f hit = new Vector3f();
        Matrix4f inv = new Matrix4f(box.getTransform().getModelMatrix4f());

        inv.invert();
        sphereCtr2.mulPosition(inv);
        sphereCtr1.sub(boxCtr, hit);
        hit.normalize();
        hit.mul(radius1);
        sxrTestUtils.waitForSceneRendering();

        SXRBoundsPicker picker = new SXRBoundsPicker(scene, true);
        picker.getEventReceiver().addListener(mPickHandler);
        picker.addCollidable(box);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere1", new Vector3f[] { hit }, null);
        mPickHandler.checkNoHits("sphere2");
        Log.d("PICK", "end canPickWithObject");
    }

    @Test
    public void pickSphereFromLeftAndRight()
    {
        Log.d("PICK", "start pickSphereFromLeftAndRight");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere1 = new SXRSphereNode(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRNode sphere2 = new SXRSphereNode(context, true, mRed);
        SXRSphereCollider collider2 = new SXRSphereCollider(context);
        SXRNode origin = new SXRNode(context);

        scene.addNode(origin);
        mPicker = new SXRPicker(scene, false);
        mPicker.getEventReceiver().addListener(mPickHandler);
        origin.attachComponent(mPicker);

        sphere1.setName("sphere1");
        sphere1.getTransform().setPosition(-2, 0, -2);
        collider1.setRadius(1.0f);
        sphere1.attachComponent(collider1);
        scene.addNode(sphere1);
        sphere2.setName("sphere2");
        sphere2.getTransform().setPosition(2, 0, -2);
        collider2.setRadius(1.0f);
        sphere2.attachComponent(collider2);
        scene.addNode(sphere2);
        sxrTestUtils.waitForSceneRendering();

        Vector3f v = new Vector3f(-4.5f, 0.0f, -2.0f);  // no hits
        v.normalize();
        mPicker.setPickRay(0, 0, 0, v.x, v.y, v.z);
        mPicker.setEnable(true);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkNoHits("sphere2");
        mPickHandler.checkNoHits("sphere1");
        v.set(-4.0f, 0.0f, -2.0f);      // hit sphere1 on the left
        v.normalize();
        mPickHandler.clearResults();
        mPicker.setPickRay(0, 0, 0, v.x, v.y, v.z);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere1", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        mPickHandler.checkNoHits("sphere2");
        mPickHandler.clearResults();
        v.set(4.0f, 0.0f, -2.0f);      // hit sphere2 on the left
        v.normalize();
        mPicker.setPickRay(0, 0, 0, v.x, v.y, v.z);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere2", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        mPickHandler.checkNoHits("sphere1");
        mPickHandler.clearResults();
        v.set(4.5f, 0.0f, -2.0f);      // ho hits
        mPicker.setPickRay(0, 0, 0, v.x, v.y, v.z);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.checkNoHits("sphere1");
        mPickHandler.checkNoHits("sphere2");
        sxrTestUtils.waitForSceneRendering();
        Log.d("PICK", "end pickSphereFromLeftAndRight");
    }


    @Test
    public void pickQuadFromLeftAndRight()
    {
        Log.d("PICK", "start pickQuadFromLeftAndRight");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode quad1 = new SXRNode(context, 2.0f, 2.0f, null, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMeshCollider collider1 = new SXRMeshCollider(context, quad1.getRenderData().getMesh(), true);
        SXRNode quad2 = new SXRNode(context, 2.0f, 2.0f, null, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMeshCollider collider2 = new SXRMeshCollider(context, quad2.getRenderData().getMesh(), true);
        SXRNode origin = new SXRNode(context);

        scene.addNode(origin);
        mPicker = new SXRPicker(context, scene);
        mPicker.setEnable(false);
        scene.getEventReceiver().addListener(mPickHandler);
        origin.attachComponent(mPicker);

        quad1.setName("quad1");
        quad1.getRenderData().setMaterial(mBlue);
        quad1.getTransform().setPosition(-2, 0, -2);
        quad1.attachComponent(collider1);
        scene.addNode(quad1);
        quad2.setName("quad2");
        quad2.getRenderData().setMaterial(mRed);
        quad2.getTransform().setPosition(2, 0, -2);
        quad2.attachComponent(collider2);
        scene.addNode(quad2);
        sxrTestUtils.waitForSceneRendering();

        Vector3f v = new Vector3f(-3.05f, 0.0f, -2.0f);  // no hits
        v.normalize();
        mPicker.setPickRay(0, 0, 0, v.x, v.y, v.z);
        mPicker.setEnable(true);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkNoHits("quad2");
        mPickHandler.checkNoHits("quad1");
        v.set(-2.999f, 0.0f, -2.0f);      // hit quad1 on the left
        v.normalize();
        mPicker.setPickRay(0, 0, 0, v.x, v.y, v.z);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(2 * NUM_WAIT_FRAMES);
        mPickHandler.checkHits("quad1", new Vector3f[]{new Vector3f(-0.999f, 0, 0)}, null);
        mPickHandler.checkTexCoords("quad1", new Vector2f[]{new Vector2f(0.0005f, 0.5f)}, null);
        mPickHandler.checkNoHits("quad2");
        mPickHandler.clearResults();
        v.set(2.999f, 0.0f, -2.0f);      // hit quad2 on the left
        v.normalize();
        mPicker.setPickRay(0, 0, 0, v.x, v.y, v.z);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("quad2", new Vector3f[]{new Vector3f(0.999f, 0, 0)}, null);
        mPickHandler.checkTexCoords("quad2", new Vector2f[]{new Vector2f(0.9995f, 0.5f)}, null);
        mPickHandler.checkNoHits("quad1");
        mPickHandler.clearResults();
        v.set(3.05f, 0.0f, -2.0f);      // ho hits
        mPicker.setPickRay(0, 0, 0, v.x, v.y, v.z);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.checkNoHits("quad1");
        mPickHandler.checkNoHits("quad2");
        sxrTestUtils.waitForSceneRendering();
        Log.d("PICK", "end pickQuadFromLeftAndRight");
    }

    @Test
    public void canPickMeshWithObject()
    {
        Log.d("PICK", "start canPickMeshWithObject");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        try
        {
            SXRMesh mesh = context.getAssetLoader().loadMesh(new SXRAndroidResource(context,
                    "PickerTests/bunny.obj"));
            SXRNode bunny = new SXRNode(context, mesh);
            bunny.getRenderData().setMaterial(mBlue);
            bunny.attachComponent(new SXRMeshCollider(context, false));

            bunny.getTransform().setPositionZ(-10.0f);

            //add the bunny to the scene
            scene.addNode(bunny);


            SXRMesh sphereMesh = context.getAssetLoader().loadMesh(new SXRAndroidResource(context,
                    "PickerTests/sphere.obj"));
            SXRNode sceneObject = new SXRNode(context, sphereMesh);
            sceneObject.getRenderData().setMaterial(mRed);
            SXRNode parent = new SXRNode(context);
            parent.getTransform().setPosition(0.2f, -0.4f, -0.4f);
            parent.getTransform().setRotation(1.0f, 0.04f, 0.01f, 0.01f);

            sceneObject.getTransform().setPositionZ(-10.0f);

            parent.addChildObject(sceneObject);
            sxrTestUtils.waitForSceneRendering();

            SXRBoundsPicker picker = new SXRBoundsPicker(scene, true);
            picker.addCollidable(sceneObject);
            picker.getEventReceiver().addListener(mPickHandler);

            //place the object behind the bunny
            scene.addNode(parent);
            sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
            mPickHandler.countPicks(NUM_WAIT_FRAMES);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d("PICK", "end canPickMeshWithObject");
    }

    @Test
    public void canPickAfterCameraMove()
    {
        Log.d("PICK", "start canPickAfterCameraMove");
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRNode sphere = new SXRSphereNode(context, true, mBlue);
        SXRNode cube = new SXRCubeNode(context,true, mRed);
        SXRSphereCollider collider = new SXRSphereCollider(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider.setRadius(1.0f);
        sphere.attachComponent(collider);
        scene.addNode(sphere);
        cube.setName("cube");
        cube.getTransform().setPosition(-1, 0, -2);
        cube.attachComponent(new SXRMeshCollider(context, cube.getRenderData().getMesh(), true));
        scene.addNode(cube);
        sxrTestUtils.waitForSceneRendering();

        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        mPickHandler.clearResults();
        mPicker.setEnable(false);
        scene.getMainCameraRig().getOwnerObject().getTransform().setPosition(-1, 0, 0);
        sxrTestUtils.waitForSceneRendering();
        mPicker.setEnable(true);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("cube", new Vector3f[] { new Vector3f(0, 0, 0.5f) }, null);
        mPickHandler.checkTexCoords("cube", new Vector2f[] { new Vector2f(0.5f, 0.5f) }, null);
        Log.d("PICK", "end canPickAfterCameraMove");
    }
}
