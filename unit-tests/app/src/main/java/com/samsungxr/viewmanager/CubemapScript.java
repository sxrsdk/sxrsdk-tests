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

import com.samsungxr.FutureWrapper;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRScript;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRCylinderNode;
import com.samsungxr.nodes.SXRSphereNode;

import android.util.Log;

public class CubemapScript extends SXRScript {

    private static final float CUBE_WIDTH = 20.0f;
    private static final float SCALE_FACTOR = 2.0f;
    private SXRContext mSXRContext = null;

    // Type of object for the environment
    // 0: surrounding sphere using SXRSphereNode
    // 1: surrounding cube using SXRCubeNode and 1 SXRCubemapTexture
    //    (method A)
    // 2: surrounding cube using SXRCubeNode and compressed ETC2 textures
    //    (method B, best performance)
    // 3: surrounding cube using SXRCubeNode and 6 SXRTexture's
    //    (method C)
    // 4: surrounding cylinder using SXRCylinderNode
    // 5: surrounding cube using six SXRSceneOjbects (quads)
    private static final int mEnvironmentType = 2;

    // Type of object for the reflective object
    // 0: reflective sphere using SXRSphereNode
    // 1: reflective sphere using OBJ model
    private static final int mReflectiveType = 0;
    
    @Override
    public void onInit(SXRContext sxrContext) {
        mSXRContext = sxrContext;

        SXRScene scene = mSXRContext.getNextMainScene();
        scene.setStatsEnabled(true);
        scene.setFrustumCulling(true);

        // Uncompressed cubemap texture
        Future<SXRTexture> futureCubemapTexture = sxrContext
                .loadFutureCubemapTexture(new SXRAndroidResource(mSXRContext,
                        R.raw.beach));

        SXRMaterial cubemapMaterial = new SXRMaterial(sxrContext,
                SXRMaterial.SXRShaderType.Cubemap.ID);
        cubemapMaterial.setMainTexture(futureCubemapTexture);

        // Compressed cubemap texture
        Future<SXRTexture> futureCompressedCubemapTexture = sxrContext
                .loadFutureCompressedCubemapTexture(new SXRAndroidResource(mSXRContext,
                        R.raw.museum));
        
        SXRMaterial compressedCubemapMaterial = new SXRMaterial(sxrContext,
                SXRMaterial.SXRShaderType.Cubemap.ID);
        compressedCubemapMaterial.setMainTexture(futureCompressedCubemapTexture);

        // List of textures (one per face)
        ArrayList<Future<SXRTexture>> futureTextureList = new ArrayList<Future<SXRTexture>>(6);
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.back)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.right)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.front)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.left)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.top)));
        futureTextureList.add(sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.bottom)));

        switch (mEnvironmentType) {
        case 0:
            // ///////////////////////////////////////////////////////
            // create surrounding sphere using SXRSphereNode //
            // ///////////////////////////////////////////////////////
            SXRSphereNode mSphereEvironment = new SXRSphereNode(
                    sxrContext, 18, 36, false, cubemapMaterial, 4, 4);
            mSphereEvironment.getTransform().setScale(CUBE_WIDTH, CUBE_WIDTH,
                    CUBE_WIDTH);
            scene.addNode(mSphereEvironment);
            break;

        case 1:
            // ////////////////////////////////////////////////////////////
            // create surrounding cube using SXRCubeNode method A //
            // ////////////////////////////////////////////////////////////
            SXRCubeNode mCubeEvironment = new SXRCubeNode(
                    sxrContext, false, cubemapMaterial);
            mCubeEvironment.getTransform().setScale(CUBE_WIDTH, CUBE_WIDTH,
                    CUBE_WIDTH);
            scene.addNode(mCubeEvironment);
            break;

        case 2:
        	// /////////////////////////////////////////////////////////////
        	// create surrounding cube using compressed textures method B //
        	// /////////////////////////////////////////////////////////////
        	mCubeEvironment = new SXRCubeNode(
        			sxrContext, false, compressedCubemapMaterial);
        	mCubeEvironment.getTransform().setScale(CUBE_WIDTH, CUBE_WIDTH,
        			CUBE_WIDTH);
        	scene.addNode(mCubeEvironment);
        	break;

        case 3:
            // ////////////////////////////////////////////////////////////
            // create surrounding cube using SXRCubeNode method C //
            // ////////////////////////////////////////////////////////////
            mCubeEvironment = new SXRCubeNode(
                    sxrContext, false, futureTextureList, 2);
            mCubeEvironment.getTransform().setScale(CUBE_WIDTH, CUBE_WIDTH,
                    CUBE_WIDTH);
            scene.addNode(mCubeEvironment);
            break;

        case 4:
            // ///////////////////////////////////////////////////////////
            // create surrounding cylinder using SXRCylinderNode //
            // ///////////////////////////////////////////////////////////
            SXRCylinderNode mCylinderEvironment = new SXRCylinderNode(
                    sxrContext, 0.5f, 0.5f, 1.0f, 10, 36, false, cubemapMaterial, 2, 4);
            mCylinderEvironment.getTransform().setScale(CUBE_WIDTH, CUBE_WIDTH,
                    CUBE_WIDTH);
            scene.addNode(mCylinderEvironment);
            break;

        case 5:
            // /////////////////////////////////////////////////////////////
            // create surrounding cube using six SXRSceneOjbects (quads) //
            // /////////////////////////////////////////////////////////////
            FutureWrapper<SXRMesh> futureQuadMesh = new FutureWrapper<SXRMesh>(
                    sxrContext.createQuad(CUBE_WIDTH, CUBE_WIDTH));

            SXRNode mFrontFace = new SXRNode(sxrContext,
                    futureQuadMesh, futureCubemapTexture);
            mFrontFace.getRenderData().setMaterial(cubemapMaterial);
            mFrontFace.setName("front");
            scene.addNode(mFrontFace);
            mFrontFace.getTransform().setPosition(0.0f, 0.0f,
                    -CUBE_WIDTH * 0.5f);

            SXRNode backFace = new SXRNode(sxrContext,
                    futureQuadMesh, futureCubemapTexture);
            backFace.getRenderData().setMaterial(cubemapMaterial);
            backFace.setName("back");
            scene.addNode(backFace);
            backFace.getTransform().setPosition(0.0f, 0.0f, CUBE_WIDTH * 0.5f);
            backFace.getTransform().rotateByAxis(180.0f, 0.0f, 1.0f, 0.0f);

            SXRNode leftFace = new SXRNode(sxrContext,
                    futureQuadMesh, futureCubemapTexture);
            leftFace.getRenderData().setMaterial(cubemapMaterial);
            leftFace.setName("left");
            scene.addNode(leftFace);
            leftFace.getTransform().setPosition(-CUBE_WIDTH * 0.5f, 0.0f, 0.0f);
            leftFace.getTransform().rotateByAxis(90.0f, 0.0f, 1.0f, 0.0f);

            SXRNode rightFace = new SXRNode(sxrContext,
                    futureQuadMesh, futureCubemapTexture);
            rightFace.getRenderData().setMaterial(cubemapMaterial);
            rightFace.setName("right");
            scene.addNode(rightFace);
            rightFace.getTransform().setPosition(CUBE_WIDTH * 0.5f, 0.0f, 0.0f);
            rightFace.getTransform().rotateByAxis(-90.0f, 0.0f, 1.0f, 0.0f);

            SXRNode topFace = new SXRNode(sxrContext,
                    futureQuadMesh, futureCubemapTexture);
            topFace.getRenderData().setMaterial(cubemapMaterial);
            topFace.setName("top");
            scene.addNode(topFace);
            topFace.getTransform().setPosition(0.0f, CUBE_WIDTH * 0.5f, 0.0f);
            topFace.getTransform().rotateByAxis(90.0f, 1.0f, 0.0f, 0.0f);

            SXRNode bottomFace = new SXRNode(sxrContext,
                    futureQuadMesh, futureCubemapTexture);
            bottomFace.getRenderData().setMaterial(cubemapMaterial);
            bottomFace.setName("bottom");
            scene.addNode(bottomFace);
            bottomFace.getTransform().setPosition(0.0f, -CUBE_WIDTH * 0.5f,
                    0.0f);
            bottomFace.getTransform().rotateByAxis(-90.0f, 1.0f, 0.0f, 0.0f);
            break;
        }

        SXRMaterial cubemapReflectionMaterial = new SXRMaterial(sxrContext,
                SXRMaterial.SXRShaderType.CubemapReflection.ID);
        cubemapReflectionMaterial.setMainTexture(futureCubemapTexture);

        SXRNode sphere = null;
        switch (mReflectiveType) {
        case 0:
            // ///////////////////////////////////////////////////////
            // create reflective sphere using SXRSphereNode //
            // ///////////////////////////////////////////////////////
            sphere = new SXRSphereNode(sxrContext, 18, 36, true,
                    cubemapReflectionMaterial);
            break;

        case 1:
            // ////////////////////////////////////////////
            // create reflective sphere using OBJ model //
            // ////////////////////////////////////////////
            Future<SXRMesh> futureSphereMesh = sxrContext
                    .getAssetLoader().loadFutureMesh(new SXRAndroidResource(mSXRContext,
                            R.raw.sphere));
            sphere = new SXRNode(sxrContext, futureSphereMesh,
                    futureCubemapTexture);
            sphere.getRenderData().setMaterial(cubemapReflectionMaterial);
            break;
        }

        if (sphere != null) {
            sphere.setName("sphere");
//            scene.addNode(sphere);
            sphere.getTransform().setScale(SCALE_FACTOR, SCALE_FACTOR,
                    SCALE_FACTOR);
            sphere.getTransform().setPosition(0.0f, 0.0f, -CUBE_WIDTH * 0.25f);
        }
        
        for (SXRNode so : scene.getWholeNodes()) {
            Log.v("", "scene object name : " + so.getName());
        }
    }

    @Override
    public void onStep() {
        FPSCounter.tick();
    }
}
