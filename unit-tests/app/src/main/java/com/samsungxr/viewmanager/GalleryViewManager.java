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
import java.util.List;

import com.samsungxr.CustomPostEffectShaderManager;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRPostEffect;
import com.samsungxr.SXRRenderData.SXRRenderMaskBit;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRScript;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.animation.SXRAnimation;
import com.samsungxr.animation.SXRAnimationEngine;
import com.samsungxr.animation.SXRRotationByAxisWithPivotAnimation;
import com.samsungxr.animation.SXRScaleAnimation;
import com.samsungxr.scene_objects.SXRVideoSceneObject;
import com.samsungxr.scene_objects.SXRVideoSceneObject.SXRVideoType;

import android.media.MediaPlayer;

public class GalleryViewManager extends SXRScript {

    private static final float ANIMATION_DURATION = 0.3f;
    private static final float SELECTED_SCALE = 2.0f;

    private SXRContext mSXRContext = null;
    private List<SXRSceneObject> mBoards = new ArrayList<SXRSceneObject>();
    private SXRSceneObject mBoardParent;
    private int mSelected = 0;
    private SXRAnimationEngine mAnimationEngine;

    private static final int LOOK_UP = 1;
    private static final int LOOK_FRONT = 0;
    private static final int LOOK_DOWN = -1;
    private static final float LOOK_AT_THRESHOLD = 0.2f;
    private int mLookAtMode = LOOK_FRONT;
    private SXRAnimation mRotationAnimation;

    //@Override
    //public SplashMode getSplashMode() {
    //    return SplashMode.NONE;
    //}

