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

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.animation.SXRAnimation;
import com.samsungxr.animation.SXRRotationByAxisWithPivotAnimation;
import com.samsungxr.viewmanager.controls.util.RenderingOrder;

public class Clouds extends SXRSceneObject {

    public SXRSceneObject[] clouds;
    private final int NUMBER_OF_CLOUDS = 4;
    private final int FULL_ROTATION = 360;
    private final int CLOUD_ANGLE = 30;
    private final float CLOUD_OFFSET = 0.5f;
    private final int CLOUD_ROTATION_DURATION = 1800;

    public Clouds(SXRContext sxrContext, float cloudDistance, int numberOfClouds) {
        super(sxrContext);

        SXRMesh[] mesh = new SXRMesh[NUMBER_OF_CLOUDS];
        mesh[0] = sxrContext.createQuad(2.1f, 1.6f);
        mesh[1] = sxrContext.createQuad(4.2f, 2.1f);
        mesh[2] = sxrContext.createQuad(6.7f, 2.4f);
        mesh[3] = sxrContext.createQuad(6.2f, 2.4f);

        SXRTexture[] texture = new SXRTexture[NUMBER_OF_CLOUDS];
        texture[0] = sxrContext.loadTexture(
                new SXRAndroidResource(sxrContext, R.drawable.cloud_01));
        texture[1] = sxrContext.loadTexture(
                new SXRAndroidResource(sxrContext, R.drawable.cloud_02));
        texture[2] = sxrContext.loadTexture(
                new SXRAndroidResource(sxrContext, R.drawable.cloud_03));
        texture[3] = sxrContext.loadTexture(
                new SXRAndroidResource(sxrContext, R.drawable.cloud_04));
        clouds = new SXRSceneObject[numberOfClouds];

        for (int i = 0; i < numberOfClouds; i++) {
            float angle = FULL_ROTATION / numberOfClouds;
            int random = i % NUMBER_OF_CLOUDS;
            // int random = (int) (Math.random() * 3);
            clouds[i] = new SXRSceneObject(sxrContext, mesh[random], texture[random]);
            clouds[i].getTransform().setPositionZ(-cloudDistance);
            sxrContext.getMainScene().addSceneObject(clouds[i]);
            clouds[i].getTransform().rotateByAxisWithPivot((float)
                    (Math.random() + CLOUD_OFFSET) * CLOUD_ANGLE, 1, 0, 0, 0, 0, 0);
            clouds[i].getTransform().rotateByAxisWithPivot(angle * i, 0, 1, 0, 0, 0, 0);
            clouds[i].getRenderData().setRenderingOrder(RenderingOrder.CLOUDS);
            SXRAnimation anim = new SXRRotationByAxisWithPivotAnimation(
                    clouds[i], CLOUD_ROTATION_DURATION, FULL_ROTATION, 0, 1, 0, 0, 0, 0);
            anim.start(sxrContext.getAnimationEngine());
        }
    }
}
