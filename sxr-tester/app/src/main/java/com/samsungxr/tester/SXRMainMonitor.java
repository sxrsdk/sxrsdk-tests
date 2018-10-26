package com.samsungxr.tester;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;


public interface SXRMainMonitor {
    void onInitCalled(SXRContext context, SXRScene scene);
    void onSceneRendered();
    void onAssetLoaded(SXRSceneObject asset);
}
