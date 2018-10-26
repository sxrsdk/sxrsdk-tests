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
package com.samsungxr.viewmanager.controls.focus;

import com.samsungxr.SXRContext;
import com.samsungxr.SXREyePointeeHolder;
import com.samsungxr.SXRPicker;
import com.samsungxr.SXRNode;

import java.util.ArrayList;

public class ControlNodeBehavior {

    public static void process(SXRContext context) {

        SXREyePointeeHolder[] eyePointeeHolders = SXRPicker.pickScene(context.getMainScene());

        ArrayList<SXRNode> needToDisableFocus = new ArrayList<SXRNode>();

        for (SXRNode obj : context.getMainScene().getWholeNodes()) {
            needToDisableFocus.add(obj);
        }

        for (SXREyePointeeHolder holder : eyePointeeHolders) {

            if (ControlNode.hasFocusMethods(holder.getOwnerObject())) {
                ControlNode controlObject = (ControlNode) holder.getOwnerObject();
                controlObject.setFocus(true);
                controlObject.dispatchInFocus();
                needToDisableFocus.remove(controlObject);
            }
        }

        for (SXRNode obj : needToDisableFocus) {
            if (ControlNode.hasFocusMethods(obj)) {
                ControlNode control = (ControlNode) obj;
                control.setFocus(false);
            }
        }

    }
}
