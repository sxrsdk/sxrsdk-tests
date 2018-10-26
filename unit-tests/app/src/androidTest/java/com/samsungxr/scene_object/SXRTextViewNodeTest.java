package com.samsungxr.node;

import android.graphics.Color;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.nodes.SXRTextViewNode;
/*import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.*;
*/

/**
 * Created by j.elidelson on 6/1/2015.
 */
public class SXRTextViewNodeTest extends ActivityInstrumentationSXRf {
    public SXRTextViewNodeTest() {
        super(SXRTestActivity.class);
    }

    //@BeforeClass
    //public static void onceExecutedBeforeAll() {
    //    System.out.println("@BeforeClass: onceExecutedBeforeAll");
    //}

    //*** CONSTRUCTOR TYPE A ***
    public void testConstructorTypeA(){
        SXRTextViewNode textViewNodeA;

        textViewNodeA = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(textViewNodeA);
    }

    public void testConstructorTypeAWithNullContext() {
        SXRTextViewNode textViewNodeA;

        try {
            textViewNodeA = new SXRTextViewNode(null);
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
        SXRTextViewNode textViewNodeA;
        try {
            textViewNodeA = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext, null);
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
        SXRTextViewNode textViewNodeB;
        CharSequence msg = "ABC";

        textViewNodeB = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext,msg);
        assertNotNull(textViewNodeB);
    }

    public void testConstructorTypeBWithNullContext(){
        SXRTextViewNode textViewNodeB;
        CharSequence msg = "ABC";

        try {
            textViewNodeB = new SXRTextViewNode(null, msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }


    //*** CONSTRUCTOR TYPE C ***
    public void testConstructorTypeC(){
        SXRTextViewNode textViewNodeC;
        CharSequence msg = "ABC";

        textViewNodeC = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext, 8.0f,4.0f,msg);
        assertNotNull(textViewNodeC);
    }

    public void testConstructorTypeCWithNullContext(){
        SXRTextViewNode textViewNodeC;
        CharSequence msg = "ABC";

        try {
            textViewNodeC = new SXRTextViewNode(null,8.0f,4.0f,msg);
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
        SXRTextViewNode textViewNodeC;
        CharSequence msg = "ABC";

        try {
            textViewNodeC = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext,
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
        SXRTextViewNode textViewNodeD;
        CharSequence msg = "ABC";

        textViewNodeD = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext,8.0f,4.0f,msg);
        assertNotNull(textViewNodeD);
    }

    public void testConstructorTypeDWithNullContext(){
        SXRTextViewNode textViewNodeD;
        CharSequence msg = "ABC";

        try {
            textViewNodeD = new SXRTextViewNode(null,8.0f,4.0f,msg);
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
        SXRTextViewNode textViewNodeD;
        CharSequence msg = "ABC";

        try {
            textViewNodeD = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext,
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
        SXRTextViewNode textViewNodeE;
        CharSequence msg = "ABC";
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext);

        textViewNodeE = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext,
                sphereNode.getRenderData().getMesh(),msg);
        assertNotNull(textViewNodeE);
    }

    public void testConstructorTypeEWithNullContext(){
        SXRTextViewNode textViewNodeE;
        CharSequence msg = "ABC";
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext);

        try {
            textViewNodeE = new SXRTextViewNode(null,
                    sphereNode.getRenderData().getMesh(), msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }


    public void testConstructorTypeEWithNullActivity(){
        SXRTextViewNode textViewNodeE;
        CharSequence msg = "ABC";
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext);

        try {
            textViewNodeE = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext,
                    sphereNode.getRenderData().getMesh(),
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
        SXRTextViewNode textViewNodeF;
        CharSequence msg = "ABC";
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext);

        textViewNodeF = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext,
                sphereNode.getRenderData().getMesh(),
                TestDefaultSXRViewManager.mSXRContext.getActivity(),10,10,msg);
        assertNotNull(textViewNodeF);
    }

    public void testConstructorTypeFWithNullContext(){
        SXRTextViewNode textViewNodeF;
        CharSequence msg = "ABC";
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext);

        try {
            textViewNodeF = new SXRTextViewNode(null,
                    sphereNode.getRenderData().getMesh(),
                    TestDefaultSXRViewManager.mSXRContext.getActivity(),10,10,msg);
            fail("Should have raised an NullPointerException or IllegalArgumentException");
        }
        catch (NullPointerException e){
        }
        catch (IllegalArgumentException e){
        }
    }

    public void testConstructorTypeFWithNullActivity(){
        SXRTextViewNode textViewNodeF;
        CharSequence msg = "ABC";
        SXRSphereNode sphereNode = new SXRSphereNode(TestDefaultSXRViewManager.mSXRContext);

        try {
            textViewNodeF = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext,
                    sphereNode.getRenderData().getMesh(),
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
        SXRTextViewNode textViewNodeA;

        textViewNodeA = new SXRTextViewNode(TestDefaultSXRViewManager.mSXRContext);
        textViewNodeA.setText("testabc");
        assertEquals("testabc", textViewNodeA.getTextString());
        textViewNodeA.setBackgroundColor(Color.WHITE);
        textViewNodeA.setBackGround(null);
        assertNull(textViewNodeA.getBackGround());
        textViewNodeA.setGravity(textViewNodeA.getGravity() * 1);
        assertEquals(8388659, textViewNodeA.getGravity());
        textViewNodeA.setTextSize(textViewNodeA.getTextSize() * (1.0f));
        assertEquals(224.0f, textViewNodeA.getTextSize());
        textViewNodeA.setRefreshFrequency(SXRTextViewNode.IntervalFrequency.LOW);
        assertEquals(SXRTextViewNode.IntervalFrequency.LOW,textViewNodeA.getRefreshFrequency());
        textViewNodeA.setRefreshFrequency(SXRTextViewNode.IntervalFrequency.MEDIUM);
        assertEquals(SXRTextViewNode.IntervalFrequency.MEDIUM, textViewNodeA.getRefreshFrequency());
        textViewNodeA.setRefreshFrequency(SXRTextViewNode.IntervalFrequency.HIGH);
        assertEquals(SXRTextViewNode.IntervalFrequency.HIGH,textViewNodeA.getRefreshFrequency());

    }

    //NOTE: parameters widht and height accepts negatives o zero values


    }
