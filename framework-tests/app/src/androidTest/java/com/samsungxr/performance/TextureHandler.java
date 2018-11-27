package com.samsungxr.performance;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.IAssetEvents;
import com.samsungxr.utility.Log;


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

    public void onAssetLoaded(SXRContext ctx, SXRNode model, String fileName, String errors) { }
    public void onModelLoaded(SXRContext ctx, SXRNode model, String fileName) { }
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


