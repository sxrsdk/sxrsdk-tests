package com.samsungxr.tester;

import com.samsungxr.SXRMain;

public abstract class  SXRTestableMain extends SXRMain{
    abstract void setMainMonitor(SXRMainMonitor mainMonitor);
    abstract boolean isOnInitCalled();
    abstract boolean isSceneRendered();
}
