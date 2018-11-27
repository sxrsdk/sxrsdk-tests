package com.samsungxr.performance;

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
import com.samsungxr.SXRNode;
import com.samsungxr.SXRShaderId;
import com.samsungxr.SXRSpotLight;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.nodes.SXRCylinderNode;
import com.samsungxr.scene.TextureEventHandler;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.sdktests.R;

import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(AndroidJUnit4.class)


public class RenderSetup
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

    public RenderSetup(Waiter waiter, SXRTestUtils testUtils)
    {
        mTestUtils = testUtils;
        mWaiter = waiter;
        mScene = testUtils.getMainScene();
    }

    private SXRNode createQuad(SXRContext ctx, String meshDesc, SXRMaterial mtl, float scale)
    {
        SXRMesh quadMesh = new SXRMesh(ctx, meshDesc);
        SXRNode quad = new SXRNode(ctx, quadMesh, mtl);
        quadMesh.createQuad(scale, scale);
        return quad;
    }

    /*
     * They cylinder is 26080 vertices
     */
    private SXRNode createCylinder(SXRContext ctx, String meshDesc, SXRMaterial mtl, float scale)
    {
        SXRCylinderNode.CylinderParams params = new SXRCylinderNode.CylinderParams();
        params.Material = mtl;
        params.VertexDescriptor = meshDesc;
        params.Height = scale;
        params.TopRadius = scale / 2.0f;
        params.BottomRadius = scale / 2.0f;
        params.StackNumber = 80;
        params.SliceNumber = 80;
        params.HasBottomCap = false;
        params.HasTopCap = false;
        params.FacingOut = true;
        SXRNode cyl = new SXRCylinderNode(ctx, params);
        return cyl;
    }

    private void setRenderState(SXRRenderData renderData, Map<String, Object> renderState)
    {
        for (Map.Entry<String, Object> entry : renderState.entrySet())
        {
            String state = entry.getKey();
            Object value = entry.getValue();

            if (state.equals("renderingorder"))
            {
                renderData.setRenderingOrder((Integer) value);
            }
            else if (state.equals("enablelight"))
            {
                Integer i = (Integer) value;
                if (i == 0)
                {
                    renderData.disableLight();
                }
                else
                {
                    renderData.enableLight();
                }
            }
            else if (state.equals("cullface"))
            {
                SXRRenderPass.SXRCullFaceEnum cull = (SXRRenderPass.SXRCullFaceEnum) value;
                renderData.setCullFace(cull);
            }
            else if (state.equals("alphablend"))
            {
                Integer i = (Integer) value;
                if (i == 0)
                {
                    renderData.setAlphaBlend(false);
                }
                else
                {
                    renderData.setAlphaBlend(true);
                }
            }
            else if (state.equals("drawmode"))
            {
                Integer drawmode = (Integer) value;
                renderData.setDrawMode(drawmode);
            }
            else if (state.equals("castshadows"))
            {
                Integer i = (Integer) value;
                if (i == 0)
                {
                    renderData.setCastShadows(false);
                }
                else
                {
                    renderData.setCastShadows(true);
                }
            }
        }
    }

    private SXRTexture createBitmap(SXRContext ctx, int resourceId, SXRTextureParameters params)
    {
        SXRAndroidResource res = new SXRAndroidResource(ctx, resourceId);
        if (params != null)
        {
            return ctx.getAssetLoader().loadTexture(res, params);
        }
        return ctx.getAssetLoader().loadTexture(res);
    }

    private SXRTexture createCubemap(SXRContext ctx, int resourceID, SXRTextureParameters params)
    {
        SXRAndroidResource res = new SXRAndroidResource(ctx, resourceID);
        SXRTexture tex = ctx.getAssetLoader().loadCubemapTexture(res);
        if (params != null)
        {
            tex.updateTextureParameters(params);
        }
        return tex;
    }

    private SXRTexture createCompressedCubemap(SXRContext ctx, int resourceID, SXRTextureParameters params)
    {
        SXRAndroidResource res = new SXRAndroidResource(ctx, resourceID);
        SXRTexture tex = ctx.getAssetLoader().loadCompressedCubemapTexture(res);
        if (params != null)
        {
            tex.updateTextureParameters(params);
        }
        return tex;
    }

    private String createMeshDesc(boolean doLight, boolean doSkin, boolean doTexture)
    {
        String meshDesc = "float3 a_position";
        if (doTexture)
        {
            meshDesc += " float2 a_texcoord";
        }
        if (doLight)
        {
            meshDesc += " float3 a_normal";
        }
        if (doSkin)
        {
            meshDesc += " float4 a_bone_weights int4 a_bone_indices";
        }
        return meshDesc;
    }

    private void createLights(SXRContext ctx, Map<String, Object> params)
    {
        if (params.containsKey("enablelight"))
        {
            boolean castShadows = false;
            Integer i = (Integer) params.get("enablelight");
            if (i != 0)
            {
                i = (Integer) params.get("castshadows");
                if (i != 0)
                {
                    castShadows = true;
                }
                if (params.containsKey("phong_spotlight"))
                {
                    SXRNode lightObj = new SXRNode(ctx);
                    SXRSpotLight spotLight = new SXRSpotLight(ctx);
                    lightObj.attachLight(spotLight);
                    spotLight.setCastShadow(castShadows);
                    mScene.addNode(lightObj);
                }
                if (params.containsKey("phong_directlight"))
                {
                    SXRNode lightObj = new SXRNode(ctx);
                    SXRDirectLight directLight = new SXRDirectLight(ctx);
                    lightObj.attachLight(directLight);
                    lightObj.getTransform().rotateByAxis(90.0f, 1, 0, 0);
                    directLight.setCastShadow(castShadows);
                    mScene.addNode(lightObj);
                }
                if (params.containsKey("phong_pointlight"))
                {
                    SXRNode lightObj = new SXRNode(ctx);
                    SXRPointLight pointLight = new SXRPointLight(ctx);
                    lightObj.attachLight(pointLight);
                    lightObj.getTransform().setPosition(-5.0f, 0, 0);
                    mScene.addNode(lightObj);
                }
            }
        }
    }

    private SXRMaterial createMaterial(SXRContext ctx, Map<String, Object> params)
    {
        SXRTexture tex = null;
        SXRMaterial material;
        SXRShaderId shaderId = SXRMaterial.SXRShaderType.Texture.ID;
        TextureEventHandler waitForLoad = null;

        if (params.containsKey("enablelight"))
        {
            Integer i = (Integer) params.get("enablelight");
            if (i != 0)
            {
                shaderId = SXRMaterial.SXRShaderType.Phong.ID;
            }
        }
        if (params.containsKey("shaderid"))
        {
            shaderId = (SXRShaderId) params.get("shaderid");
        }
        if (params.containsKey("bitmap"))
        {
            if (mBitmapImage == null)
            {
                waitForLoad = new TextureEventHandler(mTestUtils, 1);
                ctx.getEventReceiver().addListener(waitForLoad);
                tex = createBitmap(ctx, (Integer) params.get("bitmap"), null);
                mTestUtils.waitForAssetLoad();
                waitForLoad.checkTextureLoaded(mWaiter);
                mBitmapImage = tex.getImage();
                mWaiter.assertNotNull(mBitmapImage);
            }
            else
            {
                tex = new SXRTexture(ctx);
                mWaiter.assertNotNull(mBitmapImage);
                tex.setImage(mBitmapImage);
            }
            material = new SXRMaterial(ctx, shaderId);
            material.setMainTexture(tex);
        }
        else if (params.containsKey("compressedbitmap"))
        {
            if (mCompressedImage == null)
            {
                waitForLoad = new TextureEventHandler(mTestUtils, 1);
                ctx.getEventReceiver().addListener(waitForLoad);
                tex = createBitmap(ctx, (Integer) params.get("compressedbitmap"), null);
                mTestUtils.waitForAssetLoad();
                waitForLoad.checkTextureLoaded(mWaiter);
                mCompressedImage = tex.getImage();
                mWaiter.assertNotNull(mCompressedImage);
            }
            else
            {
                tex = new SXRTexture(ctx);
                mWaiter.assertNotNull(mCompressedImage);
                tex.setImage(mCompressedImage);
            }
            material = new SXRMaterial(ctx, shaderId);
            material.setMainTexture(tex);
        }
        else if (params.containsKey("cubemap"))
        {
            if (mCubemapImage == null)
            {
                waitForLoad = new TextureEventHandler(mTestUtils, 1);
                ctx.getEventReceiver().addListener(waitForLoad);
                tex = createCubemap(ctx, (Integer) params.get("cubemap"), null);
                mTestUtils.waitForAssetLoad();
                mCubemapImage = tex.getImage();
            }
            else
            {
                tex = new SXRTexture(ctx);
                tex.setImage(mCubemapImage);
            }
            shaderId = SXRMaterial.SXRShaderType.Cubemap.ID;
            material = new SXRMaterial(ctx, shaderId);
            material.setMainTexture(tex);
        }
        else
        {
            float red = 0.3f + ((float) Math.random()) * 0.7f;
            float green = 0.3f + ((float) Math.random()) * 0.7f;
            float blue = 0.3f + ((float) Math.random()) * 0.7f;

            material = new SXRMaterial(ctx, shaderId);
            if (shaderId == SXRMaterial.SXRShaderType.Texture.ID)
            {
                material.setColor(red, green, blue);
            }
            else
            {
                material.setDiffuseColor(red, green, blue, 1.0f);
            }
        }
        if (waitForLoad != null)
        {
            ctx.getEventReceiver().removeListener(waitForLoad);
        }
        return material;
    }

    private SXRNode createGeometry(SXRContext ctx, SXRMaterial material, Map<String, Object> params)
    {
        boolean doLight = false;
        boolean doSkin = false;
        boolean doTexture = false;
        String meshDesc;
        SXRNode geometry = null;
        float scale = 1.0f;

        if (params.containsKey("enablelight"))
        {
            Integer i = (Integer) params.get("enablelight");
            if (i != 0)
            {
                doLight = true;
            }
        }
        if (params.containsKey("skinning"))
        {
            Integer i = (Integer) params.get("skinning");
            if (i != 0)
            {
                doSkin = true;
            }
        }
        if (params.containsKey("bitmap"))
        {
            doTexture = true;
        }
        else if (params.containsKey("cubemap"))
        {
            doTexture = true;
        }
        meshDesc = createMeshDesc(doLight, doSkin, doTexture);
        if (params.containsKey("quadgeometry"))
        {
            geometry = createQuad(ctx, meshDesc, material, scale);
        }
        else if (params.containsKey("cylindergeometry"))
        {
            geometry = createCylinder(ctx, meshDesc, material, scale);
        }
        return geometry;
    }

    public void createTestScene(SXRContext ctx, Map<String, Object> params)
    {
        int nrows = (Integer) params.get("rows");
        int ncols = (Integer) params.get("columns");
        float zpos = (nrows > ncols) ? (float) nrows : (float) ncols;
        SXRMaterial sourceMtl = createMaterial(ctx, params);
        SXRNode sourceObj = createGeometry(ctx, sourceMtl, params);
        SXRMesh sourceMesh = sourceObj.getRenderData().getMesh();
        SXRNode root = new SXRNode(ctx);

        mScene.setBackgroundColor(0.8f, 1.0f, 0.8f, 1.0f);
        //createLights(ctx, params);
        for (int y = 0; y < nrows; ++y)
        {
            float ypos = (y - nrows / 2.0f);

            for (int x = 0; x < ncols; ++x)
            {
                float xpos = (x - ncols / 2.0f);
                SXRNode testObj;
                SXRMaterial material = sourceMtl;

                if (!params.containsKey("share_material"))
                {
                    material = createMaterial(ctx, params);
                }
                if (params.containsKey("share_geometry"))
                {
                    testObj = new SXRNode(ctx, sourceMesh, material);
                }
                else
                {
                    testObj = createGeometry(ctx, material, params);
                }
                setRenderState(testObj.getRenderData(), params);
                testObj.getTransform().setPosition(xpos, ypos, -zpos);
                root.addChildObject(testObj);
            }
        }
        mScene.addNode(root);
    }

}