    @Override
    public void onInit(SXRContext sxrContext) {
        mSXRContext = sxrContext;

        mAnimationEngine = sxrContext.getAnimationEngine();

        SXRScene mainScene = mSXRContext.getNextMainScene();

        mainScene.getMainCameraRig().getLeftCamera()
                .setBackgroundColor(0.0f, 0.0f, 0.0f, 1.0f);
        mainScene.getMainCameraRig().getRightCamera()
                .setBackgroundColor(0.0f, 0.0f, 0.0f, 1.0f);

        mainScene.getMainCameraRig().getTransform()
                .setPosition(0.0f, 0.0f, 0.0f);

        SXRMesh sphereMesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                mSXRContext, R.raw.sphere_mesh));

        SXRSceneObject leftScreen = new SXRSceneObject(mSXRContext, sphereMesh,
                mSXRContext.loadTexture(new SXRAndroidResource(mSXRContext,
                        R.drawable.left_screen)));
        leftScreen.getTransform().setScale(10.0f, 10.0f, 10.0f);
        leftScreen.getRenderData().setRenderMask(SXRRenderMaskBit.Left);
        SXRSceneObject rightScreen = new SXRSceneObject(mSXRContext,
                sphereMesh, mSXRContext.loadTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.right_screen)));
        rightScreen.getTransform().setScale(10.0f, 10.0f, 10.0f);
        rightScreen.getRenderData().setRenderMask(SXRRenderMaskBit.Right);

        mainScene.addSceneObject(leftScreen);
        mainScene.addSceneObject(rightScreen);

        List<SXRTexture> numberTextures = new ArrayList<SXRTexture>();
        int[] resourceIds = new int[] { R.drawable.photo_1,
                R.drawable.photo_2, R.drawable.photo_3, R.drawable.photo_4,
                R.drawable.photo_5, R.drawable.photo_6, R.drawable.photo_7,
                R.drawable.photo_8, R.drawable.photo_9 };
        for (int id : resourceIds) {
            numberTextures.add(mSXRContext.loadTexture(new SXRAndroidResource(
                    mSXRContext, id)));
        }

        for (int i = 0; i < numberTextures.size(); ++i) {
            SXRSceneObject number = new SXRSceneObject(mSXRContext, 2.0f, 1.0f,
                    numberTextures.get(i));
            number.getTransform().setPosition(0.0f, 0.0f, -5.0f);
            float degree = 360.0f * i / (numberTextures.size() + 1);
            number.getTransform().rotateByAxisWithPivot(degree, 0.0f, 1.0f,
                    0.0f, 0.0f, 0.0f, 0.0f);
            mBoards.add(number);
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(mSXRContext.getContext(),
                R.raw.tron);
        mediaPlayer.start();
        SXRSceneObject video = new SXRVideoSceneObject(mSXRContext, 2.0f, 1.0f,
                mediaPlayer, SXRVideoType.MONO);
        video.getTransform().setPosition(0.0f, 0.0f, -5.0f);
        float degree = 360.0f * (numberTextures.size())
                / (numberTextures.size() + 1);
        video.getTransform().rotateByAxisWithPivot(degree, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f);
        mBoards.add(video);

        mBoardParent = new SXRSceneObject(mSXRContext);

        for (SXRSceneObject board : mBoards) {
            mBoardParent.addChildObject(board);
        }

        mainScene.addSceneObject(mBoardParent);

        mBoardParent.getTransform().rotateByAxisWithPivot(90.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 0.0f);

        mBoards.get(mSelected).getTransform()
                .setScale(SELECTED_SCALE, SELECTED_SCALE, 0.0f);

        CustomPostEffectShaderManager shaderManager = new CustomPostEffectShaderManager(
                mSXRContext);
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext,
                shaderManager.getShaderId());
        postEffect.setVec3("ratio_r", 0.393f, 0.769f, 0.189f);
        postEffect.setVec3("ratio_g", 0.349f, 0.686f, 0.168f);
        postEffect.setVec3("ratio_b", 0.272f, 0.534f, 0.131f);
        mainScene.getMainCameraRig().getLeftCamera().addPostEffect(postEffect);
        mainScene.getMainCameraRig().getRightCamera().addPostEffect(postEffect);

    }

    @Override
    public void onStep() {
        float lookAtY = mSXRContext.getMainScene().getMainCameraRig()
                .getLookAt()[1];

        if (mRotationAnimation != null && mRotationAnimation.isFinished()) {
            mRotationAnimation = null;
        }

        if (mRotationAnimation == null) {
            if (mLookAtMode == LOOK_FRONT) {
                if (lookAtY > LOOK_AT_THRESHOLD) {
                    mLookAtMode = LOOK_UP;
                    rotateCounterClockwise();
                } else if (lookAtY < -LOOK_AT_THRESHOLD) {
                    mLookAtMode = LOOK_DOWN;
                    rotateClockwise();
                }
            }
            if (mLookAtMode == LOOK_UP) {
                if (lookAtY < -LOOK_AT_THRESHOLD) {
                    mLookAtMode = LOOK_DOWN;
                    rotateClockwise();
                } else if (lookAtY < LOOK_AT_THRESHOLD) {
                    mLookAtMode = LOOK_FRONT;
                }
            }
            if (mLookAtMode == LOOK_DOWN) {
                if (lookAtY > LOOK_AT_THRESHOLD) {
                    mLookAtMode = LOOK_UP;
                    rotateCounterClockwise();
                } else if (lookAtY > -LOOK_AT_THRESHOLD) {
                    mLookAtMode = LOOK_FRONT;
                }
            }
        }
    }

    private void rotateCounterClockwise() {
        mRotationAnimation = new SXRRotationByAxisWithPivotAnimation(
                mBoardParent, ANIMATION_DURATION, 360.0f / mBoards.size(),
                0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f).start(mAnimationEngine);

        new SXRScaleAnimation(mBoards.get(mSelected), ANIMATION_DURATION,
                1.0f / SELECTED_SCALE, 1.0f / SELECTED_SCALE, 1.0f)
                .start(mAnimationEngine);

        if (--mSelected < 0) {
            mSelected += mBoards.size();
        }

        new SXRScaleAnimation(mBoards.get(mSelected), ANIMATION_DURATION,
                SELECTED_SCALE, SELECTED_SCALE, 1.0f).start(mAnimationEngine);
    }

    private void rotateClockwise() {
        mRotationAnimation = new SXRRotationByAxisWithPivotAnimation(
                mBoardParent, ANIMATION_DURATION, -360.0f / mBoards.size(),
                0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f).start(mAnimationEngine);

        new SXRScaleAnimation(mBoards.get(mSelected), ANIMATION_DURATION,
                1.0f / SELECTED_SCALE, 1.0f / SELECTED_SCALE, 1.0f)
                .start(mAnimationEngine);

        if (++mSelected >= mBoards.size()) {
            mSelected -= mBoards.size();
        }

        new SXRScaleAnimation(mBoards.get(mSelected), ANIMATION_DURATION,
                SELECTED_SCALE, SELECTED_SCALE, 1.0f).start(mAnimationEngine);
    }

}
