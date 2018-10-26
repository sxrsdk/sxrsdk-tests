package com.samsungxr.asynchronous;

import android.content.res.AssetManager;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by j.elidelson on 8/24/2015.
 */
public class SXRCompressedTextureTest extends ActivityInstrumentationSXRf {

    public SXRCompressedTextureTest() {
        super(SXRTestActivity.class);
    }

    public void testSXRCompressedTextureConstructor(){

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

        SXRCompressedTexture sxrCompressedTexture = new SXRCompressedTexture(TestDefaultSXRViewManager.mSXRContext,1,16,16,144,data,1,1,SXRCompressedTexture.DEFAULT_QUALITY);
        assertNotNull(sxrCompressedTexture);
    }

    public void testSXRCompressedTextureConstructor2(){

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

        SXRTextureParameters sxrTextureParameters = new SXRTextureParameters(TestDefaultSXRViewManager.mSXRContext);
        SXRCompressedTexture sxrCompressedTexture = new SXRCompressedTexture(TestDefaultSXRViewManager.mSXRContext,1,16,16,144,data,1,1,SXRCompressedTexture.DEFAULT_QUALITY,sxrTextureParameters);
        assertNotNull(sxrCompressedTexture);
    }


    public void testSXRCompressedTextureConstructor3(){

        SXRCompressedTexture sxrCompressedTexture = new SXRCompressedTexture(TestDefaultSXRViewManager.mSXRContext,SXRCompressedTexture.GL_TARGET,1,SXRCompressedTexture.DEFAULT_QUALITY);
        assertNotNull(sxrCompressedTexture);
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
