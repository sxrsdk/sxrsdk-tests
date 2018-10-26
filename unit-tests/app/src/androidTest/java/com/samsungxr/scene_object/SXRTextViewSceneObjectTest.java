package com.samsungxr.scene_object;

import android.graphics.Color;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.scene_objects.SXRSphereSceneObject;
import com.samsungxr.scene_objects.SXRTextViewSceneObject;
/*import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.*;
*/

/**
 * Created by j.elidelson on 6/1/2015.
 */
public class SXRTextViewSceneObjectTest extends ActivityInstrumentationSXRf {
    public SXRTextViewSceneObjectTest() {
        super(SXRTestActivity.class);
    }

    //@BeforeClass
    //public static void onceExecutedBeforeAll() {
    //    System.out.println("@BeforeClass: onceExecutedBeforeAll");
    //}

    //*** CONSTRUCTOR TYPE A ***
    public void testConstructorTypeA(){
        SXRTextViewSceneObject textViewSceneObjectA;

        textViewSceneObjectA = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(textViewSceneObjectA);
    }

    public void testConstructorTypeAWithNullContext() {
        SXRTextViewSceneObject textViewSceneObjectA;

        try {
            textViewSceneObjectA = new SXRTextViewSceneObject(null);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }

    /*
    DEPRECATED
    public void testConstructorTypeAWithNullActivity() {
        SXRTextViewSceneObject textViewSceneObjectA;
        try {
            textViewSceneObjectA = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext, null);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }
    */

    //*** CONSTRUCTOR TYPE B ***
    public void testConstructorTypeB(){
        SXRTextViewSceneObject textViewSceneObjectB;
        CharSequence msg = "ABC";

        textViewSceneObjectB = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext,msg);
        assertNotNull(textViewSceneObjectB);
    }

