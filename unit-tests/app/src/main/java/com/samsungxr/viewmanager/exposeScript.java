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

import java.util.concurrent.Future;

import com.samsungxr.FutureWrapper;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXREyePointeeHolder;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRMeshEyePointee;
import com.samsungxr.SXRPicker;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRRenderData.SXRRenderMaskBit;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRScript;
import com.samsungxr.tests.R;

import android.util.Log;

public class exposeScript extends SXRScript {

    private static final float CUBE_WIDTH = 20.0f;
    private SXRContext mSXRContext = null;
    private SXRNode mFrontFace = null;
    private SXRNode mFrontFace2 = null;
    private SXRNode mFrontFace3 = null;
    private static final float SCALE_FACTOR = 2.0f;
    @Override
    public void onInit(SXRContext sxrContext) {
        mSXRContext = sxrContext;
        
        SXRScene scene = mSXRContext.getNextMainScene();

        FutureWrapper<SXRMesh> futureMesh = new FutureWrapper<SXRMesh>(
                sxrContext.createQuad(CUBE_WIDTH, CUBE_WIDTH));
        Future<SXRTexture> futureCubemapTexture = sxrContext
                .loadFutureCubemapTexture(new SXRAndroidResource(mSXRContext,
                        R.raw.beach));
        mFrontFace = new SXRNode(sxrContext, futureMesh,
                sxrContext.loadFutureTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.front)));
        mFrontFace.setName("front");
        scene.addNode(mFrontFace);
        mFrontFace.getTransform().setPosition(0.0f, 0.0f, -CUBE_WIDTH * 0.5f);

        mFrontFace2 = new SXRNode(sxrContext, futureMesh,
                sxrContext.loadFutureTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.front)));
        mFrontFace2.setName("front2");
        scene.addNode(mFrontFace2);
        mFrontFace2.getTransform().setPosition(0.0f, 0.0f,
                -CUBE_WIDTH * 0.5f * 2.0f);

        mFrontFace3 = new SXRNode(sxrContext, futureMesh,
                sxrContext.loadFutureTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.front)));
        mFrontFace3.setName("front3");
        scene.addNode(mFrontFace3);
        mFrontFace3.getTransform().setPosition(0.0f, 0.0f,
                -CUBE_WIDTH * 0.5f * 3.0f);

        SXRNode backFace = new SXRNode(sxrContext, futureMesh,
                sxrContext.loadFutureTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.back)));
        backFace.setName("back");
        scene.addNode(backFace);
        backFace.getTransform().setPosition(0.0f, 0.0f, CUBE_WIDTH * 0.5f);
        backFace.getTransform().rotateByAxis(180.0f, 0.0f, 1.0f, 0.0f);

        SXRNode leftFace = new SXRNode(sxrContext, futureMesh,
                sxrContext.loadFutureTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.left)));
        leftFace.setName("left");
        scene.addNode(leftFace);
        leftFace.getTransform().setPosition(-CUBE_WIDTH * 0.5f, 0.0f, 0.0f);
        leftFace.getTransform().rotateByAxis(90.0f, 0.0f, 1.0f, 0.0f);

        leftFace.getRenderData().setRenderMask(SXRRenderMaskBit.Left);

        SXRNode rightFace = new SXRNode(sxrContext, futureMesh,
                sxrContext.loadFutureTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.right)));
        rightFace.setName("right");
        scene.addNode(rightFace);
        rightFace.getTransform().setPosition(CUBE_WIDTH * 0.5f, 0.0f, 0.0f);
        rightFace.getTransform().rotateByAxis(-90.0f, 0.0f, 1.0f, 0.0f);

        rightFace.getRenderData().setRenderMask(SXRRenderMaskBit.Right);

        SXRNode topFace = new SXRNode(sxrContext, futureMesh,
                sxrContext.loadFutureTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.top)));
        topFace.setName("top");
        scene.addNode(topFace);
        topFace.getTransform().setPosition(0.0f, CUBE_WIDTH * 0.5f, 0.0f);
        topFace.getTransform().rotateByAxis(90.0f, 1.0f, 0.0f, 0.0f);

        SXRNode bottomFace = new SXRNode(sxrContext, futureMesh,
                sxrContext.loadFutureTexture(new SXRAndroidResource(
                        mSXRContext, R.drawable.bottom)));
        bottomFace.setName("bottom");
        scene.addNode(bottomFace);
        bottomFace.getTransform().setPosition(0.0f, -CUBE_WIDTH * 0.5f, 0.0f);
        bottomFace.getTransform().rotateByAxis(-90.0f, 1.0f, 0.0f, 0.0f);

        SXREyePointeeHolder eyePointeeHolder = new SXREyePointeeHolder(
                sxrContext);
        SXRMeshEyePointee meshEyePointee = new SXRMeshEyePointee(sxrContext,
                mFrontFace.getRenderData().getMesh());
        eyePointeeHolder.addPointee(meshEyePointee);
        mFrontFace.attachEyePointeeHolder(eyePointeeHolder);

        SXREyePointeeHolder eyePointeeHolder2 = new SXREyePointeeHolder(
                sxrContext);
        SXRMeshEyePointee meshEyePointee2 = new SXRMeshEyePointee(sxrContext,
                mFrontFace2.getRenderData().getMesh());
        eyePointeeHolder2.addPointee(meshEyePointee2);
        mFrontFace2.attachEyePointeeHolder(eyePointeeHolder2);

        SXREyePointeeHolder eyePointeeHolder3 = new SXREyePointeeHolder(
                sxrContext);
        SXRMesh boundingBox = mFrontFace3.getRenderData().getMesh()
                .getBoundingBox();
        SXRMeshEyePointee meshEyePointee3 = new SXRMeshEyePointee(sxrContext,
                boundingBox);
        eyePointeeHolder3.addPointee(meshEyePointee3);
        mFrontFace3.attachEyePointeeHolder(eyePointeeHolder3);
        // reflective object
        Future<SXRMesh> futureSphereMesh = sxrContext
                .getAssetLoader().loadFutureMesh(new SXRAndroidResource(mSXRContext,
                        R.raw.sphere));
        SXRMaterial cubemapReflectionMaterial = new SXRMaterial(sxrContext,
                SXRMaterial.SXRShaderType.CubemapReflection.ID);
        cubemapReflectionMaterial.setMainTexture(futureCubemapTexture);
        cubemapReflectionMaterial.setOpacity(0.25f);

        SXRNode sphere = new SXRNode(sxrContext,
                futureSphereMesh, futureCubemapTexture);
        sphere.getRenderData().setMaterial(cubemapReflectionMaterial);
        sphere.setName("sphere");
        scene.addNode(sphere);
        sphere.getTransform()
                .setScale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
        sphere.getTransform().setPosition(0.0f, 0.0f, -CUBE_WIDTH * 0.25f);
    }

    @Override
    public void onStep() {
        FPSCounter.tick();
        mFrontFace.getRenderData().getMaterial().setOpacity(1.0f);
        mFrontFace2.getRenderData().getMaterial().setOpacity(1.0f);
        mFrontFace3.getRenderData().getMaterial().setOpacity(1.0f);
        SXREyePointeeHolder[] eyePointeeHolders = SXRPicker
                .pickScene(mSXRContext.getMainScene());
        for (SXREyePointeeHolder eyePointeeHolder : eyePointeeHolders) {
            if (eyePointeeHolder.getOwnerObject().equals(mFrontFace)) {
                mFrontFace.getRenderData().getMaterial().setOpacity(0.5f);
            }
            if (eyePointeeHolder.getOwnerObject().equals(mFrontFace2)) {
                mFrontFace2.getRenderData().getMaterial().setOpacity(0.5f);
            }
            if (eyePointeeHolder.getOwnerObject().equals(mFrontFace3)) {
                mFrontFace3.getRenderData().getMaterial().setOpacity(0.5f);
            }
        }
    }


}
