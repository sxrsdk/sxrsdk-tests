package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRSphereCollider;
import com.samsungxr.physics.SXRRigidBody;
import com.samsungxr.physics.SXRWorld;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import com.samsungxr.utility.Log;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class RigidBodyAttributesTest {
    private SXRTestUtils sxrTestUtils;
    private Waiter mWaiter;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new
            ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @Before
    public void setUp() throws TimeoutException {
        SXRTestUtils.OnInitCallback onInitCallback = new SXRTestUtils.OnInitCallback() {
            @Override
            public void onInit(SXRContext sxrContext) {
                Log.d("SXRPHYSICS", "HAPPENING");
                SXRWorld world = new SXRWorld(sxrTestUtils.getSxrContext());
                sxrTestUtils.getMainScene().getRoot().attachComponent(world);
            }
        };
        sxrTestUtils = new SXRTestUtils(ActivityRule.getActivity(), onInitCallback);
        mWaiter = new Waiter();
        sxrTestUtils.waitForOnInit();
    }

    @Test
    public void createRigidBody() throws Exception {
        SXRRigidBody mSphereRigidBody = new SXRRigidBody(sxrTestUtils.getSxrContext());
        addSphere(sxrTestUtils.getMainScene(), mSphereRigidBody, 1.0f, 1.5f, 40.0f, -10.0f, 2.5f);
        mWaiter.assertTrue(mSphereRigidBody.getMass() == 2.5f);
        mWaiter.assertTrue(mSphereRigidBody.getRestitution() == 1.5f);
        mWaiter.assertTrue(mSphereRigidBody.getFriction() == 0.5f);
    }

    @Test
    public void enableRigidBody() throws Exception {
        SXRRigidBody mSphereRigidBody = new SXRRigidBody(sxrTestUtils.getSxrContext());
        addSphere(sxrTestUtils.getMainScene(), mSphereRigidBody, 1.0f, 1.0f, 10.0f, -10.0f, 2.5f);

        SXRRigidBody mSphereRigidBody2 = new SXRRigidBody(sxrTestUtils.getSxrContext());
        addSphere(sxrTestUtils.getMainScene(), mSphereRigidBody2, 1.0f, 2.0f, 10.0f, -10.0f, 2.5f);

        sxrTestUtils.waitForXFrames(10);

        float lastY = mSphereRigidBody.getTransform().getPositionY();
        float lastY2 =  mSphereRigidBody2.getTransform().getPositionY();
        sxrTestUtils.waitForXFrames(10);
        mWaiter.assertTrue( lastY > mSphereRigidBody.getTransform().getPositionY());//balls are falling
        mWaiter.assertTrue( lastY2 > mSphereRigidBody2.getTransform().getPositionY());

        lastY = mSphereRigidBody.getTransform().getPositionY();
        lastY2 =  mSphereRigidBody2.getTransform().getPositionY();
        mSphereRigidBody.setEnable(false);
        sxrTestUtils.waitForXFrames(60);
        mWaiter.assertTrue( lastY == mSphereRigidBody.getTransform().getPositionY()); //ball1 stoped falling
        mWaiter.assertTrue( lastY2 > mSphereRigidBody2.getTransform().getPositionY()); //ball2 is falling

        lastY = mSphereRigidBody.getTransform().getPositionY();
        lastY2 =  mSphereRigidBody2.getTransform().getPositionY();
        mSphereRigidBody.setEnable(true);
        sxrTestUtils.waitForXFrames(10);
        mWaiter.assertTrue( lastY > mSphereRigidBody.getTransform().getPositionY()); //ball1 is falling again
        mWaiter.assertTrue( lastY2 > mSphereRigidBody2.getTransform().getPositionY()); //ball2 kept falling

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

    private void addSphere(SXRScene scene, SXRRigidBody sphereRigidBody, float radius, float x, float y,
                           float z, float mass) {

        SXRSceneObject sphereObject = meshWithTexture("sphere.obj",
                "sphere.jpg");
        sphereObject.getTransform().setPosition(x, y, z);

        // Collider
        SXRSphereCollider sphereCollider = new SXRSphereCollider(sxrTestUtils.getSxrContext());
        sphereCollider.setRadius(1.0f);
        sphereObject.attachCollider(sphereCollider);

        // Physics body
        sphereRigidBody.setMass(mass);
        sphereRigidBody.setRestitution(1.5f);
        sphereRigidBody.setFriction(0.5f);

        sphereObject.attachComponent(sphereRigidBody);

        scene.addSceneObject(sphereObject);
    }
}
