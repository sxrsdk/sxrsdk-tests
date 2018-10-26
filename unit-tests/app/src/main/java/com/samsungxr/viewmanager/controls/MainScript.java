/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsungxr.viewmanager.controls;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRRenderPass.SXRCullFaceEnum;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRScript;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.viewmanager.controls.Worm.MovementDirection;
import com.samsungxr.viewmanager.controls.focus.ControlSceneObjectBehavior;
import com.samsungxr.viewmanager.controls.gamepad.GamepadObject;
import com.samsungxr.viewmanager.controls.input.GamepadInput;
import com.samsungxr.viewmanager.controls.menu.Menu;
import com.samsungxr.viewmanager.controls.util.RenderingOrder;
import com.samsungxr.viewmanager.controls.util.Util;
import com.samsungxr.viewmanager.controls.util.VRSamplesTouchPadGesturesDetector.SwipeDirection;

public class MainScript extends SXRScript {

    private SXRContext mSXRContext;
    private SXRScene scene;

    private Worm worm;
    private SXRSceneObject skybox, surroundings, sun, ground, fence;
    private SXRSceneObject clouds;
    private float GROUND_Y_POSITION = -1;
    private float SKYBOX_SIZE = 1;
    private float SUN_ANGLE_POSITION = 30;
    private float SUN_Y_POSITION = 10;
    private float CLOUDS_DISTANCE = 15;

    private float SCENE_SIZE = 0.75f;
    private float SCENE_Y = -1.0f;

    private Menu mMenu = null;

    private GamepadObject gamepadObject;

    @Override
    public void onInit(SXRContext sxrContext) {

        // save context for possible use in onStep(), even though that's empty
        // in this sample
        mSXRContext = sxrContext;

        scene = sxrContext.getMainScene();

        sxrContext.getMainScene().getMainCameraRig().getRightCamera()
                .setBackgroundColor(Color.GREEN);
        sxrContext.getMainScene().getMainCameraRig().getLeftCamera()
                .setBackgroundColor(Color.GREEN);

        // set background color
        SXRCameraRig mainCameraRig = scene.getMainCameraRig();
        mainCameraRig.getRightCamera().setBackgroundColor(Color.GREEN);
        mainCameraRig.getLeftCamera().setBackgroundColor(Color.RED);
        mainCameraRig.getTransform().setPositionY(0);
        createSkybox();
        createClouds();
        createGround();

        createSun();
        createSurroundings();
        createWorm();
        createFence();
        createMenu();
        createGamepad3D();
    }

    private void createFence() {

        SXRMesh mesh = mSXRContext.getAssetLoader().loadMesh(
                new SXRAndroidResource(mSXRContext, R.raw.fence));
        SXRTexture texture = mSXRContext.loadTexture(
                new SXRAndroidResource(mSXRContext, R.drawable.atlas01));
        fence = new SXRSceneObject(mSXRContext, mesh, texture);
        fence.getTransform().setPositionY(GROUND_Y_POSITION);
        fence.getTransform().setScale(SCENE_SIZE, SCENE_SIZE, SCENE_SIZE);
        fence.getRenderData().setCullFace(SXRCullFaceEnum.None);
        fence.getRenderData().setRenderingOrder(RenderingOrder.FENCE);
        scene.addSceneObject(fence);

    }

    private void createWorm() {

        worm = new Worm(mSXRContext);
        scene.addSceneObject(worm);
    }

    private void createGround() {

        SXRMesh mesh = mSXRContext.createQuad(55, 55);
        SXRTexture texture = mSXRContext.loadTexture(new SXRAndroidResource(mSXRContext, R.drawable.ground_tile));

        ground = new SXRSceneObject(mSXRContext, mesh, texture);
        ground.getTransform().setPositionY(GROUND_Y_POSITION);
        ground.getTransform().setScale(SCENE_SIZE, SCENE_SIZE, SCENE_SIZE);
        ground.getTransform().setRotationByAxis(-90, 1, 0, 0);
        ground.getRenderData().setRenderingOrder(RenderingOrder.GROUND);
        scene.addSceneObject(ground);
    }

