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

package com.samsungxr.viewmanager;

import java.io.IOException;

import com.samsungxr.SXRActivity;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRScript;
import com.samsungxr.utility.Log;

public class panoramaViewManager extends SXRScript {

    private static final String TAG = "SampleViewManager";

    private SXRContext mSXRContext = null;

    private SXRActivity mActivity;

    panoramaViewManager(SXRActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onInit(SXRContext sxrContext) {
        mSXRContext = sxrContext;

        SXRMesh mesh = null;
        SXRNode leftScreen = null;
        SXRNode rightScreen = null;

        /*
         * This sample places its resources in the assets folder, not in
         * res/drawable and res/raw. This means that the named file may not
         * actually exist when we try to load resources, throwing an
         * IOException.
         */
        try {
            // If "cylinder.obj" exists - but is not a valid ASSIMP mesh file -
            // loadMesh() will return null.
            mesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(mSXRContext,
                    "cylinder.obj"));

            leftScreen = new SXRNode(sxrContext, mesh,
                    sxrContext.loadTexture(new SXRAndroidResource(mSXRContext,
                            "sample_20140509_l.bmp")));
            rightScreen = new SXRNode(sxrContext, mesh,
                    sxrContext.loadTexture(new SXRAndroidResource(mSXRContext,
                            "sample_20140509_r.bmp")));
        } catch (IOException e) {
            e.printStackTrace();
            leftScreen = null;
            rightScreen = null;
        }

        if (mesh == null || leftScreen == null || rightScreen == null) {
            mActivity.finish();
            Log.e(TAG, "Error loading resources - stopping application!");
        }

        // activity was stored in order to stop the application if the mesh is
        // not loaded. Since we don't need anymore, we set it to null to reduce
        // chance of memory leak.
        mActivity = null;

        SXRScene mainScene = mSXRContext.getNextMainScene();

        mainScene.addNode(leftScreen);
        mainScene.addNode(rightScreen);
    }

    @Override
    public void onStep() {
    }

}
