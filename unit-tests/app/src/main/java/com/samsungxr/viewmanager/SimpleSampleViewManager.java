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
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRScript;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;

import android.graphics.Color;

public class SimpleSampleViewManager extends SXRScript {

    private SXRContext mSXRContext;

    @Override
    public void onInit(SXRContext sxrContext) {

        // save context for possible use in onStep(), even though that's empty
        // in this sample
        mSXRContext = sxrContext;

        SXRScene scene = sxrContext.getNextMainScene();

        // set background color
        SXRCameraRig mainCameraRig = scene.getMainCameraRig();
        mainCameraRig.getLeftCamera()
                .setBackgroundColor(Color.WHITE);
        mainCameraRig.getRightCamera()
                .setBackgroundColor(Color.WHITE);

        // load texture
        SXRTexture texture = sxrContext.loadTexture(new SXRAndroidResource(
                mSXRContext, R.drawable.gearvr_logo));

        // create a scene object (this constructor creates a rectangular scene
        // object that uses the standard 'unlit' shader)
        SXRNode sceneObject = new SXRNode(sxrContext, 4.0f, 2.0f,
                texture);

        // set the scene object position
        sceneObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);

        // add the scene object to the scene graph
        scene.addNode(sceneObject);
    }

    @Override
    public void onStep() {

    }
}
