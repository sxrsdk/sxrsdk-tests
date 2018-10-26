package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRBoxCollider;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRMeshCollider;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRSphereCollider;
import com.samsungxr.SXRTexture;
import com.samsungxr.physics.SXRRigidBody;
import com.samsungxr.physics.SXRWorld;
import com.samsungxr.physics.ICollisionEvents;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import com.samsungxr.utility.Log;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// kinematic equation
// d = 0.5*g(m/s^2)*t(s)^2
// (d/(0.5*g))^0.5 = t
// Xframes = t * 60
// tested on note4, no other apps running
// time to fall 2800ms or 168 frames, rounded up

@RunWith(AndroidJUnit4.class)
public class PhysicsSimulationTest {
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
    public void updatedOwnerTransformTest() throws Exception {
        addGroundMesh(sxrTestUtils.getMainScene(), 0.0f,0.0f,0.0f, 0.0f);

        SXRSceneObject[] objects = new SXRSceneObject[10];
        SXRRigidBody[] bodies = new SXRRigidBody[10];

        for(int i = 0; i < 10; i = i + 2) {
            SXRBoxCollider boxCollider = new SXRBoxCollider(sxrTestUtils.getSxrContext());
            boxCollider.setHalfExtents(0.5f, 0.5f, 0.5f);
            objects[i] = meshWithTexture("cube.obj", "cube.jpg");
            objects[i].getTransform().setPosition(0.0f -i, 6.0f, -10.0f - (2.0f*i));
            objects[i].attachCollider(boxCollider);
            bodies[i] = new SXRRigidBody(sxrTestUtils.getSxrContext());
            switch (i) {
                case 0 : bodies[i].setMass(0.0f); //object moves, rigid body doesn't
                         bodies[i].setSimulationType(SXRRigidBody.STATIC);
                         break;
                case 2 : bodies[i].setMass(1.0f);  //rigidbody is a "trigger  collider"
                         bodies[i].setSimulationType(SXRRigidBody.STATIC);
                         break;
                case 4 : bodies[i].setMass(0.0f);  //object moves, rigid body doesn't
                         bodies[i].setSimulationType(SXRRigidBody.KINEMATIC);
                         break;
                case 6 : bodies[i].setMass(1.0f); //rigid body is "sleeping", until it is hit by another
                         bodies[i].setSimulationType(SXRRigidBody.KINEMATIC);
                         break;
                case 8 : bodies[i].setMass(1.0f); //rigid body is obbeys all external forces
                         bodies[i].setSimulationType(SXRRigidBody.DYNAMIC);
                         break;
            }
            objects[i].attachComponent(bodies[i]);
            sxrTestUtils.getMainScene().addSceneObject(objects[i]);

            SXRSphereCollider sphereCollider = new SXRSphereCollider(sxrTestUtils.getSxrContext());
            sphereCollider.setRadius(0.5f);
            objects[i+1] = meshWithTexture("sphere.obj", "sphere.jpg");
            objects[i+1].getTransform().setScale(0.5f,0.5f,0.5f);
            objects[i+1].getTransform().setPosition(0.0f -i, 9.0f, -10.0f - (2.0f*i));
            objects[i+1].attachCollider(sphereCollider);
            bodies[i+1] = new SXRRigidBody(sxrTestUtils.getSxrContext(), 10.0f);
            objects[i+1].attachComponent(bodies[i+1]);
            sxrTestUtils.getMainScene().addSceneObject(objects[i+1]);

        }

        sxrTestUtils.waitForXFrames(47);

        for(int i = 0; i < 5; i++) {
            objects[i*2].getTransform().setPositionX(2.0f);
        }

        sxrTestUtils.waitForXFrames(600);

        float d = (bodies[1].getTransform().getPositionY()
                - bodies[0].getTransform().getPositionY()); //sphere is on top of the cube rigid body
        mWaiter.assertTrue(d > 0.5f);

        d = (bodies[3].getTransform().getPositionY()
                - bodies[2].getTransform().getPositionY()); //sphere went thru the cube
        mWaiter.assertTrue(d <= 0.5f);

        d = (bodies[5].getTransform().getPositionY()
                - bodies[4].getTransform().getPositionY()); //sphere is on top of the cube rigid body
        mWaiter.assertTrue(d > 0.5f);

        d = (bodies[7].getTransform().getPositionY()
                - bodies[6].getTransform().getPositionY()); //sphere fell of the cube
        mWaiter.assertTrue(d <= 0.5f);

        d = (bodies[9].getTransform().getPositionY()
                - bodies[8].getTransform().getPositionY()); //sphere fell of the cube
        mWaiter.assertTrue(d <= 0.5f);

        for(int i = 0; i < 5; i++) {
            mWaiter.assertTrue(objects[i*2].getTransform().getPositionX() == bodies[i*2].getTransform().getPositionX());
        }


        sxrTestUtils.waitForXFrames(60);
    }

