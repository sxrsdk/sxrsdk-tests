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
import java.util.List;

import com.samsungxr.*;
import com.samsungxr.SXRPicker.SXRPickedObject;
import com.samsungxr.SXRRenderData.SXRRenderMaskBit;

import android.util.Log;
import android.view.MotionEvent;

public class ViewerScript extends SXRMain {

    private static final String TAG = "ViewerScript";

    private SXRContext mSXRContext = null;

    private MetalOnlyShader mMetalOnlyShader = null;
    private GlassShader mGlassShader = null;
    private DiffuseShader mDiffuseShader = null;
    private ReflectionShader mReflectionShader = null;
    private PhongShader mPhongShader = null;

    private MetalShader2 mMetalShader2 = null;
    private GlassShader2 mGlassShader2 = null;
    private DiffuseShader2 mDiffuseShader2 = null;
    private PhongShader2 mPhongShader2 = null;
    private PhongShader3 mPhongShader3 = null;

    private SXRMaterial mMetalMaterial = null;
    private SXRMaterial mGlassMaterial = null;
    private SXRMaterial mDiffuseMaterial = null;
    private SXRMaterial mReflectionMaterial = null;
    private SXRMaterial mPhongMaterial = null;

    private SXRMaterial mCarBodyMaterial = null;
    private SXRMaterial mCarGlassMaterial = null;
    private SXRMaterial mCarTireMaterial = null;
    private SXRMaterial mCarWheelMaterial = null;
    private SXRMaterial mCarGrillMaterial = null;
    private SXRMaterial mCarBackMaterial = null;
    private SXRMaterial mCarLightMaterial = null;
    private SXRMaterial mCarInsideMaterial = null;

    private SXRMaterial mRobotBodyMaterial = null;
    private SXRMaterial mRobotHeadMaterial = null;
    private SXRMaterial mRobotMetalMaterial = null;
    private SXRMaterial mRobotRubberMaterial = null;

    private SXRMaterial mLeafBodyMaterial = null;
    private SXRMaterial mLeafBoxMaterial = null;

    private float THUMBNAIL_ROT = 0.0f;
    private float OBJECT_ROT = 0.0f;
    private final float EYE_TO_OBJECT = 2.4f;
    private final int THUMBNAIL_NUM = 5;

    private SXRSceneObject[] ThumbnailObject = new SXRSceneObject[THUMBNAIL_NUM];
    private SXRSceneObject[] ThumbnailRotation = new SXRSceneObject[THUMBNAIL_NUM];
    private SXRRenderData[] ThumbnailGlasses = new SXRRenderData[THUMBNAIL_NUM];
    private SXRSceneObject[] Thumbnails = new SXRSceneObject[THUMBNAIL_NUM];
    private SXRTexture[] ThumbnailTextures = new SXRTexture[THUMBNAIL_NUM];
    private float[][] ThumbnailTargetPosition = new float[THUMBNAIL_NUM][3];
    private int[] ThumbnailTargetIndex = new int[THUMBNAIL_NUM];
    private float[][] ThumbnailCurrentPosition = new float[THUMBNAIL_NUM][3];
    private int[] ThumbnailOrder = new int[THUMBNAIL_NUM];
    private int ThumbnailSelected = 2;

    private boolean SelectionMode = true;
    private boolean SelectionActive = false;

    private boolean mIsButtonDown = false;
    private boolean mIsSingleTapped = false;

    private SXRSceneObject[] Objects = new SXRSceneObject[THUMBNAIL_NUM];

    private SXRActivity mActivity;

    ViewerScript(SXRActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onInit(SXRContext sxrContext) {

        mSXRContext = sxrContext;

        mMetalOnlyShader = new MetalOnlyShader(mSXRContext);
        mDiffuseShader = new DiffuseShader(mSXRContext);
        mGlassShader = new GlassShader(mSXRContext);
        mReflectionShader = new ReflectionShader(mSXRContext);
        mPhongShader = new PhongShader(mSXRContext);

        mMetalShader2 = new MetalShader2(mSXRContext);
        mDiffuseShader2 = new DiffuseShader2(mSXRContext);
        mGlassShader2 = new GlassShader2(mSXRContext);
        mPhongShader2 = new PhongShader2(mSXRContext);
        mPhongShader3 = new PhongShader3(mSXRContext);

        SXRScene mainScene = mSXRContext.getNextMainScene();

        mainScene.getMainCameraRig().getLeftCamera()
                .setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        mainScene.getMainCameraRig().getRightCamera()
                .setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);

        try {
            SXRTexture env_tex = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext, "env.jpg"));
            mReflectionMaterial = new SXRMaterial(mSXRContext,
                    mReflectionShader.getShaderId());
            mReflectionMaterial.setVec4(ReflectionShader.COLOR_KEY, 1.0f, 1.0f,
                    1.0f, 1.0f);
            mReflectionMaterial.setFloat(ReflectionShader.RADIUS_KEY, 10.0f);
            mReflectionMaterial.setTexture(ReflectionShader.TEXTURE_KEY,
                    env_tex);

            // ------------------------------------------------------ set
            // materials
            // watch
            mMetalMaterial = new SXRMaterial(mSXRContext,
                    mMetalOnlyShader.getShaderId());
            mMetalMaterial.setVec4(MetalOnlyShader.COLOR_KEY, 1.7f, 1.4f, 1.0f,
                    1.0f);
            mMetalMaterial.setFloat(MetalOnlyShader.RADIUS_KEY, 10.0f);
            mMetalMaterial.setTexture(MetalOnlyShader.TEXTURE_KEY, env_tex);

