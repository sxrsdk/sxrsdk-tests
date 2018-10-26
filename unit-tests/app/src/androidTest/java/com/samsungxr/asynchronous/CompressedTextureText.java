package com.samsungxr.asynchronous;

import android.content.res.AssetManager;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.DefaultSXRTestActivity;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by j.elidelson on 8/21/2015.
 */
public class CompressedTextureText extends ActivityInstrumentationSXRf {

    public CompressedTextureText() {
        super(SXRTestActivity.class);
    }

    public void testCompressedTexture() {
        AssetManager assets = getInstrumentation().getTargetContext().getAssets();

        InputStream is = null;
        try {
            is = assets.open("tiny_texture.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[0];
        try {
            data = streamToByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer data2 = null;
        data2 = ByteBuffer.wrap(data);
        CompressedTexture compressedTexture = new CompressedTexture(1, 1024, 1024, 1024, 1, data2);
        assertNotNull(compressedTexture);
        SXRCompressedTexture compressedTexture1;
        compressedTexture1=compressedTexture.toTexture(TestDefaultSXRViewManager.mSXRContext, SXRContext.DEFAULT_PRIORITY);
        assertNotNull(compressedTexture1);
        SXRCompressedTexture compressedTexture2;
        SXRTextureParameters sxrTextureParameters = new SXRTextureParameters(TestDefaultSXRViewManager.mSXRContext);
        compressedTexture2=compressedTexture.toTexture(TestDefaultSXRViewManager.mSXRContext,SXRContext.DEFAULT_PRIORITY,sxrTextureParameters);
        assertNotNull(compressedTexture2);
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
