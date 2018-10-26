package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRAssetLoader;
import com.samsungxr.SXRCompressedImage;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRImage;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.scene_objects.SXRCubeSceneObject;

import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;

import org.joml.Vector3f;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class AssetTextureTests
{
    private static final String TAG = AssetTextureTests.class.getSimpleName();
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRSceneObject mRoot;
    private SXRSceneObject mBackground;
    private boolean mDoCompare = true;
    private AssetEventHandler mHandler;

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

        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();

        mWaiter.assertNotNull(scene);
        mBackground = new SXRCubeSceneObject(ctx, false, new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID));
        mBackground.getTransform().setScale(10, 10, 10);
        mBackground.setName("background");
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
        mHandler = new AssetEventHandler(scene, mWaiter, mTestUtils, getClass().getSimpleName());
    }

    @Test
    public void jassimpNormalDiffuseSpecularLightmap() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/lightmap/normal_diffuse_specular_lightmap.fbx", 25, 0, "jassimpNormalDiffuseSpecularLightmap");
    }

    @Test
    public void jassimpNormaLightmap() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/lightmap/normal_lightmap.fbx", 25, 0, "jassimpNormalLightmap");
    }

    @Test
    public void jassimpSpecularLightmap() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/lightmap/specular_lightmap.fbx", 25, 0, "jassimpSpecularLightmap");
    }

    @Test
    public void jassimpEmbeddedTextures() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRMaterial mtl =  new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID);
        SXRCubeSceneObject backgnd = new SXRCubeSceneObject(ctx, false, mtl);

        mtl.setDiffuseColor(1.0f, 1.0f, 0.7f, 1.0f);
        backgnd.getTransform().setScale(10, 10, 10);
        mTestUtils.getMainScene().addSceneObject(backgnd);
        mHandler.setWaitFrames(2);
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/bmw/bmw.FBX", 20, 1, "jassimpEmbeddedTextures");
    }

    @Test
    public void jassimpLightmap() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/lightmap/lightmap_test.fbx", 13, 0, "jassimpLightmap");
    }

    @Test
    public void jassimpCubeWrongTex() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/cube/cube_wrongtex.fbx", 0, 1, "jassimpCubeWrongTex");
    }


    @Test
    public void x3dTexcoordTest1() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/texture_coordinates/texturecoordinatetest.x3d", 5, 0, "x3dTexcoordTest1");
    }

    @Test
    public void x3dTexcoordTest2() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/texture_coordinates/texturecoordinatetestsubset.x3d", 3, 0, "x3dTexcoordTest2");
    }

    @Test
    public void x3dTexcoordTest3() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/texture_coordinates/texturecoordinatetestsubset2.x3d", 5, 0, "x3dTexcoordTest3");
    }

    @Test
    public void x3dTexcoordTest4() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "x3d/texture_coordinates/texturecoordinatetestsubset3.x3d", 5, 0, "x3dTexcoordTest4");
    }

    @Test
    public void NormalDiffuseSpecularLightmap() throws TimeoutException
    {
        mHandler.loadTestModel(SXRTestUtils.GITHUB_URL + "jassimp/lightmap/normal_diffuse_specular_lightmap.fbx", "testNormalDiffuseSpecularLightmap",
                0.006f, true, new Vector3f( 0.05f, -0.05f, -4.5f));
    }


    @Test
    public void testDownloadTextureCache() throws MalformedURLException {
        final SXRContext sxr = mTestUtils.getSxrContext();
        final String urlString = SXRTestUtils.GITHUB_URL + "asset-tests/app/src/main/res/drawable-xxxhdpi/gearvr_logo.jpg";

        final String directoryPath = sxr.getContext().getCacheDir().getAbsolutePath();
        final String outputFilename = directoryPath + File.separator + UUID.nameUUIDFromBytes(urlString.getBytes()).toString() + "gearvr_logo.jpg";
        final File file = new File(outputFilename);
        Assert.assertFalse(file.exists());

        final URL url = new URL(urlString);
        SXRAndroidResource resource = new SXRAndroidResource(sxr, url, true);
        SXRAndroidResource.TextureCallback callback = new SXRAndroidResource.TextureCallback()
        {
            public void failed(Throwable t, SXRAndroidResource androidResource)
            {
                mWaiter.fail(t);
            }
            public boolean stillWanted(SXRAndroidResource r)
            {
                return true;
            }
            public void loaded(SXRImage image, SXRAndroidResource resource)
            {
                Assert.assertTrue(file.exists());
                file.delete();
                Assert.assertFalse(file.exists());
                mTestUtils.onAssetLoaded(null);
            }
        };
        sxr.getAssetLoader().loadTexture(resource, callback, sxr.getAssetLoader().getDefaultTextureParameters(),
                                         SXRAssetLoader.DEFAULT_PRIORITY, SXRCompressedImage.BALANCED);
        mTestUtils.waitForAssetLoad();
    }
}
