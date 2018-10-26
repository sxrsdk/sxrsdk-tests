package com.samsungxr.utility;

import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.utility.RecycleBin;

/**
 * Created by j.elidelson on 6/8/2015.
 */
public class RecycleBinTest extends ActivityInstrumentationSXRf {

    public RecycleBinTest() {
        super(SXRTestActivity.class);
    }

    private Integer ax[] = {1, 2, 3, 4, 5};
    private Integer bx[] = null;
    private RecycleBin <Integer[]> recycleBinInteger;

    public void testAssertCreateHardBin(){
        recycleBinInteger = RecycleBin.hard();
        recycleBinInteger.put(ax);
        bx = recycleBinInteger.get();
        recycleBinInteger.synchronize();
        assertEquals(ax, bx);
    }

    public void testAssertSoftBinCreate(){
        recycleBinInteger = RecycleBin.soft();
        recycleBinInteger.put(ax);
        bx = recycleBinInteger.get();
        recycleBinInteger.synchronize();
        assertEquals(ax,bx);
    }

    public void testAssertWeakBinCreate(){
        recycleBinInteger = RecycleBin.weak();
        recycleBinInteger.put(ax);
        bx = recycleBinInteger.get();
        recycleBinInteger.synchronize();
        assertEquals(ax, bx);

        recycleBinInteger.put(bx);
        bx = recycleBinInteger.get();
    }


}
