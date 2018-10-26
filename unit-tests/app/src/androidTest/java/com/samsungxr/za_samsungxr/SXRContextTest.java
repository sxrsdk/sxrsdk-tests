package com.samsungxr.za_samsungxr;

import android.graphics.Bitmap;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRBitmapTexture;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRCubemapTexture;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRPostEffectShaderManager;
import com.samsungxr.SXRScene;

import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.R;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.periodic.SXRPeriodicEngine;
import com.samsungxr.ActivityInstrumentationSXRf;

import java.io.IOException;

/**
 * Created by santhyago on 2/25/15.
 */

public class SXRContextTest extends ActivityInstrumentationSXRf {
    /**
     * Test if the createQuad method creates a array of chars with 6 positions
     * (3 points = 1 triangle)
     * (6 points = 2 triangles = 1 quadrangle)
     */
    public void testCreateQuad() {
        float width = 1.0f;
        float height = 2.0f;

        /**
         * width  = X;
         * height = y;
         */

        SXRMesh quad = TestDefaultSXRViewManager.mSXRContext.createQuad(width, height);

        /**
         * Tests if the quad has 2 triangles with 2 vertices in common
         */
        assertEquals('\u0000', quad.getTriangles()[0]);
        assertEquals('\u0001', quad.getTriangles()[1]); // [1] is the same as [3]
        assertEquals('\u0002', quad.getTriangles()[2]); // [2] is the same as [5]
        assertEquals('\u0001', quad.getTriangles()[3]); // [3] is the same as [1]
        assertEquals('\u0003', quad.getTriangles()[4]);
        assertEquals('\u0002', quad.getTriangles()[5]); // [5] is the same as [2]

        /**
         * [   0] [   1] [  2]   [-width/2] [ height/2] [ 0.0]
         * [   3] [   4] [  5]   [-width/2] [-height/2] [ 0.0]
         * [   6] [   7] [  8] = [ width/2] [ height/2] [ 0.0]
         * [   9] [  10] [ 11]   [ width/2] [-height/2] [ 0.0]
         */

        float halfWidth = width / 2;
        float halfHeight = height / 2;
        assertEquals(-halfWidth, quad.getVertices()[0]);
        assertEquals(halfHeight, quad.getVertices()[1]);
        assertEquals(0.0f, quad.getVertices()[2]);
        assertEquals(-halfWidth, quad.getVertices()[3]);
        assertEquals(-halfHeight, quad.getVertices()[4]);
        assertEquals(0.0f, quad.getVertices()[5]);
        assertEquals(halfWidth, quad.getVertices()[6]);
        assertEquals(halfHeight, quad.getVertices()[7]);
        assertEquals(0.0f, quad.getVertices()[8]);
        assertEquals(halfWidth, quad.getVertices()[9]);
        assertEquals(-halfHeight, quad.getVertices()[10]);
        assertEquals(0.0f, quad.getVertices()[11]);
    }

    public void testGetMainScene() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        assertNotNull(lSXRContext);
        assertNotNull(lSXRContext.getMainScene());
        assertEquals(new SXRScene(lSXRContext).getClass().getSimpleName(),
                lSXRContext.getMainScene().getClass().getSimpleName());
    }

    public void testGetPeriodicEngine() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        SXRPeriodicEngine periodicEngine = lSXRContext.getPeriodicEngine();
        assertNotNull(periodicEngine);
        assertEquals(periodicEngine.getClass().getSimpleName(), "SXRPeriodicEngine");
    }

    public void testGetPostEffectShaderManager() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        SXRPostEffectShaderManager shaderManager = lSXRContext.getPostEffectShaderManager();

        lSXRContext.getMaterialShaderManager();
        assertNotNull(shaderManager);
    }

    public void testGetAnimationEngine() {
        SXRContext gVRContext = TestDefaultSXRViewManager.mSXRContext;
        assertNotNull(gVRContext.getAnimationEngine());
    }

    public void testgetMaterialShaderManager() {
        SXRContext gVRContext = TestDefaultSXRViewManager.mSXRContext;
        assertNotNull(gVRContext.getMaterialShaderManager());
    }

//    public void testLoadMeshWhitRaw(){
//        SXRContext gVRContext = TestDefaultSXRViewManager.mSXRContext;
//        gVRContext.getAssetLoader().loadMesh(R.raw.cinema);
//    }

//    public void testLoadMeshRelativyFileName(){
//        SXRContext gVRContext = TestDefaultSXRViewManager.mSXRContext;
//        gVRContext.getAssetLoader().loadMesh("cinema.obj");
//    }

    public void testLoadPngFile(){
        //SXRContext gVRContext = TestDefaultSXRViewManager.mSXRContext;
        //gVRContext.loadBitmap("texture1.jpg");
        TestDefaultSXRViewManager.mSXRContext.loadBitmap("texture1.jpg");
    }

