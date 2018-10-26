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

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRScript;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.scene_objects.SXRCubeSceneObject;

import android.util.Log;

public class testCubeScript extends SXRScript {

    private static final float CUBE_WIDTH = 20.0f;
    private SXRContext mSXRContext = null;

    @Override
    public void onInit(SXRContext sxrContext) {
        mSXRContext = sxrContext;

        SXRScene scene = mSXRContext.getNextMainScene();

        ArrayList<Future<SXRTexture>> futureTextureList = new ArrayList<Future<SXRTexture>>(
                6);
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(mSXRContext,
                        R.drawable.back)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(mSXRContext,
                        R.drawable.right)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(mSXRContext,
                        R.drawable.front)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(mSXRContext,
                        R.drawable.left)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(mSXRContext,
                        R.drawable.top)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(mSXRContext,
                        R.drawable.bottom)));
        SXRSceneObject mCube = new SXRCubeSceneObject(sxrContext,
                false, futureTextureList);
        mCube.setName("cube");
        mCube.getTransform().setScale(CUBE_WIDTH, CUBE_WIDTH,
                CUBE_WIDTH);
        scene.addSceneObject(mCube);

        for (SXRSceneObject so : scene.getWholeSceneObjects()) {
            Log.v("", "scene object name : " + so.getName());
        }
    }

    @Override
    public void onStep() {
        FPSCounter.tick();
    }
}
