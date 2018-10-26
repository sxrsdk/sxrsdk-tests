package com.samsungxr.tester;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRShader;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTransform;
import com.samsungxr.IAssetEvents;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRImportSettings;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.utility.FileNameUtils;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.TimeoutException;

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
    protected int mWaitFrames = 0;

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
        if (model != null)
        {
            if (mAddToScene)
            {
                mScene.addNode(model);
                if (mWaitFrames > 0)
                {
                    mTester.waitForXFrames(mWaitFrames);
                }
            }
            mTester.onAssetLoaded(model);
        }
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

    public void DisableImageCompare()
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

    public void centerModel(SXRNode model, SXRTransform camTrans)
    {
        SXRNode.BoundingVolume bv = model.getBoundingVolume();
        float x = camTrans.getPositionX();
        float y = camTrans.getPositionY();
        float z = camTrans.getPositionZ();
        float sf = 1 / bv.radius;
        model.getTransform().setScale(sf, sf, sf);
        bv = model.getBoundingVolume();
        model.getTransform().setPosition(x - bv.center.x, y - bv.center.y, z - bv.center.z - 1.5f * bv.radius);
    }

    public SXRNode loadTestModel(String modelfile, int numtex)
    {
        SXRContext ctx  = mTester.getSxrContext();
        SXRScene scene = mTester.getMainScene();
        SXRNode model = null;

        ctx.getEventReceiver().addListener(this);
        try
        {
            model = ctx.getAssetLoader().loadModel(modelfile, scene);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTester.waitForAssetLoad();
        centerModel(model, scene.getMainCameraRig().getTransform());
        return model;
    }

    public SXRNode loadTestModel(String modelfile, int numTex, int texError, String testname) throws TimeoutException
    {
        SXRContext ctx  = mTester.getSxrContext();
        SXRScene scene = mTester.getMainScene();
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
        mTester.waitForXFrames(10);

        centerModel(model, scene.getMainCameraRig().getTransform());
        checkAssetLoaded(FileNameUtils.getFilename(modelfile), numTex);
        checkAssetErrors(0, texError);
        if (testname != null)
        {
            mTester.screenShot(mCategory, testname, mWaiter, mDoCompare);
        }
        return model;
    }

    public SXRNode loadTestModel(SXRAndroidResource res, int numTex, int texError, String testname) throws TimeoutException
    {
        SXRContext ctx  = mTester.getSxrContext();
        SXRScene scene = mTester.getMainScene();
        SXRNode model = null;
        SXRTransform t = scene.getMainCameraRig().getTransform();

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
        centerModel(model, t);
        checkAssetLoaded(res.getResourceFilename(), numTex);
        checkAssetErrors(0, texError);
        if (testname != null)
        {
            mTester.waitForXFrames(2);
            mTester.screenShot(mCategory, testname, mWaiter, mDoCompare);
        }
        return model;
    }

    public SXRNode loadTestModel(String modelfile, String testname,
                                        float scale, boolean rotX90, Vector3f pos) throws TimeoutException
    {
        SXRContext ctx  = mTester.getSxrContext();
        SXRScene scene = mTester.getMainScene();
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
        SXRTransform modelTrans = model.getTransform();
        modelTrans.setScale(scale, scale, scale);
        if (rotX90)
        {
            modelTrans.rotateByAxis(90.0f, 1, 0, 0);
        }
        if (pos != null)
        {
            SXRNode.BoundingVolume bv = model.getBoundingVolume();
            modelTrans.setPosition(pos.x - bv.center.x, pos.y - bv.center.y, pos.z - bv.center.z);
        }
        else
        {
            centerModel(model, scene.getMainCameraRig().getTransform());
        }
        checkModelLoaded(FileNameUtils.getFilename(modelfile));

        if (testname != null)
        {
            mTester.waitForXFrames(2);
            mTester.screenShot(mCategory, testname, mWaiter, mDoCompare);
        }
        return model;
    }

    public void loadTestScene(String modelfile, int numTex, String testname) throws TimeoutException
    {
        SXRContext ctx  = mTester.getSxrContext();
        SXRScene scene = mTester.getMainScene();
        SXRNode model = null;

        ctx.getEventReceiver().addListener(this);
        try
        {
            model = ctx.getAssetLoader().loadScene(modelfile, scene);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTester.waitForAssetLoad();
        checkAssetLoaded(FileNameUtils.getFilename(modelfile), numTex);
        checkAssetErrors(0, 0);
        if (testname != null)
        {
            mTester.waitForXFrames(2);
            mTester.screenShot(mCategory, testname, mWaiter, mDoCompare);
        }
    }

};

