package com.samsungxr.assettests;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTransform;
import com.samsungxr.IAssetEvents;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRImportSettings;
import com.samsungxr.animation.SXRSkeleton;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.utility.FileNameUtils;
import com.samsungxr.utility.Log;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static android.content.ContentValues.TAG;

class AssetEventHandler implements IAssetEvents
{
    public int TexturesLoaded = 0;
    public int ModelsLoaded = 0;
    public int TextureErrors = 0;
    public int ModelErrors = 0;
    public String AssetErrors = null;
    protected SXRScene mScene;
    protected Waiter mWaiter;
    protected SXRTestUtils mTester;
    protected String mCategory;
    protected boolean mDoCompare = true;
    protected boolean mAddToScene = true;
    protected int mWaitFrames = 4;

    AssetEventHandler(SXRScene scene, Waiter waiter, SXRTestUtils tester, String category)
    {
        mScene = scene;
        mWaiter = waiter;
        mTester = tester;
        mCategory = category;
        mAddToScene = true;
    }

    public void setWaitFrames(int frames) { mWaitFrames = frames; }

    public void dontAddToScene()
    {
        mAddToScene = false;
    }

    public void onAssetLoaded(SXRContext context, SXRNode model, String filePath, String errors)
    {
        AssetErrors = errors;
        if (model == null)
        {
            mWaiter.fail(errors);
        }
        int wait = mWaitFrames;
        if (mAddToScene)
        {
            mScene.addNode(model);
            if (wait > 0)
            {
                mTester.waitForXFrames(wait);
            }
            centerModel(model);
        }
        else
        {
            mTester.waitForXFrames(wait);
        }
        mTester.onAssetLoaded(model);
    }

    public void onModelLoaded(SXRContext context, SXRNode model, String filePath)
    {
        ModelsLoaded++;
    }

    public void onTextureLoaded(SXRContext context, SXRTexture texture, String filePath)
    {
        TexturesLoaded++;
    }

    public void onModelError(SXRContext context, String error, String filePath)
    {
        ModelErrors++;
    }

    public void onTextureError(SXRContext context, String error, String filePath)
    {
        TextureErrors++;
    }

    public void disableImageCompare()
    {
        mDoCompare = false;
    }

    public void checkModelLoaded(String name)
    {
        mWaiter.assertEquals(1, ModelsLoaded);
        mWaiter.assertEquals(0, ModelErrors);
        if (name != null)
        {
            mWaiter.assertNotNull(mScene.getNodeByName(name));
        }
    }

    public void checkAssetLoaded(String name, int numTex)
    {
        mWaiter.assertEquals(1, ModelsLoaded);
        mWaiter.assertEquals(0, ModelErrors);
        mWaiter.assertEquals(numTex, TexturesLoaded);
        if (name != null)
        {
            mWaiter.assertNotNull(mScene.getNodeByName(name));
        }
    }

    public void checkAssetErrors(int numModelErrors, int numTexErrors)
    {
        mWaiter.assertEquals(numModelErrors, ModelErrors);
        mWaiter.assertEquals(numTexErrors, TextureErrors);
    }

    public void centerModel(SXRNode model)
    {
        SXRTransform camTrans = mScene.getMainCameraRig().getTransform();
        float x = camTrans.getPositionX();
        float y = camTrans.getPositionY();
        float z = camTrans.getPositionZ();

        List<SXRSkeleton> skeletons = model.getAllComponents(SXRSkeleton.getComponentType());

        if (skeletons.size() > 0)
        {
            SXRSkeleton skel = skeletons.get(0);
            SXRNode bone = skel.getBone(0);
            Matrix4f mtx1 = bone.getTransform().getModelMatrix4f();
            Matrix4f mtx2 = new Matrix4f();
            float[] bv = skeletons.get(0).getBound();
            Vector4f cmin = new Vector4f(bv[0], bv[1], bv[2], 1);
            Vector4f cmax = new Vector4f(bv[3], bv[4], bv[5], 1);

            skel.getPose().getLocalMatrix(0, mtx2);
            mtx2.invert(mtx2);
            mtx1.mul(mtx2, mtx2);
            cmin.mul(mtx2);
            cmax.mul(mtx2);

            float cx = (cmax.x + cmin.x) / 2;
            float cy = (cmax.y + cmin.y) / 2;
            float cz = (cmax.z + cmin.z) / 2;
            float r = Math.max(cmax.x - cmin.x,
                               Math.max(cmax.y - cmin.y,
                                        cmax.z - cmin.z));
            float sf = 0.5f / r;
            model.getTransform().setScale(sf, sf, sf);
            cx *= sf;
            cy *= sf;
            cz *= sf;
            model.getTransform().setPosition(x - cx, y - cy, z - cz - 1.5f);
        }
        else
        {
            SXRNode.BoundingVolume bv = model.getBoundingVolume();
            float sf = 1 / bv.radius;
            mWaiter.assertTrue((sf > 0.00001f) && (sf < 100000.0f));
            model.getTransform().setScale(sf, sf, sf);
            bv = model.getBoundingVolume();
            model.getTransform().setPosition(x - bv.center.x, y - bv.center.y, z - bv.center.z - 1.5f);
        }
    }


    public SXRNode loadTestModel(String modelfile, int numTex, int texError, String testname) throws TimeoutException
    {
        SXRContext ctx  = mTester.getSxrContext();
        SXRNode model = null;

        try
        {
            model = ctx.getAssetLoader().loadModel(modelfile, this);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTester.waitForAssetLoad();
        checkAssetLoaded(FileNameUtils.getFilename(modelfile), numTex);
        checkAssetErrors(0, texError);
        if (testname != null)
        {
            mTester.waitForXFrames(mWaitFrames);
            mTester.screenShot(mCategory, testname, mWaiter, mDoCompare);
        }
        return model;
    }

    public SXRNode loadTestModel(SXRAndroidResource res, int numTex, int texError, String testname) throws TimeoutException
    {
        SXRContext ctx  = mTester.getSxrContext();
        SXRScene scene = mTester.getMainScene();
        SXRNode model = null;

        ctx.getEventReceiver().addListener(this);
        try
        {
            model = ctx.getAssetLoader().loadModel(res,
                    SXRImportSettings.getRecommendedSettings(), true, scene);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTester.waitForAssetLoad();
        checkAssetLoaded(res.getResourceFilename(), numTex);
        checkAssetErrors(0, texError);
        if (testname != null)
        {
            mTester.waitForXFrames(mWaitFrames);
            mTester.screenShot(mCategory, testname, mWaiter, mDoCompare);
        }
        return model;
    }

};

