package com.samsungxr.za_samsungxr;

import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import java.lang.IllegalArgumentException;
import com.samsungxr.SXRFloatTexture;

/**
 * Created by j.elidelson on 5/20/2015.
 */
public class SXRFloatTextureTest extends ActivityInstrumentationSXRf {

    private float pixels[] = {
            0.0f, 0.0f, 0.0f,   1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,   0.0f, 0.0f, 0.0f
    };

    private float pixels2[] = {
            1.0f, 1.0f, 1.0f,   0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,   1.0f, 1.0f, 1.0f
    };

    //*************************
    //*** Constructor tests ***
    //*************************
    public void testFloatTextureConstructorWidthZero() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,0,1,pixels);
            assertNull("Width was 0: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureConstructorWidthNegative() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,-1,1,pixels);
            assertNull("Width was negative: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureConstructorHeightZero() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,1,0,pixels);
            assertNull("Height was negative: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureConstructorHeightNegative() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,1,0,pixels);
            assertNull("Height was 0: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureConstructorDataNull() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,2,2,null);
            assertNull("Data was null: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureConstructorDataLength() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,10,10,pixels);
            if(sxrFloatTexture==null) {
                assertNull("data.length < height * width * 2: ", sxrFloatTexture);
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }


    //*************************
    //*** Method Update     ***
    //*************************

    public void testFloatTextureUpdateWidthZero() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,1,1,pixels);
            sxrFloatTexture.update(0,1,pixels2);
            assertNull("update method with Widht zero: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureUpdateWidthNegative() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,1,1,pixels);
            sxrFloatTexture.update(-1,1,pixels2);
            assertNull("update method with Widht negative: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureUpdateHeightZero() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,1,1,pixels);
            sxrFloatTexture.update(1,0,pixels2);
            assertNull("update method with Height zero: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureUpdateHeightNegative() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,1,1,pixels);
            sxrFloatTexture.update(1,-1,pixels2);
            assertNull("update method with Height negative: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureUpdateDataNull() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,1,1,pixels);
            sxrFloatTexture.update(1,1,null);
            assertNull("update method with Widht zero: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void testFloatTextureUpdateDataLength() {
        try {
            SXRFloatTexture sxrFloatTexture;
            sxrFloatTexture = new SXRFloatTexture(TestDefaultSXRViewManager.mSXRContext,1,1,pixels);
            sxrFloatTexture.update(10,10,pixels2);
            assertNull("update method with Widht zero: ", sxrFloatTexture);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
