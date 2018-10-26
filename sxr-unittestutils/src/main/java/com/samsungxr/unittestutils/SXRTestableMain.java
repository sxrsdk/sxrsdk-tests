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

package com.samsungxr.unittestutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.samsungxr.SXRBitmapImage;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRTexture;
import com.samsungxr.utility.Log;

class SXRTestableMain extends SXRMain{

    private static final String TAG = SXRTestableMain.class.getSimpleName();
    private static final int WAIT_DISABLED = -1;
    private SXRContext sxrContext;
    private volatile SXRScene mainScene;
    private SXRMainMonitor mainMonitor;
    private boolean sceneRendered = false;
    private int framesRendered = 0;
    private final Object waitXFramesLock = new Object();
    private final Object waitForMonitor = new Object();
    private int waitForXFrames = WAIT_DISABLED;

    @Override
    public void onInit(SXRContext sxrContext) {
        this.sxrContext = sxrContext;
        mainScene = sxrContext.getMainScene();

        //Freeze the camera rig for the tests
        mainScene.getMainCameraRig().setCameraRigType(SXRCameraRig.SXRCameraRigType.Freeze.ID);
        synchronized (waitForMonitor) {
            while(mainMonitor == null) {
                try {
                    waitForMonitor.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG,"Interrupted wait for main monitor");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        mainMonitor.onInitCalled(sxrContext, mainScene);
    }

    @Override
    public void onStep() {
        sceneRendered = true;
        synchronized (waitXFramesLock) {
            if (waitForXFrames != WAIT_DISABLED) {
                framesRendered++;
                if (framesRendered >= waitForXFrames) {
                    mainMonitor.xFramesRendered();
                    waitForXFrames = WAIT_DISABLED;
                    framesRendered = 0;
                }
            }
        }
        mainMonitor.onSceneRendered();
    }

    public void setMainMonitor(SXRMainMonitor mainMonitor) {
        synchronized (waitForMonitor) {
            this.mainMonitor = mainMonitor;
            waitForMonitor.notifyAll();
        }
    }

    public boolean isOnInitCalled() {
        return (sxrContext != null);
    }

    public boolean isSceneRendered() {
        return sceneRendered;
    }

    public void notifyAfterXFrames(int frames) {
        synchronized (waitXFramesLock) {
            waitForXFrames = frames;
            framesRendered = 0;
        }

    }

    public SXRTexture getSplashTexture(SXRContext sxrContext) {
        Bitmap bitmap = BitmapFactory.decodeResource(
                sxrContext.getContext().getResources(),
                R.mipmap.ic_launcher);
        // return the correct splash screen bitmap
        SXRTexture tex = new SXRTexture(sxrContext);
        tex.setImage(new SXRBitmapImage(sxrContext, bitmap));
        return tex;
    }
}
