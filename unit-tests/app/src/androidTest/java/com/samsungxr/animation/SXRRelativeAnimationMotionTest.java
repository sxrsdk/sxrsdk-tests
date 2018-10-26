package com.samsungxr.animation;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import com.samsungxr.SXRNode;

/**
 * Created by Douglas on 2/28/15.
 */
public class SXRRelativeAnimationMotionTest extends ActivityInstrumentationSXRf {


    public void testSetInvalidRepeatModeAnimation() {

        try {
            SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
            new SXRRotationByAxisAnimation
                    (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setRepeatMode(2);

            SXRRelativeMotionAnimation animation = new SXRRelativeMotionAnimation(sceneObject,
                    1.0f, 1.0f, 1.0f, 1.0f);
            animation.setRepeatMode(4);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "4 is not a valid repetition type");
        }
    }

    public void testInterpolatorAnimation() {

        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setRepeatMode(2);

        SXRRelativeMotionAnimation animation = new SXRRelativeMotionAnimation(sceneObject,
                1.0f, 1.0f, 1.0f, 1.0f);
        animation.setInterpolator(new SXRInterpolator() {
            @Override
            public float mapRatio(float ratio) {

                assertNotNull(ratio);

                return 0;
            }
        });
    }

    public void testSetFinishedObject() {
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setRepeatMode(2);

        SXRRelativeMotionAnimation animation = new SXRRelativeMotionAnimation(sceneObject,
                1.0f, 1.0f, 1.0f, 1.0f);
        animation.setOnFinish(new SXROnFinish() {
            @Override
            public void finished(SXRAnimation animation) {
                assertNotNull(animation);
            }
        });
    }

    public void testSetRepeatCount() {
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setRepeatMode(2);

        SXRRelativeMotionAnimation animation = new SXRRelativeMotionAnimation(sceneObject,
                1.0f, 1.0f, 1.0f, 1.0f);
        animation.setRepeatCount(10);
    }
}
