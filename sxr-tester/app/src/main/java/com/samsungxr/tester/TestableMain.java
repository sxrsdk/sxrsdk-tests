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

package com.samsungxr.tester;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.samsungxr.SXRBitmapTexture;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRTexture;
import com.samsungxr.utility.Log;

public class TestableMain extends SXRTestableMain {

    private static final String TAG = TestableMain.class.getSimpleName();
    private SXRContext sxrContext;
    private SXRScene mainScene;
    private SXRMainMonitor mainMonitor;
    private boolean sceneRendered = false;

    @Override
    public void onInit(SXRContext sxrContext) {
        this.sxrContext = sxrContext;
        mainScene = sxrContext.getNextMainScene();
        if (mainMonitor != null) {
            mainMonitor.onInitCalled(sxrContext, mainScene);
        } else {
            Log.d(TAG, "On Init callback is null when on init is called");
        }
    }

    @Override
    public void onStep() {
        if (mainMonitor != null && !sceneRendered) {
            sceneRendered = true;
            mainMonitor.onSceneRendered();
        }
    }

    @Override
    public void setMainMonitor(SXRMainMonitor mainMonitor) {
        this.mainMonitor = mainMonitor;
    }

    @Override
    public boolean isOnInitCalled() {
        return (sxrContext != null);
    }

    public boolean isSceneRendered() {
        return sceneRendered;
    }

    @Override
    public SXRTexture getSplashTexture(SXRContext sxrContext) {
        Bitmap bitmap = BitmapFactory.decodeResource(
                sxrContext.getContext().getResources(),
                R.mipmap.ic_launcher);
        // return the correct splash screen bitmap
        return new SXRBitmapTexture(sxrContext, bitmap);
    }
}
