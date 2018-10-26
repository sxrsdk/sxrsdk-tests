package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSphereCollider;
import com.samsungxr.physics.SXRCollisionMatrix;
import com.samsungxr.physics.SXRRigidBody;
import com.samsungxr.physics.SXRWorld;
import com.samsungxr.scene_objects.SXRSphereSceneObject;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import com.samsungxr.utility.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PhysicsWorldTest {
    private SXRTestUtils sxrTestUtils;
    private Waiter mWaiter;
    SXRWorld mWorld;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new
            ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @Before
    public void setUp() throws TimeoutException {
        mWaiter = new Waiter();

        SXRTestUtils.OnInitCallback initCallback = new SXRTestUtils.OnInitCallback() {
            @Override
            public void onInit(SXRContext sxrContext) {
                mWorld = new SXRWorld(sxrContext);
                sxrContext.getMainScene().getRoot().attachComponent(mWorld);
            }
        };

        sxrTestUtils = new SXRTestUtils(ActivityRule.getActivity(), initCallback);
        sxrTestUtils.waitForOnInit();
    }

    @Test
    public void testEnableDisablePhysics() throws Exception {
        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();
        float posY;

        SXRSphereSceneObject sphere = new SXRSphereSceneObject(context);
        SXRSphereCollider collider = new SXRSphereCollider(context);
        SXRRigidBody body = new SXRRigidBody(context, 3.0f, 0);

        collider.setRadius(1.0f);

        sphere.attachCollider(collider);
        sphere.attachComponent(body);

        scene.addSceneObject(sphere);

        posY = sphere.getTransform().getPositionY();

        sxrTestUtils.waitForXFrames(60);

        mWaiter.assertTrue(posY != sphere.getTransform().getPositionY());

        mWorld.setEnable(false);

        posY = sphere.getTransform().getPositionY();

        sxrTestUtils.waitForXFrames(60);

        mWaiter.assertTrue(posY == sphere.getTransform().getPositionY());
    }
}
