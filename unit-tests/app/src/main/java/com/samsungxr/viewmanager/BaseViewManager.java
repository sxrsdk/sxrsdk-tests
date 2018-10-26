package com.samsungxr.viewmanager;

import com.samsungxr.SXRContext;
import com.samsungxr.SXREyePointeeHolder;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMeshCollider;
import com.samsungxr.SXRMeshEyePointee;
import com.samsungxr.SXRPicker;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRScript;
import com.samsungxr.misc.ColorShader;

import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by santhyago on 1/15/15.
 */
public abstract class BaseViewManager extends SXRMain {
    protected static final float UNPICKED_COLOR_R = 0.7f;
    protected static final float UNPICKED_COLOR_G = 0.7f;
    protected static final float UNPICKED_COLOR_B = 0.7f;
    protected static final float UNPICKED_COLOR_A = 1.0f;
    protected static final float PICKED_COLOR_R = 1.0f;
    protected static final float PICKED_COLOR_G = 0.0f;
    protected static final float PICKED_COLOR_B = 0.0f;
    protected static final float PICKED_COLOR_A = 1.0f;

    protected SXRContext mSXRContext = null;
    protected ColorShader mColorShader = null;
    protected Vector<SXRNode> mObjects = new Vector<SXRNode>();
    protected LinkedBlockingQueue<Runnable> runnableTests = new LinkedBlockingQueue<>();

    protected float OBJECT_ROT = 0.0f;

    public void addRunnableTests(Runnable test) {
        runnableTests.add(test);
    }

    @Override
    public void onStep() {
        Runnable runTest;
        while ((runTest = runnableTests.poll()) != null)
            runTest.run();

        SXRNode objectExt = null;

        for (SXRNode object : mObjects) {
            object.getRenderData().getMaterial().setVec4(ColorShader.COLOR_KEY, UNPICKED_COLOR_R, UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);
        }
        for (SXRPicker.SXRPickedObject hit : SXRPicker.pickObjects(mSXRContext.getMainScene(), 0, 0, 0, 0, 0, -1)) {
            for (SXRNode object : mObjects) {
                if (hit.getHitObject().equals(object)) {
                    object.getRenderData().getMaterial().setVec4(ColorShader.COLOR_KEY, PICKED_COLOR_R, PICKED_COLOR_G, PICKED_COLOR_B, PICKED_COLOR_A);
                    objectExt = object;
                    break;
                }
            }
        }
        OBJECT_ROT += -1.0f;
        for (int i = 0; i < OBJECT_ROT; i++)
            objectExt.getTransform().rotateByAxis(OBJECT_ROT, 0f, 1.0f, 0f);
    }
}


