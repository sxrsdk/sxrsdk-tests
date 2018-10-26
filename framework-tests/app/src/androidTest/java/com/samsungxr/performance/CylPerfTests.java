package com.samsungxr.performance;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.tester.TextureEventHandler;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRImage;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRPointLight;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRShaderId;
import com.samsungxr.SXRSpotLight;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.IAssetEvents;
import com.samsungxr.nodes.SXRCylinderNode;
import com.samsungxr.tester.R;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import com.samsungxr.utility.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)


public class CylPerfTests
{
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRScene mScene;
    private int BITMAP_TEXTURE = R.drawable.checker;
    private int CUBEMAP_TEXTURE = R.raw.beach;
    private int COMPRESSED_TEXTURE = R.raw.sunmap;
    private int R11_BITMAP_TEXTURE =  R.raw.etc2_r11_opaque;
    private int RG11_BITMAP_TEXTURE = R.raw.etc2_rg11_transparency;
    private int RGB8_BITMAP_TEXTURE = R.raw.etc2_rgb8_opaque;
    private SXRImage mBitmapImage = null;
    private SXRImage mCompressedImage = null;
    private SXRImage mCubemapImage = null;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @After
    public void tearDown() {
        SXRScene scene = mTestUtils.getMainScene();
        if (scene != null) {
            scene.clear();
        }
    }

    @Before
    public void setUp() throws TimeoutException {
        SXRTestableActivity activity = ActivityRule.getActivity();
        mTestUtils = new SXRTestUtils(activity);
        mTestUtils.waitForOnInit();
        mWaiter = new Waiter();

        mScene = mTestUtils.getMainScene();
        mWaiter.assertNotNull(mScene);
    }

    private void runPerfTest(SXRContext ctx, String testName, Map<String, Object> params)
    {
        RenderSetup setup = new RenderSetup(mWaiter, mTestUtils);
        int nframes = (Integer) params.get("frames");
        float expectedFPS = (Float) params.get("fps");
        setup.createTestScene(ctx, params);
        mTestUtils.waitForXFrames(2);
        long startTime = System.currentTimeMillis();
        mTestUtils.waitForXFrames(nframes);
        long endTime = System.currentTimeMillis();
        float fps =  (1000.0f * nframes) / ((float) (endTime - startTime));
        Log.e("PERFORMANCE", testName + " FPS = %f, expected %f", fps, expectedFPS);
        mWaiter.assertTrue(fps >= expectedFPS);
    }

    @Test
    public void cyl10x10ShareAll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10ShareAll", params);
    }

    @Test
    public void cyl10x10ShareGeo() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10ShareGeo", params);
    }

    @Test
    public void cyl10x10ShareMtll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10ShareMtl", params);
    }

    @Test
    public void cyl10x10() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10", params);
    }

    @Test
    public void cyl10x10BitmapShareGeo() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10BitmapShareGeo", params);
    }

    @Test
    public void cyl10x10BitmapShareMtll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10BitmapShareMtl", params);
    }

    @Test
    public void cyl10x10Bitmap() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10Bitmap", params);
    }

    @Test
    public void cyl10x10CubemapShareAll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("cubemap", CUBEMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10CubemapShareAll", params);
    }

    @Test
    public void cyl10x10CubemapShareGeo() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("cubemap", CUBEMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10CubemapShareGeo", params);
    }

    @Test
    public void cyl10x10CubemapShareMtll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("cubemap", CUBEMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10CubemapShareMtl", params);
    }


    @Test
    public void cyl10x10Cubemap() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10Cubemap", params);
    }

    @Test
    public void cyl10x10CompBmapShareAll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("compressedbitmap", COMPRESSED_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10CompBmapShareAll", params);
    }

    @Test
    public void cyl10x10CompBmapShareGeo() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("compressedbitmap", COMPRESSED_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10CompBmapShareGeo", params);
    }

    @Test
    public void cyl10x10CompBmapShareMtll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("compressedbitmap", COMPRESSED_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10CompBmapShareMtl", params);
    }


    @Test
    public void cyl10x10CompBmap() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", COMPRESSED_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 27.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10CompBmap", params);
    }

    @Test
    public void cyl10x10R11BitmapShareAll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("compressedbitmap", R11_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10R11BitmapShareAll", params);
    }

    @Test
    public void cyl10x10R11BitmapShareGeo() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", R11_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 32.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10R11BitmapShareGeo", params);
    }

    @Test
    public void cyl10x10R11BitmapShareMtl() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", R11_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 26.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10R11BitmapShareMtl", params);
    }

    @Test
    public void cyl10x10R11Bitmap() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", R11_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10R11Bitmap", params);
    }

    @Test
    public void cyl10x10RG11BitmapShareAll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("compressedbitmap", RG11_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 32.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10RG11BitmapShareAll", params);
    }

    @Test
    public void cyl10x10RG11BitmapShareGeo() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", RG11_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 32.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10RG11BitmapShareGeo", params);
    }

    @Test
    public void cyl10x10RG11BitmapShareMtl() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", RG11_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10RG11BitmapShareMtl", params);
    }

    @Test
    public void cyl10x10RG11Bitmap() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", RG11_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10RG11Bitmap", params);
    }

    @Test
    public void cyl10x10RGB8BitmapShareAll() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("compressedbitmap", RGB8_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 32.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10RGB8BitmapShareAll", params);
    }

    @Test
    public void cyl10x10RGB8BitmapShareGeo() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_geometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", RGB8_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 32.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10RGB8BitmapShareGeo", params);
    }

    @Test
    public void cyl10x10RGB8BitmapShareMtl() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("share_material", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", RGB8_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 25.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10RGB8BitmapShareMtl", params);
    }

    @Test
    public void cyl10x10RGB8Bitmap() throws TimeoutException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("cylindergeometry", 1);
        params.put("rows", 10);
        params.put("columns", 10);
        params.put("bitmap", RGB8_BITMAP_TEXTURE);
        params.put("frames", 600);
        params.put("fps", 29.0f);
        params.put("renderingorder", (int) SXRRenderData.SXRRenderingOrder.GEOMETRY);
        runPerfTest(ctx, "cyl10x10RGB8Bitmap", params);
    }

}

