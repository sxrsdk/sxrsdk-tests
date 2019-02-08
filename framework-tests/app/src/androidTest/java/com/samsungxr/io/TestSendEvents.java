package com.samsungxr.io;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.samsungxr.IApplicationEvents;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRScene;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;

import net.jodah.concurrentunit.Waiter;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import net.jodah.concurrentunit.Waiter;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSendEvents {
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    volatile boolean doneProducing = false;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @After
    public void tearDown() {
        SXRScene scene = mTestUtils.getMainScene();
        if (scene != null) {
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

        SXRScene scene = mTestUtils.getMainScene();
        mWaiter.assertNotNull(scene);
    }

    @Test
    public void testSendEvents() {
        final boolean result = new TestSendEvents().test1(mTestUtils.getSxrContext());
        if (!result) {
            throw new AssertionError("test1() returned false");
        }
    }

    /**
     * Tests thread-safety of producer and consumer side. Verifies the
     * result by checking all events were consumed in the order they
     * were produced.
     */
    public boolean test1(final SXRContext context) {
        // total number of events produced controlled by these two
        final int outerLoops = 100;
        final int innerLoops = 10;

        // store the consumed events here; need for results verification
        Looper.prepare();
        final long[] recorder = new long[outerLoops*innerLoops];
        Arrays.fill(recorder, 0);

        // set up the test environment
        context.getApplication().getEventReceiver().addListener(new IApplicationEvents() {
            int index;
            @Override
            public void dispatchTouchEvent(MotionEvent ev) {
                recorder[index++] = ev.getDownTime();
            }
            public void onPause() { }
            public void onResume() { }
            public void onDestroy() { }
            public void onSetMain(SXRMain script) { }
            public void onWindowFocusChanged(boolean hasFocus) { }
            public void onConfigurationChanged(Configuration config) { }
            public void onActivityResult(int requestCode, int resultCode, Intent data) { }
            public void onTouchEvent(MotionEvent event) { }
            public void dispatchKeyEvent(KeyEvent event) { }
            public void onControllerEvent(SXRGearCursorController.CONTROLLER_KEYS[] keys, Vector3f position, Quaternionf orientation, PointF touchpadPoint, boolean touched, Vector3f angularAcceleration,
                                   Vector3f angularVelocity) { }
        });
        Looper.myLooper().quit();

        final SXRGearCursorController.SendEvents sendEvents = new SXRGearCursorController.SendEvents(context);

        final Runnable producer = new Runnable() {
            @Override
            public void run() {
                int i = 0;

                final ArrayList<KeyEvent> keyEvents = new ArrayList<>();
                final ArrayList<MotionEvent> motionEvents = new ArrayList<>();

                for (int j = 0; j < outerLoops; ++j) {
                    motionEvents.clear();

                    for (int k = 0; k < innerLoops; ++k) {
                        final MotionEvent event = MotionEvent.obtain(++i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        motionEvents.add(event);
                    }
                    // produce
                    sendEvents.init(keyEvents, motionEvents);
                }

                doneProducing = true;
            }
        };

        final CountDownLatch done = new CountDownLatch(1);
        final Runnable consumer = new Runnable() {
            @Override
            public void run() {
                while (!doneProducing) {
                    // consume
                    sendEvents.run();
                }
                done.countDown();
            }
        };

        new Thread(consumer).start();
        new Thread(producer).start();

        try {
            done.await();
        } catch (InterruptedException e) {
            return false;
        }

        // verification
        int i = 0;
        for (long el : recorder) {
            if (el != ++i) {
                return false;
            }
        }

        return true;
    }
}
