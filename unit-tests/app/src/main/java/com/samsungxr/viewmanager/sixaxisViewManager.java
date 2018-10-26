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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRBitmapTexture;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRScript;
import com.samsungxr.tests.R;

public class sixaxisViewManager extends SXRScript {

    private enum State {
        Idle, Ready, Rotating, Pass, Fail
    };

    private State mState = State.Idle;
    private SXRContext mSXRContext = null;
    private SXRSceneObject mDegreeBoard = null;
    private SXRSceneObject mAngularVelocityBoard = null;
    private SXRSceneObject mValueBoard = null;
    private SXRSceneObject mStateBoard = null;

    private double mPreviousDegree = 0.0f;

    private float mAValue = 3.0f;
    private float mBValue = 10.0f;

    private double mFinalDegree = 0.0f;

    private Gyroscope mGyroscope = null;

    public sixaxisViewManager() {
    }

    @Override
    public void onInit(SXRContext sxrContext) {

        mSXRContext = sxrContext;
        mGyroscope = new Gyroscope(mSXRContext.getContext());

        SXRScene mainScene = mSXRContext.getNextMainScene();

        mainScene.getMainCameraRig().setCameraRigType(
                SXRCameraRig.SXRCameraRigType.YawOnly.ID);

        SXRMesh cylinderMesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(
                mSXRContext, R.raw.cylinder_obj));
        Bitmap cylinderBitmap = BitmapFactory.decodeResource(mSXRContext
                .getContext().getResources(), R.drawable.cylinder2);
        SXRSceneObject cylinder = new SXRSceneObject(mSXRContext, cylinderMesh,
                new SXRBitmapTexture(mSXRContext, cylinderBitmap));

        mainScene.addSceneObject(cylinder);

        Bitmap cursorBitmap = BitmapFactory.decodeResource(mSXRContext
                .getContext().getResources(), R.drawable.cursor);

        SXRSceneObject cursor = new SXRSceneObject(mSXRContext, 0.05f, 0.5f,
                new SXRBitmapTexture(mSXRContext, cursorBitmap));
        cursor.getTransform().setPosition(0.0f, 0.0f, -5.0f);
        mainScene.getMainCameraRig().addChildObject(cursor);

        Bitmap degreeBitmap = SXRTextBitmapFactory2.create(1024, 128,
                "degree : 0.00", 40, Align.LEFT, Color.YELLOW,
                Color.TRANSPARENT);
        mDegreeBoard = new SXRSceneObject(mSXRContext, 2.0f, 0.5f,
                new SXRBitmapTexture(mSXRContext, degreeBitmap));
        mDegreeBoard.getTransform().setPosition(-0.5f, 0.7f, -2.0f);
        degreeBitmap.recycle();
        mainScene.getMainCameraRig().addChildObject(mDegreeBoard);

        Bitmap angularVelocityBitmap = SXRTextBitmapFactory2.create(1024, 128,
                "velocity : 0.00", 50, Align.LEFT, Color.YELLOW,
                Color.TRANSPARENT);
        mAngularVelocityBoard = new SXRSceneObject(mSXRContext, 2.0f, 0.5f,
                new SXRBitmapTexture(mSXRContext, angularVelocityBitmap));
        mAngularVelocityBoard.getTransform().setPosition(-0.5f, -0.7f, -2.0f);
        angularVelocityBitmap.recycle();
        mainScene.getMainCameraRig().addChildObject(mAngularVelocityBoard);

        Bitmap aValueBitmap = SXRTextBitmapFactory2.create(1024, 128, String
                .format("ZRO : %.2f, Spec degree : %.2f", mAValue, mBValue),
                30, Align.LEFT, Color.YELLOW, Color.TRANSPARENT);
        mValueBoard = new SXRSceneObject(mSXRContext, 2.0f, 0.5f,
                new SXRBitmapTexture(mSXRContext, aValueBitmap));
        mValueBoard.getTransform().setPosition(-0.5f, 0.5f, -2.0f);
        aValueBitmap.recycle();
        mainScene.getMainCameraRig().addChildObject(mValueBoard);

