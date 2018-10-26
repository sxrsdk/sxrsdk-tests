package com.samsungxr.sample.LodTest;

import android.os.Bundle;
import android.view.MotionEvent;

import com.samsungxr.SXRActivity;
import com.samsungxr.SXRScene;

public class LODTestActivity extends SXRActivity
{
    LODTestMain lodMain;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        lodMain = new LODTestMain();
        setMain(lodMain, "sxr.xml");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            final SXRScene mainScene = getSXRContext().getMainScene();
            mainScene.setStatsEnabled(!mainScene.getStatsEnabled());
        }
        return super.dispatchTouchEvent(event);
    }

}
