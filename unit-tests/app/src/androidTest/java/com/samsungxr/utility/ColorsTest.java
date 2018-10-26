package com.samsungxr.utility;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.utility.Colors;

/**
 * Created by Douglas on 2/26/15.
 */
public class ColorsTest extends ActivityInstrumentationSXRf {

    public ColorsTest() {
        super(SXRTestActivity.class);
    }

    public void testGetBytToGl() {
        Colors colors = new Colors() {
        };

        float byteToGl = Colors.byteToGl(10);
        assertNotNull(byteToGl);
    }

    public void testGetGlToByte() {

        int glToByte = Colors.glToByte(10.0f);
        assertNotNull(glToByte);
    }

    public void testGetColor() {
        float[] rgb = {0, 0, 0};
        int color = Colors.toColor(rgb);
        assertNotNull(color);
    }

    public void testToColors() {
        float[] rgb = Colors.toColors(100);
        assertNotNull(rgb);
    }
}