    @Test
    public void freeFallTest() throws Exception {
        CollisionHandler mCollisionHandler = new CollisionHandler();
        mCollisionHandler.extimatedTime = 2800; //... + round up
        mCollisionHandler.collisionCounter = 0;

        SXRSceneObject cube = addCube(sxrTestUtils.getMainScene(), 0.0f, 1.0f, -10.0f, 0.0f);
        SXRSceneObject sphere = addSphere(sxrTestUtils.getMainScene(), mCollisionHandler, 0.0f, 40.0f, -10.0f, 1.0f);

        mCollisionHandler.startTime = System.currentTimeMillis();
        sxrTestUtils.waitForXFrames(168);

        //Log.d("PHYSICS", "    Delta time of the last collisions:" + mCollisionHandler.lastCollisionTime);
        //mWaiter.assertTrue(mCollisionHandler.lastCollisionTime <= mCollisionHandler.extimatedTime);

        float d = (sphere.getTransform().getPositionY() - cube.getTransform().getPositionY()); //sphere is on top of the cube
        mWaiter.assertTrue( d <= 1.6f);
        //Log.d("PHYSICS", "    Number of collisions:" + mCollisionHandler.collisionCounter);
        mWaiter.assertTrue(mCollisionHandler.collisionCounter >= 1);
    }

    @Test
    public void spacedFreeFallTest() throws Exception {
        OnTestStartRenderCallback beginCallback = new OnTestStartRenderCallback();
        sxrTestUtils.setOnRenderCallback(beginCallback);

        for(int i = 0; i < beginCallback.lenght; i++) {
            sxrTestUtils.waitForSceneRendering();
            sxrTestUtils.waitForXFrames(1);
        }
        sxrTestUtils.waitForSceneRendering();
        sxrTestUtils.setOnRenderCallback(null);

        beginCallback.mCollisionHandler.startTime = System.currentTimeMillis();
        sxrTestUtils.waitForXFrames(168);

        runTest(beginCallback.sphere, beginCallback.cube, beginCallback.lenght);

        //It is complicated to try to predict this timing depends on the device state, should be almost the same as the above
        //Log.d("PHYSICS", "    Delta time of the last collisions:" + beginCallback.mCollisionHandler.lastCollisionTime);
        //mWaiter.assertTrue(beginCallback.mCollisionHandler.lastCollisionTime <= beginCallback.mCollisionHandler.extimatedTime);

        //asserts can not happen on collision event, but we get an idea here
        //Log.d("PHYSICS", "    Number of collisions:" + beginCallback.mCollisionHandler.collisionCounter);
        mWaiter.assertTrue(beginCallback.mCollisionHandler.collisionCounter >= beginCallback.lenght);
    }

    @Test
    public void simultaneousFreeFallTest() throws Exception {
        OnTestStartRenderAllCallback beginCallback = new OnTestStartRenderAllCallback(100);

        sxrTestUtils.setOnRenderCallback(beginCallback);
        sxrTestUtils.waitForSceneRendering();
        sxrTestUtils.setOnRenderCallback(null);

        beginCallback.mCollisionHandler.startTime = System.currentTimeMillis();
        sxrTestUtils.waitForXFrames(168 + 60 ); // Too many simultaneous events triggered need more time to process all???

        runTest(beginCallback.sphere, beginCallback.cube, beginCallback.lenght);

        //It is complicated to try to predict this timing
        //Log.d("PHYSICS", "    Delta time of the last collisions:" + mCollisionHandler.lastCollisionTime);
        //mWaiter.assertTrue(mCollisionHandler.lastCollisionTime <= mCollisionHandler.extimatedTime);

        //asserts can not happen on collision event, but we get an idea here
        //Log.d("PHYSICS", "    Number of collisions:" + beginCallback.mCollisionHandler.collisionCounter);
        mWaiter.assertTrue(beginCallback.mCollisionHandler.collisionCounter >= beginCallback.lenght);
    }