    private void createSkybox() {

        SXRMesh mesh = mSXRContext.getAssetLoader().loadMesh(
                new SXRAndroidResource(mSXRContext, R.raw.skybox));
        SXRTexture texture = mSXRContext.loadTexture(
                new SXRAndroidResource(mSXRContext, R.drawable.skybox));

        skybox = new SXRSceneObject(mSXRContext, mesh, texture);
        skybox.getTransform().setScale(SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE);
        skybox.getRenderData().setRenderingOrder(RenderingOrder.SKYBOX);
        scene.addSceneObject(skybox);
    }

    private void createClouds() {

        clouds = new Clouds(mSXRContext, CLOUDS_DISTANCE, 9);
    }

    private void createSurroundings() {

        SXRMesh mesh = mSXRContext.getAssetLoader().loadMesh(
                new SXRAndroidResource(mSXRContext, R.raw.stones));
        SXRTexture texture = mSXRContext.loadTexture(
                new SXRAndroidResource(mSXRContext, R.drawable.atlas01));

        surroundings = new SXRSceneObject(mSXRContext, mesh, texture);
        surroundings.getTransform().setScale(SCENE_SIZE, SCENE_SIZE, SCENE_SIZE);
        surroundings.getTransform().setPositionY(SCENE_Y);
        surroundings.getRenderData().setRenderingOrder(RenderingOrder.FLOWERS);
        scene.addSceneObject(surroundings);
        // ground.addChildObject(surroundings);

        mesh = mSXRContext.getAssetLoader().loadMesh(
                new SXRAndroidResource(mSXRContext, R.raw.grass));
        texture = mSXRContext.loadTexture(
                new SXRAndroidResource(mSXRContext, R.drawable.atlas01));

        surroundings = new SXRSceneObject(mSXRContext, mesh, texture);
        surroundings.getTransform().setScale(SCENE_SIZE, SCENE_SIZE, SCENE_SIZE);
        surroundings.getTransform().setPositionY(SCENE_Y);
        scene.addSceneObject(surroundings);
        // ground.addChildObject(surroundings);
        surroundings.getRenderData().setRenderingOrder(RenderingOrder.GRASS);

        mesh = mSXRContext.getAssetLoader().loadMesh(
                new SXRAndroidResource(mSXRContext, R.raw.flowers));
        texture = mSXRContext.loadTexture(
                new SXRAndroidResource(mSXRContext, R.drawable.atlas01));

        surroundings = new SXRSceneObject(mSXRContext, mesh, texture);
        surroundings.getTransform().setScale(SCENE_SIZE, SCENE_SIZE, SCENE_SIZE);
        surroundings.getTransform().setPositionY(SCENE_Y);
        scene.addSceneObject(surroundings);
        // ground.addChildObject(surroundings);
        surroundings.getRenderData().setRenderingOrder(RenderingOrder.FLOWERS);

        mesh = mSXRContext.getAssetLoader().loadMesh(
                new SXRAndroidResource(mSXRContext, R.raw.wood));
        texture = mSXRContext.loadTexture(
                new SXRAndroidResource(mSXRContext, R.drawable.atlas01));
        surroundings = new SXRSceneObject(mSXRContext, mesh, texture);
        surroundings.getTransform().setScale(SCENE_SIZE, SCENE_SIZE, SCENE_SIZE);
        surroundings.getTransform().setPositionY(SCENE_Y);
        surroundings.getRenderData().setCullFace(SXRCullFaceEnum.None);
        scene.addSceneObject(surroundings);
        // ground.addChildObject(surroundings);
        surroundings.getRenderData().setRenderingOrder(RenderingOrder.WOOD);
    }

