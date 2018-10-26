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

import android.graphics.Color;

import com.samsungxr.DefaultSXRTestActivity;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRScript;
import com.samsungxr.scene_objects.SXRTextViewSceneObject;


public class TestDefaultSXRViewManager extends SXRMain {

    public static SXRContext mSXRContext = null;
    public static int DelayTest = 3500;

    @Override
    public void onInit(SXRContext sxrContext) {
        synchronized (DefaultSXRTestActivity.class) {
            DefaultSXRTestActivity.sContextLoaded = true;
            DefaultSXRTestActivity.class.notify();
        }
        mSXRContext = sxrContext;
    }

    @Override
    public SplashMode getSplashMode() {
        return SplashMode.NONE;
    }

    @Override
    public void onStep() {

    }

}
