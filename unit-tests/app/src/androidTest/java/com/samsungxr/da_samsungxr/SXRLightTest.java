package com.samsungxr.da_samsungxr;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRLight;
import com.samsungxr.SXRPointLight;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

/**
 * Created by j.elidelson on 9/1/2015.
 */
public class SXRLightTest extends ActivityInstrumentationSXRf {

    public SXRLightTest() {
        super(SXRTestActivity.class);
    }

    public void testConstructor(){
        assertNotNull(TestDefaultSXRViewManager.mSXRContext);
        SXRDirectLight sxrLight = new SXRDirectLight(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(sxrLight);
    }

    public void testSetGetPosition(){
        SXRPointLight sxrLight = new SXRPointLight(TestDefaultSXRViewManager.mSXRContext);
        sxrLight.setPosition(1.0f,1.0f,1.0f);
        float pos[] = sxrLight.getPosition();
        assertEquals(3,pos.length);
        assertNotNull(sxrLight);
    }

    public void testSetDisableEnable(){
        SXRDirectLight sxrLight = new SXRDirectLight(TestDefaultSXRViewManager.mSXRContext);
        sxrLight.disable();
        assertEquals(false, sxrLight.isEnabled());
        sxrLight.enable();
        assertEquals(true, sxrLight.isEnabled());
    }

    /*
    //TODO native crash due to point light not having intensity
    public void testIntensity(){
        SXRPointLight sxrLight = new SXRPointLight(TestDefaultSXRViewManager.mSXRContext);
        float aux1[] = sxrLight.getAmbientIntensity();
        assertEquals(4, aux1.length);
        float aux2[] = sxrLight.getDiffuseIntensity();
        assertEquals(4,aux2.length);
        float aux3[] = sxrLight.getSpecularIntensity();
        assertEquals(4,aux3.length);
    }
    */

}

