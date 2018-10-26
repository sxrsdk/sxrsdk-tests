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
import java.util.ArrayList;
import java.util.List;

import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRScript;
import com.samsungxr.animation.SXRAnimation;
import com.samsungxr.animation.SXRAnimationEngine;
import com.samsungxr.animation.SXRRepeatMode;
import com.samsungxr.animation.SXRRotationByAxisWithPivotAnimation;
import com.samsungxr.utility.Log;

import android.graphics.Color;

public class JassimpModelLoaderViewManager extends SXRScript {

    @SuppressWarnings("unused")
    private static final String TAG = Log
            .tag(JassimpModelLoaderViewManager.class);

    private SXRAnimationEngine mAnimationEngine;
    private SXRScene mMainScene;

    @Override
    public void onInit(SXRContext sxrContext) throws IOException {

        mAnimationEngine = sxrContext.getAnimationEngine();

        mMainScene = sxrContext.getNextMainScene(new Runnable() {

            @Override
            public void run() {
                for (SXRAnimation animation : mAnimations) {
                    animation.start(mAnimationEngine);
                }
                mAnimations = null;
            }
        });

        closeSplashScreen();

        // Apply frustum culling
        mMainScene.setFrustumCulling(true);

        SXRCameraRig mainCameraRig = mMainScene.getMainCameraRig();
        mainCameraRig.getLeftCamera().setBackgroundColor(Color.BLACK);
        mainCameraRig.getRightCamera().setBackgroundColor(Color.BLACK);
        mainCameraRig.getTransform().setPosition(0.0f, 0.0f, 0.0f);

        SXRSceneObject obj3 = sxrContext.getAssimpModel("donovan_kick_fail.fbx");
        obj3.getTransform().setPosition(0.0f, -4.0f, -5.0f);
        obj3.getTransform().setRotationByAxis(0.0f, 1.0f, 0.0f, 0.0f);
        SXRSceneObject obj4 = sxrContext.getAssimpModel("bunny.obj");
        obj4.getTransform().setPosition(0.0f, -6.0f, -5.0f);
        obj4.getTransform().setRotationByAxis(0.0f, 0.0f, 1.0f, 0.0f);

        // Model with texture
        SXRSceneObject astroBoyModel = sxrContext.getAssimpModel("astro_boy.dae");

        // Model with color
        SXRSceneObject benchModel = sxrContext.getAssimpModel("bench.dae");

        ModelPosition astroBoyModelPosition = new ModelPosition();
        astroBoyModelPosition.setPosition(0.0f, -0.0f, -5.0f);
        astroBoyModel.getTransform().setPosition(astroBoyModelPosition.x, astroBoyModelPosition.y, astroBoyModelPosition.z);
        astroBoyModel.getTransform().setRotationByAxis(0.0f, 0.0f, 0.0f, 1.0f);
        astroBoyModel.getTransform().setScale(12.0f, 12.0f, 12.0f);


        ModelPosition benchModelPosition = new ModelPosition();
        benchModelPosition.setPosition(0.0f, -4.0f, -30.0f);
        benchModel.getTransform().setPosition(benchModelPosition.x, benchModelPosition.y, benchModelPosition.z);
        benchModel.getTransform().setRotationByAxis(180.0f, 0.0f, 1.0f, 0.0f);

        mMainScene.addSceneObject(astroBoyModel);
        mMainScene.addSceneObject(benchModel);
        mMainScene.addSceneObject(obj3);
        mMainScene.addSceneObject(obj4);

        rotateModel(astroBoyModel, 10f, astroBoyModelPosition);
        rotateModel(obj3, 10f, astroBoyModelPosition);
        rotateModel(obj4, 10f, astroBoyModelPosition);
    }

    @Override
    public void onStep() {
    }

    void onTap() {
        // toggle whether stats are displayed.
        boolean statsEnabled = mMainScene.getStatsEnabled();
        mMainScene.setStatsEnabled(!statsEnabled);
    }

    private List<SXRAnimation> mAnimations = new ArrayList<SXRAnimation>();

    private void setup(SXRAnimation animation) {
        animation.setRepeatMode(SXRRepeatMode.REPEATED).setRepeatCount(-1);
        mAnimations.add(animation);
    }

    private void rotateModel(SXRSceneObject model, float duration,
            ModelPosition modelPosition) {
        setup(new SXRRotationByAxisWithPivotAnimation( //
                model, duration, -360.0f, //
                0.0f, 1.0f, 0.0f, //
                modelPosition.x, modelPosition.y, modelPosition.z));
    }
}

class ModelPosition {
    float x;
    float y;
    float z;

    void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
