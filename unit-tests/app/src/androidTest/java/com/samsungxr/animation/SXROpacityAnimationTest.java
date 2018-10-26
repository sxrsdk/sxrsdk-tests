package com.samsungxr.animation;


import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.misc.ColorShader;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRNode;

/**
 * Created by diego on 2/26/15.
 */
public class SXROpacityAnimationTest extends ActivityInstrumentationSXRf {

    private final String TAG = SXROpacityAnimationTest.class.getSimpleName();

    private static final float DEFAULT_DURATION = 2.5f;
    private static final float DEFAULT_OPACITY = 1.5f;
    private static final float DEFAULT_NEGATIVE = -1.5f;

    public void testConstructorSXROpacityAnimationNode(){
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXRRenderData rd = new SXRRenderData(TestDefaultSXRViewManager.mSXRContext);
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);

        sceneObject.attachRenderData(rd);
        sceneObject.getRenderData().setMaterial(material);
        SXROpacityAnimation opacityAnimation = new SXROpacityAnimation(sceneObject, DEFAULT_DURATION, DEFAULT_OPACITY);

        assertNotNull(opacityAnimation);
    }

    public void testConstructorMaterialDurationOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
        assertNotNull(opacity);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/20
    public void ignoretestConstructorNullMaterialDurationOpacity() {
        SXRMaterial material = null;
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialNegativeDurationOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_NEGATIVE, DEFAULT_OPACITY);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialDurationNegativeOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_NEGATIVE);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialNegativeDurationNegativeOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_NEGATIVE, DEFAULT_NEGATIVE);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialNaNDurationOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, Float.NaN, DEFAULT_OPACITY);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialDurationNaNOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, Float.NaN);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialNaNDurationNaNOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, Float.NaN, Float.NaN);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialPositiveInfintyDurationOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, Float.POSITIVE_INFINITY, DEFAULT_OPACITY);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialDurationPositiveInfintyOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, Float.POSITIVE_INFINITY);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialPositiveInfintyDurationPositiveInfintyOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialNegativeInfintyDurationOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, Float.NEGATIVE_INFINITY, DEFAULT_OPACITY);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialDurationNegativeInfintyOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, Float.NEGATIVE_INFINITY);
        assertNotNull(opacity);
    }

    public void testConstructorMaterialNegativeInfintyDurationNegativeInfintyOpacity() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        assertNotNull(opacity);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/21
    public void ignoretestConstructorNodeDurationOpacity() {
        SXRNode sceneObject = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        SXROpacityAnimation opacity = new SXROpacityAnimation(sceneObject, DEFAULT_DURATION, DEFAULT_OPACITY);
        assertNotNull(opacity);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/21
    public void ignoretestConstructorNullNodeDurationOpacity() {
        SXRNode sceneObject = null;
        SXROpacityAnimation opacity = new SXROpacityAnimation(sceneObject, DEFAULT_DURATION, DEFAULT_OPACITY);
        assertNotNull(opacity);
    }

    // TODO create test which calls animate with a duration of 0f
    public void ignoretestConstructorZeroDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
    }

    // TODO create test which calls animate with a duration of -2f
    public void ignoretestConstructorNegativeDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
    }

    // TODO create test which calls animate with a duration of NaN
    public void ignoretestConstructorNaNDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
    }

    // TODO create test which calls animate with a duration of positive infinity
    public void ignoretestConstructorPositiveInfinityDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
    }

    // TODO create test which calls animate with a duration of negative infinity
    public void ignoretestConstructorNegativeInfinityDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
    }

    public void testRepeatOpacityAnimationValid() {

        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
        opacity.setOnFinish(new SXROnFinish() {
            @Override
            public void finished(SXRAnimation animation) {
                assertNotNull(animation);
            }
        });
    }

    public void testSetInterpolatorValid() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
        opacity.setInterpolator(new SXRInterpolator() {
            @Override
            public float mapRatio(float ratio) {

                if (ratio != 0)
                    assertTrue(true);

                return 0;
            }
        });
    }

    public void testSetReapModeValid() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXROpacityAnimation opacity = new SXROpacityAnimation(material, DEFAULT_DURATION, DEFAULT_OPACITY);
        opacity.setRepeatMode(2);
    }
}
