package com.samsungxr.za_samsungxr;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.samsungxr.DefaultSXRTestActivity;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.SXRBitmapTexture;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by santhyago on 2/24/15.
 */
public class SXRBitmapTextureTest extends ActivityInstrumentationSXRf {
    SXRBitmapTexture bitmapTexture;

    private DefaultSXRTestActivity defaultSXRTestActivity;
    public SXRBitmapTextureTest() {
        super(DefaultSXRTestActivity.class);
    }

    public void testConstructor() {
        AssetManager assets = getInstrumentation().getTargetContext().getAssets();
        InputStream is = null;
        try {
            is = assets.open("coke.jpg");
        } catch (FileNotFoundException e) {
            fail("File not found.");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext, bitmap);
        } catch (NullPointerException e) {
        }
        assertNotNull(bitmapTexture);
    }

     /**
     * Tests constructor with a null SXRContext
     */
    public void testConstructorNullContext() {
        AssetManager assets = getInstrumentation().getTargetContext().getAssets();
        InputStream is = null;
        try {
            is = assets.open("coke.jpg");
        } catch (FileNotFoundException e) {
            fail("File not found.");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        try {
            bitmapTexture = new SXRBitmapTexture(null, bitmap);
            fail("Constructor allows null SXRContext object.");
        } catch (NullPointerException e) {
        }
    }

    public void testConstructorBigBitmap() {
        AssetManager assets = getInstrumentation().getTargetContext().getAssets();
        InputStream is = null;
        try {
            is = assets.open("big_texture.jpg");
        } catch (FileNotFoundException e) {
            fail("File not found.");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        try{
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext, bitmap);
            //fail("Should throws error.");
        } catch (OutOfMemoryError e) {
        }
    }

    public void testConstructorSXRTextureParameters() {
        AssetManager assets = getInstrumentation().getTargetContext().getAssets();
        InputStream is = null;
        try {
            is = assets.open("coke.jpg");
        } catch (FileNotFoundException e) {
            fail("File not found.");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        SXRTextureParameters sxrTextureParameters = new SXRTextureParameters(TestDefaultSXRViewManager.mSXRContext);
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext, bitmap,sxrTextureParameters);
        } catch (NullPointerException e) {}
        assertNotNull(bitmapTexture);
    }

    public void ignoretestConstructorpngAssetFilename() {
        String pngAssetFilename = "frigate.png";
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,pngAssetFilename);
        } catch (NullPointerException e) {
        }
        assertNotNull(bitmapTexture);
    }


    public void testConstructorSXRTextureParametersAssetFilename() {
        String pngAssetFilename = "frigate.png";
        SXRTextureParameters sxrTextureParameters = new SXRTextureParameters(TestDefaultSXRViewManager.mSXRContext);
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext, pngAssetFilename,sxrTextureParameters);
        } catch (NullPointerException e) { }
        assertNotNull(bitmapTexture);
    }


    public void testConstructorGrayScaleData() {
        byte grayScaleData[]={100,100,100,100};
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,0,4,grayScaleData);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,4,0,grayScaleData);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,2,2,null);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,4,4,grayScaleData);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,2,2,grayScaleData);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}

        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,2,2,grayScaleData);
            bitmapTexture.update(2,2,grayScaleData);
            ;
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
    }


    public void testConstructorGrayScaleDataSXRTextureParameters() {
        byte grayScaleData[]={100,100,100,100};
        SXRTextureParameters sxrTextureParameters = new SXRTextureParameters(TestDefaultSXRViewManager.mSXRContext);
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,0,4,grayScaleData,sxrTextureParameters);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,4,0,grayScaleData,sxrTextureParameters);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,2,2,null,sxrTextureParameters);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,4,4,grayScaleData,sxrTextureParameters);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}
        try {
            bitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,2,2,grayScaleData,sxrTextureParameters);
            //fail("Should throws IllegalArgumentException.");
        }catch (IllegalArgumentException e){}

    }


    public void ignoretestConstructorSXRTextureParameters2() { //by Elidelson Carvalho on 10/31/2015
        SXRTextureParameters sxrTextureParameters = new SXRTextureParameters(TestDefaultSXRViewManager.mSXRContext);

        sxrTextureParameters.setMinFilterType(SXRTextureParameters.TextureFilterType.GL_LINEAR);
        assertEquals(SXRTextureParameters.TextureFilterType.GL_LINEAR,sxrTextureParameters.getMinFilterType());

        sxrTextureParameters.setMagFilterType(SXRTextureParameters.TextureFilterType.GL_LINEAR);
        assertEquals(SXRTextureParameters.TextureFilterType.GL_LINEAR, sxrTextureParameters.getMagFilterType());

        sxrTextureParameters.setWrapSType(SXRTextureParameters.TextureWrapType.GL_CLAMP_TO_EDGE);
        assertEquals(SXRTextureParameters.TextureWrapType.GL_CLAMP_TO_EDGE, sxrTextureParameters.getWrapSType());

        sxrTextureParameters.setWrapTType(SXRTextureParameters.TextureWrapType.GL_CLAMP_TO_EDGE);
        assertEquals(SXRTextureParameters.TextureWrapType.GL_CLAMP_TO_EDGE,sxrTextureParameters.getWrapTType());

        assertEquals(false,sxrTextureParameters.isAnisotropicSupported());

        //sxrTextureParameters.setAnisotropicValue(16);
        //assertEquals(16,sxrTextureParameters.getAnisotropicValue());

        sxrTextureParameters.getMaxAnisotropicValue();

        assertNotNull(sxrTextureParameters.getDefalutValuesArray());
    }

}