//    public void testLoadMeshFromAssets() {
//        SXRContext lSXRContext = getActivity().getSXRContext();
//        SXRMesh mesh = lSXRContext.loadMeshFromAssets("pokeball.obj");
//        assertNotNull(mesh);
//        assertTrue(mesh.getTriangles().length > 0);
//    }

//    public void testLoadTexture() {
//        SXRContext lSXRContext = getActivity().getSXRContext();
//        SXRBitmapTexture bitmapTexture = lSXRContext.loadTexture("texture1.jpg");
//        assertNotNull(bitmapTexture);
//    }

    //Added by Elidelson on 10/02/2015
    public void testLoadMesh01(){

        SXRAndroidResource sxrAndroidResource = null;
        SXRAndroidResource sxrAndroidResource2 = null;
        try {
            sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "cylinder.obj");
            sxrAndroidResource2 = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "bunny.obj");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SXRAndroidResource.MeshCallback meshCallback = new SXRAndroidResource.MeshCallback() {
            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }

            @Override
            public void loaded(SXRMesh resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };
        TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(meshCallback,sxrAndroidResource);
        TestDefaultSXRViewManager.mSXRContext.getAssetLoader().loadMesh(meshCallback,sxrAndroidResource2,0);
    }


    public void ignoretestLoadTexture() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        SXRBitmapTexture bitmapTexture = lSXRContext.loadTexture("texture1.jpg");
        assertNotNull(bitmapTexture);
    }

    public void ignoretestLoadBitmap() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        Bitmap bitmap;
        bitmap = lSXRContext.loadBitmap("texture1.jpg");
        assertEquals(1024, bitmap.getWidth());

        bitmap = lSXRContext.loadBitmap("front.png");
        assertEquals(512, bitmap.getWidth());

        try {
            bitmap = lSXRContext.loadBitmap(null);
            assertEquals(3695, bitmap.getWidth());
        }catch (IllegalArgumentException e){}
    }

    public void testLoadCubeMApTesture1() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        SXRAndroidResource sxrAndroidResource[] = new SXRAndroidResource[6];
        SXRTextureParameters sxrTextureParameters = new SXRTextureParameters(TestDefaultSXRViewManager.mSXRContext);

        try {
            sxrAndroidResource[0] = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
            sxrAndroidResource[1] = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
            sxrAndroidResource[2] = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
            sxrAndroidResource[3] = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
            sxrAndroidResource[4] = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
            sxrAndroidResource[5] = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //SXRCubemapTexture sxrCubemapTexture = lSXRContext.loadCubemapTexture(sxrAndroidResource);
        //assertNotNull(sxrCubemapTexture);
        //SXRCubemapTexture sxrCubemapTexture1 = lSXRContext.loadCubemapTexture(sxrAndroidResource, sxrTextureParameters);
        //assertNotNull(sxrTextureParameters);

    }


    public void testLoadBitmapTexture1() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        SXRAndroidResource sxrAndroidResource=null;
        SXRAndroidResource sxrAndroidResource2=null;
        try {
            sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
            sxrAndroidResource2 = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posy.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SXRAndroidResource.BitmapTextureCallback bitmapTextureCallback = new SXRAndroidResource.BitmapTextureCallback() {
            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }

            @Override
            public void loaded(SXRTexture resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };
        lSXRContext.loadBitmapTexture(bitmapTextureCallback,sxrAndroidResource);
        lSXRContext.loadBitmapTexture(bitmapTextureCallback,sxrAndroidResource2,0);
    }


    public void testLoadCompressedTexture() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        SXRAndroidResource sxrAndroidResource=null;
        SXRAndroidResource sxrAndroidResource2=null;
        try {
            sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
            sxrAndroidResource2 = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posy.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SXRAndroidResource.CompressedTextureCallback compressedTextureCallback = new SXRAndroidResource.CompressedTextureCallback() {
            @Override
            public void loaded(SXRTexture resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };

        lSXRContext.loadCompressedTexture(compressedTextureCallback, sxrAndroidResource);
        lSXRContext.loadCompressedTexture(compressedTextureCallback, sxrAndroidResource2, 0);
    }


    public void testLoadTexture() {
        SXRContext lSXRContext = TestDefaultSXRViewManager.mSXRContext;
        SXRAndroidResource sxrAndroidResource=null;
        SXRAndroidResource sxrAndroidResource2=null;
        try {
            sxrAndroidResource = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posx.png");
            sxrAndroidResource2 = new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, "posy.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SXRAndroidResource.TextureCallback textureCallback = new SXRAndroidResource.TextureCallback() {
            @Override
            public boolean stillWanted(SXRAndroidResource androidResource) {
                return false;
            }

            @Override
            public void loaded(SXRTexture resource, SXRAndroidResource androidResource) {

            }

            @Override
            public void failed(Throwable t, SXRAndroidResource androidResource) {

            }
        };
        lSXRContext.loadTexture(textureCallback, sxrAndroidResource);
        lSXRContext.loadTexture(textureCallback, sxrAndroidResource2, 0);
        lSXRContext.loadTexture(textureCallback,sxrAndroidResource2,0,0);
    }


}
