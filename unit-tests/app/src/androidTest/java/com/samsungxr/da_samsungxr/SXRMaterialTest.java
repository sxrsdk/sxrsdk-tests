package com.samsungxr.da_samsungxr;

import android.graphics.Color;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.misc.ColorShader;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.misc.ReflectionShader;
import com.samsungxr.ActivityInstrumentationSXRf;

import java.text.DecimalFormat;
import java.util.concurrent.Future;

/**
 * Created by santhyago on 2/27/15.
 */
public class SXRMaterialTest extends ActivityInstrumentationSXRf {
    private static final float UNPICKED_COLOR_R = 0.7f;
    private static final float UNPICKED_COLOR_G = 0.7f;
    private static final float UNPICKED_COLOR_B = 0.7f;
    private static final float UNPICKED_COLOR_A = 1.0f;

    private ColorShader mColorShader = null;
    private float mWidth = 1.0f;
    private float mHeight = 1.0f;

    public SXRMaterialTest() {
        super(SXRTestActivity.class);
    }

    public void ignoretestGetTexture() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);

        SXRTexture m360 = TestDefaultSXRViewManager.mSXRContext.loadTexture("env.jpg");
        //Testing SXRMaterial constructor using SXRContext and a Long parameter
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        material.setTexture(ReflectionShader.TEXTURE_KEY, m360);
        material.setVec4(ColorShader.COLOR_KEY,
                UNPICKED_COLOR_R,
                UNPICKED_COLOR_G,
                UNPICKED_COLOR_B,
                UNPICKED_COLOR_A);
        SXRSceneObject board = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext, mWidth, mHeight);
        board.getRenderData().setMaterial(material);

        assertEquals(m360, material.getTexture(ReflectionShader.TEXTURE_KEY));
        assertNull(material.getTexture("0"));
    }

    public void ignoretestSetColorInt() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        material.setVec4(ColorShader.COLOR_KEY,
                UNPICKED_COLOR_R,
                UNPICKED_COLOR_G,
                UNPICKED_COLOR_B,
                UNPICKED_COLOR_A);
        SXRSceneObject board = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext, mWidth, mHeight);
        board.getRenderData().setMaterial(material);

        DecimalFormat fourPlaces = new DecimalFormat("#.####");
        material.setColor(100000);

        float fourPlacesDecimal = Float.valueOf(fourPlaces.format(material.getColor()[0]));
        assertEquals(0.0039f, fourPlacesDecimal);
    }

    public void ignoreGetVec2WithAbsentKey() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        assertNull(material.getVec2("radio_r"));
    }

    public void ignoreGetVec3WithAbsentKey() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        assertNull(material.getVec3("radio_r"));
    }

    public void ignoreGetVec4WithAbsentKey() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        assertNull(material.getVec4("radio_r"));
    }

    public void testGetVec4() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        material.setVec4(ColorShader.COLOR_KEY,
                UNPICKED_COLOR_R,
                UNPICKED_COLOR_G,
                UNPICKED_COLOR_B,
                UNPICKED_COLOR_A);
        material.getVec4(ColorShader.COLOR_KEY);
    }

    public void testGetVec3() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        material.setVec3(ColorShader.COLOR_KEY,
                UNPICKED_COLOR_R,
                UNPICKED_COLOR_G,
                UNPICKED_COLOR_B);
        material.getVec3(ColorShader.COLOR_KEY);
    }

    public void testGetVec2() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        material.setVec2(ColorShader.COLOR_KEY,
                UNPICKED_COLOR_R,
                UNPICKED_COLOR_G);
        assertNotNull(material.getVec2(ColorShader.COLOR_KEY));
    }

    public void testSetMat4Material() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        material.setMat4("radio_r", 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void testSetColorMaterial() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        float[] rgb = {0.0f, 0.0f, 0.0f};
        material.setColor(rgb[0], rgb[1], rgb[0]);
        assertNotNull(material.getColor());
    }

    public void testGetRGBColor() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        float[] rgb = {0.0f, 0.0f, 0.0f};
        material.setColor(rgb[0], rgb[1], rgb[0]);
        assertNotNull(material.getRgbColor());
    }

    public void testGetOpacity() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        material.setOpacity(1.0f);
        assertEquals(material.getOpacity(), 1.0f, 0);
    }

    public void ignoretestSetMainTexture() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());
        SXRTexture m360 = TestDefaultSXRViewManager.mSXRContext.loadTexture("sea_env.jpg");
        material.setMainTexture(m360);
        assertNotNull(material.getMainTexture());
    }

    public void testGetShaderType() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());

        material.setShaderType(SXRMaterial.SXRShaderType.OESHorizontalStereo.ID);
        assertEquals(SXRMaterial.SXRShaderType.OESHorizontalStereo.ID, material.getShaderType());
    }

    public void testSetColorShaderNull() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);

        try {
            new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, null);
            fail();

        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    public void ignoretestSetContextNull() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        try {
            new SXRMaterial(null, mColorShader.getShaderId());
            fail();
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    /**
     * Created by elidelson on 9/01/15.
     */
    public void testSetColor(){

        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        sxrMaterial.setColor(Color.BLUE);
    }


    public void testSetGetTexture(){

        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.drawable.gearvr_logo));
        sxrMaterial.setTexture(ReflectionShader.TEXTURE_KEY, futureTexture);
        assertEquals(null, sxrMaterial.getTexture(ReflectionShader.TEXTURE_KEY));
    }

    public void testGetShaderType2() {
        mColorShader = new ColorShader(TestDefaultSXRViewManager.mSXRContext);
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext, mColorShader.getShaderId());

        material.setShaderType(SXRMaterial.SXRShaderType.UnlitHorizontalStereo.ID);
        assertEquals(SXRMaterial.SXRShaderType.UnlitHorizontalStereo.ID, material.getShaderType());
        material.setShaderType(SXRMaterial.SXRShaderType.UnlitVerticalStereo.ID);
        assertEquals(SXRMaterial.SXRShaderType.UnlitVerticalStereo.ID, material.getShaderType());
        material.setShaderType(SXRMaterial.SXRShaderType.OES.ID);
        assertEquals(SXRMaterial.SXRShaderType.OES.ID, material.getShaderType());
        material.setShaderType(SXRMaterial.SXRShaderType.OESHorizontalStereo.ID);
        assertEquals(SXRMaterial.SXRShaderType.OESHorizontalStereo.ID,material.getShaderType());
        material.setShaderType(SXRMaterial.SXRShaderType.OESVerticalStereo.ID);
        assertEquals(SXRMaterial.SXRShaderType.OESVerticalStereo.ID, material.getShaderType());
        material.setShaderType(SXRMaterial.SXRShaderType.Cubemap.ID);
        assertEquals(SXRMaterial.SXRShaderType.Cubemap.ID, material.getShaderType());
        material.setShaderType(SXRMaterial.SXRShaderType.CubemapReflection.ID);
        assertEquals(SXRMaterial.SXRShaderType.CubemapReflection.ID, material.getShaderType());
        material.setShaderType(SXRMaterial.SXRShaderType.Texture.ID);
        assertEquals(SXRMaterial.SXRShaderType.Texture.ID, material.getShaderType());
        material.setShaderType(SXRMaterial.SXRShaderType.ExternalRenderer.ID);
        assertEquals(SXRMaterial.SXRShaderType.ExternalRenderer.ID, material.getShaderType());
   }

    public void testMainTexture(){

        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.drawable.gearvr_logo));
        sxrMaterial.setMainTexture(futureTexture);
        //assertEquals(null,sxrMaterial.getMainTexture());
    }

    public void testAmbientColor(){

        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        sxrMaterial.setAmbientColor(1.0f, 1.0f, 1.0f, 1.0f);
        float ambientcolor[] = sxrMaterial.getAmbientColor();
        assertEquals(4,ambientcolor.length);
    }


    public void testSpecularColor(){

        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        sxrMaterial.setSpecularColor(1.0f, 1.0f, 1.0f, 1.0f);
        float specularcolor[] = sxrMaterial.getSpecularColor();
        assertEquals(4,specularcolor.length);
    }

    public void testDiffuseColorColor(){

        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        sxrMaterial.setDiffuseColor(1.0f, 1.0f, 1.0f, 1.0f);
        float specularcolor[] = sxrMaterial.getDiffuseColor();
        assertEquals(4,specularcolor.length);
    }

    public void testSpecularExponent(){

        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        sxrMaterial.setSpecularExponent(1.0f);
        float specularcolor = sxrMaterial.getSpecularExponent();
        assertEquals(1.0f,specularcolor);
    }

}
