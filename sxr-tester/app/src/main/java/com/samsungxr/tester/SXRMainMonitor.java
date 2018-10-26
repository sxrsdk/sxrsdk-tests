package com.samsungxr.tester;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;


public interface SXRMainMonitor {
    void onInitCalled(SXRContext context, SXRScene scene);
    void onSceneRendered();
    void onAssetLoaded(SXRNode asset);
}
