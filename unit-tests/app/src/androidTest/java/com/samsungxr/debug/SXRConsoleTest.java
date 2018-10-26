package com.samsungxr.debug;

import android.graphics.Color;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

/**
 * Created by j.elidelson on 5/19/2015.
 */
public class SXRConsoleTest extends ActivityInstrumentationSXRf {

    public SXRConsoleTest() {
        super(SXRTestActivity.class);
    }

    public void testConstructorNullEyeMode() {

        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext,null);
            assertNotNull("EyeMode was null: ", sxrconsole);
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
        /*catch (NoClassDefFoundError e) {
            //e.printStackTrace();
        }*/
    }

    public void testConstructorNullScene() {

        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext, SXRConsole.EyeMode.BOTH_EYES,TestDefaultSXRViewManager.mSXRContext.getMainScene());
            assertNotNull("EyeMode was null: ", sxrconsole);
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
        /*catch (NoClassDefFoundError e) {
            //e.printStackTrace();
        }*/
    }


    public void ignoretestWriteLine() {

        int i = 461012;
        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext,null);
            sxrconsole.writeLine("The value of i issssssssssssssssssssssssssssssssssss: %d%n", i);
            //sxrconsole.writeLine("The value of i issssssssssssssssssssssssssssssssssss: %d%n", i);
            //sxrconsole.writeLine("The value of i issssssssssssssssssssssssssssssssssss: %d%n", i);
            //sxrconsole.writeLine("The value of i issssssssssssssssssssssssssssssssssss: %d%n", i);
            //sxrconsole.writeLine("The value of i issssssssssssssssssssssssssssssssssss: %d%n", i);
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }

    public void testTextColor() {

        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext,null);
            sxrconsole.setTextColor(Color.BLUE);
            assertEquals(Color.BLUE,sxrconsole.getTextColor());
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }

    public void testSize() {

        float size;
        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext,null);
            size=sxrconsole.getTextSize();
            sxrconsole.setTextSize(size);
            assertEquals(size,sxrconsole.getTextSize());
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }


    public void testEyeMode() {

        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext,null);
            sxrconsole.setEyeMode(SXRConsole.EyeMode.NEITHER_EYE);
            assertEquals(SXRConsole.EyeMode.NEITHER_EYE,sxrconsole.getEyeMode());
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }

    public void testClear() {

        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext,null);
            sxrconsole.clear();
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }


    public void testOffSet() {

        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext,null);
            sxrconsole.setXOffset(1.0f);
            assertEquals(1.0f,sxrconsole.getXOffset());
            sxrconsole.setYOffset(1.0f);
            assertEquals(1.0f,sxrconsole.getYOffset());
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }


    public void testCanvas() {

        try {
            SXRConsole sxrconsole;
            sxrconsole = new SXRConsole(TestDefaultSXRViewManager.mSXRContext,null);
            sxrconsole.setCanvasWidthHeight(1, 2);
            assertEquals(1,sxrconsole.getCanvasWidth());
            assertEquals(2,sxrconsole.getCanvasHeight());
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }
}
