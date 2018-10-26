package com.samsungxr.scene_object;

import android.hardware.Camera;

import com.samsungxr.SXRMesh;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.tests.R;
import com.samsungxr.utils.UtilResource;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.scene_objects.SXRCameraSceneObject;
import com.samsungxr.ActivityInstrumentationSXRf;

/**
 * Created by m.gorll on 2/27/2015.
 */
public class SXRCameraSceneObjectTest extends ActivityInstrumentationSXRf {

    public SXRCameraSceneObjectTest() {
        super(SXRTestActivity.class);
    }

    public void testCreateCameraSceneObject() {
        Camera c = Camera.open();
        try {
            SXRMesh mesh = TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(UtilResource.androidResource(TestDefaultSXRViewManager.mSXRContext, R.raw.cylinder3));
            SXRCameraSceneObject object = new SXRCameraSceneObject(TestDefaultSXRViewManager.mSXRContext, mesh, c);
            TestDefaultSXRViewManager.mSXRContext.getMainScene().addSceneObject(object);
            assertNotNull(object);
            object.pause();
            object.resume();
        } finally {
            c.release();
        }
    }

    public void testCreateCameraSceneObject2() {
        Camera c = Camera.open();
        try {
            SXRCameraSceneObject object = new SXRCameraSceneObject(TestDefaultSXRViewManager.mSXRContext, 10f, 10f, c);
            TestDefaultSXRViewManager.mSXRContext.getMainScene().addSceneObject(object);
            assertNotNull(object);

            object.pause();
            object.resume();
        } finally {
            c.release();
        }
    }

    //método onDrawFrame(float drawTime) deve ser chamado pelo framework, embora seja publico, por isso não foi testado.

}
