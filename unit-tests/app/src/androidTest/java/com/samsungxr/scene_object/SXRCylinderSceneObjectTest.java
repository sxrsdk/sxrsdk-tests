package com.samsungxr.scene_object;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.scene_objects.SXRCylinderSceneObject;

import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * Created by j.elidelson on 5/29/2015.
 */
public class SXRCylinderSceneObjectTest extends ActivityInstrumentationSXRf {

    public SXRCylinderSceneObjectTest() {
        super(SXRTestActivity.class);
    }

    //****************
    //*** Cylinder ***
    //****************
    public void testCylinderConstructor() {

        SXRCylinderSceneObject cylinderSceneObject = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(cylinderSceneObject);
    }

    public void ignoretestCylinderNullConstructor() {

        try {
            SXRCylinderSceneObject cylinderSceneObject = new SXRCylinderSceneObject(null);
            assertNull(cylinderSceneObject);
        } catch (NullPointerException e) {
            assertEquals(null, null);
        }
    }

    public void testCylinderConstructorWithParameters() {

        SXRCylinderSceneObject cylinderSceneObject =
                new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f,0.5f,1.0f,2,36,true);
        assertNotNull(cylinderSceneObject);
    }


    public void testCylinderConstructorNegativeRadius() {

        try {
            SXRCylinderSceneObject cylinderSceneObject =
                    new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, -1.0f, -1.0f, 1.0f, 2, 36,false);
            assertNull(cylinderSceneObject);
        }catch (IllegalArgumentException e){}
    }

    public void testCylinderConstructorZeroRadius() {

        try {
            SXRCylinderSceneObject cylinderSceneObject =
                    new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.0f, -1.0f, 1.0f, 2, 36,false);
            assertNull(cylinderSceneObject);
        }catch (IllegalArgumentException e){}
    }


    public void testCylinderConstructorZeroHeight() {

        try {
            SXRCylinderSceneObject cylinderSceneObject =
                    new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 0, 2, 36,true);
            assertNull(cylinderSceneObject);
        }catch (IllegalArgumentException e){}
    }

    public void testCylinderConstructorZeroStack() {

        try {
            SXRCylinderSceneObject cylinderSceneObject =
                    new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 0, 36,false);
            assertNull(cylinderSceneObject);
        }catch (IllegalArgumentException e){}
    }


    public void testCylinderConstructorZeroSlice() {

        try {
            SXRCylinderSceneObject cylinderSceneObject =
                    new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 0,true);
            assertNull(cylinderSceneObject);
        }catch (IllegalArgumentException e){}
    }

    public void testCylinderConstructorFacingout() { //Created by j.elidelson on 8/14/2015.

        SXRCylinderSceneObject cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,true);
        assertNotNull(cylinderSceneObjectTrue);
        SXRCylinderSceneObject cylinderSceneObjectFalse = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,false);
        assertNotNull(cylinderSceneObjectFalse);
    }

    public void testCylinderConstructorMaterial() { //Created by j.elidelson on 8/14/2015.

        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRCylinderSceneObject cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,true,material);
        assertNotNull(cylinderSceneObjectTrue);
    }

    public void testCylinderConstructorMaterial2() { //Created by j.elidelson on 8/14/2015.

        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRCylinderSceneObject cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.0f, 0.5f, 1.0f, 2, 36,true,material);
        assertNotNull(cylinderSceneObjectTrue);
        cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 4, 8,true,material,2,8);
        assertNotNull(cylinderSceneObjectTrue);
    }


    public void testCylinderConstructorFacingoutFutureTexture() {

        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.drawable.bottom));
        SXRCylinderSceneObject cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,true,futureTexture);
        assertNotNull(cylinderSceneObjectTrue);
        SXRCylinderSceneObject cylinderSceneObjectFalse = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,false,futureTexture);
        assertNotNull(cylinderSceneObjectFalse);
    }

    public void testCylinderConstructorFacingoutArrayFutureTexture() {

        ArrayList<Future<SXRTexture>> futureTextureList = new ArrayList<Future<SXRTexture>>(
                3);
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.back)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.right)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.front)));

        SXRCylinderSceneObject cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,true,futureTextureList);
        assertNotNull(cylinderSceneObjectTrue);
        SXRCylinderSceneObject cylinderSceneObjectFalse = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,false,futureTextureList);
        try{
            cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.0f, 0.0f, 1.0f, 2, 36,true,futureTextureList);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 0,true,futureTextureList);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        //Testing futurelist length != 3 (should throws IllegalArgumentException)
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));
        try {
            cylinderSceneObjectFalse = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }


    public void testCylinderConstructorFacingoutArrayFutureTexture2() {

        ArrayList<Future<SXRTexture>> futureTextureList = new ArrayList<Future<SXRTexture>>(
                3);
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.back)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.right)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.front)));

        SXRCylinderSceneObject cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 8,true,futureTextureList,1,2);
        assertNotNull(cylinderSceneObjectTrue);
        cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 36,true,futureTextureList,1,1);
        try{
            cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 36,true,futureTextureList,3,1);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.0f, 0.0f, 1.0f, 2, 36,true,futureTextureList,1,1);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            cylinderSceneObjectTrue = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 0,true,futureTextureList,1,1);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        SXRCylinderSceneObject cylinderSceneObjectFalse = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 36,false,futureTextureList);
        assertNotNull(cylinderSceneObjectFalse);
        try{
            cylinderSceneObjectFalse = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 36,true,futureTextureList,1,5);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        //Testing futurelist length != 3 (should throws IllegalArgumentException)
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));
        try {
            cylinderSceneObjectFalse = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 36,false, futureTextureList);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            cylinderSceneObjectFalse = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 36,false, futureTextureList,1,1);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }


    public void testCylinderConstructorWithNegativeParametersMaterial() {

        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        try {
            SXRCylinderSceneObject cylinderSceneObject =
                    new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.0f, 0.0f, 1.0f, 2, 36, true, material);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            SXRCylinderSceneObject cylinderSceneObject =
                    new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 0, true, material);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

    public void testCylinderConstructorMaterial_StackNum_SliceNum() {

        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRCylinderSceneObject cylinderSceneObject =
                new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 36, true, material,2,4);
        assertNotNull(cylinderSceneObject);
        cylinderSceneObject =
                new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 36, false, material,2,6);
        assertNotNull(cylinderSceneObject);
        try {
            cylinderSceneObject = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.0f, 0.0f, 1.0f, 5, 36, true, material, 2, 6);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            cylinderSceneObject = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 5, 0, true, material, 2, 6);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            cylinderSceneObject = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.0f, 0.5f, 1.0f, 5, 36, true, material, 2, 6);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            cylinderSceneObject = new SXRCylinderSceneObject(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.0f, 1.0f, 2, 36, true, material, 2, 5);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}

    }
}
