package com.samsungxr.asynchronous;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRHybridObject;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.tests.R;
import com.samsungxr.utility.MarkingFileInputStream;
import com.samsungxr.utility.ResourceCache;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by j.elidelson on 8/19/2015.
 */
public class SXRAsynchronousResourceLoaderTest extends ActivityInstrumentationSXRf {

    public SXRAsynchronousResourceLoaderTest() {
        super(SXRTestActivity.class);
    }

   public void testSXRAsynchronousResourceLoader(){
        SXRAsynchronousResourceLoader sxrAsynchronousResourceLoader = new SXRAsynchronousResourceLoader();
        assertNotNull(sxrAsynchronousResourceLoader);
   }



    public void testSXRCompressedTexture() {

        SXRCompressedTexture sxrCompressedTexture = new SXRCompressedTexture(TestDefaultSXRViewManager.mSXRContext,1,1,1);
        assertNotNull(sxrCompressedTexture);


        AssetManager assets = getInstrumentation().getTargetContext().getAssets();

        InputStream is = null;
        try {
            is = assets.open("tiny_texture.astc");
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[0];
        try {
            data = streamToByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sxrCompressedTexture = new SXRCompressedTexture(TestDefaultSXRViewManager.mSXRContext,1,2,3,6,data,1,1,1);
        assertNotNull(sxrCompressedTexture);

        SXRTextureParameters sxrTextureParameters = new SXRTextureParameters(TestDefaultSXRViewManager.mSXRContext);
        sxrCompressedTexture = new SXRCompressedTexture(TestDefaultSXRViewManager.mSXRContext,1,2,3,6,data,1,1,1,sxrTextureParameters);
        assertNotNull(sxrCompressedTexture);
    }

    public void testSXRAsynchronousResourceLoaderloadCompressedTexture(){

        SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.coke);
        ResourceCache<SXRTexture> textureCache = new ResourceCache<SXRTexture>();
        SXRAndroidResource.CompressedTextureCallback compressedTextureCallback = new SXRAndroidResource.CompressedTextureCallback() {
            @Override
            public void loaded(SXRTexture resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };

        SXRAsynchronousResourceLoader.loadCompressedTexture(TestDefaultSXRViewManager.mSXRContext,textureCache,compressedTextureCallback,sxrAndroidResource);
        SXRAsynchronousResourceLoader.loadCompressedTexture(TestDefaultSXRViewManager.mSXRContext,textureCache,compressedTextureCallback,sxrAndroidResource,SXRCompressedTexture.BALANCED);
    }

    public void testSXRAsynchronousResourceLoaderloadBitmapTexture(){

        SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.sample_20140509_r);
        ResourceCache<SXRTexture> textureCache = new ResourceCache<SXRTexture>();
        SXRAndroidResource.BitmapTextureCallback bitmapTextureCallback = new SXRAndroidResource.BitmapTextureCallback() {
            @Override
            public void loaded(SXRTexture resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }

            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }
        };
        SXRAsynchronousResourceLoader.loadBitmapTexture(TestDefaultSXRViewManager.mSXRContext,textureCache,bitmapTextureCallback,sxrAndroidResource,SXRCompressedTexture.BALANCED);
        try {//cannot load the same SXRAndroidResource more than once
            SXRAsynchronousResourceLoader.loadBitmapTexture(TestDefaultSXRViewManager.mSXRContext, textureCache, bitmapTextureCallback, sxrAndroidResource, -1);
        }catch (IllegalArgumentException e){}
        SXRAndroidResource sxrAndroidResource1 = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.sample_20140509_r);
        try {//SXRAndroidResource cannot be loaded twice
            SXRAsynchronousResourceLoader.loadBitmapTexture(TestDefaultSXRViewManager.mSXRContext, textureCache, bitmapTextureCallback, sxrAndroidResource1, SXRContext.LOWEST_PRIORITY - 1);
        }catch (IllegalArgumentException e){}
        try {//priority must be >= SXRContext.LOWEST_PRIORITY and <= SXRContext.HIGHEST_PRIORITY
            SXRAsynchronousResourceLoader.loadBitmapTexture(TestDefaultSXRViewManager.mSXRContext, textureCache, bitmapTextureCallback, sxrAndroidResource1, SXRContext.HIGHEST_PRIORITY+1);
        }catch (IllegalArgumentException e){}

    }

    public void testSXRAsynchronousResourceLoaderloadFutureCubemapTexture(){

        Map<String, Integer> m = new HashMap<String, Integer>();
        Integer key1   = new Integer(123);
        String  value1 = "value 1";
        m.put(value1,key1);
        SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.sample_20140509_r);
        ResourceCache<SXRTexture> textureCache = new ResourceCache<SXRTexture>();
        SXRAsynchronousResourceLoader.loadFutureCubemapTexture(TestDefaultSXRViewManager.mSXRContext,textureCache,sxrAndroidResource,SXRCompressedTexture.BALANCED,m);
    }

    public void testSXRAsynchronousResourceLoaderloadFutureMesh(){

        SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.sample_20140509_r);
        SXRAsynchronousResourceLoader.loadFutureMesh(TestDefaultSXRViewManager.mSXRContext, sxrAndroidResource, SXRCompressedTexture.BALANCED);
    }

    public void ignoretestSXRAsynchronousResourceLoaderloadMesh(){

        SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.drawable.cylinder2);
        SXRAndroidResource.MeshCallback meshCallback = new SXRAndroidResource.MeshCallback() {
            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }

            @Override
            public void loaded(SXRMesh resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };
        SXRAsynchronousResourceLoader.loadMesh(TestDefaultSXRViewManager.mSXRContext, meshCallback, sxrAndroidResource, SXRCompressedTexture.BALANCED);
    }

    public void ignoretestSXRAsynchronousResourceLoader2(){

        SXRAsynchronousResourceLoader sxrAsynchronousResourceLoader = new SXRAsynchronousResourceLoader();

        AssetManager assets = getInstrumentation().getTargetContext().getAssets();

        InputStream is = null;
        try {
            is = assets.open("sample_20140509_l.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }

        MarkingFileInputStream markingFileInputStreamTest=null;
        String resourcePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/home.png";
        try {
            markingFileInputStreamTest = new MarkingFileInputStream(resourcePath);
        }catch (FileNotFoundException e){}

        Bitmap bitmap = SXRAsynchronousResourceLoader.decodeStream(is, true);
        assertNotNull(bitmap);
        Bitmap bitmap1 = SXRAsynchronousResourceLoader.decodeStream(markingFileInputStreamTest, true);
        assertNotNull(bitmap1);
    }


    public void testLoadTexture(){
        SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.sample_20140509_r);
        ResourceCache<SXRTexture> textureCache = new ResourceCache<SXRTexture>();
        SXRAndroidResource.CancelableCallback cancelableCallback = new SXRAndroidResource.CancelableCallback() {
            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }

            @Override
            public void loaded(SXRHybridObject resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };

        SXRAsynchronousResourceLoader.loadTexture(TestDefaultSXRViewManager.mSXRContext, textureCache, cancelableCallback, sxrAndroidResource, SXRContext.HIGHEST_PRIORITY, 1);

    }

    public void testLoadFutureTexture(){
        SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.raw.sample_20140509_r);
        ResourceCache<SXRTexture> textureCache = new ResourceCache<SXRTexture>();
        SXRAndroidResource.CancelableCallback cancelableCallback = new SXRAndroidResource.CancelableCallback() {
            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }

            @Override
            public void loaded(SXRHybridObject resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };

        SXRAsynchronousResourceLoader.loadFutureTexture(TestDefaultSXRViewManager.mSXRContext, textureCache, sxrAndroidResource, SXRContext.HIGHEST_PRIORITY, 1);

    }


    public static byte[] streamToByteArray(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int line = 0;
        // read bytes from stream, and store them in buffer
        while ((line = stream.read(buffer)) != -1) {
            // Writes bytes from byte array (buffer) into output stream.
            os.write(buffer, 0, line);
        }
        stream.close();
        os.flush();
        os.close();
        return os.toByteArray();
    }
}
