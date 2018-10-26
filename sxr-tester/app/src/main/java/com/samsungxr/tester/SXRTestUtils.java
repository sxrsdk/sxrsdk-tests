/*
 * Copyright (c) 2016. Samsung Electronics Co., LTD
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsungxr.tester;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.utility.Log;

public class SXRTestUtils implements SXRMainMonitor {
    private static final String TAG = SXRTestUtils.class.getSimpleName();
    protected static final int TEST_TIMEOUT = 2000;

    private SXRContext sxrContext;
    private final Object onInitLock;
    private final Object onStepLock;
    private SXRTestableMain testableMain;
    private SXRScene mainScene;
    private OnInitCallback onInitCallback;

    public SXRTestUtils(SXRTestableActivity testableSXRActivity) {
        sxrContext = null;
        onInitLock = new Object();
        onStepLock = new Object();
        if (testableSXRActivity == null) {
            throw new IllegalArgumentException();
        }
        testableMain = testableSXRActivity.getSXRTestableMain();
        testableMain.setMainMonitor(this);
        onInitCallback = null;
    }

    public SXRContext waitForOnInit() {
        if (sxrContext == null) {
            if (testableMain.isOnInitCalled()) {
                sxrContext = testableMain.getSXRContext();
                return sxrContext;
            }
            synchronized (onInitLock) {
                try {
                    Log.d(TAG, "Waiting for OnInit");
                    onInitLock.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "", e);
                    return null;
                }
                return sxrContext;
            }
        } else {
            return sxrContext;
        }
    }

    public void waitForSceneRendering() {

        if (testableMain.isSceneRendered()) {
            return;
        }

        synchronized (onStepLock) {
            try {
                Log.d(TAG, "Waiting for OnStep");
                onStepLock.wait();
            } catch (InterruptedException e) {
                Log.e(TAG, "", e);
                return;
            }
        }
    }

    @Override
    public void onInitCalled(SXRContext context, SXRScene mainScene) {
        sxrContext = context;
        this.mainScene = mainScene;
        if (onInitCallback != null) {
            onInitCallback.onInit(sxrContext);
        }
        synchronized (onInitLock) {
            onInitLock.notifyAll();
        }
        Log.d(TAG, "On Init called");
    }

    @Override
    public void onSceneRendered() {
        synchronized (onStepLock) {
            onStepLock.notifyAll();
        }
        Log.d(TAG, "OnSceneRenderedCalled");
    }

    public SXRContext getSxrContext() {
        return sxrContext;
    }

    public SXRScene getMainScene() {
        return mainScene;
    }

    public void setOnInitCallback(OnInitCallback callback) {
        this.onInitCallback = callback;
    }

    public interface OnInitCallback {
        void onInit(SXRContext sxrContext);
    }
}
