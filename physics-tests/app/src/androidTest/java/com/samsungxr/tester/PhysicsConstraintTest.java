package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRBoxCollider;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRSphereCollider;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTransform;
import com.samsungxr.physics.SXRConeTwistConstraint;
import com.samsungxr.physics.SXRFixedConstraint;
import com.samsungxr.physics.SXRGenericConstraint;
import com.samsungxr.physics.SXRHingeConstraint;
import com.samsungxr.physics.SXRPoint2PointConstraint;
import com.samsungxr.physics.SXRRigidBody;
import com.samsungxr.physics.SXRSliderConstraint;
import com.samsungxr.physics.SXRWorld;
import com.samsungxr.scene_objects.SXRCubeSceneObject;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PhysicsConstraintTest {

    private SXRTestUtils sxrTestUtils;
    private Waiter mWaiter;
    SXRWorld world;

    private SXRMesh cubeMesh = null;
    private SXRTexture cubeTexture = null;

    private SXRMesh sphereMesh = null;
    private SXRTexture sphereTexture = null;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new
            ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @Before
    public void setUp() throws TimeoutException {
        mWaiter = new Waiter();

        SXRTestUtils.OnInitCallback initCallback = new SXRTestUtils.OnInitCallback() {
            @Override
            public void onInit(SXRContext sxrContext) {
                sxrContext.getMainScene().getMainCameraRig().getTransform().setPosition(0.0f, 6.0f, 0.0f);
                world = new SXRWorld(sxrContext);
                sxrContext.getMainScene().getRoot().attachComponent(world);
            }
        };

        sxrTestUtils = new SXRTestUtils(ActivityRule.getActivity(), initCallback);
        sxrTestUtils.waitForOnInit();
    }

    @Test
    public void fixedConstraintTest() throws Exception {
        SXRSceneObject ground = addGround(sxrTestUtils.getMainScene(), 0f, 0f, -15f);

        SXRSceneObject box1 = addCube(sxrTestUtils.getMainScene(), 0f, 0.5f, -30f, 1.0f);
        ((SXRRigidBody)box1.getComponent(SXRRigidBody.getComponentType())).setSimulationType(SXRRigidBody.DYNAMIC);

        SXRSceneObject box2 = addCube(sxrTestUtils.getMainScene(), 0f, 0.5f, -15f, 1.0f);
        ((SXRRigidBody)box2.getComponent(SXRRigidBody.getComponentType())).setSimulationType(SXRRigidBody.DYNAMIC);

        SXRFixedConstraint constraint = new SXRFixedConstraint(sxrTestUtils.getSxrContext(), (SXRRigidBody)box2.getComponent(SXRRigidBody.getComponentType()));
        box1.attachComponent(constraint);

        sxrTestUtils.waitForXFrames(30);
        float distance = transformsDistance(box1.getTransform(), box2.getTransform());

        ((SXRRigidBody)box1.getComponent(SXRRigidBody.getComponentType())).applyTorque(0, 0, 200);
        sxrTestUtils.waitForXFrames(120);
        float rotation = Math.abs(box1.getTransform().getRotationX() - box2.getTransform().getRotationX())
                + Math.abs(box1.getTransform().getRotationY() - box2.getTransform().getRotationY())
                + Math.abs(box1.getTransform().getRotationZ() - box2.getTransform().getRotationZ());
        mWaiter.assertTrue(rotation < 0.2f);
        mWaiter.assertTrue(Math.abs(distance - transformsDistance(box1.getTransform(), box2.getTransform())) < 0.2);

        ((SXRRigidBody)box2.getComponent(SXRRigidBody.getComponentType())).applyCentralForce(300,0,300);
        sxrTestUtils.waitForXFrames(180);
        rotation = Math.abs(box1.getTransform().getRotationX() - box2.getTransform().getRotationX())
                + Math.abs(box1.getTransform().getRotationY() - box2.getTransform().getRotationY())
                + Math.abs(box1.getTransform().getRotationZ() - box2.getTransform().getRotationZ());
        mWaiter.assertTrue(rotation < 0.2f);
        mWaiter.assertTrue(Math.abs(distance - transformsDistance(box1.getTransform(), box2.getTransform())) < 0.2);

        sxrTestUtils.waitForXFrames(30);
    }

    @Test
    public void point2pointConstraintTest() throws Exception {
        float pivotInA[] = {0f, -1.5f, 0f};
        float pivotInB[] = {-8f, -1.5f, 0f};

        SXRSceneObject ball = addSphere(sxrTestUtils.getMainScene(), 0.0f, 10.0f, -10.0f, 0.0f);
        SXRSceneObject box = addCube(sxrTestUtils.getMainScene(), 8.0f, 10.0f, -10.0f, 1.0f);
        ((SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType())).setSimulationType(SXRRigidBody.DYNAMIC);

        SXRPoint2PointConstraint constraint = new SXRPoint2PointConstraint(sxrTestUtils.getSxrContext(), (SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType()), pivotInA, pivotInB);
        ball.attachComponent(constraint);

        sxrTestUtils.waitForXFrames(30);

        sxrTestUtils.waitForXFrames(60);
        float distance = transformsDistance(ball.getTransform(), box.getTransform());
        mWaiter.assertTrue(distance < 9.8);

        sxrTestUtils.waitForXFrames(60);
        distance = transformsDistance(ball.getTransform(), box.getTransform());
        mWaiter.assertTrue(distance < 9.5);

        sxrTestUtils.waitForXFrames(60);
    }

    @Test
    public void hingeConstraintTest() throws Exception {
        float pivotInA[] = {0f, -3f, 0f};
        float pivotInB[] = {0f, 3f, 0f};
        float axisInA[] = {1f, 0f, 0f};
        float axisInB[] = {1f, 0f, 0f};

        SXRSceneObject ball = addSphere(sxrTestUtils.getMainScene(), 0.0f, 10.0f, -10.0f, 0.0f);
        SXRSceneObject box = addCube(sxrTestUtils.getMainScene(), 0.0f, 4.0f, -10.0f, 1.0f);

        SXRRigidBody boxBody = (SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType());
        boxBody.setSimulationType(SXRRigidBody.DYNAMIC);

        SXRHingeConstraint constraint = new SXRHingeConstraint(sxrTestUtils.getSxrContext(),
                boxBody, pivotInA, pivotInB, axisInA, axisInB);
        constraint.setLimits(-1f, 1f);
        ball.attachComponent(constraint);

        final float maxDistabceX = 0.000001f;
        final float minDistanceY = 3.f + 1.62f;
        final float maxDIstanceZ = 2.52f;

        sxrTestUtils.waitForXFrames(30);

        boxBody.applyCentralForce(0f, 0f, 1000f); // Must move towards camera
        sxrTestUtils.waitForXFrames(180);
        float dx = Math.abs(ball.getTransform().getPositionX() - box.getTransform().getPositionX());
        float dy = Math.abs(ball.getTransform().getPositionY() - box.getTransform().getPositionY());
        float dz = Math.abs(ball.getTransform().getPositionZ() - box.getTransform().getPositionZ());
        mWaiter.assertTrue(dx < maxDistabceX && dy > minDistanceY && dz < maxDIstanceZ);

        boxBody.applyCentralForce(0f, 1000f, 0f); // May have some effect
        sxrTestUtils.waitForXFrames(180);
        dx = Math.abs(ball.getTransform().getPositionX() - box.getTransform().getPositionX());
        dy = Math.abs(ball.getTransform().getPositionY() - box.getTransform().getPositionY());
        dz = Math.abs(ball.getTransform().getPositionZ() - box.getTransform().getPositionZ());
        mWaiter.assertTrue(dx < maxDistabceX && dy > minDistanceY && dz < maxDIstanceZ);

        boxBody.applyCentralForce(1000f, 0f, 0f); // Must have no effect
        sxrTestUtils.waitForXFrames(180);
        dx = Math.abs(ball.getTransform().getPositionX() - box.getTransform().getPositionX());
        dy = Math.abs(ball.getTransform().getPositionY() - box.getTransform().getPositionY());
        dz = Math.abs(ball.getTransform().getPositionZ() - box.getTransform().getPositionZ());
        mWaiter.assertTrue(dx < maxDistabceX && dy > minDistanceY && dz < maxDIstanceZ);
    }

    @Test
    public void sliderConstraintTest() throws Exception {
        SXRSceneObject ground = addGround(sxrTestUtils.getMainScene(), 0f, 0f, -15f);

        SXRSceneObject box1 = addCube(sxrTestUtils.getMainScene(), 3.0f, 0.5f, -15.0f, 1.0f);
        ((SXRRigidBody)box1.getComponent(SXRRigidBody.getComponentType())).setSimulationType(SXRRigidBody.DYNAMIC);

        SXRSceneObject box2 = addCube(sxrTestUtils.getMainScene(), -2.0f, 0.5f, -15.0f, 1.0f);
        ((SXRRigidBody)box2.getComponent(SXRRigidBody.getComponentType())).setSimulationType(SXRRigidBody.DYNAMIC);

        SXRSliderConstraint constraint = new SXRSliderConstraint(sxrTestUtils.getSxrContext(), (SXRRigidBody)box2.getComponent(SXRRigidBody.getComponentType()));
        constraint.setAngularLowerLimit(-2f);
        constraint.setAngularUpperLimit(2f);
        constraint.setLinearLowerLimit(-5f);
        constraint.setLinearUpperLimit(-2f);
        box1.attachComponent(constraint);

        sxrTestUtils.waitForXFrames(30);

        ((SXRRigidBody)box2.getComponent(SXRRigidBody.getComponentType())).applyCentralForce(400f, 0f, 0f);
        sxrTestUtils.waitForXFrames(180);
        float d = transformsDistance(box1.getTransform(), box2.getTransform());
        mWaiter.assertTrue(d >= 2.0f && d <= 5.0f);

        ((SXRRigidBody)box2.getComponent(SXRRigidBody.getComponentType())).applyCentralForce(-500f, 0f, 0f);
        sxrTestUtils.waitForXFrames(180);
        d = transformsDistance(box1.getTransform(), box2.getTransform());
        mWaiter.assertTrue(d >= 2.0f && d <= 5.0f);

        ((SXRRigidBody)box1.getComponent(SXRRigidBody.getComponentType())).applyTorque(100f, 0f, 0f);
        sxrTestUtils.waitForXFrames(180);
    }

    @Test
    public void ConeTwistConstraintTest() throws Exception {
        SXRSceneObject box = addCube(sxrTestUtils.getMainScene(), 0f, -5f, -15f, 0f);

        SXRSceneObject ball = addSphere(sxrTestUtils.getMainScene(), 0, 5f, -15f, 1f);

        float pivot[] = {0f, -5f, 0f};
        float rotation[] = {0f, -1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

        SXRConeTwistConstraint constraint = new SXRConeTwistConstraint(sxrTestUtils.getSxrContext(),
                (SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType()), pivot,
                rotation, rotation);

        ball.attachComponent(constraint);

        final float maxDistance = (float)(Math.sin(Math.PI * 0.375) * 10.0);

        ((SXRRigidBody)ball.getComponent(SXRRigidBody.getComponentType())).applyCentralForce(100f, 0f, 100f);
        sxrTestUtils.waitForXFrames(180);
        mWaiter.assertTrue(maxDistance >= transformsDistance(ball.getTransform(), box.getTransform()));

        ((SXRRigidBody)ball.getComponent(SXRRigidBody.getComponentType())).applyCentralForce(-500f, 0f, 0f);
        sxrTestUtils.waitForXFrames(180);
        mWaiter.assertTrue(maxDistance >= transformsDistance(ball.getTransform(), box.getTransform()));
    }

    @Test
    public void GenericConstraintTest() throws Exception {
        SXRSceneObject ground = addGround(sxrTestUtils.getMainScene(), 0f, -0.5f, -15f);

        SXRSceneObject box = addCube(sxrTestUtils.getMainScene(), -3f, 0f, -10f, 1f);
        ((SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType())).setSimulationType(SXRRigidBody.DYNAMIC);

        SXRSceneObject ball = addSphere(sxrTestUtils.getMainScene(), 3f, 0f, -10f, 1f);

        final float joint[] = {-6f, 0f, 0f};
        final float rotation[] = {1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};

        SXRGenericConstraint constraint = new SXRGenericConstraint(
                sxrTestUtils.getSxrContext(), (SXRRigidBody)box.getComponent(
                        SXRRigidBody.getComponentType()), joint, rotation, rotation);
        constraint.setAngularLowerLimits((float)-Math.PI, (float)-Math.PI, (float)-Math.PI);
        constraint.setAngularUpperLimits((float)Math.PI, (float)Math.PI, (float)Math.PI);
        constraint.setLinearLowerLimits(-3f, -10f, -3f);
        constraint.setLinearUpperLimits(3f, 10f, 3f);

        ball.attachComponent(constraint);

        sxrTestUtils.waitForXFrames(30);

        float anchor[] = {ball.getTransform().getPositionX(), ball.getTransform().getPositionY(),
                ball.getTransform().getPositionZ()};
        float offsetLimit = 0.005f;

        ((SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType())).applyCentralForce(100f, 200f, 100f);
        sxrTestUtils.waitForXFrames(90);
        mWaiter.assertTrue(checkTransformOffset(ball.getTransform(), anchor, offsetLimit));

        ((SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType())).applyCentralForce(-100f, 200f, -100f);
        sxrTestUtils.waitForXFrames(90);
        mWaiter.assertTrue(checkTransformOffset(ball.getTransform(), anchor, offsetLimit));

        ((SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType())).applyTorque(0f, 1000f, 0f);
        sxrTestUtils.waitForXFrames(180);
        mWaiter.assertTrue(checkTransformOffset(ball.getTransform(), anchor, offsetLimit));

        ((SXRRigidBody)box.getComponent(SXRRigidBody.getComponentType())).applyCentralForce(-1000f, 500f, 500f);
        sxrTestUtils.waitForXFrames(180);
        mWaiter.assertTrue(!checkTransformOffset(ball.getTransform(), anchor, offsetLimit));
    }

    /*
    * Function to add a sphere of dimension and position specified in the
    * Bullet physics world and scene graph
    */
    private SXRSceneObject addSphere(SXRScene scene, float x, float y, float z, float mass) {

        if (sphereMesh == null) {
            try {
                sphereMesh = sxrTestUtils.getSxrContext().getAssetLoader().loadMesh(
                        new SXRAndroidResource(sxrTestUtils.getSxrContext(), "sphere.obj"));
                sphereTexture = sxrTestUtils.getSxrContext().getAssetLoader().loadTexture(
                        new SXRAndroidResource(sxrTestUtils.getSxrContext(), "sphere.jpg"));
            } catch (IOException e) {

            }
        }

        SXRSceneObject sphereObject = new SXRSceneObject(sxrTestUtils.getSxrContext(), sphereMesh, sphereTexture);

        sphereObject.getTransform().setScaleX(0.5f);
        sphereObject.getTransform().setScaleY(0.5f);
        sphereObject.getTransform().setScaleZ(0.5f);
        sphereObject.getTransform().setPosition(x, y, z);

        // Collider
        SXRSphereCollider sphereCollider = new SXRSphereCollider(sxrTestUtils.getSxrContext());
        sphereCollider.setRadius(0.5f);
        sphereObject.attachCollider(sphereCollider);

        // Physics body
        SXRRigidBody mSphereRigidBody = new SXRRigidBody(sxrTestUtils.getSxrContext());
        mSphereRigidBody.setMass(mass);

        sphereObject.attachComponent(mSphereRigidBody);

        scene.addSceneObject(sphereObject);
        return sphereObject;
    }

    private SXRSceneObject addCube(SXRScene scene, float x, float y, float z, float mass) {

        if (cubeMesh == null) {
            try {
                cubeMesh = sxrTestUtils.getSxrContext().getAssetLoader().loadMesh(
                        new SXRAndroidResource(sxrTestUtils.getSxrContext(), "cube.obj"));
                cubeTexture = sxrTestUtils.getSxrContext().getAssetLoader().loadTexture(
                        new SXRAndroidResource(sxrTestUtils.getSxrContext(), "cube.jpg"));
            } catch (IOException e) {

            }
        }

        SXRSceneObject cubeObject = new SXRSceneObject(sxrTestUtils.getSxrContext(), cubeMesh, cubeTexture);

        cubeObject.getTransform().setPosition(x, y, z);

        // Collider
        SXRBoxCollider boxCollider = new SXRBoxCollider(sxrTestUtils.getSxrContext());
        boxCollider.setHalfExtents(0.5f, 0.5f, 0.5f);
        cubeObject.attachCollider(boxCollider);

        // Physics body
        SXRRigidBody body = new SXRRigidBody(sxrTestUtils.getSxrContext());
        body.setMass(mass);
        body.setSimulationType(SXRRigidBody.KINEMATIC);

        cubeObject.attachComponent(body);

        scene.addSceneObject(cubeObject);
        return cubeObject;
    }

    private SXRSceneObject addGround(SXRScene scene, float x, float y, float z) {

        SXRSceneObject groundObject = new SXRCubeSceneObject(sxrTestUtils.getSxrContext());

        groundObject.getTransform().setScale(100f, 0.5f, 100f);
        groundObject.getTransform().setPosition(x, y, z);

        SXRBoxCollider boxCollider = new SXRBoxCollider(sxrTestUtils.getSxrContext());
        boxCollider.setHalfExtents(0.5f, 0.5f, 0.5f);
        groundObject.attachCollider(boxCollider);

        SXRRigidBody body = new SXRRigidBody(sxrTestUtils.getSxrContext());
        body.setMass(0f);
        groundObject.attachComponent(body);

        scene.addSceneObject(groundObject);

        return groundObject;
    }

    static float transformsDistance(SXRTransform a, SXRTransform b) {
        float x = a.getPositionX() - b.getPositionX();
        float y = a.getPositionY() - b.getPositionY();
        float z = a.getPositionZ() - b.getPositionZ();

        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    static boolean checkTransformOffset(SXRTransform tr, float comp[], float limit) {
        return Math.abs(tr.getPositionX() - comp[0]) < limit
                && Math.abs(tr.getPositionY() - comp[1]) < limit
                && Math.abs(tr.getPositionZ() - comp[2]) < limit;
    }
}
