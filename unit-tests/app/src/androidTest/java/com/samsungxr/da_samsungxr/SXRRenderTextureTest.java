package com.samsungxr.da_samsungxr;

import com.samsungxr.SXRRenderTexture;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

/**
 * Created by santhyago on 2/27/15.
 */
public class SXRRenderTextureTest extends ActivityInstrumentationSXRf {

    public SXRRenderTextureTest() {
        super(SXRTestActivity.class);
    }

    public void testSXRRenderTextureCtx3Int() {
        SXRRenderTexture renderTex = new SXRRenderTexture(TestDefaultSXRViewManager.mSXRContext, 1, 2, 3);
        //assertEquals(0,renderTex.getUseCount()); // DEPRECATED
        assertEquals(2, renderTex.getHeight());
        assertEquals(1,renderTex.getWidth());
        SXRRenderTexture renderTex2 = new SXRRenderTexture(TestDefaultSXRViewManager.mSXRContext, 1, 2);
        assertNotNull(renderTex2);
    }
}
