package com.samsungxr.scene_object;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.scene_objects.SXRCubeSceneObject;

import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * Created by j.elidelson on 5/29/2015.
 */
public class SXRCubeSceneObjectTest extends ActivityInstrumentationSXRf {

    public SXRCubeSceneObjectTest() {
        super(SXRTestActivity.class);
    }

    //************
    //*** Cube ***
    //************
    public void testCubeConstructor() {

        SXRCubeSceneObject cubeSceneObject = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(cubeSceneObject);
    }

    public void ignoretestCubeNullConstructor() {

        try {
            SXRCubeSceneObject cubeSceneObject2 = new SXRCubeSceneObject(null);
            assertNull(cubeSceneObject2);
        } catch (NullPointerException e) {
            assertEquals(null, null);
        }
    }

    public void testCubeConstructorFacingout() {

        SXRCubeSceneObject cubeSceneObjectTrue = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, true);
        assertNotNull(cubeSceneObjectTrue);
        SXRCubeSceneObject cubeSceneObjectFalse = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, false);
        assertNotNull(cubeSceneObjectFalse);
    }


    public void testCubeConstructorFacingoutMaterial() {

        SXRCubeSceneObject cubeSceneObject = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRCubeSceneObject cubeSceneObjectTrue = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, true, cubeSceneObject.getRenderData().getMaterial());
        assertNotNull(cubeSceneObjectTrue);
        SXRCubeSceneObject cubeSceneObjectFalse = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, false, cubeSceneObject.getRenderData().getMaterial());
        assertNotNull(cubeSceneObjectFalse);

    }

    public void testCubeConstructorFacingoutFutureTexture() {

        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, R.drawable.bottom));
        SXRCubeSceneObject cubeSceneObjectTrue = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, true, futureTexture);
        assertNotNull(cubeSceneObjectTrue);
        SXRCubeSceneObject cubeSceneObjectFalse = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, false, futureTexture);
        assertNotNull(cubeSceneObjectFalse);
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

        SXRCubeSceneObject cubeSceneObjectTrue = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, true, futureTextureList);
        assertNotNull(cubeSceneObjectTrue);
        SXRCubeSceneObject cubeSceneObjectFalse = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList);
        assertNotNull(cubeSceneObjectFalse);
        //Testing futurelist length != 6 (should throws IllegalArgumentException)
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));
        try {
            cubeSceneObjectFalse = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList);
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

        SXRCubeSceneObject cubeSceneObjectTrue = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, true, futureTextureList, 1);
        assertNotNull(cubeSceneObjectTrue);
        SXRCubeSceneObject cubeSceneObjectFalse = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList, 1);
        assertNotNull(cubeSceneObjectFalse);
        //Testing futurelist length != 6 (should throws IllegalArgumentException)
        futureTextureList.add(TestDefaultSXRViewManager.mSXRContext
                .loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext,
                        R.drawable.bottom)));
        try {
            cubeSceneObjectFalse = new SXRCubeSceneObject(TestDefaultSXRViewManager.mSXRContext, false, futureTextureList, 1);
            fail("should throws IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
}
