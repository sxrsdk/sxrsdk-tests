package com.samsungxr.tester;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.nodes.SXRVideoNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class VideoTests
{
    private SXRTestUtils sxrTestUtils;
    private Waiter mWaiter;
    private SXRNode mRoot;
    private boolean mDoCompare = true;

    public VideoTests() {
        super();
    }

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new
            ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);


    @After
    public void tearDown()
    {
        SXRScene scene = sxrTestUtils.getMainScene();
        if (scene != null)
        {
            scene.clear();
        }
    }

    @Before
    public void setUp() throws TimeoutException {
        sxrTestUtils = new SXRTestUtils(ActivityRule.getActivity());
        mWaiter = new Waiter();
        sxrTestUtils.waitForOnInit();
    }

    private SXRVideoNode createVideoObject(SXRContext sxrContext, String file, int videotype) throws IOException
    {
        MediaPlayer.OnInfoListener listener = new MediaPlayer.OnInfoListener()
        {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra)
            {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                {
                    sxrTestUtils.onAssetLoaded(null);
                    return true;
                }
                return false;
            }
        };
        final AssetFileDescriptor afd = sxrContext.getActivity().getAssets().openFd(file);
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mediaPlayer.setOnInfoListener(listener);
        mediaPlayer.prepare();
        SXRVideoNode video = new SXRVideoNode(sxrContext, 8.0f, 4.0f, mediaPlayer, videotype);
        video.setName("video");
        video.getTransform().setPosition(0.0f, 0.0f, -4.0f);
        while (video.getMediaPlayer() == null)
        {
            ;
        }
        video.getMediaPlayer().start();
        return video;
    }

    private SXRVideoNode createVideoMeshObject(SXRContext sxrContext, String file, int videotype, SXRMesh mesh) throws IOException
    {
        MediaPlayer.OnInfoListener listener = new MediaPlayer.OnInfoListener()
        {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra)
            {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                {
                    sxrTestUtils.onAssetLoaded(null);
                    return true;
                }
                return false;
            }
        };
        final AssetFileDescriptor afd = sxrContext.getActivity().getAssets().openFd(file);
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mediaPlayer.setOnInfoListener(listener);
        mediaPlayer.prepare();
        SXRVideoNode video = new SXRVideoNode(sxrContext, mesh, mediaPlayer, videotype);
        video.setName("video");
        video.getTransform().setPosition(0.0f, 0.0f, -2.0f);
        while (video.getMediaPlayer() == null)
        {
            ;
        }
        video.getMediaPlayer().start();
        return video;
    }

    @Test
    public void testMonoVideo() throws TimeoutException {
        SXRContext ctx  = sxrTestUtils.getSxrContext();
        SXRScene mainScene = sxrTestUtils.getMainScene();

        try
        {
            SXRVideoNode video =
                    createVideoObject(ctx, "tnb.mp4", SXRVideoNode.SXRVideoType.MONO);
            mainScene.addNode(video);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        sxrTestUtils.waitForAssetLoad();
        sxrTestUtils.waitForXFrames(4);
        sxrTestUtils.screenShotRight(getClass().getSimpleName(), "testMonoVideo", mWaiter, mDoCompare);
    }

    @Test
    public void testMonoVideoMesh() throws TimeoutException {
        SXRContext ctx  = sxrTestUtils.getSxrContext();
        SXRScene mainScene = sxrTestUtils.getMainScene();
        SXRMesh mesh = new SXRMesh(ctx, "float3 a_position float2 a_texcoord");
        mesh.createQuad(3, 2);

        try
        {
            SXRVideoNode video =
                    createVideoMeshObject(ctx, "tnb.mp4", SXRVideoNode.SXRVideoType.MONO, mesh);
            mainScene.addNode(video);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        sxrTestUtils.waitForAssetLoad();
        sxrTestUtils.waitForXFrames(4);
        sxrTestUtils.screenShotRight(getClass().getSimpleName(), "testMonoVideoMesh", mWaiter, mDoCompare);
    }

    @Test
    public void testHorizontalStereo() throws TimeoutException {
        SXRContext ctx  = sxrTestUtils.getSxrContext();
        SXRScene mainScene = sxrTestUtils.getMainScene();

        try
        {
            SXRVideoNode video =
                    createVideoObject(ctx, "sbs.mp4", SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);
            mainScene.addNode(video);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        sxrTestUtils.waitForAssetLoad();
        sxrTestUtils.waitForXFrames(4);
        sxrTestUtils.screenShotRight(getClass().getSimpleName(), "testHorizontalStereo", mWaiter, mDoCompare);
    }

    @Test
    public void testVerticalStereo() throws TimeoutException {
        SXRContext ctx  = sxrTestUtils.getSxrContext();
        SXRScene mainScene = sxrTestUtils.getMainScene();

        try
        {
            SXRVideoNode video =
                    createVideoObject(ctx, "tnb.mp4", SXRVideoNode.SXRVideoType.VERTICAL_STEREO);
            mainScene.addNode(video);
        }
        catch (IOException ex)
        {
            mWaiter.fail(ex);
        }
        sxrTestUtils.waitForAssetLoad();
        sxrTestUtils.waitForXFrames(4);
        sxrTestUtils.screenShotRight(getClass().getSimpleName(), "testVerticalStereo", mWaiter, mDoCompare);
    }

}
