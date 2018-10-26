package com.samsungxr.za_samsungxr;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import com.samsungxr.tests.R;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRAndroidResource.BitmapTextureCallback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by j.elidelson on 5/15/2015.
 */
public class SXRAndroidResourceTest extends ActivityInstrumentationSXRf {

    BitmapTextureCallback test2;

    /*
    CONSTRUCTORS Tests
    */
    public void ignoretestConstructorNullResource() {

        try {
            SXRAndroidResource sxrAndroidResource;
            sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, (String) null);
            assertNotNull("Resource was null: ", sxrAndroidResource);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void testConstructorNullContext() {
        try {
            Context ctx = null;
            SXRAndroidResource sxrAndroidResource;
            sxrAndroidResource = new SXRAndroidResource(ctx, R.raw.sample_20140509_r);
            assertNotNull("Context was null: ", sxrAndroidResource);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void testConstructorStringPath() {
        String resourcePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/home.png";
        try {
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(resourcePath);
            assertNotNull("Resource is null: " + resourcePath, sxrAndroidResource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void testConstructorFile() {
        String resourcePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/home.png";
        try {
            File file = new File(resourcePath);
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(file);
            assertNotNull("Resource is null: " + resourcePath, sxrAndroidResource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void testConstructor_SXRContext_res() {
        try {
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.sample_20140509_r);
            assertNotNull("R.raw.Bitmap was not loaded: ", sxrAndroidResource);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void testConstructor_AndroidContext_res() {
        SXRAndroidResource sxrAndroidResource;
        try {
            Context applicationContext = mActivity.getApplicationContext();
            if (applicationContext != null) {
                sxrAndroidResource = new SXRAndroidResource(applicationContext, R.raw.sample_20140509_r);
                assertNotNull("R.raw.Bitmap was not loaded: ", sxrAndroidResource);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void testConstructor_SXRContext_asset() {
        AssetManager assetMgr = TestDefaultSXRViewManager.mSXRContext.getContext().getAssets();

        try {
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "coke.jpg");
            assertNotNull("R.raw.Bitmap was not loaded: ", sxrAndroidResource);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ignoretestConstructor_AndroidContext_asset() {
        SXRAndroidResource sxrAndroidResource;
        try {
            Context applicationContext = getActivity().getApplicationContext();
            if (applicationContext != null) {
                sxrAndroidResource = new SXRAndroidResource(applicationContext, R.raw.sample_20140509_r);
                assertNotNull("R.raw.Bitmap was not loaded: ", sxrAndroidResource);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /*
    METOHODS Tests
    */

    public void testGet_and_Close_Stream() {
        try {
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.test);
            InputStream inputstream = sxrAndroidResource.getStream();
            if (inputstream != null) {
                assertNotNull("GetStream method fail: ", inputstream);
                sxrAndroidResource.closeStream();
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }
/*
    SXRAndroidResource.mark & reset are private now

    public void testOthers() {
        try {
            AssetManager assets = getInstrumentation().getTargetContext().getAssets();
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.test);
            sxrAndroidResource.mark();
            sxrAndroidResource.reset();
            assertNotNull(sxrAndroidResource.toString());
            assertEquals(2131129462, sxrAndroidResource.hashCode());
            assertEquals("test.txt", sxrAndroidResource.getResourceFilename());
            SXRAndroidResource sxrAndroidResource2 = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.test);
            assertNotNull(sxrAndroidResource.equals(sxrAndroidResource));
            assertNotNull(sxrAndroidResource.equals(null));
            assertNotNull(sxrAndroidResource.equals(sxrAndroidResource2));
            String resourcePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/home.png";

            try {
                SXRAndroidResource sxrAndroidResource3 = new SXRAndroidResource(resourcePath);
                assertNotNull(sxrAndroidResource.equals(sxrAndroidResource3));
                assertNotNull(sxrAndroidResource3.getResourceFilename());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                SXRAndroidResource sxrAndroidResource4 = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "coke.jpg");
                assertNotNull(sxrAndroidResource.equals(sxrAndroidResource4));
                assertNotNull(sxrAndroidResource4.getResourceFilename());
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            SXRAndroidResource sxrAndroidResource5=null;
            assertNotNull(sxrAndroidResource5.getResourceFilename());
            sxrAndroidResource5.closeStream();
            sxrAndroidResource5.reset();

            sxrAndroidResource.closeStream();
            sxrAndroidResource.reset();

        } catch (NullPointerException e) {e.printStackTrace();}
    }
    */
}
