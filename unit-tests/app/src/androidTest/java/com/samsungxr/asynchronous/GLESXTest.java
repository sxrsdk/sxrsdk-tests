package com.samsungxr.asynchronous;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRTestActivity;

/**
 * Created by j.elidelson on 8/24/2015.
 */
public class GLESXTest extends ActivityInstrumentationSXRf {

    public GLESXTest() {
        super(SXRTestActivity.class);
    }

    public  void testGLESX(){

        assertEquals(0x93B0,GLESX.GL_COMPRESSED_RGBA_ASTC_4x4_KHR);
    }

}
