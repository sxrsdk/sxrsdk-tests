package com.samsungxr.scene;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.IAssetEvents;
import com.samsungxr.unittestutils.SXRTestUtils;

public class TextureEventHandler implements IAssetEvents
{
    public int TexturesLoaded = 0;
    public int TextureErrors = 0;
    protected SXRTestUtils mTester;
    protected int mNumTextures = 0;

    public TextureEventHandler(SXRTestUtils tester, int numTex)
    {
        mTester = tester;
        mNumTextures = numTex;
    }

    public void reset() { TexturesLoaded = 0; TextureErrors = 0; }
    public void onAssetLoaded(SXRContext context, SXRNode model, String filePath, String errors) { }
    public void onModelLoaded(SXRContext context, SXRNode model, String filePath) { }
    public void onModelError(SXRContext context, String error, String filePath) { }

    public void onTextureLoaded(SXRContext context, SXRTexture texture, String filePath)
    {
        TexturesLoaded++;
        if ((TexturesLoaded + TextureErrors) == mNumTextures)
        {
            mTester.onAssetLoaded(null);
        }
    }

    public void onTextureError(SXRContext context, String error, String filePath)
    {
        TextureErrors++;
        if ((TexturesLoaded + TextureErrors) == mNumTextures)
        {
            mTester.onAssetLoaded(null);
        }
    }

    public void checkTextureLoaded(Waiter waiter)
    {
        waiter.assertEquals(1, TexturesLoaded);
    }

};

