package com.samsungxr.tester;


import android.graphics.Color;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRComponent;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRExternalScene;
import com.samsungxr.SXRImportSettings;
import com.samsungxr.SXRLight;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRMeshMorph;
import com.samsungxr.SXRPointLight;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRResourceVolume;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRShaderId;
import com.samsungxr.SXRSpotLight;
import com.samsungxr.SXRTransform;
import com.samsungxr.SXRVertexBuffer;
import com.samsungxr.animation.SXRAnimator;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRModelNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class AssetAnimTests
{
    private static final String TAG = AssetAnimTests.class.getSimpleName();
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRNode mRoot;
    private boolean mDoCompare = true;
    private AssetEventHandler mHandler;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @After
    public void tearDown()
    {
        SXRScene scene = mTestUtils.getMainScene();
        if (scene != null)
        {
            scene.clear();
        }
    }

    @Before
    public void setUp() throws TimeoutException
    {
        SXRTestableActivity activity = ActivityRule.getActivity();
        mTestUtils = new SXRTestUtils(activity);
        mTestUtils.waitForOnInit();
        mWaiter = new Waiter();

        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();

        mWaiter.assertNotNull(scene);
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
        mHandler = new AssetEventHandler(scene, mWaiter, mTestUtils, getClass().getSimpleName());
    }

    public void centerModel(SXRNode model, SXRTransform camTrans)
    {
        SXRNode.BoundingVolume bv = model.getBoundingVolume();
        float x = camTrans.getPositionX();
        float y = camTrans.getPositionY();
        float z = camTrans.getPositionZ();
        float sf = 1 / bv.radius;
        model.getTransform().setScale(sf, sf, sf);
        bv = model.getBoundingVolume();
        model.getTransform().setPosition(x - bv.center.x, y - bv.center.y, z - bv.center.z - 1.5f * bv.radius);
    }

    @Test
    public void jassimpMorphTest() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode lightObj = new SXRNode(ctx);
        SXRPointLight pointLight = new SXRPointLight(ctx);
        SXRCameraRig rig = scene.getMainCameraRig();
        SXRNode model = null;

        rig.getCenterCamera().setBackgroundColor(Color.LTGRAY);
        rig.getLeftCamera().setBackgroundColor(Color.LTGRAY);
        rig.getRightCamera().setBackgroundColor(Color.LTGRAY);
        pointLight.setDiffuseIntensity(0.8f, 0.8f, 08f, 1.0f);
        pointLight.setSpecularIntensity(0.8f, 0.8f, 08f, 1.0f);
        lightObj.attachComponent(pointLight);
        lightObj.getTransform().setPosition(-1.0f, 1.0f, 0);
        scene.addNode(lightObj);

        try
        {
            EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedMorphSettings();
            model = ctx.getAssetLoader().loadModel("jassimp/faceBlendShapes_center.fbx", settings, true, null);
            centerModel(model, rig.getTransform());
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }

        String[] shapeNames = { "Jason_Shapes_Ref:JasnNeutral:Default", "Jaw_Open", "Smile" };
        float[] weights = new float[] { 1, 0 };
        SXRMeshMorph morph = addMorph(model, shapeNames);
        morph.setWeights(weights);

        scene.addNode(model);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "jassimpMorphTest", mWaiter, mDoCompare);
    }

    private SXRMeshMorph addMorph(SXRNode model, String shapeNames[])
    {
        SXRNode baseShape = model.getNodeByName(shapeNames[0]);
        SXRMeshMorph morph = new SXRMeshMorph(model.getSXRContext(), 2, false);

        baseShape.attachComponent(morph);
        for (int i = 1; i < shapeNames.length; ++i)
        {
            SXRNode blendShape = model.getNodeByName(shapeNames[i]);
            blendShape.getParent().removeChildObject(blendShape);
            morph.setBlendShape(i - 1, blendShape);
        }
        morph.update();
        return morph;
    }
}
