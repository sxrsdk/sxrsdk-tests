package com.samsungxr.scene_object;

import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.scene_objects.SXRConeSceneObject;

/**
 * Created by j.elidelson on 5/29/2015.
 */
public class SXRConeSceneObjectTest extends ActivityInstrumentationSXRf {

    public SXRConeSceneObjectTest() {
        super(SXRTestActivity.class);
    }

    //************
    //*** Cone ***
    //************
    public void testConeConstructor() {

        SXRConeSceneObject coneSceneObject = new SXRConeSceneObject(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(coneSceneObject);
    }

    public void ignoretestConeNullConstructor() {

        try {
            SXRConeSceneObject coneSceneObject2 = new SXRConeSceneObject(null);
            assertNull(coneSceneObject2);
        } catch (NullPointerException e) {
            assertEquals(null, null);
        }
    }

    public void testConeConstructorFacingout() {

        SXRConeSceneObject coneSceneObjectTrue = new SXRConeSceneObject(TestDefaultSXRViewManager.mSXRContext, true);
        assertNotNull(coneSceneObjectTrue);
        SXRConeSceneObject coneSceneObjectFalse = new SXRConeSceneObject(TestDefaultSXRViewManager.mSXRContext, false);
        assertNotNull(coneSceneObjectFalse);
    }

    public void testConeConstructorFacingoutMaterial() {

        SXRConeSceneObject coneSceneObject = new SXRConeSceneObject(TestDefaultSXRViewManager.mSXRContext);
        SXRConeSceneObject coneSceneObjectTrue = new SXRConeSceneObject(TestDefaultSXRViewManager.mSXRContext, true, coneSceneObject.getRenderData().getMaterial());
        assertNotNull(coneSceneObjectTrue);
        SXRConeSceneObject coneSceneObjectFalse = new SXRConeSceneObject(TestDefaultSXRViewManager.mSXRContext, false, coneSceneObject.getRenderData().getMaterial());
        assertNotNull(coneSceneObjectFalse);
    }

    public void testIscollinding() {

        SXRConeSceneObject coneSceneObject1 = new SXRConeSceneObject(TestDefaultSXRViewManager.mSXRContext);
        coneSceneObject1.getTransform().setPosition(0.0f,0.0f,-5.0f);
        SXRConeSceneObject coneSceneObject2 = new SXRConeSceneObject(TestDefaultSXRViewManager.mSXRContext);
        coneSceneObject2.getTransform().setPosition(0.0f,0.0f,-5.0f);
        assertEquals(true,coneSceneObject1.isColliding(coneSceneObject2));
    }

}
