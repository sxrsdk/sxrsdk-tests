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

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRScript;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;

public class CockpitViewManager extends SXRScript {

    private SXRContext mSXRContext = null;
    private SXRSceneObject mShipSceneObject = null;
    private SXRSceneObject mSpaceSceneObject = null;

    //@Override
    //public SplashMode getSplashMode() {
    //    return SplashMode.NONE;
    //}

    @Override
    public void onInit(SXRContext sxrContext) {

        mSXRContext = sxrContext;
        SXRScene mainScene = mSXRContext.getNextMainScene();

        mainScene.getMainCameraRig().getTransform()
                .setPosition(0.0f, 6.0f, 1.0f);

        SXRMesh shipMesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                mSXRContext, R.raw.sxrf_ship_mesh));
        SXRMesh spaceMesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                mSXRContext, R.raw.sxrf_space_mesh));

        SXRTexture shipTexture = sxrContext.loadTexture(new SXRAndroidResource(
                mSXRContext, R.drawable.sxrf_ship));
        mShipSceneObject = new SXRSceneObject(sxrContext, shipMesh, shipTexture);
        SXRTexture spaceTexture = sxrContext.loadTexture(new SXRAndroidResource(mSXRContext,R.drawable.sxrf_space));
        mSpaceSceneObject = new SXRSceneObject(sxrContext, spaceMesh,
                spaceTexture);

        mainScene.addSceneObject(mShipSceneObject);
        mainScene.addSceneObject(mSpaceSceneObject);

    }

    @Override
    public void onStep() {
    }

}
