package com.samsungxr.litshader_func;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.LitshaderActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;


/**
 * Created by j.elidelson on 9/18/2015.
 */
public class LitshaderTest extends ActivityInstrumentationSXRf {

    public LitshaderTest() {
        super(SXRTestActivity.class);
    }

    public void testGetInstance() {
        Instrumentation mInstrumentation = getInstrumentation();
        // We register our interest in the activity
        Instrumentation.ActivityMonitor monitor = mInstrumentation.addMonitor(LitshaderActivity.class.getName(), null, false);
        // We launch it
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(mInstrumentation.getTargetContext(), LitshaderActivity.class);
        mInstrumentation.startActivitySync(intent);
        try {
            Thread.sleep(TestDefaultSXRViewManager.DelayTest+4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Activity currentActivity = getInstrumentation().waitForMonitor(monitor);

        assertNotNull(currentActivity);
        // We register our interest in the next activity from the sequence in this use case
        //mInstrumentation.removeMonitor(monitor);
        //monitor = mInstrumentation.addMonitor(YourNextClass.class.getName(), null, false);
    }
}
