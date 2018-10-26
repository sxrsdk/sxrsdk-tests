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
package com.samsungxr.viewmanager.controls.menu;

import android.graphics.Color;
import android.graphics.Paint.Align;

import com.samsungxr.SXRBitmapTexture;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.animation.SXRPositionAnimation;
import com.samsungxr.tests.R;
import com.samsungxr.viewmanager.controls.focus.ControlSceneObject;
import com.samsungxr.viewmanager.controls.focus.FocusListener;
import com.samsungxr.viewmanager.controls.util.SXRTextBitmapFactory;
import com.samsungxr.viewmanager.controls.util.Text;

class MenuItem extends SXRSceneObject implements FocusListener {

    private static float[] originalPosition = null;

    private static final int TEXT_TEXTURE_WIDTH = 100;
    private static final int TEXT_TEXTURE_HEIGHT = 100;
    private static final int TEXT_FONT_SIZE = 20;
    private static final int TEXT_MAX_LENGHT = 255;

    private static final float X_OFFSET = -4.0f;
    private static final float Y_OFFSET = 0f;
    private static final float Z_OFFSET = 0f;
    private static final float SPACING = 2.0f;

    private static final float Z_HOVER_ANIMATION_OFFSET = 0.2f;
    private static final float Z_HOVER_ANIMATION_TIME = 0.5f;

    private SXRSceneObject motion;
    private SXRSceneObject color;
    private SXRSceneObject scale;
    private SXRSceneObject rotation;
    private SXRContext sxrContext;
    private static final float WIDTH = 2.0f;
    private static final float HEIGHT = 0.6f;

    MenuItem(SXRContext sxrContext) {
        super(sxrContext);
        this.sxrContext = sxrContext;
        createMenuItems();

    }

    private void createMenuItems() {

        motion = getMenuItem(new Text(this.sxrContext.getContext().getResources()
                .getString(R.string.motion), Align.CENTER, TEXT_FONT_SIZE, Color.BLACK,
                Color.WHITE,
                TEXT_MAX_LENGHT));

        color = getMenuItem(new Text(this.sxrContext.getContext().getResources()
                .getString(R.string.color), Align.CENTER, TEXT_FONT_SIZE, Color.BLACK,
                Color.RED, TEXT_MAX_LENGHT));

        scale = getMenuItem(new Text(this.sxrContext.getContext().getResources()
                .getString(R.string.scale), Align.CENTER, TEXT_FONT_SIZE, Color.BLACK, Color.WHITE,
                TEXT_MAX_LENGHT));

        rotation = getMenuItem(new Text(this.sxrContext.getContext().getResources()
                .getString(R.string.rotatation), Align.CENTER, TEXT_FONT_SIZE, Color.BLACK,
                Color.WHITE,
                TEXT_MAX_LENGHT));

        float itemPositionX = 0f;
        float itemPositionY = 0f;
        float itemPositionZ = 0f;

        motion.getTransform().setPosition(itemPositionX + X_OFFSET, itemPositionY + Y_OFFSET,
                itemPositionZ + Z_OFFSET);

        color.getTransform().setPosition(motion.getTransform().getPositionX() + SPACING,
                itemPositionY, itemPositionZ);

        scale.getTransform().setPosition(color.getTransform().getPositionX() + SPACING,
                itemPositionY, itemPositionZ);

        rotation.getTransform().setPosition(scale.getTransform().getPositionX() + SPACING,
                itemPositionY, itemPositionZ);

        addChildObject(motion);
        addChildObject(color);
        addChildObject(scale);
        addChildObject(rotation);

    }

    private SXRSceneObject getMenuItem(Text text) {

        SXRMaterial material = new SXRMaterial(sxrContext);
        SXRRenderData renderData = new SXRRenderData(sxrContext);
        SXRMesh mesh = getSXRContext().createQuad(WIDTH, HEIGHT);

        renderData.setMesh(mesh);
        renderData.setMaterial(material);
        ControlSceneObject sceneObject = new ControlSceneObject(sxrContext);
        sceneObject.focusListener = this;

        sceneObject.attachRenderData(renderData);
        sceneObject.getRenderData().setRenderingOrder(1110);
        SXRBitmapTexture bitmap = new SXRBitmapTexture(sxrContext,
                SXRTextBitmapFactory.create(sxrContext.getContext(), TEXT_TEXTURE_WIDTH,
                        TEXT_TEXTURE_HEIGHT, text, 0));

        sceneObject.getRenderData().getMaterial().setMainTexture(bitmap);
        sceneObject.attachEyePointeeHolder();

        return sceneObject;
    }

    private void setOriginalPosition() {
        originalPosition = new float[3];
        originalPosition[0] = this.getTransform().getPositionX();
        originalPosition[1] = this.getTransform().getPositionY();
        originalPosition[2] = this.getTransform().getPositionZ();
    }

    private void bringMenuItemToFront(SXRSceneObject item) {
        if (originalPosition == null) {
            setOriginalPosition();
        }

        new SXRPositionAnimation(item.getTransform(),
                Z_HOVER_ANIMATION_TIME, item.getTransform().getPositionX(), item.getTransform()
                        .getPositionY(),
                originalPosition[2] + Z_HOVER_ANIMATION_OFFSET)
                .start(getSXRContext().getAnimationEngine());

    }

    private void sendMenuItemToBack(SXRSceneObject item) {

        if (originalPosition == null) {
            setOriginalPosition();
        }

        new SXRPositionAnimation(item.getTransform(),
                Z_HOVER_ANIMATION_TIME, item.getTransform().getPositionX(), item.getTransform()
                        .getPositionY(),
                originalPosition[2])
                .start(getSXRContext().getAnimationEngine());

    }

    @Override
    public void gainedFocus(SXRSceneObject object) {
        bringMenuItemToFront(object);
    }

    @Override
    public void lostFocus(SXRSceneObject object) {
        sendMenuItemToBack(object);

    }

    @Override
    public void inFocus(SXRSceneObject object) {
        // TODO Auto-generated method stub

    }
}
