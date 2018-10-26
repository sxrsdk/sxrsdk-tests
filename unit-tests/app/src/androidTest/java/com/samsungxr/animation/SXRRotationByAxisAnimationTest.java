package com.samsungxr.animation;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRHybridObject;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import com.samsungxr.SXRSceneObject;

/**
 * Created by Douglas on 2/28/15.
 */
public class SXRRotationByAxisAnimationTest extends ActivityInstrumentationSXRf {

    public void testCreateSXRRotationByAxisAnimationObject() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation(sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void testStartAnimationEngine() {
        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).start(animationEngine);
    }

    public void testFinishedAnimation() {

        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setOnFinish(new SXROnFinish() {
            @Override
            public void finished(SXRAnimation animation) {
                assertNotNull(animation);
            }
        });
    }

    public void testGetElapsedTime() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).getElapsedTime());
    }

    public void testSetInterpolator() {

        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
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
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).setRepeatMode(2);
        assertEquals(new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).getRepeatCount(), 2);
    }

    public void testGetDurationAnimation() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(new SXRRotationByAxisAnimation
                (sceneObject, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f).getDuration());
    }

}
