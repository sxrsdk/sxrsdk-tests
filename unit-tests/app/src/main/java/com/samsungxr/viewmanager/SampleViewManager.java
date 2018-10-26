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
import java.util.concurrent.Future;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRScript;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.scene_objects.SXRCameraSceneObject;
import com.samsungxr.scene_objects.SXRConeSceneObject;
import com.samsungxr.scene_objects.SXRCubeSceneObject;
import com.samsungxr.scene_objects.SXRCylinderSceneObject;
import com.samsungxr.scene_objects.SXRSphereSceneObject;
import com.samsungxr.scene_objects.SXRTextViewSceneObject;
import com.samsungxr.scene_objects.SXRVideoSceneObject;
import com.samsungxr.scene_objects.SXRVideoSceneObject.SXRVideoType;
import com.samsungxr.scene_objects.SXRWebViewSceneObject;

import android.media.MediaPlayer;
import android.view.Gravity;
import android.webkit.WebView;

public class SampleViewManager extends SXRScript {
    private List<SXRSceneObject> objectList = new ArrayList<SXRSceneObject>();

    private int currentObject = 0;
    private SceneObjectActivity mActivity;

    //@Override
    //public SplashMode getSplashMode() {
    //    return SplashMode.NONE;
    //}

    SampleViewManager(SceneObjectActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onInit(SXRContext sxrContext) {

        SXRScene scene = sxrContext.getNextMainScene();

        // load texture asynchronously
        Future<SXRTexture> futureTexture = sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.gearvr_logo));
        Future<SXRTexture> futureTextureTop = sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.top));
        Future<SXRTexture> futureTextureBottom = sxrContext
                .loadFutureTexture(new SXRAndroidResource(sxrContext,
                        R.drawable.bottom));
        ArrayList<Future<SXRTexture>> futureTextureList = new ArrayList<Future<SXRTexture>>(
                3);
        futureTextureList.add(futureTextureTop);
        futureTextureList.add(futureTexture);
        futureTextureList.add(futureTextureBottom);

        // setup material
        SXRMaterial material = new SXRMaterial(sxrContext);
        material.setMainTexture(futureTexture);

        // create a scene object (this constructor creates a rectangular scene
        // object that uses the standard 'unlit' shader)
        SXRSceneObject quadObject = new SXRSceneObject(sxrContext, 4.0f, 2.0f);
        SXRCubeSceneObject cubeObject = new SXRCubeSceneObject(sxrContext,
                true, material);
        SXRSphereSceneObject sphereObject = new SXRSphereSceneObject(
                sxrContext, true, material);
        SXRCylinderSceneObject cylinderObject = new SXRCylinderSceneObject(
                sxrContext, 0.5f, 0.5f, 1.0f, 10, 36, true, futureTextureList, 2, 4);
        SXRConeSceneObject coneObject = new SXRConeSceneObject(sxrContext,
                true, material);
        SXRWebViewSceneObject webViewObject = createWebViewObject(sxrContext);
        SXRCameraSceneObject cameraObject = new SXRCameraSceneObject(
                sxrContext, 8.0f, 4.0f, mActivity.getCamera());
        SXRVideoSceneObject videoObject = createVideoObject(sxrContext);
        SXRTextViewSceneObject textViewSceneObject = new SXRTextViewSceneObject(
                sxrContext, "Hello World!");
        textViewSceneObject.setGravity(Gravity.CENTER);
        textViewSceneObject
                .setTextSize(textViewSceneObject.getTextSize() * 1.5f);
        objectList.add(quadObject);
        objectList.add(cubeObject);
        objectList.add(sphereObject);
        objectList.add(cylinderObject);
        objectList.add(coneObject);
        objectList.add(webViewObject);
        objectList.add(cameraObject);
        objectList.add(videoObject);
        objectList.add(textViewSceneObject);

        // turn all objects off, except the first one
        int listSize = objectList.size();
        for (int i = 1; i < listSize; i++) {
            objectList.get(i).getRenderData().setRenderMask(0);
        }

        quadObject.getRenderData().setMaterial(material);

        // set the scene object positions
        quadObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);
        cubeObject.getTransform().setPosition(0.0f, -1.0f, -3.0f);
        cylinderObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);
        coneObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);
        sphereObject.getTransform().setPosition(0.0f, -1.0f, -3.0f);
        cameraObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);
        videoObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);
        textViewSceneObject.getTransform().setPosition(0.0f, 0.0f, -2.0f);

        // add the scene objects to the scene graph
        scene.addSceneObject(quadObject);
        scene.addSceneObject(cubeObject);
        scene.addSceneObject(sphereObject);
        scene.addSceneObject(cylinderObject);
        scene.addSceneObject(coneObject);
        scene.addSceneObject(webViewObject);
        scene.addSceneObject(cameraObject);
        scene.addSceneObject(videoObject);
        scene.addSceneObject(textViewSceneObject);


        //OnTap copy
        SXRSceneObject object = objectList.get(currentObject);
        object.getRenderData().setRenderMask(0);
        if (object instanceof SXRVideoSceneObject) {
            SXRVideoSceneObject video = (SXRVideoSceneObject) object;
            video.getMediaPlayer().pause();
        }

        currentObject+=2;
        int totalObjects = objectList.size();


        if (currentObject >= totalObjects) {
            currentObject = 0;
        }

        object = objectList.get(currentObject);
        if (object instanceof SXRVideoSceneObject) {
            SXRVideoSceneObject video = (SXRVideoSceneObject) object;
            video.getMediaPlayer().start();
        }

        object.getRenderData().setRenderMask(
                SXRRenderData.SXRRenderMaskBit.Left
                        | SXRRenderData.SXRRenderMaskBit.Right);

    }

    private SXRVideoSceneObject createVideoObject(SXRContext sxrContext) {
        MediaPlayer mediaPlayer = MediaPlayer.create(sxrContext.getContext(),
                R.raw.tron);
        SXRVideoSceneObject video = new SXRVideoSceneObject(sxrContext, 8.0f,
                4.0f, mediaPlayer, SXRVideoType.MONO);
        video.setName("video");
        return video;
    }

    private SXRWebViewSceneObject createWebViewObject(SXRContext sxrContext) {
        WebView webView = mActivity.getWebView();
        SXRWebViewSceneObject webObject = new SXRWebViewSceneObject(sxrContext,
                8.0f, 4.0f, webView);
        webObject.setName("web view object");
        webObject.getRenderData().getMaterial().setOpacity(1.0f);
        webObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);

        return webObject;
    }

    private float mYAngle = 0;
    private float mXAngle = 0;

    public void setYAngle(float angle) {
        mYAngle = angle;
    }

    public void setXAngle(float angle) {
        mXAngle = angle;
    }

    public void onPause() {
        SXRSceneObject object = objectList.get(currentObject);
        if (object instanceof SXRVideoSceneObject) {
            SXRVideoSceneObject video = (SXRVideoSceneObject) object;
            video.getMediaPlayer().pause();
        }
    }

    public void onTap() {

        SXRSceneObject object = objectList.get(currentObject);
        object.getRenderData().setRenderMask(0);
        if (object instanceof SXRVideoSceneObject) {
            SXRVideoSceneObject video = (SXRVideoSceneObject) object;
            video.getMediaPlayer().pause();
        }

        currentObject++;
        int totalObjects = objectList.size();


        if (currentObject >= totalObjects) {
            currentObject = 0;
        }

        object = objectList.get(currentObject);
        if (object instanceof SXRVideoSceneObject) {
            SXRVideoSceneObject video = (SXRVideoSceneObject) object;
            video.getMediaPlayer().start();
        }

        object.getRenderData().setRenderMask(
                SXRRenderData.SXRRenderMaskBit.Left
                        | SXRRenderData.SXRRenderMaskBit.Right);

    }

    @Override
    public void onStep() {
        objectList.get(currentObject).getTransform()
                .rotateByAxis(mXAngle, 1.0f, 0.0f, 0.0f);
        objectList.get(currentObject).getTransform()
                .rotateByAxis(mYAngle, 0.0f, 1.0f, 0.0f);
    }
}
