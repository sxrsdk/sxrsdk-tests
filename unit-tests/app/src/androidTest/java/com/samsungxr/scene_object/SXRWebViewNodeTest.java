package com.samsungxr.node;

import android.content.Context;
import android.view.Surface;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.samsungxr.DefaultSXRTestActivity;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.nodes.SXRWebViewNode;

/**
 * Created by Douglas on 2/27/15.
 */
public class SXRWebViewNodeTest extends ActivityInstrumentationSXRf {

    public SXRWebViewNodeTest() {
        super(SXRTestActivity.class);
    }

    public void testCreateSXRWebViewNode() {

      SXRWebViewNode sxrWebViewNode = createWebViewObject(TestDefaultSXRViewManager.mSXRContext);
      assertNotNull(sxrWebViewNode);
      try {
          for (int i = 1; i < 35; i++) sxrWebViewNode.onDrawFrame(0.1f);
      }
      catch (IllegalStateException e){}
      catch (Surface.OutOfResourcesException t){}
    }

    private SXRWebViewNode createWebViewObject(SXRContext sxrContext) {
        //WebView webView = getActivity().getWebView();
        SXRWebViewNode webObject = new SXRWebViewNode(sxrContext,
                8.0f, 4.0f, DefaultSXRTestActivity.webView);
        //webObject.setName("web view object");
        //webObject.getRenderData().getMaterial().setOpacity(1.0f);
        //webObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);

        return webObject;
    }

}
