package com.samsungxr;

import com.samsungxr.viewmanager.TestViewManager;

public class SXRTestActivity extends DefaultSXRTestActivity {

    @Override
    protected void initSXRTestActivity() {
        setMain(new TestViewManager(), "sxr.xml");
    }
}
