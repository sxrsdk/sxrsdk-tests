package com.samsungxr.node;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.nodes.SXRCylinderNode;

import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * Created by j.elidelson on 5/29/2015.
 */
public class SXRCylinderNodeTest extends ActivityInstrumentationSXRf {

    public SXRCylinderNodeTest() {
        super(SXRTestActivity.class);
    }

    //****************
    //*** Cylinder ***
    //****************
    public void testCylinderConstructor() {

        SXRCylinderNode cylinderNode = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(cylinderNode);
    }

    public void ignoretestCylinderNullConstructor() {

        try {
            SXRCylinderNode cylinderNode = new SXRCylinderNode(null);
            assertNull(cylinderNode);
        } catch (NullPointerException e) {
            assertEquals(null, null);
        }
    }

    public void testCylinderConstructorWithParameters() {

        SXRCylinderNode cylinderNode =
                new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f,0.5f,1.0f,2,36,true);
        assertNotNull(cylinderNode);
    }


    public void testCylinderConstructorNegativeRadius() {

        try {
            SXRCylinderNode cylinderNode =
                    new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, -1.0f, -1.0f, 1.0f, 2, 36,false);
            assertNull(cylinderNode);
        }catch (IllegalArgumentException e){}
    }

    public void testCylinderConstructorZeroRadius() {

        try {
            SXRCylinderNode cylinderNode =
                    new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.0f, -1.0f, 1.0f, 2, 36,false);
            assertNull(cylinderNode);
        }catch (IllegalArgumentException e){}
    }


    public void testCylinderConstructorZeroHeight() {

        try {
            SXRCylinderNode cylinderNode =
                    new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 0, 2, 36,true);
            assertNull(cylinderNode);
        }catch (IllegalArgumentException e){}
    }

    public void testCylinderConstructorZeroStack() {

        try {
            SXRCylinderNode cylinderNode =
                    new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 0, 36,false);
            assertNull(cylinderNode);
        }catch (IllegalArgumentException e){}
    }


    public void testCylinderConstructorZeroSlice() {

        try {
            SXRCylinderNode cylinderNode =
                    new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 0,true);
            assertNull(cylinderNode);
        }catch (IllegalArgumentException e){}
    }

    public void testCylinderConstructorFacingout() { //Created by j.elidelson on 8/14/2015.

        SXRCylinderNode cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,true);
        assertNotNull(cylinderNodeTrue);
        SXRCylinderNode cylinderNodeFalse = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,false);
        assertNotNull(cylinderNodeFalse);
    }

    public void testCylinderConstructorMaterial() { //Created by j.elidelson on 8/14/2015.

        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRCylinderNode cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,true,material);
        assertNotNull(cylinderNodeTrue);
    }

    public void testCylinderConstructorMaterial2() { //Created by j.elidelson on 8/14/2015.

        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRCylinderNode cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.0f, 0.5f, 1.0f, 2, 36,true,material);
        assertNotNull(cylinderNodeTrue);
        cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 4, 8,true,material,2,8);
        assertNotNull(cylinderNodeTrue);
    }


    public void testCylinderConstructorFacingoutFutureTexture() {

        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,R.drawable.bottom));
        SXRCylinderNode cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,true,futureTexture);
        assertNotNull(cylinderNodeTrue);
        SXRCylinderNode cylinderNodeFalse = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,false,futureTexture);
        assertNotNull(cylinderNodeFalse);
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

        SXRCylinderNode cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,true,futureTextureList);
        assertNotNull(cylinderNodeTrue);
        SXRCylinderNode cylinderNodeFalse = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,false,futureTextureList);
        try{
            cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.0f, 0.0f, 1.0f, 2, 36,true,futureTextureList);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 0,true,futureTextureList);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        //Testing futurelist length != 3 (should throws IllegalArgumentException)
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));
        try {
            cylinderNodeFalse = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList);
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

        SXRCylinderNode cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 8,true,futureTextureList,1,2);
        assertNotNull(cylinderNodeTrue);
        cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 36,true,futureTextureList,1,1);
        try{
            cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 36,true,futureTextureList,3,1);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.0f, 0.0f, 1.0f, 2, 36,true,futureTextureList,1,1);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try{
            cylinderNodeTrue = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 0,true,futureTextureList,1,1);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        SXRCylinderNode cylinderNodeFalse = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 36,false,futureTextureList);
        assertNotNull(cylinderNodeFalse);
        try{
            cylinderNodeFalse = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext,0.5f, 0.5f, 1.0f, 2, 36,true,futureTextureList,1,5);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        //Testing futurelist length != 3 (should throws IllegalArgumentException)
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));
        try {
            cylinderNodeFalse = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 36,false, futureTextureList);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            cylinderNodeFalse = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 36,false, futureTextureList,1,1);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }


    public void testCylinderConstructorWithNegativeParametersMaterial() {

        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        try {
            SXRCylinderNode cylinderNode =
                    new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.0f, 0.0f, 1.0f, 2, 36, true, material);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            SXRCylinderNode cylinderNode =
                    new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 0, true, material);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
    }

    public void testCylinderConstructorMaterial_StackNum_SliceNum() {

        SXRMaterial material = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        SXRCylinderNode cylinderNode =
                new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 36, true, material,2,4);
        assertNotNull(cylinderNode);
        cylinderNode =
                new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 2, 36, false, material,2,6);
        assertNotNull(cylinderNode);
        try {
            cylinderNode = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.0f, 0.0f, 1.0f, 5, 36, true, material, 2, 6);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            cylinderNode = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.5f, 1.0f, 5, 0, true, material, 2, 6);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            cylinderNode = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.0f, 0.5f, 1.0f, 5, 36, true, material, 2, 6);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}
        try {
            cylinderNode = new SXRCylinderNode(TestDefaultSXRViewManager.mSXRContext, 0.5f, 0.0f, 1.0f, 2, 36, true, material, 2, 5);
            fail("should throws IllegalArgumentException");
        }catch (IllegalArgumentException e){}

    }
}
