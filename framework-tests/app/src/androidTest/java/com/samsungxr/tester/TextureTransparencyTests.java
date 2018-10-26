package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRShader;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.SXRTransform;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRTextViewNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@RunWith(AndroidJUnit4.class)
public class TextureTransparencyTests
{
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRNode mRoot;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @After
    public void tearDown()
    {
        SXRScene scene = mTestUtils.getMainScene();
        if (scene != null)
        {
            scene.clear();
        }
    }

    @Before
    public void setUp() throws TimeoutException
    {
        SXRTestableActivity activity = ActivityRule.getActivity();
        mTestUtils = new SXRTestUtils(activity);
        mTestUtils.waitForOnInit();
        mWaiter = new Waiter();

        SXRScene scene = mTestUtils.getMainScene();
        mWaiter.assertNotNull(scene);
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
    }

    public void centerModel(SXRNode model)
    {
        SXRNode.BoundingVolume bv = model.getBoundingVolume();
        float sf = 1 / bv.radius;
        model.getTransform().setScale(sf, sf, sf);
        bv = model.getBoundingVolume();
        model.getTransform().setPosition(-bv.center.x, -bv.center.y, -bv.center.z - 1.5f * bv.radius);
    }

    public void checkResults(int actual, int truth)
    {
        mWaiter.assertEquals(truth, actual);
    }

    @Test
    public void testTransparencyJPG() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.jpg_opaque));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "JPG order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencyPNG4_Transp() throws TimeoutException
    {
        android.util.Log.d("Texture:", "beginning texture transparency detection");
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load png, 4 component, transparency, RenderOrder == TRANSPARENT
        texHandler.reset();
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.png_4_transparency));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "PNG 4 transparent order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.TRANSPARENT);
    }

    @Test
    public void testTransparencyPNG3() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load png, 3 component, RenderOrder == GEOMETRY
        texHandler.reset();
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.png_3_opaque));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "PNG 3 opaque order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencyTGA4_Transp() throws TimeoutException
    {
        android.util.Log.d("Texture:", "beginning texture transparency detection");
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load tga, 4 component, transparency, RenderOrder == TRANSPARENT
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.tga_4_transparency));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "TGA 4 transparent order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.TRANSPARENT);
    }

    @Test
    public void testTransparencyPNG4_Opaque() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load png, 4 component, opaque, RenderOrder == GEOMETRY
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.png_4_opaque));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "PNG 4 opaque order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencyASTC() throws TimeoutException
    {
        android.util.Log.d("Texture:", "beginning texture transparency detection");
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load astc, RenderOrder == TRANSPARENT
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.astc_transparency));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "ASTC order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.TRANSPARENT);
    }

    @Test
    public void testTransparencyTGA3() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load tga, 3 component, RenderOrder == GEOMETRY
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.tga_3_opaque));
        material.setTexture("diffuseTexture", tex);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "after setTexture TGA 3 opaque order = " + order);

        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "TGA 3 opaque order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencyRG11() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load etc2, GL_COMPRESSED_RG11_EAC, RenderOrder == TRANSPARENT
        texHandler.reset();
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.etc2_rg11_transparency));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "rg11 order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.TRANSPARENT);
    }

    @Test
    public void testTransparencyTGA4_Opaque() throws TimeoutException
    {
        android.util.Log.d("Texture:", "beginning texture transparency detection");
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load tga, 4 component, opaque, RenderOrder == GEOMETRY
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.tga_4_opaque));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "TGA 4 opaque order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencySR11_Transp() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load etc2, GL_COMPRESSED_SIGNED_RG11_EAC, RenderOrder == TRANSPARENT
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.etc2_signed_rg11_transparency));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "signed rg11 order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.TRANSPARENT);
    }

    @Test
    public void testTransparencyR11() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load etc2, GL_COMPRESSED_R11_EAC, RenderOrder == GEOMETRY
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.etc2_r11_opaque));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "r11 order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencySR11() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load etc2, GL_COMPRESSED_R11_EAC, RenderOrder == GEOMETRY
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.etc2_r11_opaque));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "r11 order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencyRGBA8() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load etc2, GL_COMPRESSED_RGBA8_ETC2_EAC, RenderOrder == TRANSPARENT
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.etc2_rgba8_transparency));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "rgba8 order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.TRANSPARENT);
        groundObject.getRenderData().setRenderingOrder(SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencySR11_Opaque() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load etc2, GL_COMPRESSED_SIGNED_R11_EAC, RenderOrder == GEOMETRY
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.etc2_signed_r11_opaque));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "sr11 order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testTransparencyRGBA1() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load etc2, GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2, RenderOrder == TRANSPARENT
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.etc2_rgb8_punchthrough_alpha1_transparency));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("sxrf", "rgba1 order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.TRANSPARENT);
    }

    @Test
    public void testTransparencyRGB8_Opaque() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load etc2, GL_COMPRESSED_RGB8_ETC2, RenderOrder == GEOMETRY
        texHandler.reset();
        SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, R.raw.etc2_rgb8_opaque));
        material.setTexture("diffuseTexture", tex);
        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "rgb8 order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }

    @Test
    public void testCompressedTextureASTC_Transparent() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load astc, RenderOrder == TRANSPARENT
        try {
            SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, "3dgreen_transparent.astc"));
            material.setTexture("diffuseTexture", tex);
        } catch (IOException ex) {
            mWaiter.fail(ex);
        }

        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("sxrf", "astc transparent order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.TRANSPARENT);
    }

    @Test
    public void testCompressedTextureASTC_Opaque() throws TimeoutException
    {
        final SXRContext ctx  = mTestUtils.getSxrContext();
        final SXRMaterial material = new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        final SXRNode groundObject = new SXRCubeNode(ctx, true, material);
        TextureEventHandler texHandler = new TextureEventHandler(mTestUtils, 1);

        ctx.getEventReceiver().addListener(texHandler);
        groundObject.getTransform().setPositionZ(-3.0f);
        mTestUtils.getMainScene().addNode(groundObject);

        // load astc, RenderOrder == GEOMETRY
        texHandler.reset();
        try {
            SXRTexture tex = ctx.getAssetLoader().loadTexture(new SXRAndroidResource(ctx, "3dgreen_opaque.astc"));
            material.setTexture("diffuseTexture", tex);
        } catch (IOException ex) {
            mWaiter.fail(ex);
        }

        mTestUtils.waitForAssetLoad();
        texHandler.checkTextureLoaded(mWaiter);
        mTestUtils.waitForXFrames(2);
        int order = groundObject.getRenderData().getRenderingOrder();
        android.util.Log.d("Texture:", "astc opaque order = " + order);
        checkResults(order, SXRRenderData.SXRRenderingOrder.GEOMETRY);
    }
}

