package com.samsungxr.da_samsungxr;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRNotifications;
import com.samsungxr.SXRTestActivity;

/**
 * Created by j.elidelson on 9/3/2015.
 */
public class SXRNotificationsTest extends ActivityInstrumentationSXRf {
    public SXRNotificationsTest() {
        super(SXRTestActivity.class);
    }

    public void test1(){

        SXRNotifications.waitBeforeStep();
        SXRNotifications.waitAfterStep();
    }
}
