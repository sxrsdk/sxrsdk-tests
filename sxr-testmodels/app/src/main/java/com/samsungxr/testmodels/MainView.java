/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsungxr.testmodels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRSceneObject.BoundingVolume;
import com.samsungxr.SXRScreenshotCallback;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRMain;
import com.samsungxr.scene_objects.SXRModelSceneObject;
import com.samsungxr.utility.FileNameUtils;
import com.samsungxr.utility.Log;
import com.samsungxr.IAssetEvents;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

public class MainView extends SXRMain
{
    class AssetListener implements IAssetEvents
    {
        @Override
        public void onAssetLoaded(SXRContext ctx, SXRSceneObject model, String filename, String errors)
        {
            if (model != null)
            {
                mState = 10;
                BoundingVolume bv = model.getBoundingVolume();
                float sf = 1 / (4.0f * bv.radius);
                model.getTransform().setScale(sf, sf, sf);
                bv = model.getBoundingVolume();
                model.getTransform().setPosition(-bv.center.x, -bv.center.y, -bv.center.z - 1.5f * bv.radius);
                mMainScene.clear();
                mCurrentModel = (SXRModelSceneObject) model;
                if (mMainScene.getLightList().length == 0)
                {
                	SXRDirectLight headlight = new SXRDirectLight(ctx);
                	mMainScene.getMainCameraRig().getOwnerObject().attachComponent(headlight);
                    mMainScene.addSceneObject(model);
                }
             }
            else
            {
                mState = 0;
            }
        }
        public void onModelError(SXRContext arg0, String arg1, String arg2) { }
        public void onModelLoaded(SXRContext arg0, SXRSceneObject arg1, String arg2) { }
        public void onTextureError(SXRContext arg0, String arg1, String arg2) { }
        public void onTextureLoaded(SXRContext arg0, SXRTexture arg1, String arg2) { }        
    }

    private static final String TAG = Log
            .tag(MainActivity.class);

    private SXRScene mMainScene;
    private SXRContext mContext;
    private int mCurrentFileIndex;
    private String mFileName;
    private ArrayList<String> mFileList = new ArrayList<String>();
    private SXRModelSceneObject mCurrentModel;
    private int mState = 0;
    private AssetListener mAssetListener;

    @Override
    public void onInit(SXRContext sxrContext) {
        mContext = sxrContext;
        mMainScene = sxrContext.getNextMainScene();
        mMainScene.setFrustumCulling(true);
        mCurrentFileIndex = 0;
        mCurrentModel = null;
        getModelList("GearVRF", mFileList);
        SXRCameraRig mainCameraRig = mMainScene.getMainCameraRig();
        mainCameraRig.getLeftCamera().setBackgroundColor(Color.WHITE);
        mainCameraRig.getRightCamera().setBackgroundColor(Color.WHITE);
        mainCameraRig.getTransform().setPosition(0.0f, 0.0f, 0.0f);
        mAssetListener = new AssetListener();
    }

    public SXRModelSceneObject loadModel(String filename)
    {
        SXRModelSceneObject model = (SXRModelSceneObject) mMainScene.getSceneObjectByName(filename);
        if (model == null)
        {
            try
            {
               model = mContext.getAssetLoader().loadModel("sd:" + filename, mAssetListener);
               model.setName(filename);
            }
            catch (IOException e)
            {
                Log.e(TAG, "Failed to load model: %s", e);
                mState = 0;
                return null;
            }
        }
        return model;
    }

    private void getModelList(String directory, ArrayList<String> fileList)
    {
        final String environmentPath = Environment.getExternalStorageDirectory().getPath();
        String extensions = ".fbx .3ds .dae .obj .ma .x3d";
        File dir = new File(environmentPath + "/" + directory);

        if (dir.exists() && dir.isDirectory())
        {
            File list[] = dir.listFiles();
            for (File f : list)
            {
                String fileName = f.getName();
                String ext = "." + FileNameUtils.getExtension(fileName.toLowerCase());
                if (f.isDirectory())
                {
                    getModelList(directory + "/" + fileName, fileList);
                }
                else if (extensions.contains(ext))
                {
                    fileList.add(directory + "/" + fileName);
                }
             }
        }
    }
    
    public void onTap() {}
    
    public void onStep()
    {
        if (mState == 0)
        {
            mState = 1;     	// 1 = model loading
            switchModel();
        }
        else if (mState >= 2)	// >2 = wait for display
        {
        	if (mState == 2)	// 2 = taking screenshot
        	{
            	screenShot(mFileName);        		
        	}
        	--mState;
        }
    }
    
    void switchModel()
    {
        if (mCurrentFileIndex >= mFileList.size())
        {
            return;          
        }
        mFileName = mFileList.get(mCurrentFileIndex++);
        SXRModelSceneObject model = loadModel(mFileName);
        if (model == null)
        {
            return;
        }
        if (mCurrentModel != null)
        {
            mCurrentModel.setEnable(false);
        }
        model.setEnable(true);
        mCurrentModel = model;
    }

    void screenShot(final String filename)
    {
        SXRScreenshotCallback callback = new SXRScreenshotCallback() 
        {
            @Override
            public void onScreenCaptured(Bitmap bitmap)
            {
                try
                {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    File sdcard = Environment.getExternalStorageDirectory();
                    int i = filename.lastIndexOf("/");
                    String basename = filename;
                    if (i > 0)
                    {
                        basename = filename.substring(i + 1);
                    }
                    basename = FileNameUtils.getBaseName(basename);
                    String fname = sdcard.getAbsolutePath() + "/GearVRF/" + basename + "_screen.png";
                    File f = new File(fname);
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    Log.d(TAG, "Saved screenshot of %s", filename);
                    mState = 0;
                }
                catch (Exception e)
                {
                    Log.d(TAG, "Could not save screenshot of %s", filename);
                    mState = 0;
                }
            }   
        };
        mContext.captureScreenCenter(callback);
    }
}
