package com.samsungxr.tester;

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
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRSphereCollider;
import com.samsungxr.scene_objects.SXRCubeSceneObject;
import com.samsungxr.scene_objects.SXRSphereSceneObject;
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
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject box = new SXRCubeSceneObject(context, true, mBlue);
        SXRBoxCollider collider = new SXRBoxCollider(context);

        box.setName("box");
        box.getTransform().setPosition(0, 0, -2);
        collider.setHalfExtents(0.5f, 0.5f, 0.5f);
        box.attachComponent(collider);
        scene.addSceneObject(box);
        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("box", new Vector3f[] { new Vector3f(0, 0, 0.5f) }, null);
    }

    @Test
    public void canPickSphereCollider()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider = new SXRSphereCollider(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider.setRadius(1.0f);
        sphere.attachComponent(collider);
        scene.addSceneObject(sphere);
        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
    }

    @Test
    public void canPickColliderGroup()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider = new SXRSphereCollider(context);
        SXRColliderGroup group = new SXRColliderGroup(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider.setRadius(1.0f);
        group.addCollider(collider);
        sphere.attachComponent(group);
        scene.addSceneObject(sphere);
        String types = "";
        for (SXRCollider c : group)
        {
            types += c.getClass().getSimpleName();
        }
        mWaiter.assertEquals("SXRSphereCollider", types);
        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
    }

    @Test
    public void canSendEventsToHitObjects()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider = new SXRSphereCollider(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider.setRadius(1.0f);
        sphere.attachComponent(collider);
        scene.addSceneObject(sphere);
        sxrTestUtils.waitForXFrames(1);

        sphere.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(scene, false);
        mPicker.setEventOptions(EnumSet.of(SXRPicker.EventOptions.SEND_TO_HIT_OBJECT, SXRPicker.EventOptions.SEND_PICK_EVENTS));
        mPicker.setEnable(true);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
    }


    @Test
    public void canPickMultiple()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere.attachComponent(collider1);
        scene.addSceneObject(sphere);

        SXRSceneObject box = new SXRCubeSceneObject(context, true, mRed);
        SXRBoxCollider collider2 = new SXRBoxCollider(context);

        box.setName("box");
        box.getTransform().setPosition(0, 0, -2);
        collider2.setHalfExtents(0.5f, 0.5f, 0.5f);
        box.attachComponent(collider2);
        scene.addSceneObject(box);


        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(scene, false);
        mPicker.setPickClosest(false);
        mPicker.setEnable(true);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        mPickHandler.checkHits("box", new Vector3f[] { new Vector3f(0, 0, 0.5f) }, null);
    }

    @Test
    public void canPickBoundsCollider()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRMeshCollider collider = new SXRMeshCollider(context, true);

        sphere.getTransform().setPosition(0, 0, -2);
        sphere.attachComponent(collider);
        sphere.setName("sphere");
        scene.addSceneObject(sphere);
        sxrTestUtils.waitForXFrames(1);
        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
    }

    @Test
    public void canPickMeshColliderSphere()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRMeshCollider collider = new SXRMeshCollider(context, sphere.getRenderData().getMesh(), true);

        sphere.getTransform().setPosition(0, 0, -2);
        sphere.attachComponent(collider);
        sphere.setName("sphere");
        scene.addSceneObject(sphere);
        sxrTestUtils.waitForXFrames(1);
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
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject cube = new SXRCubeSceneObject(context, true, mBlue);
        SXRMeshCollider collider = new SXRMeshCollider(context, cube.getRenderData().getMesh(), true);

        cube.getTransform().setPosition(0, 0, -2);
        cube.attachComponent(collider);
        cube.setName("cube");
        scene.addSceneObject(cube);
        sxrTestUtils.waitForXFrames(1);
        mPicker = new SXRPicker(context, scene);
        mPicker.getEventReceiver().addListener(mPickHandler);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkObject("cube", cube, 1, 0, NUM_WAIT_FRAMES - 1);
        mPickHandler.checkHits("cube", new Vector3f[] { new Vector3f(0, 0, 0.5f) }, null);
        mPickHandler.checkTexCoords("cube", new Vector2f[] { new Vector2f(0.5f, 0.5f) }, null);
    }

    @Test
    public void canPickQuad()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sceneObj = new SXRSceneObject(context, 2.0f, 2.0f);
        SXRMeshCollider collider = new SXRMeshCollider(context, sceneObj.getRenderData().getMesh(), true);
        SXRRenderData rdata = sceneObj.getRenderData();

        sceneObj.attachCollider(collider);
        sceneObj.setName("quad");
        sceneObj.getTransform().setPositionZ(-5.0f);
        rdata.setMaterial(mBlue);
        scene.addSceneObject(sceneObj);
        sxrTestUtils.waitForXFrames(1);
        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkObject("quad", sceneObj, 1, 0, NUM_WAIT_FRAMES - 1);
        mPickHandler.checkHits("quad", new Vector3f[] { new Vector3f(0, 0, 0) }, null);
        mPickHandler.checkTexCoords("quad", new Vector2f[] { new Vector2f(0.5f, 0.5f) }, null);
    }

    @Test
    public void canPickTriangle()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRMesh triangleMesh = new SXRMesh(context);
        float[] a = {0f, 0f, 0f, 5f, 5f, 5f, 1f, 4f, 1f};
        char indices[] = { 0, 1, 2 };
        triangleMesh.setVertices(a);
        triangleMesh.setIndices(indices);
        SXRSceneObject sceneObjTriangle = new SXRSceneObject(context, triangleMesh);
        SXRMeshCollider collider = new SXRMeshCollider(context, sceneObjTriangle.getRenderData().getMesh(), true);
        SXRRenderData rdata = sceneObjTriangle.getRenderData();

        sceneObjTriangle.attachCollider(collider);
        sceneObjTriangle.setName("Triangle");
        rdata.setMaterial(mBlue);
        scene.addSceneObject(sceneObjTriangle);
        sceneObjTriangle.getTransform().setPosition(-2.0f, -4.0f, -15.0f);
        sceneObjTriangle.getTransform().setScale(5, 5, 5);
        sxrTestUtils.waitForXFrames(1);

        mPicker = new SXRPicker(scene, true);
        mPicker.getEventReceiver().addListener(mPickHandler);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkObject("Triangle", sceneObjTriangle, 1, 0, NUM_WAIT_FRAMES - 1);
        mPickHandler.checkHits("Triangle", new Vector3f[] { new Vector3f(0.4f, 0.8f, 0.4f) }, null);
    }

    @Test
    public void testPickObjects()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRSceneObject box = new SXRCubeSceneObject(context, true, mRed);
        SXRMeshCollider collider2 = new SXRMeshCollider(context, false);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere.attachComponent(collider1);
        scene.addSceneObject(sphere);
        box.setName("box");
        box.getTransform().setPosition(0, 0.25f, -1);
        box.attachComponent(collider2);
        scene.addSceneObject(sphere);
        scene.addSceneObject(box);

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
    }

    @Test
    public void canPickObjectWithRay()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere1 = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);

        sphere1.setName("sphere1");
        sphere1.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere1.attachComponent(collider1);
        scene.addSceneObject(sphere1);

        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        final SXRPicker.SXRPickedObject pickedObject = SXRPicker.pickSceneObject(sphere1);
        mWaiter.assertEquals(1.0f, pickedObject.getHitDistance());
    }


    @Test
    public void canPickWithFrustum()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRSceneObject box = new SXRCubeSceneObject(context, true, mRed);
        SXRMeshCollider collider2 = new SXRMeshCollider(context, false);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere.attachComponent(collider1);
        scene.addSceneObject(sphere);
        box.setName("box");
        box.getTransform().setPosition(-2, 0, -1);
        box.attachComponent(collider2);
        scene.addSceneObject(sphere);
        scene.addSceneObject(box);
        sxrTestUtils.waitForXFrames(1);

        SXRFrustumPicker picker = new SXRFrustumPicker(context, scene);
        picker.setFrustum(45.0f, 1.0f, 0.1f, 100.0f);
        picker.getEventReceiver().addListener(mPickHandler);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, -2) }, null);
        mPickHandler.checkNoHits("box");
    }


    @Test
    public void canPickFromObject()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRSceneObject box = new SXRCubeSceneObject(context, true, mRed);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider1.setRadius(1.0f);
        sphere.attachComponent(collider1);
        scene.addSceneObject(sphere);
        box.setName("box");
        box.getTransform().setPosition(-2, 0, -2);
        scene.addSceneObject(sphere);
        scene.addSceneObject(box);
        sxrTestUtils.waitForXFrames(1);

        mPicker = new SXRPicker(context, scene);
        mPicker.setPickRay(0, 0, 0, 1, 0, 0);
        mPicker.getEventReceiver().addListener(mPickHandler);
        box.attachComponent(mPicker);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(-1, 0, 0) }, null);
    }

    @Test
    public void canPickWithObject()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere1 = new SXRSphereSceneObject(context, true, mBlue);
        SXRSceneObject sphere2 = new SXRSphereSceneObject(context, true, mRed);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRSphereCollider collider2 = new SXRSphereCollider(context);
        SXRSceneObject box = new SXRCubeSceneObject(context, true, mRed);
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
        scene.addSceneObject(sphere1);
        scene.addSceneObject(sphere2);
        scene.addSceneObject(box);

        Vector3f hit = new Vector3f();
        Matrix4f inv = new Matrix4f(box.getTransform().getModelMatrix4f());

        inv.invert();
        sphereCtr2.mulPosition(inv);
        sphereCtr1.sub(boxCtr, hit);
        hit.normalize();
        hit.mul(radius1);

        sxrTestUtils.waitForXFrames(1);

        SXRBoundsPicker picker = new SXRBoundsPicker(scene, true);
        picker.getEventReceiver().addListener(mPickHandler);
        picker.addCollidable(box);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere1", new Vector3f[] { hit }, null);
        mPickHandler.checkNoHits("sphere2");
    }

    @Test

    public void pickSphereFromLeftAndRight()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere1 = new SXRSphereSceneObject(context, true, mBlue);
        SXRSphereCollider collider1 = new SXRSphereCollider(context);
        SXRSceneObject sphere2 = new SXRSphereSceneObject(context, true, mRed);
        SXRSphereCollider collider2 = new SXRSphereCollider(context);
        SXRSceneObject origin = new SXRSceneObject(context);

        scene.addSceneObject(origin);
        mPicker = new SXRPicker(scene, false);
        mPicker.getEventReceiver().addListener(mPickHandler);
        origin.attachComponent(mPicker);

        sphere1.setName("sphere1");
        sphere1.getTransform().setPosition(-2, 0, -2);
        collider1.setRadius(1.0f);
        sphere1.attachComponent(collider1);
        scene.addSceneObject(sphere1);
        sphere2.setName("sphere2");
        sphere2.getTransform().setPosition(2, 0, -2);
        collider2.setRadius(1.0f);
        sphere2.attachComponent(collider2);
        scene.addSceneObject(sphere2);
        sxrTestUtils.waitForXFrames(1);

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
    }


    @Test
    public void pickQuadFromLeftAndRight()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject quad1 = new SXRSceneObject(context, 2.0f, 2.0f, null, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMeshCollider collider1 = new SXRMeshCollider(context, quad1.getRenderData().getMesh(), true);
        SXRSceneObject quad2 = new SXRSceneObject(context, 2.0f, 2.0f, null, SXRMaterial.SXRShaderType.Phong.ID);
        SXRMeshCollider collider2 = new SXRMeshCollider(context, quad2.getRenderData().getMesh(), true);
        SXRSceneObject origin = new SXRSceneObject(context);

        scene.addSceneObject(origin);
        mPicker = new SXRPicker(context, scene);
        mPicker.setEnable(false);
        scene.getEventReceiver().addListener(mPickHandler);
        origin.attachComponent(mPicker);

        quad1.setName("quad1");
        quad1.getRenderData().setMaterial(mBlue);
        quad1.getTransform().setPosition(-2, 0, -2);
        quad1.attachComponent(collider1);
        scene.addSceneObject(quad1);
        quad2.setName("quad2");
        quad2.getRenderData().setMaterial(mRed);
        quad2.getTransform().setPosition(2, 0, -2);
        quad2.attachComponent(collider2);
        scene.addSceneObject(quad2);

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
    }

    @Test
    public void canPickMeshWithObject()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        try
        {
            SXRMesh mesh = context.getAssetLoader().loadMesh(new SXRAndroidResource(context,
                    "PickerTests/bunny.obj"));
            SXRSceneObject bunny = new SXRSceneObject(context, mesh);
            bunny.getRenderData().setMaterial(mBlue);
            bunny.attachComponent(new SXRMeshCollider(context, false));

            bunny.getTransform().setPositionZ(-10.0f);

            //add the bunny to the scene
            scene.addSceneObject(bunny);


            SXRMesh sphereMesh = context.getAssetLoader().loadMesh(new SXRAndroidResource(context,
                    "PickerTests/sphere.obj"));
            SXRSceneObject sceneObject = new SXRSceneObject(context, sphereMesh);
            sceneObject.getRenderData().setMaterial(mRed);
            SXRSceneObject parent = new SXRSceneObject(context);
            parent.getTransform().setPosition(0.2f, -0.4f, -0.4f);
            parent.getTransform().setRotation(1.0f, 0.04f, 0.01f, 0.01f);

            sceneObject.getTransform().setPositionZ(-10.0f);

            parent.addChildObject(sceneObject);
            sxrTestUtils.waitForXFrames(1);

            SXRBoundsPicker picker = new SXRBoundsPicker(scene, true);
            picker.addCollidable(sceneObject);
            picker.getEventReceiver().addListener(mPickHandler);

            //place the object behind the bunny
            scene.addSceneObject(parent);
            sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
            mPickHandler.countPicks(NUM_WAIT_FRAMES);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test


    public void canPickAfterCameraMove()
    {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRSceneObject sphere = new SXRSphereSceneObject(context, true, mBlue);
        SXRSceneObject cube = new SXRCubeSceneObject(context,true, mRed);
        SXRSphereCollider collider = new SXRSphereCollider(context);

        sphere.setName("sphere");
        sphere.getTransform().setPosition(0, 0, -2);
        collider.setRadius(1.0f);
        sphere.attachComponent(collider);
        scene.addSceneObject(sphere);
        cube.setName("cube");
        cube.getTransform().setPosition(-1, 0, -2);
        cube.attachComponent(new SXRMeshCollider(context, cube.getRenderData().getMesh(), true));
        scene.addSceneObject(cube);

        scene.getEventReceiver().addListener(mPickHandler);
        mPicker = new SXRPicker(context, scene);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("sphere", new Vector3f[] { new Vector3f(0, 0, 1) }, null);
        mPickHandler.clearResults();
        scene.getMainCameraRig().getOwnerObject().getTransform().setPosition(-1, 0, 0);
        sxrTestUtils.waitForXFrames(NUM_WAIT_FRAMES);
        mPickHandler.countPicks(NUM_WAIT_FRAMES);
        mPickHandler.checkHits("cube", new Vector3f[] { new Vector3f(0, 0, 0.5f) }, null);
        mPickHandler.checkTexCoords("cube", new Vector2f[] { new Vector2f(0.5f, 0.5f) }, null);
    }
}