    @Test
    public void applyingForcesTest() throws Exception {
        float distance;
        float rotation;
        addGroundMesh(sxrTestUtils.getMainScene(), 0.0f,0.0f,0.0f, 0.0f);
        SXRSceneObject cube = addCube(sxrTestUtils.getMainScene(), 0.0f, 0.6f, -5.0f, 0.01f);
        sxrTestUtils.waitForSceneRendering();
        sxrTestUtils.waitForXFrames(10);

        distance = cube.getTransform().getPositionZ();
        rotation = cube.getTransform().getRotationPitch();
        ((SXRRigidBody)cube.getComponent(
                SXRRigidBody.getComponentType())).applyCentralForce(0,0,-1);
        sxrTestUtils.waitForXFrames(60);
        mWaiter.assertFalse(distance - cube.getTransform().getPositionZ() == 0);
        mWaiter.assertTrue(Math.abs(rotation - cube.getTransform().getRotationPitch()) < 1);

        distance = cube.getTransform().getPositionZ();
        rotation = cube.getTransform().getRotationPitch();
        ((SXRRigidBody)cube.getComponent(
                SXRRigidBody.getComponentType())).applyForce(0,0,-1,
                0.0f, 0.5f, 0.0f);
        sxrTestUtils.waitForXFrames(120);
        mWaiter.assertFalse(distance - cube.getTransform().getPositionZ() == 0);
        mWaiter.assertFalse(Math.abs(rotation - cube.getTransform().getRotationPitch()) < 1);

        distance = cube.getTransform().getPositionZ();
        rotation = cube.getTransform().getRotationPitch();
        ((SXRRigidBody)cube.getComponent(
                SXRRigidBody.getComponentType())).applyCentralImpulse(0,0,-0.02f);
        sxrTestUtils.waitForXFrames(120);
        mWaiter.assertFalse(distance - cube.getTransform().getPositionZ() == 0);
        mWaiter.assertTrue(Math.abs(rotation - cube.getTransform().getRotationPitch()) < 1);

        distance = cube.getTransform().getPositionZ();
        rotation = cube.getTransform().getRotationPitch();
        ((SXRRigidBody)cube.getComponent(
                SXRRigidBody.getComponentType())).applyImpulse(0,0, -0.02f,
                0.0f, 0.5f, 0.0f);
        sxrTestUtils.waitForXFrames(120);
        mWaiter.assertFalse(distance - cube.getTransform().getPositionZ() == 0);
        mWaiter.assertFalse(Math.abs(rotation - cube.getTransform().getRotationPitch()) < 1);

        distance = cube.getTransform().getPositionZ();
        rotation = cube.getTransform().getRotationPitch();
        ((SXRRigidBody)cube.getComponent(
                SXRRigidBody.getComponentType())).applyTorque(-1, 0, 0);
        sxrTestUtils.waitForXFrames(120);
        mWaiter.assertFalse(distance - cube.getTransform().getPositionZ() == 0);
        mWaiter.assertFalse(Math.abs(rotation - cube.getTransform().getRotationPitch()) < 1);

        distance = cube.getTransform().getPositionZ();
        rotation = cube.getTransform().getRotationPitch();
        ((SXRRigidBody)cube.getComponent(
                SXRRigidBody.getComponentType())).applyTorqueImpulse(-0.1f, 0, 0);
        sxrTestUtils.waitForXFrames(60);
        mWaiter.assertFalse(distance - cube.getTransform().getPositionZ() == 0);
        mWaiter.assertFalse(Math.abs(rotation - cube.getTransform().getRotationPitch()) < 1);
    }

    class OnTestStartRenderCallback implements SXRTestUtils.OnRenderCallback {
        public int lenght;
        public SXRSceneObject cube[];
        public SXRSceneObject sphere[];
        public CollisionHandler mCollisionHandler;
        int currentIndex;
        int k;

        OnTestStartRenderCallback () {
            cube = new SXRSceneObject[100];
            sphere  = new SXRSceneObject[100];
            mCollisionHandler = new CollisionHandler();
            mCollisionHandler.extimatedTime = 2800; //... + round up
            mCollisionHandler.collisionCounter = 0;
            currentIndex = 0;
            k = -100;
            lenght = 100;
        }

        @Override
        public void onSceneRendered() {
            k += 2;
            cube[currentIndex] = addCube(sxrTestUtils.getMainScene(), (float)k, 1.0f, -10.0f, 0.0f);
            sphere[currentIndex] = addSphere(sxrTestUtils.getMainScene(), mCollisionHandler, (float)k, 40.0f, -10.0f, 1.0f);
            currentIndex++;
        }
    }

    class OnTestStartRenderAllCallback implements SXRTestUtils.OnRenderCallback {
        public int lenght;
        public SXRSceneObject cube[];
        public SXRSceneObject sphere[];
        public CollisionHandler mCollisionHandler;
        private boolean objectsAdded = false;

        OnTestStartRenderAllCallback (int lenght) {
            cube = new SXRSceneObject[lenght];
            sphere  = new SXRSceneObject[lenght];
            mCollisionHandler = new CollisionHandler();
            mCollisionHandler.extimatedTime = 2800; //... + round up
            mCollisionHandler.collisionCounter = 0;
            this.lenght = lenght;
        }

