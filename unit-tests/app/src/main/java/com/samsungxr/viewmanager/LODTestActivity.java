package com.samsungxr.viewmanager;

import com.samsungxr.SXRActivity;
import com.samsungxr.viewmanager.LODTestScript;

import android.app.Activity;
import android.os.Bundle;

public class LODTestActivity extends SXRActivity
{
    LODTestScript lodScript;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        lodScript = new LODTestScript();
        setScript(lodScript, "sxr.xml");
    }
}
