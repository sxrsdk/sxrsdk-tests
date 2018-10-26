package com.samsungxr.scene_object;

import android.media.MediaPlayer;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRNotifications;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.scene_objects.SXRVideoSceneObject;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

public class SXRVideoSceneObjectTest extends ActivityInstrumentationSXRf {

    public SXRVideoSceneObjectTest() {
        super(SXRTestActivity.class);
    }

    public void testCreateSXRVideoSceneObject() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        assertNotNull(new SXRVideoSceneObject(TestDefaultSXRViewManager.mSXRContext, 100f, 100f, mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO));
    }

    public void testCreateNameVideoSceneObject() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        videoSceneObject.setName("Test");
        assertEquals(videoSceneObject.getName(), "Test");
    }

    public void testStartVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        videoSceneObject.getMediaPlayer().start();
        mediaPlayer.stop();
    }

    public void testStopVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        videoSceneObject.getMediaPlayer().start();
        mediaPlayer.stop();
    }

    public void testPauseVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        videoSceneObject.getMediaPlayer().start();
        videoSceneObject.getMediaPlayer().pause();
        mediaPlayer.stop();
    }

    public void testGetSXRVideoSceneObjectTransform() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        assertNotNull(videoSceneObject.getTransform());
    }

    public void testGetSXRVideoSceneObjectTimeStamp() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        assertNotNull(videoSceneObject.getTimeStamp());
    }

    public void testGetSXRVideoSceneIsActive() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        assertTrue(videoSceneObject.isActive());
    }

    public void testGetSXRVideoSceneCameraRig() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.VERTICAL_STEREO);

        SXRCameraRig cameraRig = SXRCameraRig.makeInstance(TestDefaultSXRViewManager.mSXRContext);
        videoSceneObject.attachCameraRig(cameraRig);
        videoSceneObject.getMediaPlayer().start();
        assertNotNull(videoSceneObject.getCameraRig());
    }

    public void testDeactivateVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.MONO);

        videoSceneObject.deactivate();
        assertFalse(videoSceneObject.isActive());
    }

    public void testReleaseVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        videoSceneObject.getMediaPlayer().start();
        videoSceneObject.release();

        assertTrue(null == videoSceneObject.getMediaPlayer());
    }

    public void testSetVideoTypeInvalid() {
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            new SXRVideoSceneObject(TestDefaultSXRViewManager.mSXRContext, 100f, 100f, mediaPlayer, 320);
        } catch (IllegalArgumentException e) {
            return;
        }
        assertTrue("Ctor should throw an exception on invalid type argument", false);
    }

    public void testSetMediaPlayer() { //Created by j.elidelson on 08/17/2015
        MediaPlayer mediaPlayer = new MediaPlayer();
        SXRVideoSceneObject videoSceneObject = makeVideoSceneObject(mediaPlayer, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);

        MediaPlayer mediaPlayer2 = new MediaPlayer();
        videoSceneObject.setMediaPlayer(SXRVideoSceneObject.makePlayerInstance(mediaPlayer2));
        videoSceneObject.activate();
        videoSceneObject.deactivate();
    }

    public void testCreateSXRVideoSceneObjectWrongVideoType() {

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            assertNotNull(new SXRVideoSceneObject(TestDefaultSXRViewManager.mSXRContext, 100f, 100f, mediaPlayer, 3));
            fail("should throws IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSXRVideotype() {
        assertEquals(0, SXRVideoSceneObject.SXRVideoType.MONO);
        assertEquals(1, SXRVideoSceneObject.SXRVideoType.HORIZONTAL_STEREO);
        assertEquals(2, SXRVideoSceneObject.SXRVideoType.VERTICAL_STEREO);
    }

    private SXRVideoSceneObject makeVideoSceneObject(MediaPlayer mediaPlayer, int verticalStereo) {
        SXRVideoSceneObject videoSceneObject = new SXRVideoSceneObject(TestDefaultSXRViewManager.mSXRContext, 100f, 100f, mediaPlayer, verticalStereo);
        //have to wait for at least a frame to go past so the texture the video scene object needs
        //is created
        SXRNotifications.waitBeforeStep();
        SXRNotifications.waitAfterStep();
        return videoSceneObject;
    }
}
