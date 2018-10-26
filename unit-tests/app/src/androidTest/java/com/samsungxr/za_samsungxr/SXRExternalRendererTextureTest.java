package com.samsungxr.za_samsungxr;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRExternalRendererTexture;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

/**
 * Created by j.elidelson on 8/31/2015.
 */
public class SXRExternalRendererTextureTest extends ActivityInstrumentationSXRf {

    public void testConstructor(){
        SXRExternalRendererTexture sxrExternalRendererTexture = new SXRExternalRendererTexture(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(sxrExternalRendererTexture);
    }

    public void testsetgetData(){
        SXRExternalRendererTexture sxrExternalRendererTexture = new SXRExternalRendererTexture(TestDefaultSXRViewManager.mSXRContext);
        sxrExternalRendererTexture.setData(1000l);
        assertEquals(1000l,sxrExternalRendererTexture.getData());
    }
}
