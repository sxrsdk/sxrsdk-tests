
package com.samsungxr.stress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.os.Environment;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRBitmapImage;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRNotifications;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.io.TestSendEvents;
import com.samsungxr.tester.R;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StressTests {
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;

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

        SXRScene scene = mTestUtils.getMainScene();
        mWaiter.assertNotNull(scene);
    }

    /**
     * Used to crash; verifies it doesn't anymore.
     * @throws TimeoutException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void stressRenderDataDirty() throws TimeoutException, InterruptedException, ExecutionException {
        final SXRContext ctx = mTestUtils.getSxrContext();
        final SXRScene scene = mTestUtils.getMainScene();

        final Bitmap gearvr_logo = BitmapFactory.decodeResource(ctx.getActivity().getResources(), R.drawable.gearvr_logo);
        final SXRMaterial material = new SXRMaterial(ctx);
        final SXRMesh mesh = ctx.createQuad(4, 2);
        final SXRRenderPass pass = new SXRRenderPass(ctx);
        pass.setMaterial(material);

        try {
            for (int testRun = 0; testRun < 2000; ++testRun) {
                SXRTexture t = new SXRTexture(ctx);
                SXRBitmapImage bmap = new SXRBitmapImage(ctx, gearvr_logo);
                t.setImage(bmap);
                final SXRNode so1 = new SXRNode(ctx, 3, 2, t);
                so1.getTransform().setPosition(0, 0, -3);
                so1.getRenderData().setMaterial(material);
                so1.getRenderData().setMesh(mesh);
                so1.getRenderData().addPass(pass);
                scene.addNode(so1);

                final SXRNode so2 = new SXRNode(ctx, 2, 1, t);
                so2.getTransform().setPosition(-1, -1, -3);
                so2.getRenderData().setMaterial(material);
                so2.getRenderData().setMesh(mesh);
                so2.getRenderData().addPass(pass);
                scene.addNode(so2);

                //dirty the updateGPU data; allocate a big buffer to create some memory pressure
                //and have the gc run sooner
                scene.clear();
                byte[] b = new byte[1*1024*1024];
                pass.setCullFace(SXRRenderPass.SXRCullFaceEnum.None);
                final float[] texCoords = mesh.getTexCoords();
                mesh.setFloatArray("a_texcoord", texCoords);
                material.setDiffuseColor(0, 0, 0, 0);
                SXRNotifications.waitAfterStep();
            }
        } catch (final Throwable t) {
            t.printStackTrace();
            mWaiter.assertTrue(false);
        }
    }

    @Test
    @UiThreadTest
    public void gcOomTest1() throws Exception {
        oomTest(false);
    }

    @Test
    @UiThreadTest
    public void gcOomTest2() throws Exception {
        oomTest(true);
    }

    //  Change this between true & false to either trigger an OutOfMemoryError exception or a
    // "global reference table overflow" crash.
    private void oomTest(boolean createBitmap) throws IOException {
        try {
            final int MaxInstances = 100000;
            SXRContext sxrContext = ActivityRule.getActivity() .getSXRContext();
            for (int count = 0; count < MaxInstances; count++) {
                Log.d(TAG, "Count: " + count);
                SXRNode sceneObject = new SXRNode(sxrContext, sxrContext.createQuad(10f, 10f));
                SXRRenderData renderData = sceneObject.getRenderData();
                SXRTexture texture;
                if (createBitmap) {
                    Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
                    texture = new SXRTexture(sxrContext);
                    texture.setImage(new SXRBitmapImage(sxrContext, bitmap));
                } else {
                    texture = sxrContext.getAssetLoader().loadTexture(new SXRAndroidResource(sxrContext, "StencilTests/GearVR.jpg"));
                }
                renderData.getMaterial().setMainTexture(texture);
                renderData.setAlphaBlend(true);
                if ((count % 100) == 99) {
                    System.gc();
                    System.runFinalization();
                }
            }
        } catch (OutOfMemoryError oom) {
            HeapDump();
            fail(oom.getMessage());
        }
    }

    private void HeapDump() {
        Log.d(TAG, "Dumping heap");
        try {
            File external = Environment.getExternalStorageDirectory();
            File folder = new File(external, "Documents");
            File heapDumpFile1 = new File(folder, "oom.hprof");
            Debug.dumpHprofData(heapDumpFile1.getPath());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Log.d(TAG, "Finished heap dump");
    }


    @Test
    public void testSendEvents() {
        final boolean result = new TestSendEvents().test1(mTestUtils.getSxrContext());
        if (!result) {
            throw new AssertionError("test1() returned false");
        }
    }

    private final static String TAG = "MiscTests";
}
