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

import com.samsungxr.*;
import com.samsungxr.SXRPicker.SXRPickedObject;
import com.samsungxr.utility.Log;

public class eyepickViewManager extends SXRScript {

    private static final String TAG = "SampleViewManager";

    private static final float UNPICKED_COLOR_R = 0.7f;
    private static final float UNPICKED_COLOR_G = 0.7f;
    private static final float UNPICKED_COLOR_B = 0.7f;
    private static final float UNPICKED_COLOR_A = 1.0f;
    private static final float PICKED_COLOR_R = 1.0f;
    private static final float PICKED_COLOR_G = 0.0f;
    private static final float PICKED_COLOR_B = 0.0f;
    private static final float PICKED_COLOR_A = 1.0f;

    private SXRContext mSXRContext = null;
    private ColorShader mColorShader = null;
    private List<SXRNode> mObjects = new ArrayList<SXRNode>();

    private SXRActivity mActivity;

    eyepickViewManager(SXRActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onInit(SXRContext sxrContext) {
        mSXRContext = sxrContext;

        SXRScene mainScene = mSXRContext.getNextMainScene();

        mainScene.getMainCameraRig().getLeftCamera()
                .setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        mainScene.getMainCameraRig().getRightCamera()
                .setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        mColorShader = new ColorShader(mSXRContext);

        /*
         * Adding Boards
         */
        SXRNode object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(0.0f, 3.0f, -5.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(0.0f, -3.0f, -5.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(-3.0f, 0.0f, -5.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(3.0f, 0.0f, -5.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(3.0f, 3.0f, -5.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(3.0f, -3.0f, -5.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(-3.0f, 3.0f, -5.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(-3.0f, -3.0f, -5.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        /*
         * Adding bunnies.
         */

        SXRMesh mesh = null;
        try {
            mesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(mSXRContext,
                    "bunny.obj"));
        } catch (IOException e) {
            e.printStackTrace();
            mesh = null;
        }
        if (mesh == null) {
            mActivity.finish();
            Log.e(TAG, "Mesh was not loaded. Stopping application!");
        }
        // activity was stored in order to stop the application if the mesh is
        // not loaded. Since we don't need anymore, we set it to null to reduce
        // chance of memory leak.
        mActivity = null;

        // These 2 are testing by the whole mesh.
        object = getColorMesh(1.0f, mesh);
        object.getTransform().setPosition(0.0f, 0.0f, -2.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorMesh(1.0f, mesh);
        object.getTransform().setPosition(3.0f, 3.0f, -2.0f);
        attachDefaultEyePointee(object);
        mainScene.addNode(object);
        object.getRenderData().setCullTest(false);
        mObjects.add(object);

        // These 2 are testing by the bounding box of the mesh.
        object = getColorMesh(2.0f, mesh);
        object.getTransform().setPosition(-5.0f, 0.0f, -2.0f);
        attachBoundingBoxEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);

        object = getColorMesh(1.0f, mesh);
        object.getTransform().setPosition(0.0f, -5.0f, -2.0f);
        attachBoundingBoxEyePointee(object);
        mainScene.addNode(object);
        mObjects.add(object);
    }

    @Override
    public void onStep() {
        for (SXRNode object : mObjects) {
            object.getRenderData()
                    .getMaterial()
                    .setVec4(ColorShader.COLOR_KEY, UNPICKED_COLOR_R,
                            UNPICKED_COLOR_G, UNPICKED_COLOR_B,
                            UNPICKED_COLOR_A);
        }
        for (SXRPickedObject pickedObject : SXRPicker.findObjects(mSXRContext
                .getMainScene())) {
            for (SXRNode object : mObjects) {
                if (pickedObject.getHitObject().equals(object)) {
                    object.getRenderData()
                            .getMaterial()
                            .setVec4(ColorShader.COLOR_KEY, PICKED_COLOR_R,
                                    PICKED_COLOR_G, PICKED_COLOR_B,
                                    PICKED_COLOR_A);
                    break;
                }
            }
        }
    }

    private SXRNode getColorBoard(float width, float height) {
        SXRMaterial material = new SXRMaterial(mSXRContext,
                mColorShader.getShaderId());
        material.setVec4(ColorShader.COLOR_KEY, UNPICKED_COLOR_R,
                UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);
        SXRNode board = new SXRNode(mSXRContext, width, height);
        board.getRenderData().setMaterial(material);

        return board;
    }

    private SXRNode getColorMesh(float scale, SXRMesh mesh) {
        SXRMaterial material = new SXRMaterial(mSXRContext,
                mColorShader.getShaderId());
        material.setVec4(ColorShader.COLOR_KEY, UNPICKED_COLOR_R,
                UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);

        SXRNode meshObject = null;
        meshObject = new SXRNode(mSXRContext, mesh);
        meshObject.getTransform().setScale(scale, scale, scale);
        meshObject.getRenderData().setMaterial(material);

        return meshObject;
    }

    private void attachDefaultEyePointee(SXRNode sceneObject) {
        sceneObject.attachEyePointeeHolder();
    }

    private void attachBoundingBoxEyePointee(SXRNode sceneObject) {
        SXREyePointeeHolder eyePointeeHolder = new SXREyePointeeHolder(
                mSXRContext);
        SXRMeshEyePointee eyePointee = new SXRMeshEyePointee(mSXRContext,
                sceneObject.getRenderData().getMesh().getBoundingBox());
        eyePointeeHolder.addPointee(eyePointee);
        sceneObject.attachEyePointeeHolder(eyePointeeHolder);
    }
}
