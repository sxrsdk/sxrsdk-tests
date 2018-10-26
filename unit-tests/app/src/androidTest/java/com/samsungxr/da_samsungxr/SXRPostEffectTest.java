package com.samsungxr.da_samsungxr;

import android.os.Environment;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.BoundsValues;
import com.samsungxr.CustomPostEffectShaderManager;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRPostEffect;
import com.samsungxr.SXRPostEffectShaderId;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.SXRTexture;
import com.samsungxr.animation.SXRAnimationTest;
import com.samsungxr.tests.R;
import com.samsungxr.utility.Log;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Future;

/**
 * Push sea_env.jpg to /sdcard/Android/data/com.example.samsungxr.samsungxrapp_02/cache/
 */
public class SXRPostEffectTest extends ActivityInstrumentationSXRf {
    private final String TAG = SXRPostEffectTest.class.getSimpleName();

    private static final float DEFAULT_R = 1f;
    private static final float DEFAULT_G = 1f;
    private static final float DEFAULT_B = 1f;
    private static final float ANIM_DURATION = 1.5f;

    private static SXRContext mSXRContext = null;

    private CustomPostEffectShaderManager shaderManager;

    public SXRPostEffectTest() {
        super(SXRTestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mSXRContext = TestDefaultSXRViewManager.mSXRContext;
        shaderManager = SXRAnimationTest.makeCustomPostEffectShaderManager(mSXRContext);
    }

    public void testPostEffectConstructor(){
        Log.d(TAG, "Starting testPostEffectConstructor!");

        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        assertNotNull(postEffect);

    }

    public void testSetFloat() {
        Log.d(TAG, "Starting testSetFloat");

        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        float value = 1.5f;
        String key = "ratio_r";
        postEffect.setFloat(key, value);
        assertEquals(value, postEffect.getFloat(key));

    }

    public void testFloatBounds(){
        Log.d(TAG, "Starting testFloatBounds");

        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        String key = "ratio_r";

        int size = BoundsValues.getFloatList().size();
        for (int i = 0; i<size; i++){
            postEffect.setFloat(key, BoundsValues.getFloatList().get(i));
            assertEquals(BoundsValues.getFloatList().get(i), postEffect.getFloat(key));
        }

    }

    public void testSizeVec3(){
        Log.d(TAG, "Starting testSizeVec3");

        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        float[] value = {0.395f, 0.789f, 0.200f};
        String key = "ratio_r";
        postEffect.setVec3(key, value[0], value[1], value[2]);
        float[] ratioR = postEffect.getVec3(key);

        assertEquals(value.length, ratioR.length);

    }


    public void testSetVec3(){
        Log.d(TAG, "Starting testSetVec3");

        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        float[] value = {0.395f, 0.789f, 0.200f};
        String key = "ratio_r";
        postEffect.setVec3(key, value[0], value[1], value[2]);
        float[] ratioR = postEffect.getVec3(key);

        for(int i=0; i<value.length; i++)
            assertEquals(value[i], ratioR[i]);


    }

    public void testSizeVec2(){
        Log.d(TAG, "Starting testSizeVec2");
        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        float[] value = {0.395f, 0.789f};
        String key = "ratio_r";
        postEffect.setVec2(key, value[0], value[1]);
        float[] ratioR = postEffect.getVec2(key);

        assertEquals(value.length, ratioR.length);

    }


    public void testSetVec2(){
        Log.d(TAG, "Starting testSetVec2");
        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        float[] value = {0.395f, 0.789f};
        String key = "ratio_r";
        postEffect.setVec2(key, value[0], value[1]);
        float[] ratioR = postEffect.getVec2(key);

        for(int i=0; i<value.length; i++)
            assertEquals(value[i], ratioR[i]);


    }

    public void testSizeVec4(){
        Log.d(TAG, "Starting testSizeVec4");
        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        float[] value = {0.395f, 0.789f, 0.200f, 0.070f};
        String key = "ratio_r";
        postEffect.setVec4(key, value[0], value[1], value[2], value[3]);
        float[] ratioR = postEffect.getVec4(key);

        assertEquals(value.length, ratioR.length);

    }


    public void testSetVec4(){
        Log.d(TAG, "Starting testSetVec4");
        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());

        float[] value = {0.395f, 0.789f, 0.200f, 0.070f};
        String key = "ratio_r";
        postEffect.setVec4(key, value[0], value[1], value[2], value[3]);
        float[] ratioR = postEffect.getVec4(key);

        for(int i=0; i<value.length; i++)
            assertEquals(value[i], ratioR[i]);


    }