        @Override
        public void onSceneRendered() {
            if (objectsAdded) {
                return;
            }

            objectsAdded = true;
            int x = -25;
            int j = 0;
            int k = 10;
            int z = -10;
            int step = 50 / k;

            world.setEnable(false);
            for(int i = 0; i < lenght; i++) {
                x += step;
                cube[i] = addCube(sxrTestUtils.getMainScene(), (float)x, 1.0f, (float)z, 0.0f);
                sphere[i] = addSphere(sxrTestUtils.getMainScene(), mCollisionHandler, (float)x, 40.0f, (float)z, 1.0f);
                j++;
                if (j == k) {
                    x = -25;
                    z -= 10;
                    j = 0;
                }
            }
            world.setEnable(true);
        }
    }

    public void runTest(SXRSceneObject sphere[], SXRSceneObject cube[], int lenght) throws  Exception{
        world.setEnable(false);
        for(int i = 0; i < lenght; i++) {
            float cubeX = cube[i].getTransform().getPositionX();
            float cubeY = cube[i].getTransform().getPositionY();
            float cubeZ = cube[i].getTransform().getPositionZ();
            float sphereX = sphere[i].getTransform().getPositionX();
            float sphereY = sphere[i].getTransform().getPositionY();
            float sphereZ = sphere[i].getTransform().getPositionZ();

            //Log.d("runTest", "[" + i + "] cube: " + cubeX + ", " + cubeY + ", " + cubeZ +
            //        " sphere: " + sphereX + ", " + sphereY + ", " + sphereZ);

            float d = (sphereY - cubeY);
            //sphere is on top of the cube

            mWaiter.assertTrue( d <= 1.6f);
            mWaiter.assertTrue(sphereX == cubeX);
            mWaiter.assertTrue(sphereZ == cubeZ);
            //Log.d("PHYSICS", "    Index:" + i + "    Collision distance:" + d);
        }
    }

    public class CollisionHandler implements ICollisionEvents {
        public long startTime;
        public long extimatedTime;
        public long lastCollisionTime;
        public int collisionCounter;

        public void onEnter(SXRSceneObject sceneObj0, SXRSceneObject sceneObj1, float normal[], float distance) {
            lastCollisionTime = System.currentTimeMillis() - startTime;
            collisionCounter++;
        }

        public void onExit(SXRSceneObject sceneObj0, SXRSceneObject sceneObj1, float normal[], float distance) {
        }

    }

    private SXRSceneObject meshWithTexture(String mesh, String texture) {
        SXRSceneObject object = null;
        try {
            object = new SXRSceneObject(sxrTestUtils.getSxrContext(), new SXRAndroidResource(
                    sxrTestUtils.getSxrContext(), mesh), new SXRAndroidResource(sxrTestUtils.getSxrContext(),
                    texture));
        } catch (IOException e) {
            mWaiter.fail(e);
        }
        return object;
    }

    /*
     * Function to add a sphere of dimension and position specified in the
     * Bullet physics world and scene graph
     */
    private SXRSceneObject addSphere(SXRScene scene, ICollisionEvents mCollisionHandler, float x, float y, float z, float mass) {

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
        sphereObject.getEventReceiver().addListener(mCollisionHandler);

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
                e.printStackTrace();
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

        cubeObject.attachComponent(body);

        scene.addSceneObject(cubeObject);
        return cubeObject;
    }

    private void addGroundMesh(SXRScene scene, float x, float y, float z, float mass) {
        try {
            SXRMesh mesh = sxrTestUtils.getSxrContext().createQuad(100.0f, 100.0f);
            SXRTexture texture =
                    sxrTestUtils.getSxrContext().getAssetLoader().loadTexture(new SXRAndroidResource(sxrTestUtils.getSxrContext(), "floor.jpg"));
            SXRSceneObject meshObject = new SXRSceneObject(sxrTestUtils.getSxrContext(), mesh, texture);

            meshObject.getTransform().setPosition(x, y, z);
            meshObject.getTransform().setRotationByAxis(-90.0f, 1.0f, 0.0f, 0.0f);

            // Collider
            SXRMeshCollider meshCollider = new SXRMeshCollider(sxrTestUtils.getSxrContext(), mesh);
            meshObject.attachCollider(meshCollider);

            // Physics body
            SXRRigidBody body = new SXRRigidBody(sxrTestUtils.getSxrContext());

            body.setRestitution(0.5f);
            body.setFriction(1.0f);

            meshObject.attachComponent(body);

            scene.addSceneObject(meshObject);
        } catch (IOException exception) {
            Log.d("sxrf", exception.toString());
        }
    }
}
