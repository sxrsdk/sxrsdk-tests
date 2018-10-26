package com.samsungxr.da_samsungxr;

import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRVersion;

/**
 * Created by j.elidelson on 6/10/2015.
 */
public class SXRVersionTest extends ActivityInstrumentationSXRf {

    private SXRVersion version;
    private SXRVersion sxrVersion;

    public SXRVersionTest() {
        super(SXRTestActivity.class);
    }

    public void testConstructor(){

        this.version = new SXRVersion();
        assertNotNull(version);
    }

    public void testVersionsNumbers(){

        assertEquals("3.0.1",SXRVersion.CURRENT);
        assertEquals("1.5.0",SXRVersion.V_1_5_0);
        assertEquals("1.6.0",SXRVersion.V_1_6_0);
        assertEquals("1.6.1",SXRVersion.V_1_6_1);
        assertEquals("1.6.2",SXRVersion.V_1_6_2);
        assertEquals("1.6.3",SXRVersion.V_1_6_3);
        assertEquals("1.6.4",SXRVersion.V_1_6_4);
        assertEquals("1.6.5",SXRVersion.V_1_6_5);
        assertEquals("1.6.6",SXRVersion.V_1_6_6);
        assertEquals("1.6.7",SXRVersion.V_1_6_7);
        assertEquals("1.6.8",SXRVersion.V_1_6_8);
        assertEquals("1.6.9",SXRVersion.V_1_6_9);
        assertEquals("2.0.0",SXRVersion.V_2_0_0);
    }
}
