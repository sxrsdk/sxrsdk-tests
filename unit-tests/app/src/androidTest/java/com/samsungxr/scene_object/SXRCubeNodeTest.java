package com.samsungxr.node;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.nodes.SXRCubeNode;

import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * Created by j.elidelson on 5/29/2015.
 */
public class SXRCubeNodeTest extends ActivityInstrumentationSXRf {

    public SXRCubeNodeTest() {
        super(SXRTestActivity.class);
    }

    //************
    //*** Cube ***
    //************
    public void testCubeConstructor() {

        SXRCubeNode cubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(cubeNode);
    }

    public void ignoretestCubeNullConstructor() {

        try {
            SXRCubeNode cubeNode2 = new SXRCubeNode(null);
            assertNull(cubeNode2);
        } catch (NullPointerException e) {
            assertEquals(null, null);
        }
    }

    public void testCubeConstructorFacingout() {

        SXRCubeNode cubeNodeTrue = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, true);
        assertNotNull(cubeNodeTrue);
        SXRCubeNode cubeNodeFalse = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, false);
        assertNotNull(cubeNodeFalse);
    }


    public void testCubeConstructorFacingoutMaterial() {

        SXRCubeNode cubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        SXRCubeNode cubeNodeTrue = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, true, cubeNode.getRenderData().getMaterial());
        assertNotNull(cubeNodeTrue);
        SXRCubeNode cubeNodeFalse = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, false, cubeNode.getRenderData().getMaterial());
        assertNotNull(cubeNodeFalse);

    }

    public void testCubeConstructorFacingoutFutureTexture() {

        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, R.drawable.bottom));
        SXRCubeNode cubeNodeTrue = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, true, futureTexture);
        assertNotNull(cubeNodeTrue);
        SXRCubeNode cubeNodeFalse = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, false, futureTexture);
        assertNotNull(cubeNodeFalse);
    }

    public void testCubeConstructorFacingoutArrayFutureTexture() {

        ArrayList<Future<SXRTexture>> futureTextureList = new ArrayList<Future<SXRTexture>>(
                6);
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.back)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.right)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.front)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.left)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.top)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));

        SXRCubeNode cubeNodeTrue = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, true, futureTextureList);
        assertNotNull(cubeNodeTrue);
        SXRCubeNode cubeNodeFalse = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList);
        assertNotNull(cubeNodeFalse);
        //Testing futurelist length != 6 (should throws IllegalArgumentException)
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));
        try {
            cubeNodeFalse = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList);
            fail("should throws IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testCubeConstructorFacingoutArrayFutureTextureSegmentNumber() {

        ArrayList<Future<SXRTexture>> futureTextureList = new ArrayList<Future<SXRTexture>>(
                6);
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.back)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.right)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.front)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.left)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.top)));
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));

        SXRCubeNode cubeNodeTrue = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, true, futureTextureList, 1);
        assertNotNull(cubeNodeTrue);
        SXRCubeNode cubeNodeFalse = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList, 1);
        assertNotNull(cubeNodeFalse);
        //Testing futurelist length != 6 (should throws IllegalArgumentException)
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));
        try {
            cubeNodeFalse = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList, 1);
            fail("should throws IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
}
