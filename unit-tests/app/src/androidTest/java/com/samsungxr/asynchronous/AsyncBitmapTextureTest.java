package com.samsungxr.asynchronous;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRBitmapTexture;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by j.elidelson on 8/21/2015.
 */
public class AsyncBitmapTextureTest extends ActivityInstrumentationSXRf {

    public AsyncBitmapTextureTest() {
        super(SXRTestActivity.class);
    }

    public void testloadTextureA() {
        SXRAndroidResource.CancelableCallback<SXRBitmapTexture> bitmapTextureCallback = new SXRAndroidResource.CancelableCallback<SXRBitmapTexture>() {
            @Override
            public void loaded(SXRBitmapTexture resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }

            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }
        };
        SXRAndroidResource sxrAndroidResource = null;
        try {
            sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "texture1.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        AsyncBitmapTexture.loadTexture(TestDefaultSXRViewManager.mSXRContext, bitmapTextureCallback, sxrAndroidResource, SXRContext.LOWEST_PRIORITY);
    }

    public void testloadTextureB() {

        SXRAndroidResource.CancelableCallback<SXRBitmapTexture> cancelableCallback = new SXRAndroidResource.CancelableCallback<SXRBitmapTexture>() {
            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }

            @Override
            public void loaded(SXRBitmapTexture resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };

        SXRAndroidResource sxrAndroidResource = null;
        try {
            sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "big_texture.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        AsyncBitmapTexture.loadTexture(TestDefaultSXRViewManager.mSXRContext, cancelableCallback, sxrAndroidResource, SXRContext.HIGHEST_PRIORITY);
    }

    public void testDecodeStream() {
        AssetManager assets = getInstrumentation().getTargetContext().getAssets();
        InputStream is = null;
        try {
            is = assets.open("sample_20140509_l.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap=null;
        bitmap=AsyncBitmapTexture.decodeStream(is,1024,1024,true,bitmap,true);
        assertNotNull(bitmap);
    }
}
