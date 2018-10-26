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
package com.samsungxr.viewmanager.controls.gamepad;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRSceneObject;


public class GamepadObject extends SXRSceneObject {

    private GamepadVirtual gamepadVirtual;

    public GamepadObject(SXRContext sxrContext) {
        super(sxrContext);

        gamepadVirtual = new GamepadVirtual(sxrContext);
        
        SXRSceneObject mSXRSceneObject = new SXRSceneObject(sxrContext);
        mSXRSceneObject.addChildObject(gamepadVirtual);
        addChildObject(mSXRSceneObject);
    }

    public GamepadVirtual getGamepadVirtual() {
        return gamepadVirtual;
    }
}
