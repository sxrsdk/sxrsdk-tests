package com.samsungxr.scene;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;

import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTransform;
import com.samsungxr.nodes.SXRConeNode;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import com.samsungxr.sdktests.R;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class BoundingVolumeTests {
    private static final float SQRT_2 = (float) Math.sqrt(2.0f);
    private SXRTestUtils sxrTestUtils;
    private Waiter mWaiter;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new
            ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @Before
    public void setUp() throws TimeoutException {
        sxrTestUtils = new SXRTestUtils(ActivityRule.getActivity());
        mWaiter = new Waiter();
        sxrTestUtils.waitForOnInit();
    }

    @Test
    public void testSimpleQuadBV() {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRTexture texture = context.getAssetLoader().loadTexture(new SXRAndroidResource(
                context, R.drawable.gearvr_logo));
        SXRNode sceneObject = new SXRNode(context, 5.0f, 5.0f, texture);
        sceneObject.getTransform().setPosition(0.0f, 0.0f, -5.0f);
        scene.addNode(sceneObject);
        sxrTestUtils.waitForSceneRendering();
        SXRNode.BoundingVolume boundingVolume = sceneObject.getBoundingVolume();
        Vector3f bvMin = boundingVolume.minCorner;
        Vector3f bvMax = boundingVolume.maxCorner;

        mWaiter.assertTrue(checkResult(new Vector3f(-2.5f, -2.5f, -5.0f), bvMin));
        mWaiter.assertTrue(checkResult(new Vector3f(2.5f, 2.5f, -5.0f), bvMax));
    }

    @Test
    public void testRotatedQuadBV() {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRTexture texture = context.getAssetLoader().loadTexture(new SXRAndroidResource(
                context, R.drawable.gearvr_logo));
        SXRNode sceneObject = new SXRNode(context, 5.0f, 5.0f, texture);

        sceneObject.getTransform().setPosition(-5.0f, 0.0f, 0.0f);
        sceneObject.getTransform().rotateByAxis(+90.0f, 0.0f, 1.0f, 0.0f);
        scene.addNode(sceneObject);
        sxrTestUtils.waitForSceneRendering();
        SXRNode.BoundingVolume boundingVolume = sceneObject.getBoundingVolume();
        Vector3f bvMin = boundingVolume.minCorner;
        Vector3f bvMax = boundingVolume.maxCorner;

        mWaiter.assertTrue(checkResult(new Vector3f(-5.0f, -2.5f, -2.5f), bvMin));
        mWaiter.assertTrue(checkResult(new Vector3f(-5.0f, 2.5f, 2.5f), bvMax));
    }

    @Test
    public void testBoxBV() {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        SXRTexture texture = context.getAssetLoader().loadTexture(new SXRAndroidResource(
                context, R.drawable.gearvr_logo));

        SXRCubeNode cubeNode = new SXRCubeNode(context, true, texture);
        scene.addNode(cubeNode);
        sxrTestUtils.waitForSceneRendering();
        SXRNode.BoundingVolume boundingVolume = cubeNode.getBoundingVolume();
        Vector3f bvMin = boundingVolume.minCorner;
        Vector3f bvMax = boundingVolume.maxCorner;

        mWaiter.assertTrue(checkResult(new Vector3f(-0.5f, -0.5f, -0.5f), bvMin));
        mWaiter.assertTrue(checkResult(new Vector3f(0.5f, 0.5f, 0.5f), bvMax));
        // rotate by 45 degrees
        cubeNode.getTransform().rotateByAxis(45.0f, 0.0f, 1.0f, 0.0f);

        boundingVolume = cubeNode.getBoundingVolume();
        bvMin = boundingVolume.minCorner;
        bvMax = boundingVolume.maxCorner;

        mWaiter.assertTrue(checkResult(new Vector3f(-0.5f * SQRT_2, -0.5f, -0.5f * SQRT_2), bvMin));
        mWaiter.assertTrue(checkResult(new Vector3f(0.5f * SQRT_2, 0.5f, 0.5f * SQRT_2), bvMax));
        // rotate by 45 degrees
        cubeNode.getTransform().rotateByAxis(45.0f, 0.0f, 1.0f, 0.0f);
        boundingVolume = cubeNode.getBoundingVolume();
        bvMin = boundingVolume.minCorner;
        bvMax = boundingVolume.maxCorner;

        mWaiter.assertTrue(checkResult(new Vector3f(-0.5f, -0.5f, -0.5f), bvMin));
        mWaiter.assertTrue(checkResult(new Vector3f(0.5f, 0.5f, 0.5f), bvMax));
    }

    @Test
    public void testScaledBV() {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();

        SXRConeNode coneNode = new SXRConeNode(context, true);
        scene.addNode(coneNode);
        sxrTestUtils.waitForSceneRendering();

        SXRNode.BoundingVolume boundingVolume = coneNode.getBoundingVolume();

        Vector3f bvMin = boundingVolume.minCorner;
        Vector3f bvMax = boundingVolume.maxCorner;
        mWaiter.assertTrue(checkResult(new Vector3f(-0.5f, -0.5f, -0.5f), bvMin));
        mWaiter.assertTrue(checkResult(new Vector3f(0.5f, 0.5f, 0.5f), bvMax));

        coneNode.getTransform().setScale(2.0f, 2.0f, 2.0f);

        boundingVolume = coneNode.getBoundingVolume();
        bvMin = boundingVolume.minCorner;
        bvMax = boundingVolume.maxCorner;

        mWaiter.assertTrue(checkResult(new Vector3f(-1.0f, -1.0f, -1.0f), bvMin));
        mWaiter.assertTrue(checkResult(new Vector3f(1.0f, 1.0f, 1.0f), bvMax));

        coneNode.getTransform().rotateByAxis(45.0f, 1.0f, 0.0f, 0.0f);
        boundingVolume = coneNode.getBoundingVolume();
        bvMin = boundingVolume.minCorner;
        bvMax = boundingVolume.maxCorner;

        mWaiter.assertTrue(checkResult(new Vector3f(-1.0f, -1.0f * SQRT_2, -1.0f * SQRT_2), bvMin));
        mWaiter.assertTrue(checkResult(new Vector3f(1.0f, 1.0f * SQRT_2, 1.0f * SQRT_2), bvMax));
    }

    @Test
    public void testHierarchicalBV() {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();

        SXRNode parent = new SXRNode(context);
        SXRSphereNode sphereNode = new SXRSphereNode(context, true);
        SXRNode.BoundingVolume orig = sphereNode.getBoundingVolume();
        SXRTransform trans = sphereNode.getTransform();

        // rotate by 45 degrees
        trans.rotateByAxis(45.0f, 0.0f, 1.0f, 0.0f);
        Vector3f expectedMinCorner = new Vector3f(orig.minCorner);
        Vector3f expectedMaxCorner = new Vector3f(orig.maxCorner);

        calcBounds(expectedMinCorner, expectedMaxCorner, trans);
        parent.addChildObject(sphereNode);
        scene.addNode(parent);
        sxrTestUtils.waitForSceneRendering();

        SXRNode.BoundingVolume boundingVolume = parent.getBoundingVolume();
        Vector3f bvMin = boundingVolume.minCorner;
        Vector3f bvMax = boundingVolume.maxCorner;

        mWaiter.assertTrue(checkResult(expectedMinCorner, bvMin));
        mWaiter.assertTrue(checkResult(expectedMaxCorner, bvMax));
    }

    //
    // Duplicates the algorithm GearVRf uses to calculate bounding volumes
    //
    private void calcBounds(Vector3f minCorner, Vector3f maxCorner, SXRTransform trans)
    {
        Matrix4f mtx = trans.getModelMatrix4f();
        Vector3f center = new Vector3f(minCorner);
        Vector3f extents = new Vector3f(maxCorner);
        Matrix3f newMtx = new Matrix3f(
                Math.abs(mtx.m00()), Math.abs(mtx.m01()), Math.abs(mtx.m02()),
                Math.abs(mtx.m10()), Math.abs(mtx.m11()), Math.abs(mtx.m12()),
                Math.abs(mtx.m20()), Math.abs(mtx.m21()), Math.abs(mtx.m22()));

        center.add(maxCorner);
        center.mul(0.5f);
        extents.sub(minCorner);
        extents.mul(0.5f);
        mtx.transformPosition(center);
        newMtx.transform(extents);
        Vector3f c1 = new Vector3f(center);
        Vector3f c2 = new Vector3f(center);

        c1.sub(extents);
        c2.add(extents);
        minCorner.set(c1);
        maxCorner.set(c1);
        minCorner.min(c2);
        maxCorner.max(c2);
    }


    private boolean checkResult(Vector3f expected, Vector3f actual) {
        if (Math.abs(expected.x - actual.x) < 0.0001f && Math.abs(expected.y - actual.y) <
                0.0001f && Math.abs(expected.z - actual.z) < 0.0001f) {
            return true;
        }
        return false;
    }
}
