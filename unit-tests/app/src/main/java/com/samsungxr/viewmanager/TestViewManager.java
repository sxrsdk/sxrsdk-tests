package com.samsungxr.viewmanager;

import com.samsungxr.SXRSceneObject;
import com.samsungxr.misc.Gyroscope;


public class TestViewManager extends TestDefaultSXRViewManager {
	
    private enum State {
        Idle, Ready, Rotating, Pass, Fail
    };

    private State mState = State.Idle;
    private SXRSceneObject mDegreeBoard = null;
    private SXRSceneObject mAngularVelocityBoard = null;
    private SXRSceneObject mValueBoard = null;
    private SXRSceneObject mStateBoard = null;

    private double mPreviousDegree = 0.0f;
    private long mTimeStamp = 0l;

    private float mAValue = 3.0f;
    private float mBValue = 10.0f;

    private double mFinalDegree = 0.0f;

    private Gyroscope mGyroscope = null;

    @Override
    public void onStep() {

}
}
