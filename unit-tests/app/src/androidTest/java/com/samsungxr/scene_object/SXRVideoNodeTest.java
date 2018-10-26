package com.samsungxr.node;

import android.media.MediaPlayer;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRNotifications;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.nodes.SXRVideoNode;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

public class SXRVideoNodeTest extends ActivityInstrumentationSXRf {

    public SXRVideoNodeTest() {
        super(SXRTestActivity.class);
    }

    public void testCreateSXRVideoNode() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        assertNotNull(new SXRVideoNode(TestDefaultSXRViewManager.mSXRContext, 100f, 100f, mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO));
    }

    public void testCreateNameVideoNode() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        videoNode.setName("Test");
        assertEquals(videoNode.getName(), "Test");
    }

    public void testStartVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        videoNode.getMediaPlayer().start();
        mediaPlayer.stop();
    }

    public void testStopVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        videoNode.getMediaPlayer().start();
        mediaPlayer.stop();
    }

    public void testPauseVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        videoNode.getMediaPlayer().start();
        videoNode.getMediaPlayer().pause();
        mediaPlayer.stop();
    }

    public void testGetSXRVideoNodeTransform() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        assertNotNull(videoNode.getTransform());
    }

    public void testGetSXRVideoNodeTimeStamp() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        assertNotNull(videoNode.getTimeStamp());
    }

    public void testGetSXRVideoSceneIsActive() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        assertTrue(videoNode.isActive());
    }

    public void testGetSXRVideoSceneCameraRig() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.VERTICAL_STEREO);

        SXRCameraRig cameraRig = SXRCameraRig.makeInstance(TestDefaultSXRViewManager.mSXRContext);
        videoNode.attachCameraRig(cameraRig);
        videoNode.getMediaPlayer().start();
        assertNotNull(videoNode.getCameraRig());
    }

    public void testDeactivateVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.MONO);

        videoNode.deactivate();
        assertFalse(videoNode.isActive());
    }

    public void testReleaseVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        videoNode.getMediaPlayer().start();
        videoNode.release();

        assertTrue(null == videoNode.getMediaPlayer());
    }

    public void testSetVideoTypeInvalid() {
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            new SXRVideoNode(TestDefaultSXRViewManager.mSXRContext, 100f, 100f, mediaPlayer, 320);
        } catch (IllegalArgumentException e) {
            return;
        }
        assertTrue("Ctor should throw an exception on invalid type argument", false);
    }

    public void testSetMediaPlayer() { //Created by j.elidelson on 08/17/2015
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoNode videoNode = makeVideoNode(mediaPlayer, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);

        MediaPlayer mediaPlayer2 = new MediaPlayer();
        videoNode.setMediaPlayer(SXRVideoNode.makePlayerInstance(mediaPlayer2));
        videoNode.activate();
        videoNode.deactivate();
    }

    public void testCreateSXRVideoNodeWrongVideoType() {

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            assertNotNull(new SXRVideoNode(TestDefaultSXRViewManager.mSXRContext, 100f, 100f, mediaPlayer, 3));
            fail("should throws IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSXRVideotype() {
        assertEquals(0, SXRVideoNode.SXRVideoType.MONO);
        assertEquals(1, SXRVideoNode.SXRVideoType.HORIZONTAL_STEREO);
        assertEquals(2, SXRVideoNode.SXRVideoType.VERTICAL_STEREO);
    }

    private SXRVideoNode makeVideoNode(MediaPlayer mediaPlayer, int verticalStereo) {
        SXRVideoNode videoNode = new SXRVideoNode(TestDefaultSXRViewManager.mSXRContext, 100f, 100f, mediaPlayer, verticalStereo);
        //have to wait for at least a frame to go past so the texture the video scene object needs
        //is created
        SXRNotifications.waitBeforeStep();
        SXRNotifications.waitAfterStep();
        return videoNode;
    }
}
