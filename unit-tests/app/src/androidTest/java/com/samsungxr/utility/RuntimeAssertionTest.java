package com.samsungxr.utility;

import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRFutureOnGlThread;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.R;
import com.samsungxr.animation.SXRAnimationEngine;
import com.samsungxr.animation.SXRRepeatMode;
import com.samsungxr.animation.SXRScaleAnimation;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.utility.RuntimeAssertion;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import java.util.concurrent.Future;

/**
 * Created by j.elidelson on 6/9/2015.
 */
public class RuntimeAssertionTest extends ActivityInstrumentationSXRf {

    public RuntimeAssertionTest() {
        super(SXRTestActivity.class);
    }

    public void testConstructorA(){
        RuntimeAssertion runtimeAssertionTest = new RuntimeAssertion("test");
        assertNotNull(runtimeAssertionTest);
    }

    public void testConstructorAEmptyparam(){
        RuntimeAssertion runtimeAssertionTest = new RuntimeAssertion("");
        assertNotNull(runtimeAssertionTest);
    }

    public void testConstructorANullparam(){
        RuntimeAssertion runtimeAssertionTest = new RuntimeAssertion(null);
        assertNotNull(runtimeAssertionTest);
    }

    public void testConstructorB(){
        float a=1.0f;
        RuntimeAssertion runtimeAssertionTest = new RuntimeAssertion("%f",a);
        assertNotNull(runtimeAssertionTest);
    }

    public void testConstructorBEmptyparam(){
        float a=1.0f;
        RuntimeAssertion runtimeAssertionTest = new RuntimeAssertion("",a);
        assertNotNull(runtimeAssertionTest);
    }

    public void testConstructorBNullparam(){
        float a=1.0f;
        RuntimeAssertion runtimeAssertionTest = new RuntimeAssertion("%f", (Object) null);
        assertNotNull(runtimeAssertionTest);
    }
}
