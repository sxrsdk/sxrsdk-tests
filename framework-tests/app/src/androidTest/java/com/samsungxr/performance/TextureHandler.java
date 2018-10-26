package com.samsungxr.performance;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

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
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRShaderId;
import com.samsungxr.SXRSpotLight;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.IAssetEvents;
import com.samsungxr.scene_objects.SXRCylinderSceneObject;
import com.samsungxr.tester.R;
import com.samsungxr.tester.TextureEventHandler;
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


public class TextureHandler implements IAssetEvents
{
    private SXRContext mContext;
    private boolean mTextureLoaded = false;
    private final Object onAssetLock = new Object();
    private final String TAG = "TEXTURE";

    public TextureHandler(SXRContext ctx)
    {
        mContext = ctx;
    }

    public void onAssetLoaded(SXRContext ctx, SXRSceneObject model, String fileName, String errors) { }
    public void onModelLoaded(SXRContext ctx, SXRSceneObject model, String fileName) { }
    public void onModelError(SXRContext ctx, String fileName, String errors) { }

    @Override
    public void onTextureLoaded(SXRContext context, SXRTexture texture, String filePath)
    {
        if (mTextureLoaded)
        {
            Log.e(TAG, "Unanticipated texture load " + filePath);
        }
        else
        {
            textureLoaded();
        }
    }

    @Override
    public void onTextureError(SXRContext context, String error, String filePath)
    {
        Log.e(TAG, "texture load failed for " + filePath);
        if (!mTextureLoaded)
        {
            textureLoaded();
        }
    }

    public SXRTexture loadTexture(SXRAndroidResource texResource)
    {
        mTextureLoaded = false;
        synchronized (onAssetLock)
        {
            SXRTexture tex = null;
            try
            {
                tex = mContext.getAssetLoader().loadTexture(texResource);
                Log.d(TAG, "Waiting for texture load");
                onAssetLock.wait();
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "", e);
                Thread.currentThread().interrupt();
                return null;
            }
            return tex;
        }
    }

    private void textureLoaded()
    {
        synchronized (onAssetLock)
        {
            onAssetLock.notifyAll();
        }
        Log.d(TAG, "allTexturesLoaded Called");
    }
};


