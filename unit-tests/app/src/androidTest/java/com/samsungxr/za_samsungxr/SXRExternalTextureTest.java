package com.samsungxr.za_samsungxr;

import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRExternalTexture;

/**
 * Created by j.elidelson on 5/20/2015.
 */
public class SXRExternalTextureTest extends ActivityInstrumentationSXRf {

    public void testExternalTextureConstructor () {
        try {
            SXRExternalTexture sxrExternalTexture;
            sxrExternalTexture = new SXRExternalTexture(TestDefaultSXRViewManager.mSXRContext);
            assertNotNull("Resource was null: ", sxrExternalTexture);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void testExternalTextureNullConstructor() {
        try {
            SXRExternalTexture sxrExternalTexture;
            sxrExternalTexture = new SXRExternalTexture(null);
            //assertNull("Constructor parameter was null: ", sxrExternalTexture);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //Methods are tested in SXRTexture and SXRHybridObject
}
