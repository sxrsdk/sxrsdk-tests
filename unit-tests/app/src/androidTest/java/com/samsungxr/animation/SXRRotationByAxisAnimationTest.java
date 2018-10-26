package com.samsungxr.animation;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRHybridObject;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import com.samsungxr.SXRNode;

/**
 * Created by Douglas on 2/28/15.
 */
public class SXRRotationByAxisAnimationTest extends ActivityInstrumentationSXRf {

    public void testCreateSXRRotationByAxisAnimationObject() {
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation(sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void testStartAnimationEngine() {
        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).start(animationEngine);
    }

    public void testFinishedAnimation() {

        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setOnFinish(new SXROnFinish() {
            @Override
            public void finished(SXRAnimation animation) {
                assertNotNull(animation);
            }
        });
    }

    public void testGetElapsedTime() {
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).getElapsedTime());
    }

    public void testSetInterpolator() {

        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setInterpolator(new SXRInterpolator() {
            @Override
            public float mapRatio(float ratio) {

                assertNotNull(ratio);
                return 0;
            }
        });
    }

    public void testRepeatMode() {
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setRepeatMode(2);
        assertEquals(new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).getRepeatCount(), 2);
    }

    public void testGetDurationAnimation() {
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).getDuration());
    }

}