    private void createSun() {

        SXRMesh mesh = mSXRContext.createQuad(25, 25);
        SXRTexture texture = mSXRContext.loadTexture(
                new SXRAndroidResource(mSXRContext, R.drawable.sun));

        sun = new SXRSceneObject(mSXRContext, mesh, texture);
        sun.getTransform().setRotationByAxis(90, 1, 0, 0);
        sun.getTransform().setPositionY(SUN_Y_POSITION);
        sun.getTransform().rotateByAxisWithPivot(SUN_ANGLE_POSITION, 1, 0, 0, 0, 0, 0);
        sun.getRenderData().setRenderingOrder(RenderingOrder.SUN);
        scene.addSceneObject(sun);
    }

    @Override
    public void onStep() {
        worm.chainMove(mSXRContext);

        GamepadInput.process();

        GamepadInput.interactWithDPad(worm);
        ControlSceneObjectBehavior.process(mSXRContext);

        if (gamepadObject != null) {

            gamepadObject.getGamepadVirtual().handlerAnalogL(
                    GamepadInput.getCenteredAxis(MotionEvent.AXIS_X),
                    GamepadInput.getCenteredAxis(MotionEvent.AXIS_Y),
                    0);

            gamepadObject.getGamepadVirtual().handlerAnalogR(
                    GamepadInput.getCenteredAxis(MotionEvent.AXIS_RX),
                    GamepadInput.getCenteredAxis(MotionEvent.AXIS_RY),
                    0);

            gamepadObject.getGamepadVirtual().dpadTouch(
                    GamepadInput.getCenteredAxis(MotionEvent.AXIS_HAT_X),
                    GamepadInput.getCenteredAxis(MotionEvent.AXIS_HAT_Y));

            gamepadObject.getGamepadVirtual().handlerLRButtons(
                    GamepadInput.getKey(KeyEvent.KEYCODE_BUTTON_L1),
                    GamepadInput.getKey(KeyEvent.KEYCODE_BUTTON_R1));

            gamepadObject.getGamepadVirtual().buttonsPressed(
                    GamepadInput.getKey(KeyEvent.KEYCODE_BUTTON_X),
                    GamepadInput.getKey(KeyEvent.KEYCODE_BUTTON_Y),
                    GamepadInput.getKey(KeyEvent.KEYCODE_BUTTON_A),
                    GamepadInput.getKey(KeyEvent.KEYCODE_BUTTON_B));
        }
    }

    public void animateWorm(
            com.samsungxr.viewmanager.controls.util.VRSamplesTouchPadGesturesDetector.SwipeDirection swipeDirection) {

        float duration = 0.6f;
        float movement = 0.75f;
        float degree = 22.5f;

        if (swipeDirection.name() == SwipeDirection.Up.name()) {
            worm.moveAlongCameraVector(duration, movement);
            worm.rotateWorm(MovementDirection.Up);

        } else if (swipeDirection.name() == SwipeDirection.Down.name()) {
            worm.moveAlongCameraVector(duration, -movement);
            worm.rotateWorm(MovementDirection.Down);

        } else if (swipeDirection.name() == SwipeDirection.Forward.name()) {
            worm.rotateAroundCamera(duration, -degree);
            worm.rotateWorm(MovementDirection.Right);

        } else {
            worm.rotateAroundCamera(duration, degree);
            worm.rotateWorm(MovementDirection.Left);
        }
    }

    private void createMenu() {
        mMenu = new Menu(mSXRContext);
        mMenu.getTransform().setScale(0.4f, 0.4f, 0.4f);
        mMenu.getTransform().setPosition(0, -.5f, -3f);
        mMenu.getRenderData().getMaterial().setOpacity(0.5f);
        // scene.addSceneObject(mMenu);
    }

    private void createGamepad3D() {
        SXRCameraRig cameraObject = mSXRContext.getMainScene()
                .getMainCameraRig();
        gamepadObject = new GamepadObject(mSXRContext);

        gamepadObject.getTransform().setPosition(-3, 1.f, 8f);
        float angle = Util.getYRotationAngle(gamepadObject, cameraObject);

        gamepadObject.getTransform().rotateByAxis(angle, 0, 1, 0);

        scene.addSceneObject(gamepadObject);
    }
}
