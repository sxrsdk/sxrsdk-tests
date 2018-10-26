package com.samsungxr.viewmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRMaterial.SXRShaderType;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRScript;
import com.samsungxr.SXRTexture;
import com.samsungxr.tests.R;
import com.samsungxr.animation.SXRAnimation;
import com.samsungxr.animation.SXRAnimationEngine;
import com.samsungxr.animation.SXRPositionAnimation;
import com.samsungxr.animation.SXRRepeatMode;
import com.samsungxr.nodes.SXRSphereNode;

public class LODTestScript extends SXRScript {
    
    private List<SXRAnimation> animations = new ArrayList<SXRAnimation>();
    private SXRAnimationEngine animationEngine;
    
    @Override
    public void onInit(SXRContext sxrContext) throws IOException {
        animationEngine = sxrContext.getAnimationEngine();
        
        SXRScene scene = sxrContext.getNextMainScene(new Runnable() {
            @Override
            public void run() {
                for(SXRAnimation animation : animations) {
                    animation.start(animationEngine);
                }
            }
        });
        
        scene.setFrustumCulling(true);
        


        Future<SXRTexture> redFutureTexture = sxrContext.loadFutureTexture(new SXRAndroidResource(sxrContext, R.drawable.red));
        Future<SXRTexture> greenFutureTexture = sxrContext.loadFutureTexture(new SXRAndroidResource(sxrContext, R.drawable.green));
        Future<SXRTexture> blueFutureTexture = sxrContext.loadFutureTexture(new SXRAndroidResource(sxrContext, R.drawable.blue));

        SXRSphereNode sphereHighDensity = new SXRSphereNode(sxrContext);
        setupObject(sxrContext, sphereHighDensity, redFutureTexture);
        sphereHighDensity.setLODRange(0.0f, 5.0f);
        scene.addNode(sphereHighDensity);
        
        SXRSphereNode sphereMediumDensity = new SXRSphereNode(sxrContext, 9, 9,
                true, new SXRMaterial(sxrContext));
        setupObject(sxrContext, sphereMediumDensity, greenFutureTexture); 
        sphereMediumDensity.setLODRange(5.0f, 9.0f);
        scene.addNode(sphereMediumDensity);
        
        SXRSphereNode sphereLowDensity = new SXRSphereNode(sxrContext, 6, 6,
                true, new SXRMaterial(sxrContext));
        setupObject(sxrContext, sphereLowDensity, blueFutureTexture);   
        sphereLowDensity.setLODRange(9.0f, Float.MAX_VALUE);
        scene.addNode(sphereLowDensity);
        
    }
    
    private void setupObject(SXRContext sxrContext, SXRNode object, Future<SXRTexture> futureTexture) {
        object.getTransform().setPosition(0,  0,  -3.0f);
        SXRMaterial unlit = new SXRMaterial(sxrContext, SXRShaderType.Texture.ID);
        unlit.setMainTexture(futureTexture);
        object.getRenderData().setMaterial(unlit);
        setupAnimation(object);  
    }
    
    private void setupAnimation(SXRNode object) {
        SXRAnimation animation = new SXRPositionAnimation(object, 2.0f, 0.0f, 0.0f, -10.0f);
        animation.setRepeatMode(SXRRepeatMode.PINGPONG).setRepeatCount(-1);
        animations.add(animation);
    }
    
    @Override
    public void onStep() {
    }
    
}