            mGlassMaterial = new SXRMaterial(mSXRContext,
                    mGlassShader.getShaderId());
            mGlassMaterial.setVec4(GlassShader.COLOR_KEY, 1.0f, 1.0f, 1.0f,
                    1.0f);
            mGlassMaterial.setFloat(MetalOnlyShader.RADIUS_KEY, 10.0f);
            mGlassMaterial.setTexture(GlassShader.TEXTURE_KEY, env_tex);

            SXRTexture board_tex = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "board.jpg"));
            mDiffuseMaterial = new SXRMaterial(mSXRContext,
                    mDiffuseShader.getShaderId());
            mDiffuseMaterial.setVec4(DiffuseShader.COLOR_KEY, 1.0f, 1.0f, 1.0f,
                    1.0f);
            mDiffuseMaterial.setTexture(DiffuseShader.TEXTURE_KEY, board_tex);

            // jar
            mPhongMaterial = new SXRMaterial(mSXRContext,
                    mPhongShader.getShaderId());
            mPhongMaterial.setVec4(PhongShader.COLOR_KEY, 1.2f, 1.2f, 1.3f,
                    1.0f);
            mPhongMaterial.setFloat(PhongShader.RADIUS_KEY, 10.0f);
            mPhongMaterial.setTexture(PhongShader.TEXTURE_KEY, env_tex);

            // car
            SXRTexture car_body_tex = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "body.jpg"));
            mCarBodyMaterial = new SXRMaterial(mSXRContext,
                    mPhongShader3.getShaderId());
            mCarBodyMaterial.setFloat(PhongShader3.RADIUS_KEY, 10.0f);
            mCarBodyMaterial.setTexture(PhongShader3.ENV_KEY, env_tex);
            mCarBodyMaterial.setTexture(PhongShader3.TEXTURE_KEY, car_body_tex);

            mCarWheelMaterial = new SXRMaterial(mSXRContext,
                    mMetalShader2.getShaderId());
            mCarWheelMaterial.setVec4(MetalShader2.COLOR_KEY, 1.2f, 1.2f, 1.2f,
                    1.0f);
            mCarWheelMaterial.setFloat(MetalShader2.RADIUS_KEY, 10.0f);
            mCarWheelMaterial.setTexture(MetalShader2.TEXTURE_KEY, env_tex);

            mCarGlassMaterial = new SXRMaterial(mSXRContext,
                    mGlassShader2.getShaderId());
            mCarGlassMaterial.setVec4(GlassShader2.COLOR_KEY, 1.0f, 1.0f, 1.0f,
                    1.0f);
            mCarGlassMaterial.setFloat(GlassShader2.RADIUS_KEY, 10.0f);
            mCarGlassMaterial.setTexture(GlassShader2.TEXTURE_KEY, env_tex);

            SXRTexture default_tex = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "default.bmp"));
            mCarTireMaterial = new SXRMaterial(mSXRContext,
                    mDiffuseShader2.getShaderId());
            mCarTireMaterial.setVec4(DiffuseShader2.COLOR_KEY, 0.1f, 0.1f,
                    0.1f, 1.0f);
            mCarTireMaterial
                    .setTexture(DiffuseShader2.TEXTURE_KEY, default_tex);

            SXRTexture back_tex = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "back.jpg"));
            mCarBackMaterial = new SXRMaterial(mSXRContext,
                    mDiffuseShader2.getShaderId());
            mCarBackMaterial.setVec4(DiffuseShader2.COLOR_KEY, 1.0f, 1.0f,
                    1.0f, 1.0f);
            mCarBackMaterial.setTexture(DiffuseShader2.TEXTURE_KEY, back_tex);

            SXRTexture grill_tex = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "grill.jpg"));
            mCarGrillMaterial = new SXRMaterial(mSXRContext,
                    mDiffuseShader2.getShaderId());
            mCarGrillMaterial.setVec4(DiffuseShader2.COLOR_KEY, 1.0f, 1.0f,
                    1.0f, 1.0f);
            mCarGrillMaterial.setTexture(DiffuseShader2.TEXTURE_KEY, grill_tex);

            mCarLightMaterial = new SXRMaterial(mSXRContext,
                    mGlassShader2.getShaderId());
            mCarLightMaterial.setVec4(GlassShader2.COLOR_KEY, 2.5f, 2.5f, 2.5f,
                    1.0f);
            mCarLightMaterial.setFloat(GlassShader2.RADIUS_KEY, 10.0f);
            mCarLightMaterial.setTexture(GlassShader2.TEXTURE_KEY, env_tex);

            mCarInsideMaterial = new SXRMaterial(mSXRContext,
                    mPhongShader2.getShaderId());
            mCarInsideMaterial.setVec4(PhongShader2.COLOR_KEY, 0.0f, 0.0f,
                    0.0f, 1.0f);
            mCarInsideMaterial.setFloat(PhongShader2.RADIUS_KEY, 10.0f);
            mCarInsideMaterial.setTexture(PhongShader2.TEXTURE_KEY, env_tex);

            // robot
            SXRTexture robot_head_tex = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "head.jpg"));
            mRobotHeadMaterial = new SXRMaterial(mSXRContext,
                    mPhongShader3.getShaderId());
            mRobotHeadMaterial.setFloat(PhongShader3.RADIUS_KEY, 10.0f);
            mRobotHeadMaterial.setTexture(PhongShader3.ENV_KEY, env_tex);
            mRobotHeadMaterial.setTexture(PhongShader3.TEXTURE_KEY,
                    robot_head_tex);

            mRobotMetalMaterial = new SXRMaterial(mSXRContext,
                    mMetalShader2.getShaderId());
            mRobotMetalMaterial.setVec4(MetalShader2.COLOR_KEY, 1.5f, 1.5f,
                    1.5f, 1.0f);
            mRobotMetalMaterial.setFloat(MetalShader2.RADIUS_KEY, 10.0f);
            mRobotMetalMaterial.setTexture(MetalShader2.TEXTURE_KEY, env_tex);

            mRobotBodyMaterial = new SXRMaterial(mSXRContext,
                    mPhongShader2.getShaderId());
            mRobotBodyMaterial.setVec4(PhongShader2.COLOR_KEY, 1.0f, 1.0f,
                    1.0f, 1.0f);
            mRobotBodyMaterial.setFloat(PhongShader2.RADIUS_KEY, 10.0f);
            mRobotBodyMaterial.setTexture(PhongShader2.TEXTURE_KEY, env_tex);

            mRobotRubberMaterial = new SXRMaterial(mSXRContext,
                    mDiffuseShader2.getShaderId());
            mRobotRubberMaterial.setVec4(DiffuseShader2.COLOR_KEY, 0.3f, 0.3f,
                    0.3f, 1.0f);
            mRobotRubberMaterial.setTexture(DiffuseShader2.TEXTURE_KEY,
                    default_tex);

            // leaf
            SXRTexture leaf_box_tex = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "box.jpg"));
            mLeafBoxMaterial = new SXRMaterial(mSXRContext,
                    mPhongShader3.getShaderId());
            mLeafBoxMaterial.setFloat(PhongShader3.RADIUS_KEY, 10.0f);
            mLeafBoxMaterial.setTexture(PhongShader3.ENV_KEY, env_tex);
            mLeafBoxMaterial.setTexture(PhongShader3.TEXTURE_KEY, leaf_box_tex);

            mLeafBodyMaterial = new SXRMaterial(mSXRContext,
                    mMetalShader2.getShaderId());
            mLeafBodyMaterial.setVec4(MetalShader2.COLOR_KEY, 2.5f, 2.5f, 2.5f,
                    1.0f);
            mLeafBodyMaterial.setFloat(MetalShader2.RADIUS_KEY, 10.0f);
            mLeafBodyMaterial.setTexture(MetalShader2.TEXTURE_KEY, env_tex);

            // ------------------------------------------------------ set
            // objects

            for (int i = 0; i < THUMBNAIL_NUM; i++)
                Objects[i] = new SXRSceneObject(mSXRContext);

            // --------------watch

            SXRSceneObject obj1 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData1 = new SXRRenderData(mSXRContext);
            SXRMesh mesh1 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "frame.obj"));
            renderData1.setMesh(mesh1);
            renderData1.setMaterial(mMetalMaterial);
            obj1.attachRenderData(renderData1);
            Objects[2].addChildObject(obj1);

            SXRSceneObject obj2 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData2 = new SXRRenderData(mSXRContext);
            SXRMesh mesh2 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "board.obj"));
            renderData2.setMesh(mesh2);
            renderData2.setMaterial(mDiffuseMaterial);
            obj2.attachRenderData(renderData2);
            Objects[2].addChildObject(obj2);

            SXRSceneObject obj3 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData3 = new SXRRenderData(mSXRContext);
            SXRMesh mesh3 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "glass.obj"));
            renderData3.setMesh(mesh3);
            renderData3.setMaterial(mGlassMaterial);
            obj3.attachRenderData(renderData3);

            obj3.getRenderData().setRenderingOrder(3000);
            Objects[2].addChildObject(obj3);

            Objects[2].getTransform().setPosition(0.0f, 0.0f, -EYE_TO_OBJECT);
            mainScene.addSceneObject(Objects[2]);

            // --------------jar

            SXRSceneObject obj5 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData5 = new SXRRenderData(mSXRContext);
            SXRMesh mesh5 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "jar.obj"));
            renderData5.setMesh(mesh5);
            renderData5.setMaterial(mPhongMaterial);
            obj5.attachRenderData(renderData5);
            Objects[1].addChildObject(obj5);

            SXRSceneObject obj4 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData4 = new SXRRenderData(mSXRContext);
            SXRMesh mesh4 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "edge.obj"));
            renderData4.setMesh(mesh4);
            renderData4.setMaterial(mMetalMaterial);
            obj4.attachRenderData(renderData4);
            obj4.getRenderData().setRenderingOrder(3000);
            Objects[1].addChildObject(obj4);

            Objects[1].getTransform().setPosition(0.0f, 0.0f, -EYE_TO_OBJECT);
            mainScene.addSceneObject(Objects[1]);

            // --------------car

            SXRSceneObject obj6 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData6 = new SXRRenderData(mSXRContext);
            SXRMesh mesh6 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "body.obj"));
            renderData6.setMesh(mesh6);
            renderData6.setMaterial(mCarBodyMaterial);
            obj6.attachRenderData(renderData6);
            obj6.getRenderData().setCullTest(false);
            Objects[3].addChildObject(obj6);

            SXRSceneObject obj9 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData9 = new SXRRenderData(mSXRContext);
            SXRMesh mesh9 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "tire.obj"));
            renderData9.setMesh(mesh9);
            renderData9.setMaterial(mCarTireMaterial);
            obj9.attachRenderData(renderData9);
            Objects[3].addChildObject(obj9);

            SXRSceneObject obj10 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData10 = new SXRRenderData(mSXRContext);
            SXRMesh mesh10 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "glass.obj"));
            renderData10.setMesh(mesh10);
            renderData10.setMaterial(mCarGlassMaterial);
            obj10.attachRenderData(renderData10);
            obj10.getRenderData().setCullTest(false);
            obj10.getRenderData().setRenderingOrder(3000);
            Objects[3].addChildObject(obj10);

            SXRSceneObject obj11 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData11 = new SXRRenderData(mSXRContext);
            SXRMesh mesh11 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "wheel.obj"));
            renderData11.setMesh(mesh11);
            renderData11.setMaterial(mCarWheelMaterial);
            obj11.attachRenderData(renderData11);
            Objects[3].addChildObject(obj11);

            SXRSceneObject obj12 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData12 = new SXRRenderData(mSXRContext);
            SXRMesh mesh12 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "back.obj"));
            renderData12.setMesh(mesh12);
            renderData12.setMaterial(mCarBackMaterial);
            obj12.attachRenderData(renderData12);
            Objects[3].addChildObject(obj12);

            SXRSceneObject obj13 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData13 = new SXRRenderData(mSXRContext);
            SXRMesh mesh13 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "grill.obj"));
            renderData13.setMesh(mesh13);
            renderData13.setMaterial(mCarGrillMaterial);
            obj13.attachRenderData(renderData13);
            obj10.getRenderData().setRenderingOrder(3000);
            Objects[3].addChildObject(obj13);

            SXRSceneObject obj14 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData14 = new SXRRenderData(mSXRContext);
            SXRMesh mesh14 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "glass2.obj"));
            renderData14.setMesh(mesh14);
            renderData14.setMaterial(mCarLightMaterial);
            obj14.attachRenderData(renderData14);
            obj14.getRenderData().setRenderingOrder(4000);
            Objects[3].addChildObject(obj14);

            SXRSceneObject obj19 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData19 = new SXRRenderData(mSXRContext);
            SXRMesh mesh19 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "inside.obj"));
            renderData19.setMesh(mesh19);
            renderData19.setMaterial(mCarInsideMaterial);
            obj19.attachRenderData(renderData19);
            Objects[3].addChildObject(obj19);

            Objects[3].getTransform().setPosition(0.0f, -2.0f,
                    -EYE_TO_OBJECT - 3.0f);
            mainScene.addSceneObject(Objects[3]);

            // robot

            SXRSceneObject obj15 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData15 = new SXRRenderData(mSXRContext);
            SXRMesh mesh15 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "body.obj"));
            renderData15.setMesh(mesh15);
            renderData15.setMaterial(mRobotBodyMaterial);
            obj15.attachRenderData(renderData15);
            Objects[4].addChildObject(obj15);

            SXRSceneObject obj16 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData16 = new SXRRenderData(mSXRContext);
            SXRMesh mesh16 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "head.obj"));
            renderData16.setMesh(mesh16);
            renderData16.setMaterial(mRobotHeadMaterial);
            obj16.attachRenderData(renderData16);
            Objects[4].addChildObject(obj16);

            SXRSceneObject obj17 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData17 = new SXRRenderData(mSXRContext);
            SXRMesh mesh17 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "metal.obj"));
            renderData17.setMesh(mesh17);
            renderData17.setMaterial(mRobotMetalMaterial);
            obj17.attachRenderData(renderData17);
            obj17.getRenderData().setRenderingOrder(3000);
            Objects[4].addChildObject(obj17);

            SXRSceneObject obj18 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData18 = new SXRRenderData(mSXRContext);
            SXRMesh mesh18 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "rubber.obj"));
            renderData18.setMesh(mesh18);
            renderData18.setMaterial(mRobotRubberMaterial);
            obj18.attachRenderData(renderData18);
            Objects[4].addChildObject(obj18);

            Objects[4].getTransform().setPosition(0.0f, 0.0f, -EYE_TO_OBJECT);
            mainScene.addSceneObject(Objects[4]);

            // leaf

            SXRSceneObject obj20 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData20 = new SXRRenderData(mSXRContext);
            SXRMesh mesh20 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "leaf.obj"));
            renderData20.setMesh(mesh20);
            renderData20.setMaterial(mLeafBodyMaterial);
            obj20.attachRenderData(renderData20);
            Objects[0].addChildObject(obj20);

            SXRSceneObject obj21 = new SXRSceneObject(mSXRContext);
            SXRRenderData renderData21 = new SXRRenderData(mSXRContext);
            SXRMesh mesh21 = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "box.obj"));
            renderData21.setMesh(mesh21);
            renderData21.setMaterial(mLeafBoxMaterial);
            obj21.attachRenderData(renderData21);
            Objects[0].addChildObject(obj21);

            Objects[0].getTransform().setPosition(0.0f, 0.0f, -EYE_TO_OBJECT);
            mainScene.addSceneObject(Objects[0]);

            for (int I = 0; I < THUMBNAIL_NUM; I++)
                for (int i = 0; i < Objects[I].getChildrenCount(); i++)
                    Objects[I].getChildByIndex(i).getRenderData()
                            .setRenderMask(0);
            // ------------------------------------------------------ set
            // thumbnails

            ThumbnailTextures[0] = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "leaf.jpg"));
            ThumbnailTextures[1] = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "jar.png"));
            ThumbnailTextures[2] = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "watch.png"));
            ThumbnailTextures[3] = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "car.bmp"));
            ThumbnailTextures[4] = mSXRContext
                    .loadTexture(new SXRAndroidResource(mSXRContext,
                            "robot.jpg"));

            ThumbnailTargetPosition[0][0] = -2.2f;
            ThumbnailTargetPosition[0][1] = 0.0f;
            ThumbnailTargetPosition[0][2] = -EYE_TO_OBJECT - 2.8f;
            ThumbnailTargetPosition[1][0] = -1.0f;
            ThumbnailTargetPosition[1][1] = 0.0f;
            ThumbnailTargetPosition[1][2] = -EYE_TO_OBJECT - 1.5f;
            ThumbnailTargetPosition[2][0] = 0.0f;
            ThumbnailTargetPosition[2][1] = 0.0f;
            ThumbnailTargetPosition[2][2] = -EYE_TO_OBJECT - 0.0f;
            ThumbnailTargetPosition[3][0] = 1.0f;
            ThumbnailTargetPosition[3][1] = 0.0f;
            ThumbnailTargetPosition[3][2] = -EYE_TO_OBJECT - 1.5f;
            ThumbnailTargetPosition[4][0] = 2.2f;
            ThumbnailTargetPosition[4][1] = 0.0f;
            ThumbnailTargetPosition[4][2] = -EYE_TO_OBJECT - 2.8f;

            for (int i = 0; i < THUMBNAIL_NUM; i++)
                for (int j = 0; j < 3; j++)
                    ThumbnailCurrentPosition[i][j] = ThumbnailTargetPosition[i][j];
            for (int i = 0; i < THUMBNAIL_NUM; i++)
                ThumbnailTargetIndex[i] = i;

            ThumbnailOrder[0] = 10000;
            ThumbnailOrder[1] = 10001;
            ThumbnailOrder[2] = 10002;
            ThumbnailOrder[3] = 10001;
            ThumbnailOrder[4] = 10000;

            SXRMesh glass_mesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "glass.obj"));
            SXRMesh board_mesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "board.obj"));
            SXRMesh picks_mesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "pick.obj"));
            for (int i = 0; i < THUMBNAIL_NUM; i++) {
                ThumbnailObject[i] = new SXRSceneObject(mSXRContext);
                ThumbnailRotation[i] = new SXRSceneObject(mSXRContext);
                SXRSceneObject obj = new SXRSceneObject(mSXRContext);
                ThumbnailGlasses[i] = new SXRRenderData(mSXRContext);
                ThumbnailGlasses[i].setMesh(glass_mesh);
                ThumbnailGlasses[i].setMaterial(mReflectionMaterial);
                obj.attachRenderData(ThumbnailGlasses[i]);
                obj.getRenderData().setRenderingOrder(ThumbnailOrder[i]);
                ThumbnailRotation[i].addChildObject(obj);
                ThumbnailObject[i].addChildObject(ThumbnailRotation[i]);

                Thumbnails[i] = new SXRSceneObject(mSXRContext, board_mesh,
                        ThumbnailTextures[i]);
                Thumbnails[i].getRenderData().setRenderingOrder(
                        ThumbnailOrder[i] - 100);
                Thumbnails[i].getRenderData().setCullTest(false);
                Thumbnails[i].getTransform().setScale(1.0f, 1.2f, 1.0f);
                ThumbnailRotation[i].addChildObject(Thumbnails[i]);

                ThumbnailObject[i].getTransform().setPosition(
                        ThumbnailTargetPosition[i][0],
                        ThumbnailTargetPosition[i][1],
                        ThumbnailTargetPosition[i][2]);
                mainScene.addSceneObject(ThumbnailObject[i]);

                SXREyePointeeHolder eyePointeeHolder = new SXREyePointeeHolder(
                        sxrContext);
                SXRMeshEyePointee eyePointee = new SXRMeshEyePointee(
                        sxrContext, picks_mesh);
                eyePointeeHolder.addPointee(eyePointee);
                ThumbnailObject[i].attachEyePointeeHolder(eyePointeeHolder);
            }

            SXRTexture m360 = mSXRContext.loadTexture(new SXRAndroidResource(
                    mSXRContext, "env.jpg"));
            SXRMesh sphere = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                    mSXRContext, "sphere.obj"));

            SXRSceneObject env_object = new SXRSceneObject(mSXRContext, sphere,
                    m360);
            env_object.getRenderData().setCullTest(false);
            mainScene.addSceneObject(env_object);

            SXRSceneObject headTracker = new SXRSceneObject(sxrContext,
                    sxrContext.createQuad(0.1f, 0.1f),
                    sxrContext.loadTexture(new SXRAndroidResource(mSXRContext,
                            "Headtracking_pointer.png")));
            headTracker.getTransform().setPosition(0.0f, 0.0f, -EYE_TO_OBJECT);
            headTracker.getRenderData().setDepthTest(false);
            headTracker.getRenderData().setRenderingOrder(100000);
            mainScene.getMainCameraRig().addChildObject(headTracker);
        } catch (IOException e) {
            e.printStackTrace();
            mActivity.finish();
            Log.e(TAG, "Assets were not loaded. Stopping application!");
        }
        // activity was stored in order to stop the application if the mesh is
        // not loaded. Since we don't need anymore, we set it to null to reduce
        // chance of memory leak.
        mActivity = null;
    }

    @Override
    public void onStep() {
        FPSCounter.tick();

        boolean isButtonDown = mIsButtonDown;
        boolean isSingleTapped = mIsSingleTapped;
        mIsButtonDown = false;
        mIsSingleTapped = false;

        if (isButtonDown)
            mSXRContext.getMainScene().getMainCameraRig().resetYaw();

        // ---------------------------------------thumbnail motion
        boolean MoveActive = false;
        if (Math.abs(ThumbnailTargetPosition[ThumbnailTargetIndex[0]][0]
                - ThumbnailCurrentPosition[0][0]) < 0.2f)
            MoveActive = true;

        if (SelectionActive && MoveActive) {
            if (ThumbnailCurrentPosition[ThumbnailSelected][0] < -0.5f) {
                for (int i = 0; i < THUMBNAIL_NUM; i++) {
                    ThumbnailTargetIndex[i]++;
                    if (ThumbnailTargetIndex[i] >= THUMBNAIL_NUM)
                        ThumbnailTargetIndex[i] = ThumbnailTargetIndex[i]
                                - THUMBNAIL_NUM;
                    if (ThumbnailTargetIndex[i] < 0)
                        ThumbnailTargetIndex[i] = ThumbnailTargetIndex[i]
                                + THUMBNAIL_NUM;
                }
            } else if (ThumbnailCurrentPosition[ThumbnailSelected][0] > 0.5f) {
                for (int i = 0; i < THUMBNAIL_NUM; i++) {
                    ThumbnailTargetIndex[i]--;
                    if (ThumbnailTargetIndex[i] >= THUMBNAIL_NUM)
                        ThumbnailTargetIndex[i] = ThumbnailTargetIndex[i]
                                - THUMBNAIL_NUM;
                    if (ThumbnailTargetIndex[i] < 0)
                        ThumbnailTargetIndex[i] = ThumbnailTargetIndex[i]
                                + THUMBNAIL_NUM;
                }
            }
        }

        for (int i = 0; i < THUMBNAIL_NUM; i++) {
            float speed = 0.08f;
            for (int j = 0; j < 3; j++)
                ThumbnailCurrentPosition[i][j] += speed
                        * (ThumbnailTargetPosition[ThumbnailTargetIndex[i]][j] - ThumbnailCurrentPosition[i][j]);
            ThumbnailObject[i].getTransform().setPosition(
                    ThumbnailCurrentPosition[i][0],
                    ThumbnailCurrentPosition[i][1],
                    ThumbnailCurrentPosition[i][2]);
        }

        // if(
        // Math.abs(ThumbnailTargetPosition[ThumbnailTargetIndex[0]][0]-ThumbnailCurrentPosition[0][0])
        // > 0.02f )
        // {
        // if( THUMBNAIL_ROT > 180.0f )
        // THUMBNAIL_ROT += 0.05f*( 360.0f - THUMBNAIL_ROT );
        // else
        // THUMBNAIL_ROT += 0.05f*( 0.0f - THUMBNAIL_ROT );
        // }
        // else
        // {
        // THUMBNAIL_ROT += -1.0f;
        // if( THUMBNAIL_ROT > 360.0f ) THUMBNAIL_ROT = THUMBNAIL_ROT - 360.0f;
        // }
        THUMBNAIL_ROT = -1.0f;
        for (int i = 0; i < THUMBNAIL_NUM; i++)
            ThumbnailRotation[i].getTransform().rotateByAxis(THUMBNAIL_ROT,
                    0.0f, 1.0f, 0.0f);

        // ---------------------------------------object motion

        OBJECT_ROT = -1.0f;
        for (int i = 0; i < THUMBNAIL_NUM; i++)
            Objects[i].getTransform()
                    .rotateByAxis(OBJECT_ROT, 0.0f, 1.0f, 0.0f);

        float[] light = new float[4];
        light[0] = 6.0f;
        light[1] = 10.0f;
        light[2] = 10.0f;
        light[3] = 1.0f;

        float[] eye = new float[4];
        eye[0] = 0.0f;
        eye[1] = 0.0f;
        eye[2] = 3.0f * EYE_TO_OBJECT;
        eye[3] = 1.0f;

        float[] matT = ThumbnailRotation[0].getTransform().getModelMatrix();
        float[] matO = Objects[ThumbnailSelected].getTransform()
                .getModelMatrix();

        // ---------------------------- watch, jar

        float x = matO[0] * light[0] + matO[1] * light[1] + matO[2] * light[2]
                + matO[3] * light[3];
        float y = matO[4] * light[0] + matO[5] * light[1] + matO[6] * light[2]
                + matO[7] * light[3];
        float z = matO[8] * light[0] + matO[9] * light[1] + matO[10] * light[2]
                + matO[11] * light[3];

        float mag = (float) Math.sqrt(x * x + y * y + z * z);

        mMetalMaterial.setVec3(MetalOnlyShader.LIGHT_KEY, x / mag, y / mag, z
                / mag);
        mDiffuseMaterial.setVec3(DiffuseShader.LIGHT_KEY, x / mag, y / mag, z
                / mag);
        mGlassMaterial
                .setVec3(GlassShader.LIGHT_KEY, x / mag, y / mag, z / mag);
        mPhongMaterial
                .setVec3(PhongShader.LIGHT_KEY, x / mag, y / mag, z / mag);

        x = matO[0] * eye[0] + matO[1] * eye[1] + matO[2] * eye[2] + matO[3]
                * eye[3];
        y = matO[4] * eye[0] + matO[5] * eye[1] + matO[6] * eye[2] + matO[7]
                * eye[3];
        z = matO[8] * eye[0] + matO[9] * eye[1] + matO[10] * eye[2] + matO[11]
                * eye[3];

        mag = (float) Math.sqrt(x * x + y * y + z * z);

        mMetalMaterial.setVec3(MetalOnlyShader.EYE_KEY, x / mag, y / mag, z
                / mag);
        mDiffuseMaterial.setVec3(DiffuseShader.EYE_KEY, x / mag, y / mag, z
                / mag);
        mGlassMaterial.setVec3(GlassShader.EYE_KEY, x / mag, y / mag, z / mag);
        mPhongMaterial.setVec3(PhongShader.EYE_KEY, x / mag, y / mag, z / mag);

        // ---------------------------- robot

        mRobotHeadMaterial.setVec4(PhongShader3.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mRobotHeadMaterial.setVec4(PhongShader3.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mRobotHeadMaterial.setVec4(PhongShader3.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mRobotHeadMaterial.setVec4(PhongShader3.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mRobotHeadMaterial.setVec3(PhongShader3.LIGHT_KEY, light[0], light[1],
                light[2]);
        mRobotHeadMaterial
                .setVec3(PhongShader3.EYE_KEY, eye[0], eye[1], eye[2]);

        mRobotMetalMaterial.setVec4(MetalShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mRobotMetalMaterial.setVec4(MetalShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mRobotMetalMaterial.setVec4(MetalShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mRobotMetalMaterial.setVec4(MetalShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mRobotMetalMaterial.setVec3(MetalShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mRobotMetalMaterial.setVec3(MetalShader2.EYE_KEY, eye[0], eye[1],
                eye[2]);

        mRobotBodyMaterial.setVec4(PhongShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mRobotBodyMaterial.setVec4(PhongShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mRobotBodyMaterial.setVec4(PhongShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mRobotBodyMaterial.setVec4(PhongShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mRobotBodyMaterial.setVec3(PhongShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mRobotBodyMaterial
                .setVec3(PhongShader2.EYE_KEY, eye[0], eye[1], eye[2]);

        mRobotRubberMaterial.setVec4(DiffuseShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mRobotRubberMaterial.setVec4(DiffuseShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mRobotRubberMaterial.setVec4(DiffuseShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mRobotRubberMaterial.setVec4(DiffuseShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mRobotRubberMaterial.setVec3(DiffuseShader2.LIGHT_KEY, light[0],
                light[1], light[2]);
        mRobotRubberMaterial.setVec3(DiffuseShader2.EYE_KEY, eye[0], eye[1],
                eye[2]);

        // ---------------------------- leaf

        mLeafBodyMaterial.setVec4(MetalShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mLeafBodyMaterial.setVec4(MetalShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mLeafBodyMaterial.setVec4(MetalShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mLeafBodyMaterial.setVec4(MetalShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mLeafBodyMaterial.setVec3(MetalShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mLeafBodyMaterial.setVec3(MetalShader2.EYE_KEY, eye[0], eye[1], eye[2]);

        mLeafBoxMaterial.setVec4(PhongShader3.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mLeafBoxMaterial.setVec4(PhongShader3.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mLeafBoxMaterial.setVec4(PhongShader3.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mLeafBoxMaterial.setVec4(PhongShader3.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mLeafBoxMaterial.setVec3(PhongShader3.LIGHT_KEY, light[0], light[1],
                light[2]);
        mLeafBoxMaterial.setVec3(PhongShader3.EYE_KEY, eye[0], eye[1], eye[2]);

        // ---------------------------- car
        eye[0] = 4.0f;
        eye[1] = 0.0f;
        eye[2] = 3.0f * EYE_TO_OBJECT;
        eye[3] = 1.0f;

        mCarBodyMaterial.setVec4(PhongShader3.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mCarBodyMaterial.setVec4(PhongShader3.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mCarBodyMaterial.setVec4(PhongShader3.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mCarBodyMaterial.setVec4(PhongShader3.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mCarBodyMaterial.setVec3(PhongShader3.LIGHT_KEY, light[0], light[1],
                light[2]);
        mCarBodyMaterial.setVec3(PhongShader3.EYE_KEY, eye[0], eye[1], eye[2]);

        mCarTireMaterial.setVec4(DiffuseShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mCarTireMaterial.setVec4(DiffuseShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mCarTireMaterial.setVec4(DiffuseShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mCarTireMaterial.setVec4(DiffuseShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mCarTireMaterial.setVec3(DiffuseShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mCarTireMaterial
                .setVec3(DiffuseShader2.EYE_KEY, eye[0], eye[1], eye[2]);

        mCarGlassMaterial.setVec4(GlassShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mCarGlassMaterial.setVec4(GlassShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mCarGlassMaterial.setVec4(GlassShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mCarGlassMaterial.setVec4(GlassShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mCarGlassMaterial.setVec3(GlassShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mCarGlassMaterial.setVec3(GlassShader2.EYE_KEY, eye[0], eye[1], eye[2]);

        mCarWheelMaterial.setVec4(MetalShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mCarWheelMaterial.setVec4(MetalShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mCarWheelMaterial.setVec4(MetalShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mCarWheelMaterial.setVec4(MetalShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mCarWheelMaterial.setVec3(MetalShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mCarWheelMaterial.setVec3(MetalShader2.EYE_KEY, eye[0], eye[1], eye[2]);

        mCarBackMaterial.setVec4(DiffuseShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mCarBackMaterial.setVec4(DiffuseShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mCarBackMaterial.setVec4(DiffuseShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mCarBackMaterial.setVec4(DiffuseShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mCarBackMaterial.setVec3(DiffuseShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mCarBackMaterial
                .setVec3(DiffuseShader2.EYE_KEY, eye[0], eye[1], eye[2]);

        mCarGrillMaterial.setVec4(DiffuseShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mCarGrillMaterial.setVec4(DiffuseShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mCarGrillMaterial.setVec4(DiffuseShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mCarGrillMaterial.setVec4(DiffuseShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mCarGrillMaterial.setVec3(DiffuseShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mCarGrillMaterial.setVec3(DiffuseShader2.EYE_KEY, eye[0], eye[1],
                eye[2]);

        mCarLightMaterial.setVec4(GlassShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mCarLightMaterial.setVec4(GlassShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mCarLightMaterial.setVec4(GlassShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mCarLightMaterial.setVec4(GlassShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mCarLightMaterial.setVec3(GlassShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mCarLightMaterial.setVec3(GlassShader2.EYE_KEY, eye[0], eye[1], eye[2]);

        mCarInsideMaterial.setVec4(PhongShader2.MAT1_KEY, matO[0], matO[4],
                matO[8], matO[12]);
        mCarInsideMaterial.setVec4(PhongShader2.MAT2_KEY, matO[1], matO[5],
                matO[9], matO[13]);
        mCarInsideMaterial.setVec4(PhongShader2.MAT3_KEY, matO[2], matO[6],
                matO[10], matO[14]);
        mCarInsideMaterial.setVec4(PhongShader2.MAT4_KEY, matO[3], matO[7],
                matO[11], matO[15]);
        mCarInsideMaterial.setVec3(PhongShader2.LIGHT_KEY, light[0], light[1],
                light[2]);
        mCarInsideMaterial
                .setVec3(PhongShader2.EYE_KEY, eye[0], eye[1], eye[2]);

        // ---------------------------- thumbnail glasses

        eye[0] = 0.0f;
        eye[1] = 0.0f;
        eye[2] = EYE_TO_OBJECT;
        eye[3] = 1.0f;

        mReflectionMaterial.setVec4(ReflectionShader.MAT1_KEY, matT[0],
                matT[4], matT[8], matT[12]);
        mReflectionMaterial.setVec4(ReflectionShader.MAT2_KEY, matT[1],
                matT[5], matT[9], matT[13]);
        mReflectionMaterial.setVec4(ReflectionShader.MAT3_KEY, matT[2],
                matT[6], matT[10], matT[14]);
        mReflectionMaterial.setVec4(ReflectionShader.MAT4_KEY, matT[3],
                matT[7], matT[11], matT[15]);
        mReflectionMaterial.setVec3(ReflectionShader.LIGHT_KEY, light[0],
                light[1], light[2]);
        mReflectionMaterial.setVec3(ReflectionShader.EYE_KEY, eye[0], eye[1],
                eye[2]);

        List<SXRPickedObject> pickedObjects = SXRPicker.findObjects(mSXRContext
                .getMainScene());
        if (SelectionMode && pickedObjects.size() > 0) {
            SXRSceneObject pickedObject = pickedObjects.get(0).getHitObject();
            for (int i = 0; i < THUMBNAIL_NUM; ++i)
                if (ThumbnailObject[i].equals(pickedObject)) {
                    ThumbnailSelected = i;
                    break;
                }
            SelectionActive = true;
        } else
            SelectionActive = false;

        if (isSingleTapped) {
            SelectionMode = !SelectionMode;
            if (SelectionMode) {
                for (int i = 0; i < THUMBNAIL_NUM; ++i) {
                    ThumbnailGlasses[i].setRenderMask(SXRRenderMaskBit.Left
                            | SXRRenderMaskBit.Right);
                    Thumbnails[i].getRenderData().setRenderMask(
                            SXRRenderMaskBit.Left | SXRRenderMaskBit.Right);
                }
                for (int I = 0; I < THUMBNAIL_NUM; I++)
                    for (int i = 0; i < Objects[I].getChildrenCount(); i++)
                        Objects[I].getChildByIndex(i).getRenderData()
                                .setRenderMask(0);
            } else {
                for (int i = 0; i < THUMBNAIL_NUM; ++i) {
                    ThumbnailGlasses[i].setRenderMask(0);
                    Thumbnails[i].getRenderData().setRenderMask(0);
                }
                for (int I = 0; I < THUMBNAIL_NUM; I++)
                    for (int i = 0; i < Objects[I].getChildrenCount(); i++)
                        Objects[I].getChildByIndex(i).getRenderData()
                                .setRenderMask(0);
                for (int i = 0; i < Objects[ThumbnailSelected]
                        .getChildrenCount(); i++)
                    Objects[ThumbnailSelected]
                            .getChildByIndex(i)
                            .getRenderData()
                            .setRenderMask(
                                    SXRRenderMaskBit.Left
                                            | SXRRenderMaskBit.Right);
            }
        }
    }

    public void onButtonDown() {
        mIsButtonDown = true;
    }

    public void onSingleTap(MotionEvent e) {
        mIsSingleTapped = true;
    }

}
