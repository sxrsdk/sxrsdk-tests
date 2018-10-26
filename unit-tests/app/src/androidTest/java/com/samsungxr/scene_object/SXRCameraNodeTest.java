package com.samsungxr.node;

import android.hardware.Camera;

import com.samsungxr.SXRMesh;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.tests.R;
import com.samsungxr.utils.UtilResource;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.nodes.SXRCameraNode;
import com.samsungxr.ActivityInstrumentationSXRf;

/**
 * Created by m.gorll on 2/27/2015.
 */
public class SXRCameraNodeTest extends ActivityInstrumentationSXRf {

    public SXRCameraNodeTest() {
        super(SXRTestActivity.class);
    }

    public void testCreateCameraNode() {
        Camera c = Camera.open();
        try {
            SXRMesh mesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
            SXRCameraNode object = new SXRCameraNode(TestDefaultSXRViewManager.mSXRContext, mesh, c);
            TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(object);
            assertNotNull(object);
            object.pause();
            object.resume();
        } finally {
            c.release();
        }
    }

    public void testCreateCameraNode2() {
        Camera c = Camera.open();
        try {
            SXRCameraNode object = new SXRCameraNode(TestDefaultSXRViewManager.mSXRContext, 10f, 10f, c);
            TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(object);
            assertNotNull(object);

            object.pause();
            object.resume();
        } finally {
            c.release();
        }
    }

    //método onDrawFrame(float drawTime) deve ser chamado pelo framework, embora seja publico, por isso não foi testado.

}
