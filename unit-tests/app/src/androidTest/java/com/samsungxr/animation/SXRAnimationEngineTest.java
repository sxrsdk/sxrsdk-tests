package com.samsungxr.animation;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import com.samsungxr.SXRHybridObject;
import com.samsungxr.SXRSceneObject;

/**
 * Created by diego on 2/20/15.
 */
public class SXRAnimationEngineTest extends ActivityInstrumentationSXRf {

    private static final float ANIM_DURATION = 1.5f;

    public SXRAnimationEngineTest() {super(SXRTestActivity.class);}

    public void testGetInstance() {
        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        assertNotNull(animationEngine);
    }

    // TODO make a test that calls animate() to show start() working
    public void testStart() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {}
        };

        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        animation = animationEngine.start(animation);
        animation.setRepeatCount(5);
        animation.setRepeatMode(SXRRepeatMode.PINGPONG);
        animation.setRepeatCount(0);
        animation.setRepeatMode(SXRRepeatMode.ONCE);
        animation.setRepeatCount(3);






        animation.setRepeatMode(SXRRepeatMode.REPEATED);

        assertNotNull(animationEngine);
    }

    // TODO make a test that calls animate() to show start() working with null
    public void ignoretestStartNull() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {}
        };

        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        animation = animationEngine.start(null);
        assertNotNull(animationEngine);
    }

    // TODO make a test that shows start() and stop() working by checking animation state
    public void ignoretestStartStopAnimation() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {}
        };

        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        animationEngine.stop(null);
        assertNotNull(animationEngine);
    }

    public void testStopAnimation() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {}
        };

        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        animationEngine.stop(animation);
        assertNotNull(animationEngine);
    }

    public void testStopNull() {
        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        animationEngine.stop(null);
        assertNotNull(animationEngine);
    }
}