    public void testConstructorTypeBWithNullContext(){
        SXRTextViewSceneObject textViewSceneObjectB;
        CharSequence msg = "ABC";

        try {
            textViewSceneObjectB = new SXRTextViewSceneObject(null, msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }


    //*** CONSTRUCTOR TYPE C ***
    public void testConstructorTypeC(){
        SXRTextViewSceneObject textViewSceneObjectC;
        CharSequence msg = "ABC";

        textViewSceneObjectC = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext, 8.0f,4.0f,msg);
        assertNotNull(textViewSceneObjectC);
    }

    public void testConstructorTypeCWithNullContext(){
        SXRTextViewSceneObject textViewSceneObjectC;
        CharSequence msg = "ABC";

        try {
            textViewSceneObjectC = new SXRTextViewSceneObject(null,8.0f,4.0f,msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }

    /*
    DEPRECATED
    public void testConstructorTypeCWithNullActivity(){
        SXRTextViewSceneObject textViewSceneObjectC;
        CharSequence msg = "ABC";

        try {
            textViewSceneObjectC = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext,
                    null,8.0f,4.0f,msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }
    */


    //*** CONSTRUCTOR TYPE D ***
    public void testConstructorTypeD(){
        SXRTextViewSceneObject textViewSceneObjectD;
        CharSequence msg = "ABC";

        textViewSceneObjectD = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext,8.0f,4.0f,msg);
        assertNotNull(textViewSceneObjectD);
    }

    public void testConstructorTypeDWithNullContext(){
        SXRTextViewSceneObject textViewSceneObjectD;
        CharSequence msg = "ABC";

        try {
            textViewSceneObjectD = new SXRTextViewSceneObject(null,8.0f,4.0f,msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }

    /*
    DEPRECATED
    public void testConstructorTypeDWithNullActivity(){
        SXRTextViewSceneObject textViewSceneObjectD;
        CharSequence msg = "ABC";

        try {
            textViewSceneObjectD = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext,
                    null,8.0f,4.0f,10,10,msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }



    //*** CONSTRUCTOR TYPE E ***
    public void testConstructorTypeE(){
        SXRTextViewSceneObject textViewSceneObjectE;
        CharSequence msg = "ABC";
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext);

        textViewSceneObjectE = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext,
                sphereSceneObject.getRenderData().getMesh(),msg);
        assertNotNull(textViewSceneObjectE);
    }

    public void testConstructorTypeEWithNullContext(){
        SXRTextViewSceneObject textViewSceneObjectE;
        CharSequence msg = "ABC";
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext);

        try {
            textViewSceneObjectE = new SXRTextViewSceneObject(null,
                    sphereSceneObject.getRenderData().getMesh(), msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }


    public void testConstructorTypeEWithNullActivity(){
        SXRTextViewSceneObject textViewSceneObjectE;
        CharSequence msg = "ABC";
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext);

        try {
            textViewSceneObjectE = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext,
                    sphereSceneObject.getRenderData().getMesh(),
                    null,msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }


    //*** CONSTRUCTOR TYPE F ***
    public void testConstructorTypeF(){
        SXRTextViewSceneObject textViewSceneObjectF;
        CharSequence msg = "ABC";
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext);

        textViewSceneObjectF = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext,
                sphereSceneObject.getRenderData().getMesh(),
                TestDefaultSXRViewManager.mSXRContext.getActivity(),10,10,msg);
        assertNotNull(textViewSceneObjectF);
    }

    public void testConstructorTypeFWithNullContext(){
        SXRTextViewSceneObject textViewSceneObjectF;
        CharSequence msg = "ABC";
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext);

        try {
            textViewSceneObjectF = new SXRTextViewSceneObject(null,
                    sphereSceneObject.getRenderData().getMesh(),
                    TestDefaultSXRViewManager.mSXRContext.getActivity(),10,10,msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }

    public void testConstructorTypeFWithNullActivity(){
        SXRTextViewSceneObject textViewSceneObjectF;
        CharSequence msg = "ABC";
        SXRSphereSceneObject sphereSceneObject = new SXRSphereSceneObject(TestDefaultSXRViewManager.mSXRContext);

        try {
            textViewSceneObjectF = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext,
                    sphereSceneObject.getRenderData().getMesh(),
                    null,10,10,msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }
   */

    public void testSetGet(){
        SXRTextViewSceneObject textViewSceneObjectA;

        textViewSceneObjectA = new SXRTextViewSceneObject(TestDefaultSXRViewManager.mSXRContext);
        textViewSceneObjectA.setText("testabc");
        assertEquals("testabc", textViewSceneObjectA.getTextString());
        textViewSceneObjectA.setBackgroundColor(Color.WHITE);
        textViewSceneObjectA.setBackGround(null);
        assertNull(textViewSceneObjectA.getBackGround());
        textViewSceneObjectA.setGravity(textViewSceneObjectA.getGravity() * 1);
        assertEquals(8388659, textViewSceneObjectA.getGravity());
        textViewSceneObjectA.setTextSize(textViewSceneObjectA.getTextSize() * (1.0f));
        assertEquals(224.0f, textViewSceneObjectA.getTextSize());
        textViewSceneObjectA.setRefreshFrequency(SXRTextViewSceneObject.IntervalFrequency.LOW);
        assertEquals(SXRTextViewSceneObject.IntervalFrequency.LOW,textViewSceneObjectA.getRefreshFrequency());
        textViewSceneObjectA.setRefreshFrequency(SXRTextViewSceneObject.IntervalFrequency.MEDIUM);
        assertEquals(SXRTextViewSceneObject.IntervalFrequency.MEDIUM, textViewSceneObjectA.getRefreshFrequency());
        textViewSceneObjectA.setRefreshFrequency(SXRTextViewSceneObject.IntervalFrequency.HIGH);
        assertEquals(SXRTextViewSceneObject.IntervalFrequency.HIGH,textViewSceneObjectA.getRefreshFrequency());

    }

    //NOTE: parameters widht and height accepts negatives o zero values


    }