    public void testSetMat4(){
        Log.d(TAG, "Starting testSetVec4");
        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());
        String key = "ratio_r";
        postEffect.setMat4(key, 1.0f,1.0f,1.0f,1.0f,2.0f,2.0f,2.0f,2.0f,3.0f,3.0f,3.0f,3.0f,4.0f,4.0f,4.0f,4.0f);
    }

    public void testTextureNull() {
        Log.d(TAG, "Starting testTextureNull");
        if (mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());
        final String resourcePath = mSXRContext.getActivity().getExternalCacheDir().toString() + "/sea_env.jpg";
        try {
            File file = new File(resourcePath);
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(file);
            SXRTexture texture = mSXRContext.loadTexture(sxrAndroidResource);
            String key = "texture";

            postEffect.setTexture(key, texture);

            assertNotNull(postEffect.getTexture(key));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void testGetTexture() {
        Log.d(TAG, "Starting testGetTexture");
        if (mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());
        final String resourcePath = mSXRContext.getActivity().getExternalCacheDir().toString() + "/sea_env.jpg";
        try {
            File file = new File(resourcePath);
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(file);
            SXRTexture texture = mSXRContext.loadTexture(sxrAndroidResource);
            String key = "texture";

            postEffect.setTexture(key, texture);

            assertEquals(texture, postEffect.getTexture(key));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void testMainTextureNull(){
        Log.d(TAG, "Starting testMainTextureNull");
        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());
        final String resourcePath = mSXRContext.getActivity().getExternalCacheDir().toString() + "/sea_env.jpg";
        try {
            File file = new File(resourcePath);
            SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(file);
            SXRTexture texture = mSXRContext.loadTexture(sxrAndroidResource);

            postEffect.setMainTexture(texture);

            assertNotNull(postEffect.getMainTexture());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void testGetMainTexture(){
        Log.d(TAG, "Starting testGetTexture");
        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        Future<SXRTexture> futureTexture = TestDefaultSXRViewManager.mSXRContext.loadFutureTexture(new SXRAndroidResource(TestDefaultSXRViewManager.mSXRContext, R.drawable.gearvr_logo));
        //sxrMaterial.setMainTexture(futureTexture);

        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());
        //String resourcePath = Environment.getExternalStorageDirectory()+ "sea_env.jpg";
        //String resourcePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/sea_env.jpg";
        //try {
            //File file = new File(resourcePath);
            //SXRAndroidResource sxrAndroidResource = new SXRAndroidResource(file);
            //SXRTexture texture = mSXRContext.loadTexture(sxrAndroidResource);
            postEffect.setMainTexture(futureTexture);
            postEffect.getMainTexture();
            postEffect.getTexture("0");
            postEffect.setTexture("MAIN_TEXTURE",futureTexture);
            //postEffect.setMainTexture(postEffect.getTexture("MAIN_TEXTURE"));
        //} catch(){};

    }


    public void testGetAetShaderType(){
        Log.d(TAG, "Starting testGetTexture");
        if(mSXRContext == null) {
            try {
                setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());
        SXRPostEffectShaderId sxrPostEffectShaderId = postEffect.getShaderType();
        assertNotNull(sxrPostEffectShaderId);
        postEffect.setShaderType(sxrPostEffectShaderId);
    }


    public void testSXRPostEffectShaderType(){

        assertEquals("r",SXRPostEffect.SXRPostEffectShaderType.ColorBlend.R);
        assertEquals("g",SXRPostEffect.SXRPostEffectShaderType.ColorBlend.G);
        assertEquals("b",SXRPostEffect.SXRPostEffectShaderType.ColorBlend.B);
        assertEquals("factor",SXRPostEffect.SXRPostEffectShaderType.ColorBlend.FACTOR);
        assertNotNull(SXRPostEffect.SXRPostEffectShaderType.ColorBlend.ID);
        assertNotNull(SXRPostEffect.SXRPostEffectShaderType.HorizontalFlip.ID);
    }



//    public void testShaderTypeNull(){
//        Log.d(TAG, "Starting testShaderTypeNull");
//
//        SXRPostEffect postEffect = new SXRPostEffect(mSXRContext, shaderManager.getShaderId());
//        SXRTexture texture = mSXRContext.loadTexture("sea_env.jpg");
//
//        postEffect.setShaderType(new SXRPostEffectShaderId);
//
//        postEffect.setMainTexture(texture);
//
//        assertNotNull(postEffect.getMainTexture());
//
//    }

}
