package com.samsungxr.asynchronous;

import android.content.res.AssetManager;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.utility.RuntimeAssertion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by j.elidelson on 8/24/2015.
 */
public class EricssonTextureCompression2Test extends ActivityInstrumentationSXRf {

    public EricssonTextureCompression2Test() {
        super(SXRTestActivity.class);
    }

    public void testHeaderLength(){

        EricssonTextureCompression2 ericssonTextureCompression2 = new EricssonTextureCompression2();
        assertEquals(16, ericssonTextureCompression2.headerLength());
    }

    public void testSniff1() {
        EricssonTextureCompression2 ericssonTextureCompression2 = new EricssonTextureCompression2();

        AssetManager assets = getInstrumentation().getTargetContext().getAssets();

        InputStream is = null;
        try {
            is = assets.open("tiny_texture.ktx");
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[0];
        try {
            data = streamToByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SXRCompressedTextureLoader.Reader reader = new SXRCompressedTextureLoader.Reader(data);
        assertEquals(false, ericssonTextureCompression2.sniff(data, reader));
    }

    public void testSniff2() {
        EricssonTextureCompression2 ericssonTextureCompression2 = new EricssonTextureCompression2();

        AssetManager assets = getInstrumentation().getTargetContext().getAssets();

        InputStream is = null;
        try {
            is = assets.open("tiny_texture.pkm");
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[0];
        try {
            data = streamToByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SXRCompressedTextureLoader.Reader reader = new SXRCompressedTextureLoader.Reader(data);
        assertEquals(true,ericssonTextureCompression2.sniff(data, reader));
    }

    public void testParse() {
        EricssonTextureCompression2 ericssonTextureCompression2 = new EricssonTextureCompression2();

        AssetManager assets = getInstrumentation().getTargetContext().getAssets();

        InputStream is = null;
        try {
            is = assets.open("tiny_texture.pkm");
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[0];
        try {
            data = streamToByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SXRCompressedTextureLoader.Reader reader = new SXRCompressedTextureLoader.Reader(data);
        CompressedTexture compressedTexture = ericssonTextureCompression2.parse(data,reader);
        assertNotNull(compressedTexture);
    }

    public void testParse2() {
        EricssonTextureCompression2 ericssonTextureCompression2 = new EricssonTextureCompression2();

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
        SXRCompressedTextureLoader.Reader reader = new SXRCompressedTextureLoader.Reader(data);
        try {
            CompressedTexture compressedTexture = ericssonTextureCompression2.parse(data, reader);
            fail("should throws RuntimeAssertion: wrong file format");
        }catch (RuntimeAssertion e){}
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
