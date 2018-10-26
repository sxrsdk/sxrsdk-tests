package com.samsungxr.node;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.nodes.SXRConeNode;

/**
 * Created by j.elidelson on 5/29/2015.
 */
public class SXRConeNodeTest extends ActivityInstrumentationSXRf {

    public SXRConeNodeTest() {
        super(SXRTestActivity.class);
    }

    //************
    //*** Cone ***
    //************
    public void testConeConstructor() {

        SXRConeNode coneNode = new SXRConeNode(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(coneNode);
    }

    public void ignoretestConeNullConstructor() {

        try {
            SXRConeNode coneNode2 = new SXRConeNode(null);
            assertNull(coneNode2);
        } catch (NullPointerException e) {
            assertEquals(null, null);
        }
    }

    public void testConeConstructorFacingout() {

        SXRConeNode coneNodeTrue = new SXRConeNode(TestDefaultSXRViewManager.mSXRContext, true);
        assertNotNull(coneNodeTrue);
        SXRConeNode coneNodeFalse = new SXRConeNode(TestDefaultSXRViewManager.mSXRContext, false);
        assertNotNull(coneNodeFalse);
    }

    public void testConeConstructorFacingoutMaterial() {

        SXRConeNode coneNode = new SXRConeNode(TestDefaultSXRViewManager.mSXRContext);
        SXRConeNode coneNodeTrue = new SXRConeNode(TestDefaultSXRViewManager.mSXRContext, true, coneNode.getRenderData().getMaterial());
        assertNotNull(coneNodeTrue);
        SXRConeNode coneNodeFalse = new SXRConeNode(TestDefaultSXRViewManager.mSXRContext, false, coneNode.getRenderData().getMaterial());
        assertNotNull(coneNodeFalse);
    }

    public void testIscollinding() {

        SXRConeNode coneNode1 = new SXRConeNode(TestDefaultSXRViewManager.mSXRContext);
        coneNode1.getTransform().setPosition(0.0f,0.0f,-5.0f);
        SXRConeNode coneNode2 = new SXRConeNode(TestDefaultSXRViewManager.mSXRContext);
        coneNode2.getTransform().setPosition(0.0f,0.0f,-5.0f);
        assertEquals(true,coneNode1.isColliding(coneNode2));
    }

}
