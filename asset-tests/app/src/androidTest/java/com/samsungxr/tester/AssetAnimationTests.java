package com.samsungxr.tester;


import android.graphics.Color;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRComponent;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRImportSettings;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRMeshMorph;
import com.samsungxr.SXRPointLight;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTransform;
import com.samsungxr.SXRVertexBuffer;
import com.samsungxr.animation.SXRAnimation;
import com.samsungxr.animation.SXRAnimator;
import com.samsungxr.animation.SXRPose;
import com.samsungxr.animation.SXRRepeatMode;
import com.samsungxr.animation.SXRSkeleton;
import com.samsungxr.animation.keyframe.SXRAnimationBehavior;
import com.samsungxr.animation.keyframe.SXRAnimationChannel;
import com.samsungxr.animation.keyframe.SXRSkeletonAnimation;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
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
public class AssetAnimationTests
{
    private static final String TAG = AssetAnimationTests.class.getSimpleName();
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private SXRNode mRoot;
    private SXRNode mBackground;
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
        mBackground = new SXRCubeNode(ctx, false, new SXRMaterial(ctx, SXRMaterial.SXRShaderType.Phong.ID));
        mBackground.getTransform().setScale(10, 10, 10);
        mBackground.setName("background");
        mRoot = scene.getRoot();
        mWaiter.assertNotNull(mRoot);
        mHandler = new AssetEventHandler(scene, mWaiter, mTestUtils, getClass().getSimpleName());
    }

    @Test
    public void canStartAnimations() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.START_ANIMATIONS));
        SXRNode model = null;

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            model = ctx.getAssetLoader().loadModel("jassimp/astro_boy.dae", settings, false, scene);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.centerModel(model, scene.getMainCameraRig().getTransform());
        mHandler.checkAssetLoaded(null, 4);
        mHandler.checkAssetErrors(0, 0);
        mWaiter.assertNotNull(scene.getNodeByName("astro_boy.dae"));
        SXRAnimator animator = (SXRAnimator) model.getComponent(SXRAnimator.getComponentType());
        animator.setRepeatMode(SXRRepeatMode.REPEATED);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "canStartAnimations", mWaiter, mDoCompare);
    }

    class MeshVisitorNoAnim implements SXRNode.ComponentVisitor
    {
        public boolean visit(SXRComponent comp)
        {
            SXRRenderData rdata = (SXRRenderData) comp;
            SXRMesh mesh = rdata.getMesh();
            if (mesh != null)
            {
                SXRVertexBuffer vbuf = mesh.getVertexBuffer();
                mWaiter.assertNotNull(vbuf);
                mWaiter.assertTrue(vbuf.hasAttribute("a_position"));
                mWaiter.assertTrue(vbuf.hasAttribute("a_normal"));
            }
            return true;
        }
    }

    @Test
    public void canLoadModelWithoutAnimation() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode model = null;

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.NO_ANIMATION));
            model = ctx.getAssetLoader().loadModel("jassimp/astro_boy.dae", settings, true, (SXRScene) null);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mTestUtils.waitForXFrames(5);
        mHandler.checkAssetLoaded(null, 4);
        mHandler.checkAssetErrors(0, 0);
        mWaiter.assertNull(model.getComponent(SXRAnimator.getComponentType()));
        model.forAllComponents(new MeshVisitorNoAnim(), SXRRenderData.getComponentType());
    }

    @Test
    public void canLoadX3DModelWithoutAnimation() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRNode model = null;

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(SXRImportSettings.NO_ANIMATION));
            model = ctx.getAssetLoader().loadModel(SXRTestUtils.GITHUB_URL + "x3d/animation/animation04.x3d", settings, true, (SXRScene) null);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mTestUtils.waitForXFrames(5);
        mHandler.checkAssetLoaded(null, 4);
        mHandler.checkAssetErrors(0, 0);
        mWaiter.assertNull(model.getComponent(SXRAnimator.getComponentType()));
        model.forAllComponents(new MeshVisitorNoAnim(), SXRRenderData.getComponentType());
    }

    @Test
    public void testSkeleton() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;
        SXRCameraRig rig = scene.getMainCameraRig();

        rig.getLeftCamera().setBackgroundColor(Color.LTGRAY);
        rig.getRightCamera().setBackgroundColor(Color.LTGRAY);
        rig.getTransform().rotateByAxis(0, 1, 0, 90);

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            model = ctx.getAssetLoader().loadModel("jassimp/astro_boy.dae", scene);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.centerModel(model, scene.getMainCameraRig().getTransform());
        mWaiter.assertNotNull(scene.getNodeByName("astro_boy.dae"));
        SXRAnimator animator = (SXRAnimator) model.getComponent(SXRAnimator.getComponentType());
        SXRSkeleton skel = null;
        Quaternionf q = new Quaternionf();

        for (int i = 0; i < animator.getAnimationCount(); ++i)
        {
            SXRAnimation anim = animator.getAnimation(i);

            if (anim instanceof SXRSkeletonAnimation)
            {
                SXRSkeletonAnimation skelAnim = (SXRSkeletonAnimation) anim;
                skel = skelAnim.getSkeleton();
                break;
            }
        }
        mWaiter.assertNotNull(skel);
        int rightShoulder = skel.getBoneIndex("astroBoy_newSkeleton_R_shoulder");
        int leftShoulder = skel.getBoneIndex("astroBoy_newSkeleton_L_shoulder");
        SXRPose pose = new SXRPose(skel);

        mWaiter.assertTrue(rightShoulder >= 0);
        mWaiter.assertTrue(leftShoulder >= 0);
        q.fromAxisAngleDeg(1, 0, 0, -45);
        pose.setLocalRotation(leftShoulder, q.x, q.y, q.z, q.w);
        pose.setLocalRotation(rightShoulder, q.x, q.y, q.z, q.w);
        skel.applyPose(pose, SXRSkeleton.BIND_POSE_RELATIVE);
        skel.poseToBones();
        skel.updateSkinPose();

        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "testSkeleton", mWaiter, mDoCompare);
    }

    @Test
    public void testSkeleton2() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;
        SXRCameraRig rig = scene.getMainCameraRig();

        rig.getLeftCamera().setBackgroundColor(Color.LTGRAY);
        rig.getRightCamera().setBackgroundColor(Color.LTGRAY);
        rig.getTransform().rotateByAxis(0, 1, 0, 90);

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            model = ctx.getAssetLoader().loadModel("jassimp/DeepMotionSkeleton.fbx", scene);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.centerModel(model, rig.getTransform());
        mWaiter.assertNotNull(scene.getNodeByName("DeepMotionSkeleton.fbx"));

        List<SXRComponent> components = model.getAllComponents(SXRSkeleton.getComponentType());
        Quaternionf q1 = new Quaternionf();
        Quaternionf q2 = new Quaternionf();
        SXRSkeleton skel = null;

        for (SXRComponent c : components)
        {
            if (c instanceof SXRSkeleton)
            {
                skel = (SXRSkeleton) c;
                break;
            }
        }
        mWaiter.assertNotNull(skel);

        int rightShoulder = skel.getBoneIndex("ShoulderRight");
        int leftShoulder = skel.getBoneIndex("ShoulderLeft");
        int rightElbow = skel.getBoneIndex("ElbowRight");
        int leftElbow = skel.getBoneIndex("ElbowLeft");
        int leftWrist = skel.getBoneIndex("WristLeft");
        SXRPose bindpose = skel.getBindPose();
        SXRPose pose = skel.getPose();

        mWaiter.assertTrue(rightShoulder >= 0);
        mWaiter.assertTrue(leftShoulder >= 0);
        mWaiter.assertTrue(rightElbow >= 0);
        mWaiter.assertTrue(leftElbow >= 0);
        q1.fromAxisAngleDeg(0, 0, 1, -45);
        bindpose.getLocalRotation(leftShoulder, q2);
        q2.mul(q1);
        pose.setLocalRotation(leftShoulder, q2.x, q2.y, q2.z, q2.w);
        bindpose.getLocalRotation(leftElbow, q2);
        q2.mul(q1);
        pose.setLocalRotation(leftElbow, q2.x, q2.y, q2.z, q2.w);
        bindpose.getLocalRotation(leftWrist, q2);
        q2.mul(q1);
        pose.setLocalRotation(leftWrist, q2.x, q2.y, q2.z, q2.w);

        bindpose.getLocalRotation(rightShoulder, q2);
        q1.invert();
        q2.mul(q1);
        pose.setLocalRotation(rightShoulder, q2.x, q2.y, q2.z, q2.w);
        bindpose.getLocalRotation(rightElbow, q2);
        q2.mul(q1);
        pose.setLocalRotation(rightElbow, q2.x, q2.y, q2.z, q2.w);
        skel.poseToBones();
        skel.updateSkinPose();

        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "testSkeleton2", mWaiter, mDoCompare);
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
        SXRMeshMorph morph = new SXRMeshMorph(model.getSXRContext(), 2);

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

    @Test
    public void testSkeletonAnimation() throws TimeoutException
    {
        SXRContext ctx  = mTestUtils.getSxrContext();
        SXRScene scene = mTestUtils.getMainScene();
        SXRNode model = null;
        SXRCameraRig rig = scene.getMainCameraRig();

        rig.getLeftCamera().setBackgroundColor(Color.LTGRAY);
        rig.getRightCamera().setBackgroundColor(Color.LTGRAY);
        rig.getTransform().rotateByAxis(0, 1, 0, 90);

        ctx.getEventReceiver().addListener(mHandler);
        try
        {
            model = ctx.getAssetLoader().loadModel(SXRTestUtils.GITHUB_URL + "jassimp/Andromeda/Andromeda.dae", scene);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        mTestUtils.waitForAssetLoad();
        mHandler.centerModel(model, scene.getMainCameraRig().getTransform());
        mWaiter.assertNotNull(scene.getNodeByName("Andromeda.dae"));
        List<SXRComponent> components = model.getAllComponents(SXRSkeleton.getComponentType());

        mWaiter.assertTrue(components.size() > 0);
        SXRSkeleton skel = (SXRSkeleton) components.get(0);
        mWaiter.assertNotNull(skel);

        SXRSkeletonAnimation skelAnim = makeSkeletonAnimation(skel);

        skelAnim.animate(0);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "testSkeletonAnimation0", mWaiter, mDoCompare);
        skelAnim.animate(1);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "testSkeletonAnimation1", mWaiter, mDoCompare);
        skelAnim.animate(2);
        mTestUtils.waitForXFrames(2);
        mTestUtils.screenShot(getClass().getSimpleName(), "testSkeletonAnimation2", mWaiter, mDoCompare);
    }

    public SXRSkeletonAnimation makeSkeletonAnimation(SXRSkeleton skel)
    {
        Quaternionf q = new Quaternionf();
        Matrix4f mtx = new Matrix4f();
        SXRPose pose = new SXRPose(skel);
        int leftShoulderBone = skel.getBoneIndex("mixamorig_LeftShoulder");
        int rightShoulderBone = skel.getBoneIndex("mixamorig_RightShoulder");
        Vector3f v = new Vector3f();
        float[] rotKeys = new float[] { 0, 0, 0, 0, 1,   2, 0, 0, 0, 1 };
        float[] posKeys = new float[] { 0, 0, 0, 0,      2, 0, 0, 0 };

        pose.copy(skel.getBindPose());
        pose.getLocalMatrix(leftShoulderBone, mtx);
        mtx.getTranslation(v);
        mtx.getUnnormalizedRotation(q);
        posKeys[1] = v.x;
        posKeys[2] = v.y;
        posKeys[3] = v.z;
        rotKeys[1] = q.x;
        rotKeys[2] = q.y;
        rotKeys[3] = q.z;
        rotKeys[4] = q.w;

        q.fromAxisAngleDeg(1, 0, 0, -45);
        mtx.rotate(q);
        pose.setLocalMatrix(leftShoulderBone, mtx);
        mtx.getTranslation(v);
        mtx.getUnnormalizedRotation(q);
        posKeys[5] = v.x;
        posKeys[6] = v.y;
        posKeys[7] = v.z;
        rotKeys[6] = q.x;
        rotKeys[7] = q.y;
        rotKeys[8] = q.z;
        rotKeys[9] = q.w;
        SXRAnimationChannel leftShoulder = new SXRAnimationChannel("mixamorig_LeftShoulder", posKeys, rotKeys, null,
                                                                   SXRAnimationBehavior.DEFAULT, SXRAnimationBehavior.DEFAULT);
        rotKeys = new float[] { 0, 0, 0, 0, 1,   2, 0, 0, 0, 1 };
        posKeys = new float[] { 0, 0, 0, 0,      2, 0, 0, 0 };
        pose.getLocalMatrix(rightShoulderBone, mtx);
        mtx.getTranslation(v);
        mtx.getUnnormalizedRotation(q);
        posKeys[1] = v.x;
        posKeys[2] = v.y;
        posKeys[3] = v.z;
        rotKeys[1] = q.x;
        rotKeys[2] = q.y;
        rotKeys[3] = q.z;
        rotKeys[4] = q.w;

        q.fromAxisAngleDeg(1, 0, 0, -45);
        mtx.rotate(q);
        pose.setLocalMatrix(rightShoulderBone, mtx);
        mtx.getTranslation(v);
        mtx.getUnnormalizedRotation(q);
        posKeys[5] = v.x;
        posKeys[6] = v.y;
        posKeys[7] = v.z;
        rotKeys[6] = q.x;
        rotKeys[7] = q.y;
        rotKeys[8] = q.z;
        rotKeys[9] = q.w;
        SXRAnimationChannel rightShoulder = new SXRAnimationChannel("mixamorig_RightShoulder", posKeys, rotKeys, null,
                                                                   SXRAnimationBehavior.DEFAULT, SXRAnimationBehavior.DEFAULT);
        SXRSkeletonAnimation skelAnim = new SXRSkeletonAnimation("LiftArms", skel, 2);
        skelAnim.setTarget(skel.getOwnerObject());
        skelAnim.addChannel("mixamorig_LeftShoulder", leftShoulder);
        skelAnim.addChannel("mixamorig_RightShoulder", rightShoulder);
        return skelAnim;
    }

}
