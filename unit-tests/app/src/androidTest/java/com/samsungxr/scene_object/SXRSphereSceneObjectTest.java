package com.samsungxr.scene_object;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.scene_objects.SXRSphereSceneObject;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import java.util.concurrent.Future;

/**
 * Created by j.elidelson on 5/29/2015.
 */
public class SXRSphereSceneObjectTest extends ActivityInstrumentationSXRf {

    public SXRSphereSceneObjectTest() {
        super(SXRTestActivity.class);
    }

    //**************
    //*** Sphere ***
    //**************
    public void testConeConstructor() {

        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(sphereSceneObject);
    }

    public void testConeConstructorFacingout() {

        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,true);
        assertNotNull(sphereSceneObject);
        sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,false);
        assertNotNull(sphereSceneObject);
    }

    public void testConeConstructorFutureTexture() {
        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.drawable.bottom));
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,true,futureTexture);
        assertNotNull(sphereSceneObject);
        sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,false,futureTexture);
        assertNotNull(sphereSceneObject);
    }

    public void testConeConstructorMaterial() {
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,true,material);
        assertNotNull(sphereSceneObject);
        sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,false,material);
        assertNotNull(sphereSceneObject);
    }

    public void testConeConstructorMaterial2() {
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,3,4,true,material);
        assertNotNull(sphereSceneObject);
        sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,3,4,false,material);
        assertNotNull(sphereSceneObject);
        try{
            sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,2,4,false,material);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,3,3,false,material);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

    public void testConeConstructorMaterial3() {
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,5,8,true,material,3,4);
        assertNotNull(sphereSceneObject);
        sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,5,8,false,material,3,4);
        assertNotNull(sphereSceneObject);
        try{
            sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,2,4,false,material,3,4);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,3,3,false,material,3,4);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,4,4,false,material,3,4);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext,5,4,false,material,3,3);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

}
