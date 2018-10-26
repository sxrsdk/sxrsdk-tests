package com.samsungxr.da_samsungxr;

import com.samsungxr.DefaultSXRTestActivity;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRMeshCollider;
import com.samsungxr.SXRMeshEyePointee;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.viewmanager.BaseViewManager;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.BoundsValues;

import com.samsungxr.SXREyePointeeHolder;
import com.samsungxr.SXRPicker;
import com.samsungxr.SXRScene;
import com.samsungxr.viewmanager.ViewerScript;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by rodolfo.ps on 5/19/2015.
 */
public class SXRPickerTest extends ActivityInstrumentationSXRf {

    public SXRPickerTest() {
        super(SXRTestActivity.class);
    }

    public void testPickScene() {

        SXRScene sxrScene = new SXRScene(TestDefaultSXRViewManager.mSXRContext);
        SXRPicker.SXRPickedObject[] hits = SXRPicker.pickObjects(sxrScene, 0, 0, 0, 0, 0, -1);
        assertNotNull(hits);
        assertTrue(hits.length == 0);
        SXRNode sxrNode = new SXRNode(TestDefaultSXRViewManager.mSXRContext);
        SXRCameraRig sxrCameraRigb = SXRCameraRig.makeInstance(TestDefaultSXRViewManager.mSXRContext);
        float rtn = SXRPicker.pickNode(sxrNode, sxrCameraRigb);
        assertNotNull(rtn);
    }

    public void testPickSceneMinFloat() {
        SXRScene sxrScene = new SXRScene(TestDefaultSXRViewManager.mSXRContext);
        SXRPicker.SXRPickedObject[] hits = SXRPicker.pickObjects(sxrScene,
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0),
                BoundsValues.getFloatList().get(0));
        assertNotNull(hits);
        assertTrue(hits.length == 0);
    }

    public void testPickSceneMaxFloat() {
        SXRScene sxrScene = new SXRScene(TestDefaultSXRViewManager.mSXRContext);
        BoundsValues.getFloatList().get(1);
        SXRPicker.SXRPickedObject[] hits = SXRPicker.pickObjects(sxrScene,
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1),
                BoundsValues.getFloatList().get(1));
        assertNotNull(hits);
    }

    public void testFindObjects() {
        assertNotNull(TestDefaultSXRViewManager.mSXRContext);
        SXRScene sxrScene = new SXRScene(TestDefaultSXRViewManager.mSXRContext);
        List<SXRPicker.SXRPickedObject> sxrPO1 = SXRPicker.findObjects(sxrScene);
        assertNotNull(sxrPO1);
        SXRPicker.SXRPickedObject[] sxrPO2 = SXRPicker.pickObjects(sxrScene, 1.0f,1.0f,1.0f,1.0f,1.0f,1.0f);
        assertNotNull(sxrPO2);
        // for this to be true the test must wait until the scene is rendered
        // that is when visible objects are picked
        //assertTrue(sxrPO2.length > 0);
    }

    //Added by Elidelson Carvalho on 10/01/2015

    public void testOthers() {

        SXRContext mSXRContext = TestDefaultSXRViewManager.mSXRContext;
        assertNotNull(mSXRContext);
        SXRScene scene = mSXRContext.getNextMainScene();
        DefaultSXRTestActivity activity = DefaultSXRTestActivity.getInstance();
        SXRMain main = activity.getMain();

        Future<SXRTexture> futureCubemapTexture = mSXRContext.loadFutureCubemapTexture(new SXRAndroidResource(mSXRContext, R.raw.beach));
        SXRMesh sphereMesh = mSXRContext.getAssetLoader().loadMesh(new SXRAndroidResource(mSXRContext, R.raw.sphere));
        SXRMaterial cubemapReflectionMaterial = new SXRMaterial(mSXRContext,SXRMaterial.SXRShaderType.CubemapReflection.ID);
        cubemapReflectionMaterial.setMainTexture(futureCubemapTexture);

        SXRNode sphere = new SXRNode(mSXRContext, sphereMesh);
        sphere.getRenderData().setMaterial(cubemapReflectionMaterial);
        sphere.setName("sphere");
        scene.addNode(sphere);
        sphere.getTransform().setScale(2.0f, 2.0f, 2.0f);
        sphere.getTransform().setPosition(0.0f, 0.0f, -5.0f);
        sphere.attachComponent(new SXRMeshCollider(mSXRContext, false));
        List<SXRPicker.SXRPickedObject> sxrPO1 = SXRPicker.findObjects(scene);

        assertNotNull(sxrPO1);
        // for this to be true the test must wait until the scene is rendered
        // that is when visible objects are picked
        //assertTrue(sxrPO1.size() > 0);
        if (sxrPO1.size() > 0)
        {
            assertNotNull(sxrPO1.get(0).getHitLocation());
            assertNotNull(sxrPO1.get(0).getHitX());
            assertNotNull(sxrPO1.get(0).getHitY());
            assertNotNull(sxrPO1.get(0).getHitZ());
        }
    }

}
