package com.samsungxr.scene_object;

import android.content.Context;
import android.view.Surface;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.samsungxr.DefaultSXRTestActivity;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.scene_objects.SXRWebViewSceneObject;

/**
 * Created by Douglas on 2/27/15.
 */
public class SXRWebViewSceneObjectTest extends ActivityInstrumentationSXRf {

    public SXRWebViewSceneObjectTest() {
        super(SXRTestActivity.class);
    }

    public void testCreateSXRWebViewSceneObject() {

      SXRWebViewSceneObject sxrWebViewSceneObject = createWebViewObject(TestDefaultSXRViewManager.mSXRContext);
      assertNotNull(sxrWebViewSceneObject);
      try {
          for (int i = 1; i < 35; i++) sxrWebViewSceneObject.onDrawFrame(0.1f);
      }
      catch (IllegalStateException e){}
      catch (Surface.OutOfResourcesException t){}
    }

    private SXRWebViewSceneObject createWebViewObject(SXRContext sxrContext) {
        //WebView webView = getActivity().getWebView();
        SXRWebViewSceneObject webObject = new SXRWebViewSceneObject(sxrContext,
                8.0f, 4.0f, DefaultSXRTestActivity.webView);
        //webObject.setName("web view object");
        //webObject.getRenderData().getMaterial().setOpacity(1.0f);
        //webObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);

        return webObject;
    }

}
