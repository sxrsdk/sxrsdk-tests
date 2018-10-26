package com.samsungxr.za_samsungxr;

import android.graphics.Bitmap;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRCubemapTexture;

/**
 * Created by j.elidelson on 6/9/2015.
 */
public class SXRCubemapTextureTest extends ActivityInstrumentationSXRf {

    public void testConstructor(){
        Bitmap bitmap1 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("posx.png");
        Bitmap bitmap2 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("negx.png");
        Bitmap bitmap3 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("posy.png");
        Bitmap bitmap4 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("negy.png");
        Bitmap bitmap5 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("posz.png");
        Bitmap bitmap6 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("negz.png");

        Bitmap[] bitmapArray = {bitmap1,bitmap2,bitmap3,bitmap4,bitmap5,bitmap6};
        String names [] ={"posx.png", "negx.png", "posy.png", "negx.png", "posz.png","negz.png"};
        SXRCubemapTexture cubemapTexture=null;
        SXRCubemapTexture.setFaceNames(names);
        cubemapTexture = new  SXRCubemapTexture(TestDefaultSXRViewManager.mSXRContext,bitmapArray);
        assertNotNull(cubemapTexture);
    }


    public void testConstructor2(){
        Bitmap bitmap1 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("posx.png");
        Bitmap bitmap2 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("negx.png");
        Bitmap bitmap3 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("posy.png");
        Bitmap bitmap4 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("negy.png");
        Bitmap bitmap5 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("posz.png");
        Bitmap bitmap6 = TestDefaultSXRViewManager.mSXRContext.loadBitmap("negz.png");

        Bitmap[] bitmapArray = {bitmap1,bitmap2,bitmap3,bitmap4,bitmap5,bitmap6};
        String names [] ={"posx.png", "negx.png", "posy.png", "negx.png", "posz.png","negz.png"};
        SXRCubemapTexture cubemapTexture=null;
        SXRCubemapTexture.setFaceNames(names);
        cubemapTexture = new  SXRCubemapTexture(TestDefaultSXRViewManager.mSXRContext,bitmapArray);
        assertNotNull(cubemapTexture);
    }


    public void testsetFaceNames(){

        String names [] ={"posx.png", "negx.png", "posy.png", "negx.png", "posz.png","negz.png","last"};
        try {
            SXRCubemapTexture.setFaceNames(names);
            fail("Should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }
}
