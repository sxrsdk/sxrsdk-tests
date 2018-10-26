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

import com.samsungxr.SXRContext;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.animation.SXRPositionAnimation;
import com.samsungxr.viewmanager.controls.focus.ControlSceneObject;
import com.samsungxr.viewmanager.controls.focus.FocusListener;
import com.samsungxr.viewmanager.controls.util.Util;

public class Menu extends ControlSceneObject implements FocusListener {

    private static final float Z_HOVER_ANIMATION_OFFSET = 0.5f;
    private static final float Z_HOVER_ANIMATION_TIME = 0.5f;

    private static final float MENU_WIDTH = 6.0f;
    private static final float MENU_HEIGHT = 0.7f;

    private float[] originalPosition = null;
    private MenuItem menuItem;

    public Menu(SXRContext sxrContext) {
        super(sxrContext, MENU_WIDTH, MENU_HEIGHT, Util.transparentTexture(sxrContext));

        attachEyePointeeHolder();
        this.focusListener = this;

        menuItem = new MenuItem(sxrContext);
        
        addChildObject(menuItem);
    }

    public void setOriginalPosition() {
        originalPosition = new float[3];
        originalPosition[0] = this.getTransform().getPositionX();
        originalPosition[1] = this.getTransform().getPositionY();
        originalPosition[2] = this.getTransform().getPositionZ();
    }

    private void bringMenuToFront() {
        if (originalPosition == null)
            setOriginalPosition();

        new SXRPositionAnimation(this.getTransform(),
                Z_HOVER_ANIMATION_TIME, originalPosition[0], originalPosition[1],
                originalPosition[2]
                        + Z_HOVER_ANIMATION_OFFSET)
                .start(getSXRContext().getAnimationEngine());

    }

    private void sendMenuToBack() {
        if (originalPosition == null)
            setOriginalPosition();

        new SXRPositionAnimation(this.getTransform(),
                Z_HOVER_ANIMATION_TIME, originalPosition[0], originalPosition[1],
                originalPosition[2])
                .start(getSXRContext().getAnimationEngine());
    }

    @Override
    public void gainedFocus(SXRSceneObject object) {
        bringMenuToFront();
    }

    @Override
    public void lostFocus(SXRSceneObject object) {
        sendMenuToBack();
    }

    @Override
    public void inFocus(SXRSceneObject object) {
    }
}
