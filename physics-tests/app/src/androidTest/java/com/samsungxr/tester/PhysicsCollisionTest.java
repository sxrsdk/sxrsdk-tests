package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRSphereCollider;
import com.samsungxr.physics.SXRCollisionMatrix;
import com.samsungxr.physics.SXRRigidBody;
import com.samsungxr.physics.SXRWorld;
import com.samsungxr.physics.ICollisionEvents;
import com.samsungxr.scene_objects.SXRSphereSceneObject;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import com.samsungxr.utility.Assert;
import com.samsungxr.utility.Exceptions;
import com.samsungxr.utility.Log;
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
public class PhysicsCollisionTest {
    private SXRTestUtils sxrTestUtils;
    private Waiter mWaiter;
    private SXRCollisionMatrix mCollisionMatrix;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new
            ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @Before
    public void setUp() throws TimeoutException {
        mWaiter = new Waiter();
        mCollisionMatrix = new SXRCollisionMatrix();

        SXRTestUtils.OnInitCallback initCallback = new SXRTestUtils.OnInitCallback() {
            @Override
            public void onInit(SXRContext sxrContext) {
                SXRWorld world = new SXRWorld(sxrContext, mCollisionMatrix);
                sxrContext.getMainScene().getRoot().attachComponent(world);
            }
        };

        sxrTestUtils = new SXRTestUtils(ActivityRule.getActivity(), initCallback);
        sxrTestUtils.waitForOnInit();
    }

    @Test
    public void testCollisionMask() throws Exception {

        for (int groupA = 0; groupA < 16; groupA++) {
            for (int groupB = 0; groupB < 16; groupB++) {
                if (groupA == groupB) {
                    continue;
                }

                mCollisionMatrix.enableCollision(groupA, groupB);

                if ((SXRCollisionMatrix.getCollisionFilterGroup(groupA)
                        & mCollisionMatrix.getCollisionFilterMask(groupB)) == 0
                        || (SXRCollisionMatrix.getCollisionFilterGroup(groupB)
                        & mCollisionMatrix.getCollisionFilterMask(groupA)) == 0) {
                    throw Exceptions.IllegalArgument("Failed to enable collision between groups "
                                                        + groupA + " and " + groupB);
                }

                mCollisionMatrix.disableCollision(groupA, groupB);

                if ((SXRCollisionMatrix.getCollisionFilterGroup(groupA)
                        & mCollisionMatrix.getCollisionFilterMask(groupB)) != 0
                        || (SXRCollisionMatrix.getCollisionFilterGroup(groupB)
                        & mCollisionMatrix.getCollisionFilterMask(groupA)) != 0) {
                    throw Exceptions.IllegalArgument("Failed to disable collision between groups "
                                                        + groupA + " and " + groupB);
                }
            }
        }
    }

    @Test
    public void testCollisionEvent() throws Exception  {

        SXRContext context = sxrTestUtils.getSxrContext();
        SXRScene scene = sxrTestUtils.getMainScene();

        SXRSphereSceneObject sphereA = new SXRSphereSceneObject(context);
        SXRSphereSceneObject sphereB = new SXRSphereSceneObject(context);
        SXRSphereCollider colliderA = new SXRSphereCollider(context);
        SXRSphereCollider colliderB = new SXRSphereCollider(context);
        SXRRigidBody bodyA = new SXRRigidBody(context, 3.0f, 0);
        SXRRigidBody bodyB = new SXRRigidBody(context, 0.0f, 1);

        mCollisionMatrix.enableCollision(0, 1);

        CollisionHandler collisionHandler = new CollisionHandler();

        sphereA.getTransform().setPosition(0.5f, 3.0f, 0.0f);
        sphereB.getTransform().setPosition(0.0f, 0.0f, 0.0f);

        sphereA.getEventReceiver().addListener(collisionHandler);

        colliderA.setRadius(1.0f);
        colliderB.setRadius(1.0f);

        bodyA.setRestitution(1.5f);
        bodyA.setFriction(0.5f);

        bodyB.setRestitution(0.5f);
        bodyB.setFriction(0.5f);

        sphereA.attachCollider(colliderA);
        sphereB.attachCollider(colliderB);

        sphereA.attachComponent(bodyA);
        sphereB.attachComponent(bodyB);

        scene.addSceneObject(sphereA);
        scene.addSceneObject(sphereB);

        mWaiter.assertTrue(collisionHandler.waitForCollision(5 * 60 * 1000));
    }

    public class CollisionHandler implements ICollisionEvents {
        private final Object mCollisionLock;
        private boolean mCollisionEnter;

        CollisionHandler() {
            mCollisionLock = new Object();
            mCollisionEnter = false;
        }

        public void onEnter(SXRSceneObject sceneObj0, SXRSceneObject sceneObj1, float normal[], float distance) {
            synchronized (mCollisionLock) {
                mCollisionEnter = true;
                mCollisionLock.notifyAll();
            }
        }

        public void onExit(SXRSceneObject sceneObj0, SXRSceneObject sceneObj1, float normal[], float distance) {
        }

        boolean waitForCollision(long timeout) {
            synchronized (mCollisionLock) {
                try {
                    mCollisionLock.wait(timeout);
                } catch (InterruptedException e) {}
            }
            return mCollisionEnter;
        }

    }
}
