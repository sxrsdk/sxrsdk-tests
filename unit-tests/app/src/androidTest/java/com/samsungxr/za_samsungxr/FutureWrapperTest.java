package com.samsungxr.za_samsungxr;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.FutureWrapper;
import com.samsungxr.SXRBitmapTexture;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by j.elidelson on 8/26/2015.
 */
public class FutureWrapperTest extends ActivityInstrumentationSXRf {

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
        SXRBitmapTexture sxrBitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,bitmap);
        FutureWrapper futureWrapper = new FutureWrapper(sxrBitmapTexture);
        assertNotNull(futureWrapper);
    }

    public void testGetBooleans() {
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
        SXRBitmapTexture sxrBitmapTexture = new SXRBitmapTexture(TestDefaultSXRViewManager.mSXRContext,bitmap);
        FutureWrapper futureWrapper = new FutureWrapper(sxrBitmapTexture);
        assertNotNull(futureWrapper);
        try {
            futureWrapper.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            futureWrapper.get(50L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        assertNotNull(futureWrapper.cancel(true));
        assertNotNull(futureWrapper.isCancelled());
        assertNotNull(futureWrapper.isDone());
    }
}
