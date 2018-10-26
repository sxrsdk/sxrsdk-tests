package com.samsungxr.animation;

import android.util.Log;

import com.samsungxr.tests.R;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.misc.ColorShader;
import com.samsungxr.utils.UtilResource;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRSceneObject;

/**
 * Created by diego on 2/20/15.
 */
public class SXRColorAnimationTest extends ActivityInstrumentationSXRf {

    private final String TAG = SXRColorAnimationTest.class.getSimpleName();

    private static final float DEFAULT_R = 1f;
    private static final float DEFAULT_G = 1f;
    private static final float DEFAULT_B = 1f;
    private static final float ANIM_DURATION = 1.5f;

    protected SXRSceneObject init(){

        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXRRenderData rd = new SXRRenderData(TestDefaultSXRViewManager.mSXRContext);
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);

        sceneObject.attachRenderData(rd);
        sceneObject.getRenderData().setMaterial(material);

        return sceneObject;

    }

    public void testConstructorSXRColorAnimationSceneObject1(){
        Log.d(TAG, "starting testConstructorSXRColorAnimationSceneObject1");

        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRSceneObject sceneObject = init();
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, 1.0f, rgb);

        assertNotNull(colorAnimation);
    }

    public void testConstructorSXRColorAnimationSceneObject2(){
        Log.d(TAG, "starting testConstructorSXRColorAnimationSceneObject2");

        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRSceneObject sceneObject = init();
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, 1.0f, 135);

        assertNotNull(colorAnimation);
    }

    public void testConstructorMaterialRGB() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/17
    public void ignoretestConstructorNullMaterialRGB() {
        SXRMaterial material = null;
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/19
    public void ignoretestConstructorMaterialRG() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {DEFAULT_R, DEFAULT_G};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/19
    public void ignoretestConstructorMaterialR() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {DEFAULT_R};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/19
    public void ignoretestConstructorMaterialNaNRGB() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {Float.NaN, Float.NaN, Float.NaN};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/19
    public void ignoretestConstructorMaterialEmptyRGB() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/19
    public void ignoretestConstructorMaterialNullRGB() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, null);
        assertNotNull(colorAnimation);
    }

    public void testConstructorMaterialColor() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        int androidColor = android.R.color.holo_blue_dark;
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, androidColor);
        assertNotNull(colorAnimation.mMaterial);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/17
    public void ignoretestConstructorNullMaterialColor() {
        SXRMaterial material = null;
        int androidColor = android.R.color.holo_blue_dark;
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, androidColor);
        assertNotNull(colorAnimation);
    }

    public void testConstructorMaterialColorNegative() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        int androidColor = -1;
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, androidColor);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorSceneRGB() {
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorNullSceneRGB() {
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRSceneObject sceneObject = null;
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorSceneRG() {
        float[] rgb = {DEFAULT_R, DEFAULT_G};
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorSceneR() {
        float[] rgb = {DEFAULT_R};
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorSceneEmptyRGB() {
        float[] rgb = {};
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorSceneNullRGB() {
        float[] rgb = null;
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, rgb);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorSceneColor() {
        int androidColor = android.R.color.holo_blue_dark;
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, androidColor);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorNullSceneColor() {
        int androidColor = android.R.color.holo_blue_dark;
        SXRSceneObject sceneObject = null;
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, androidColor);
        assertNotNull(colorAnimation);
    }

    // FIXME https://github.com/Samsung/Gear-VR-Hybrid/issues/18
    public void ignoretestConstructorSceneColorNegative() {
        int androidColor = -1;
        SXRSceneObject sceneObject = null;
        SXRColorAnimation colorAnimation = new SXRColorAnimation(sceneObject, ANIM_DURATION, androidColor);
        assertNotNull(colorAnimation);
    }

    // TODO create test which calls animate with a duration of 0f
    public void ignoretestConstructorZeroDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
    }

    // TODO create test which calls animate with a duration of -2f
    public void ignoretestConstructorNegativeDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
    }

    // TODO create test which calls animate with a duration of NaN
    public void ignoretestConstructorNaNDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
    }

    // TODO create test which calls animate with a duration of positive infinity
    public void ignoretestConstructorPositiveInfinityDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
    }

    // TODO create test which calls animate with a duration of negative infinity
    public void ignoretestConstructorNegativeInfinityDuration() {
        ColorShader colorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, colorShader.getShaderId());
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        SXRColorAnimation colorAnimation = new SXRColorAnimation(material, ANIM_DURATION, rgb);
    }

    public void ignoreCreateObjectColorAnimationWithSceneObject() {
        SXRMesh mesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext, mesh);
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        new SXRColorAnimation(sceneObject, 1.0f, rgb);
    }

    public void ignoreCreateObjectColorAnimationWithColor() {
        SXRMesh mesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.cylinder3));
        SXRSceneObject sceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext, mesh);
        float[] rgb = {DEFAULT_R, DEFAULT_G, DEFAULT_B};
        new SXRColorAnimation(sceneObject, 1.0f, 83);
    }
}
