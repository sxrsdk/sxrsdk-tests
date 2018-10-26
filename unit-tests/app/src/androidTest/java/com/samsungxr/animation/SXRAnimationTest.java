package com.samsungxr.animation;


import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.CustomPostEffectShaderManager;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRHybridObject;
import com.samsungxr.SXRPostEffect;
import com.samsungxr.SXRSceneObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

//import org.junit.BeforeClass;

/**
 * Created by diego on 2/14/15.
 */
public class SXRAnimationTest extends ActivityInstrumentationSXRf {

    private static final float ANIM_DURATION = 1.5f;
    private static final float ELAPSED_TIME = 0f;
    private static final int REPEAT_COUNT = 2;
    private static final int INVALID_REPEAT_MODE = -1;

    private SXRContext mSXRContext = null;

    public SXRAnimationTest() {
        super(SXRTestActivity.class);
    }

    public void testConstructor() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };
        assertNotNull(animation);

    }

    public void testPostEffectAnimation() throws InterruptedException {
        mSXRContext = TestDefaultSXRViewManager.mSXRContext;

        final CustomPostEffectShaderManager shaderManager = makeCustomPostEffectShaderManager(mSXRContext);
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());
        SXRPostEffectAnimation animation = new SXRPostEffectAnimation(postEffect, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };
        assertNotNull(animation);
    }

    // TODO create test which calls animate and uses the null object
    public void ignoretestConstructorNullObject() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        assertNotNull(animation);
    }

     public void testSetInterpolator() {
        SXRInterpolator interpolator = new SXRInterpolator() {
            @Override
            public float mapRatio(float v) {
                return 0;
            }
        };

        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setInterpolator(interpolator);
        assertNotNull(animation);
    }

    public void testSetInterpolatorNull() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setInterpolator(null);
        assertNotNull(animation);
    }

    public void testSetOnFinish() {
        SXROnFinish onFinishAnimation = new SXROnFinish(){
            @Override
            public void finished(SXRAnimation sxrAnimation) {
            }
        };

        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setOnFinish(new SXROnRepeat() {
            @Override
            public boolean iteration(SXRAnimation animation, int count) {
                return false;
            }

            @Override
            public void finished(SXRAnimation animation) {

            }
        });
        assertNotNull(animation);
    }

    public void testSetOnFinishNull() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setOnFinish(null);
        assertNotNull(animation);
    }

    public void testSetRepeatCount() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setRepeatCount(REPEAT_COUNT);
        assertNotNull(animation);
    }

    public void testGetRepeatCountBySet() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setRepeatCount(REPEAT_COUNT);
        int repeatCount = animation.getRepeatCount();
        assertEquals(REPEAT_COUNT, repeatCount);
    }

    public void testSetRepeatModeOnce() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setRepeatMode(SXRRepeatMode.ONCE);
        assertNotNull(animation);
    }

    public void testSetRepeatModePingPong() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setRepeatMode(SXRRepeatMode.PINGPONG);
        assertNotNull(animation);
    }

    public void testSetRepeatModeRepeated() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.setRepeatMode(SXRRepeatMode.REPEATED);
        assertNotNull(animation);
    }

    public void testSetRepeatModeInvalid() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        try {
            animation.setRepeatMode(INVALID_REPEAT_MODE);
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
        }

        assertTrue(true);
    }

    public void testStart() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        animation = animation.start(animationEngine);
        assertNotNull(animation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/11
    public void ignoretestStartNull() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        animation = animation.start(null);
        assertNotNull(animation);
    }

    public void testGetDuration() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        float duration = animation.getDuration();
        assertEquals(ANIM_DURATION, duration, 0);
    }

    public void testGetElapsedTime() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        float elapsedTime = animation.getElapsedTime();
        assertEquals(ELAPSED_TIME, elapsedTime, 0);
    }

    public void testGetRepeatCount() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        int repeatCount = animation.getRepeatCount();
        assertEquals(SXRAnimation.DEFAULT_REPEAT_COUNT, repeatCount);
    }

    public void testIsFinishedFalse() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, ANIM_DURATION) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

        boolean isFinished = animation.isFinished();
        assertFalse(isFinished);
    }

    public void testIsFinishedTrue() throws InterruptedException {
        final SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);

        final CountDownLatch cdl = new CountDownLatch(1);
        final SXRAnimation animation = new SXRAnimation(sceneObject, 0f) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
                cdl.countDown();
            }
        };

        animation.start(TestDefaultSXRViewManager.mSXRContext.getAnimationEngine());
        assertTrue(cdl.await(1, TimeUnit.SECONDS));
        assertTrue(animation.isFinished());
    }

    // TODO create test which calls animate with a duration of 0f
    public void ignoretestConstructorZeroDuration() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, 0f) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };
    }

    // TODO create test which calls animate with a duration of -2f
    public void ignoretestConstructorNegativeDuration() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, -2f) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };
    }

    // TODO create test which calls animate with a duration of NaN
    public void ignoretestConstructorNaNDuration() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, Float.NaN) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };
    }

    // TODO create test which calls animate with a duration of positive infinity
    public void ignoretestConstructorPositiveInfinityDuration() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, Float.POSITIVE_INFINITY) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };
    }

    // TODO create test which calls animate with a duration of negative infinity
    public void ignoretestConstructorNegativeInfinityDuration() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, Float.NEGATIVE_INFINITY) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };
    }

    public void testcheckTarget() { //Created by Elidelson on 8/12/15.

        final Class<?>[] SUPPORTED = { SXRSceneObject.class };

        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRAnimation animation = new SXRAnimation(sceneObject, 1.0f) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };
        //Test the object target (must be SXRSceneObject)
        Class<?> type = animation.checkTarget(sceneObject, SUPPORTED);
        //Test wrong object target (should throws IllegalArgumentException)
        SXRMaterial sxrmaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        try {
            Class<?> type2 = animation.checkTarget(sxrmaterial, SUPPORTED);
            fail("should throws IllegalArgumentException");
        }
        catch (IllegalArgumentException e){}
    }

    public void testSetPosition() {
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRPositionAnimation sxrPositionAnimation = new SXRPositionAnimation(sceneObject,1.0f,1.0f,1.0f,1.0f);
        assertNotNull(sxrPositionAnimation);
        SXRPositionAnimation sxrPositionAnimation2 = new SXRPositionAnimation(sceneObject.getTransform(),1.0f,1.0f,1.0f,1.0f);
        assertNotNull(sxrPositionAnimation2);

        SXRPositionAnimation sxrPositionAnimation3 = new  SXRPositionAnimation(sceneObject,1.0f,1.0f,1.0f,1.0f) {
            @Override
            protected void animate(SXRHybridObject sxrHybridObject, float v) {
            }
        };

    }

    public void ignoretestMaterialAnimation() { //Created by Elidelson on 8/12/15.

        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial sxrmaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterialAnimation sxrMaterialAnimation = new SXRMaterialAnimation(sceneObject,1.0f) {
            @Override
            protected void animate(SXRHybridObject target, float ratio) {

            }
        };
    }

    public static CustomPostEffectShaderManager makeCustomPostEffectShaderManager(final SXRContext mSXRContext) throws InterruptedException {
        final CustomPostEffectShaderManager[] shaderManager = {null};
        final CountDownLatch cdl = new CountDownLatch(1);
        mSXRContext.runOnGlThread(new Runnable() {
            @Override
            public void run() {
                shaderManager[0] = new CustomPostEffectShaderManager(mSXRContext);
                cdl.countDown();
            }
        });
        assertTrue(cdl.await(1, TimeUnit.SECONDS));
        return shaderManager[0];
    }
}
