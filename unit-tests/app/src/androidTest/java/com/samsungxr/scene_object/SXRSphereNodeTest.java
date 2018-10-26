package com.samsungxr.node;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import java.util.concurrent.Future;

/**
 * Created by j.elidelson on 5/29/2015.
 */
public class SXRSphereNodeTest extends ActivityInstrumentationSXRf {

    public SXRSphereNodeTest() {
        super(SXRTestActivity.class);
    }

    //**************
    //*** Sphere ***
    //**************
    public void testConeConstructor() {

        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(sphereNode);
    }

    public void testConeConstructorFacingout() {

        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,true);
        assertNotNull(sphereNode);
        sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,false);
        assertNotNull(sphereNode);
    }

    public void testConeConstructorFutureTexture() {
        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.drawable.bottom));
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,true,futureTexture);
        assertNotNull(sphereNode);
        sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,false,futureTexture);
        assertNotNull(sphereNode);
    }

    public void testConeConstructorMaterial() {
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,true,material);
        assertNotNull(sphereNode);
        sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,false,material);
        assertNotNull(sphereNode);
    }

    public void testConeConstructorMaterial2() {
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,3,4,true,material);
        assertNotNull(sphereNode);
        sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,3,4,false,material);
        assertNotNull(sphereNode);
        try{
            sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,2,4,false,material);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,3,3,false,material);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

    public void testConeConstructorMaterial3() {
        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,5,8,true,material,3,4);
        assertNotNull(sphereNode);
        sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,5,8,false,material,3,4);
        assertNotNull(sphereNode);
        try{
            sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,2,4,false,material,3,4);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,3,3,false,material,3,4);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,4,4,false,material,3,4);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext,5,4,false,material,3,3);
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

}