        Bitmap stateBitmap = SXRTextBitmapFactory2.create(1024, 128, "", 50,
                Align.LEFT, Color.TRANSPARENT, Color.TRANSPARENT);
        mStateBoard = new SXRSceneObject(mSXRContext, 2.5f, 0.625f,
                new SXRBitmapTexture(mSXRContext, stateBitmap));
        mStateBoard.getTransform().setPosition(-0.5f, -0.7f, -5.0f);
        stateBitmap.recycle();
        mainScene.getMainCameraRig().addChildObject(mStateBoard);

    }

    @Override
    public void onStep() {
        float[] lookAt = mSXRContext.getMainScene().getMainCameraRig()
                .getLookAt();
        double degree = Math.atan2(lookAt[0], -lookAt[2]) * 180.0 / Math.PI;
        if (degree < 0.0) {
            degree += 360.0;
        }

        double deltaDegree = degree - mPreviousDegree;
        mPreviousDegree = degree;

        if (deltaDegree > 180.0f) {
            deltaDegree -= 360.0f;
        } else if (deltaDegree < -180.0f) {
            deltaDegree += 360.0f;
        }

        float angularVelocity = mGyroscope.getMagnitude();

        Log.v("", String.format("degree : %f", degree));
        Log.v("", String.format("angularVelocity : %f", angularVelocity));

        if (mState == State.Idle) {
            if (angularVelocity < mAValue && angularVelocity > -mAValue) {
                mState = State.Ready;
            }
        } else if (mState == State.Ready) {
            if (angularVelocity > mAValue || angularVelocity < -mAValue) {
                mState = State.Rotating;
            }
        } else if (mState == State.Rotating) {
            if (angularVelocity < mAValue || angularVelocity > -mAValue) {
                if ((degree >= 0.0f && degree < mBValue)
                        || (degree > 360.0f - mBValue && degree < 360.0f)) {
                    mState = State.Pass;
                } else {
                    mState = State.Fail;
                }
                mFinalDegree = degree;
            }
        }

        Bitmap degreeBitmap = SXRTextBitmapFactory2.create(1024, 128,
                String.format("degree : %.2f", degree), 50, Align.LEFT,
                Color.YELLOW, Color.TRANSPARENT);
        SXRMaterial degreeMaterial = mDegreeBoard.getRenderData().getMaterial();
        degreeMaterial.setMainTexture(new SXRBitmapTexture(mSXRContext,
                degreeBitmap));
        degreeBitmap.recycle();

        Bitmap angularVelocityBitmap = SXRTextBitmapFactory2.create(1024, 128,
                String.format("velocity : %.2f", angularVelocity), 50,
                Align.LEFT, Color.YELLOW, Color.TRANSPARENT);
        SXRMaterial angularVelocityMaterial = mAngularVelocityBoard
                .getRenderData().getMaterial();
        angularVelocityMaterial.setMainTexture(new SXRBitmapTexture(
                mSXRContext, angularVelocityBitmap));
        angularVelocityBitmap.recycle();

        Bitmap aValueBitmap = SXRTextBitmapFactory2.create(1024, 128, String
                .format("ZRO : %.2f, Spec degree : %.2f", mAValue, mBValue),
                30, Align.LEFT, Color.YELLOW, Color.TRANSPARENT);
        SXRMaterial aValueMaterial = mValueBoard.getRenderData().getMaterial();
        aValueMaterial.setMainTexture(new SXRBitmapTexture(mSXRContext,
                aValueBitmap));
        aValueBitmap.recycle();

        Bitmap stateBitmap = null;

        switch (mState) {
        case Idle:
            stateBitmap = SXRTextBitmapFactory2.create(1024, 128, "", 50,
                    Align.LEFT, Color.TRANSPARENT, Color.TRANSPARENT);
            break;
        case Ready:
            stateBitmap = SXRTextBitmapFactory2.create(1024, 128, "Ready", 50,
                    Align.LEFT, Color.BLACK, Color.WHITE);
            break;
        case Rotating:
            stateBitmap = SXRTextBitmapFactory2.create(1024, 128, "", 50,
                    Align.LEFT, Color.TRANSPARENT, Color.TRANSPARENT);
            break;
        case Pass:
            stateBitmap = SXRTextBitmapFactory2.create(1024, 128,
                    String.format("PASS degree : %.2f", mFinalDegree), 50,
                    Align.LEFT, Color.BLACK, Color.GREEN);
            break;
        case Fail:
            stateBitmap = SXRTextBitmapFactory2.create(1024, 128,
                    String.format("FAIL degree : %.2f", mFinalDegree), 50,
                    Align.LEFT, Color.BLACK, Color.RED);
            break;
        }

        SXRMaterial stateMaterial = mStateBoard.getRenderData().getMaterial();
        stateMaterial.setMainTexture(new SXRBitmapTexture(mSXRContext,
                stateBitmap));
        stateBitmap.recycle();
    }

    public void onDoubleTap() {
        mState = State.Idle;
    }

    public void addAValue(float a) {
        mAValue += a;
        if (mAValue < 0.0f) {
            mAValue = 0.0f;
        }
    }

    public void addBValue(float b) {
        mBValue += b;
        if (mBValue < 0.0f) {
            mBValue = 0.0f;
        }
    }

}
