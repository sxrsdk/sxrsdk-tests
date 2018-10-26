package com.samsungxr.sample.LodTest;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRAssetLoader;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRLODGroup;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMaterial.SXRShaderType;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.SXRTexture;
import com.samsungxr.animation.SXRAnimation;
import com.samsungxr.animation.SXRAnimationEngine;
import com.samsungxr.animation.SXRPositionAnimation;
import com.samsungxr.animation.SXRRepeatMode;
import com.samsungxr.scene_objects.SXRSphereSceneObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class LODTestMain extends SXRMain {

    private List<SXRAnimation> animations = new ArrayList<SXRAnimation>();
    private SXRAnimationEngine animationEngine;

    @Override
    public void onInit(SXRContext sxrContext) throws IOException {
        animationEngine = sxrContext.getAnimationEngine();

        SXRScene scene = sxrContext.getMainScene();

        scene.setFrustumCulling(true);

        final SXRAssetLoader assetLoader = sxrContext.getAssetLoader();
        Future<SXRTexture> redFutureTexture = assetLoader.loadFutureTexture(new SXRAndroidResource(sxrContext, R.drawable.red));
        Future<SXRTexture> greenFutureTexture = assetLoader.loadFutureTexture(new SXRAndroidResource(sxrContext, R.drawable.green));
        Future<SXRTexture> blueFutureTexture = assetLoader.loadFutureTexture(new SXRAndroidResource(sxrContext, R.drawable.blue));

        SXRSphereSceneObject root = new SXRSphereSceneObject(sxrContext);

        SXRSphereSceneObject sphereHighDensity = new SXRSphereSceneObject(sxrContext);
        sphereHighDensity.setName("sphereHighDensity");
        setupObject(sxrContext, sphereHighDensity, redFutureTexture);
        root.addChildObject(sphereHighDensity);

        SXRSphereSceneObject sphereMediumDensity = new SXRSphereSceneObject(sxrContext, 9, 9,
                true, new SXRMaterial(sxrContext));
        sphereMediumDensity.setName("sphereMediumDensity");
        setupObject(sxrContext, sphereMediumDensity, greenFutureTexture);
        root.addChildObject(sphereMediumDensity);

        SXRSphereSceneObject sphereLowDensity = new SXRSphereSceneObject(sxrContext, 6, 6,
                true, new SXRMaterial(sxrContext));
        sphereLowDensity.setName("sphereLowDensity");
        setupObject(sxrContext, sphereLowDensity, blueFutureTexture);
        root.addChildObject(sphereLowDensity);

        final SXRLODGroup lodGroup = new SXRLODGroup(sxrContext);
        lodGroup.addRange(9, sphereLowDensity);
        lodGroup.addRange(0, sphereHighDensity);
        root.attachComponent(lodGroup);
        lodGroup.addRange(5, sphereMediumDensity);

        scene.addSceneObject(root);

        for(SXRAnimation animation : animations) {
            animation.start(animationEngine);
        }
    }

    private void setupObject(SXRContext sxrContext, SXRSceneObject object, Future<SXRTexture> futureTexture) {
        object.getTransform().setPosition(0,  0,  -3.0f);
        SXRMaterial unlit = new SXRMaterial(sxrContext, SXRShaderType.Texture.ID);
        unlit.setMainTexture(futureTexture);
        object.getRenderData().setMaterial(unlit);
        setupAnimation(object);
    }

    private void setupAnimation(SXRSceneObject object) {
        SXRAnimation animation = new SXRPositionAnimation(object, 2.0f, 0.0f, 0.0f, -10.0f);
        animation.setRepeatMode(SXRRepeatMode.PINGPONG).setRepeatCount(-1);
        animations.add(animation);
    }
}
