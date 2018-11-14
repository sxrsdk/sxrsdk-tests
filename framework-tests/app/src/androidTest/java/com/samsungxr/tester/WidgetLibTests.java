package com.samsungxr.tester;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRAtlasInformation;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTextureParameters;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestableActivity;

import com.samsungxr.utility.RuntimeAssertion;
import com.samsungxr.widgetlib.content_scene.ContentSceneController;
import com.samsungxr.widgetlib.log.Log;
import com.samsungxr.widgetlib.main.SXRBitmapTexture;
import com.samsungxr.widgetlib.main.MainScene;
import com.samsungxr.widgetlib.main.WidgetLib;
import com.samsungxr.widgetlib.widget.FlingHandler;
import com.samsungxr.widgetlib.widget.GroupWidget;
import com.samsungxr.widgetlib.widget.Widget;
import com.samsungxr.widgetlib.widget.layout.Layout;
import com.samsungxr.widgetlib.widget.layout.OrientedLayout;
import com.samsungxr.widgetlib.widget.layout.basic.GridLayout;
import com.samsungxr.widgetlib.widget.layout.basic.LinearLayout;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.samsungxr.utility.Log.tag;

@RunWith(AndroidJUnit4.class)

public class WidgetLibTests {
    private static final String TAG = tag(WidgetLibTests.class);
    private SXRTestUtils mTestUtils;
    private Waiter mWaiter;
    private WidgetLib mWidgetLib;
    private ContentSceneController mContentSceneController;
    private MainScene mMainScene;
    private boolean mDoCompare = true;

    @Rule
    public ActivityTestRule<SXRTestableActivity> ActivityRule = new ActivityTestRule<>(SXRTestableActivity.class);

    public WidgetLibTests() {
        super();
    }
    @After
    public void tearDown() {
        SXRScene scene = mTestUtils.getMainScene();
        if (scene != null) {
            scene.clear();
        }
        if (WidgetLib.isInitialized()) {
            WidgetLib.destroy();
        }
    }

    @Before
    public void setUp() throws TimeoutException
    {
        SXRTestableActivity activity = ActivityRule.getActivity();
        mTestUtils = new SXRTestUtils(activity);
        mTestUtils.waitForOnInit();
        mWaiter = new Waiter();

        SXRContext ctx  = mTestUtils.getSxrContext();

        try {
            mWidgetLib = WidgetLib.init(ctx, "app_metadata.json");
            Log.init(ctx.getContext(), true);
            Log.enableSubsystem(Log.SUBSYSTEM.INPUT, true);
            Log.d(TAG, "WidgetLib is initialized!");
        } catch (Exception e) {
            Log.e(TAG, "WidgetLib cannot be initialized!");
            e.printStackTrace();
        }

        mWaiter.assertNotNull(mWidgetLib);

        mContentSceneController = WidgetLib.getContentSceneController();

        mMainScene = WidgetLib.getMainScene();
        mMainScene.adjustClippingDistanceForAllCameras();
        mMainScene.addNode(new BackgroundWidget(ctx));
    }

    class BackgroundWidget extends Widget {
        BackgroundWidget(SXRContext sxrContext) {
            super(sxrContext);
            setRenderingOrder(SXRRenderData.SXRRenderingOrder.BACKGROUND - 1);
            setCullFace(SXRRenderPass.SXRCullFaceEnum.None);
        }
    }

    static class SimpleQuad extends Widget {
        static Map<Integer, SXRTexture> textures = new HashMap<>();

        SimpleQuad(SXRContext sxrContext) {
            super(sxrContext);
        }

        void setSimpleColor(int color) {
            SXRTexture t = textures.get(color);
            if (t == null) {
                t = getSolidColorTexture(getSXRContext(), color);
                Log.d(TAG, "Texture is not found for color %d", color);
            } else {
                Log.d(TAG, "Texture is found for color %d", color);
            }
            setTexture(t);
        }

        private SXRTexture getSolidColorTexture(SXRContext sxrContext, int color) {
            SXRTexture texture = new ImmutableBitmapTexture(sxrContext, makeSolidColorBitmap(color));
            Log.d(TAG, "getSolidColorTexture(): caching texture for 0x%08X", new Object[]{color});
            return texture;
        }

        private static Bitmap makeSolidColorBitmap(int color) {
            Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            if (color != -1) {
                bitmap.eraseColor(color);
            }

            return bitmap;
        }

        private static class ImmutableBitmapTexture extends SXRBitmapTexture {
            public ImmutableBitmapTexture(SXRContext sxrContext, Bitmap bitmap) {
                super(sxrContext, bitmap);
            }

            public void setAtlasInformation(List<SXRAtlasInformation> atlasInformation) {
                this.onMutatingCall("setAtlasInformation");
            }

            public void updateTextureParameters(SXRTextureParameters textureParameters) {
                this.onMutatingCall("updateTextureParameters");
            }

            private void onMutatingCall(String method) {
                throw new RuntimeAssertion("%s(): mutating call on ImmutableBitmapTexture!", new Object[]{method});
            }
        }

    }

    @Test
    public void simpleContentScene() throws TimeoutException
    {
        mContentSceneController.goTo(new BaseContentScene(mTestUtils.getSxrContext()) {
            protected Widget createContent() {
                SimpleQuad c = new SimpleQuad(mSxrContext);
                c.setSimpleColor(Color.RED);
                return c;
            }

        });
        mTestUtils.waitForXFrames(5);
        mTestUtils.screenShot(getClass().getSimpleName(), "simpleContentScene", mWaiter, mDoCompare);
    }

    @Test
    public void simpleList() throws TimeoutException
    {
        mContentSceneController.goTo(new BaseContentScene(mTestUtils.getSxrContext()) {
            protected Widget createContent() {
                Log.init(mSxrContext.getContext(), true);
                GroupWidget c = new GroupWidget(mSxrContext, 0, 0);

                LinearLayout mainLayout = new LinearLayout();
                mainLayout.setOrientation(OrientedLayout.Orientation.VERTICAL);
                mainLayout.setDividerPadding(0.5f, Layout.Axis.Y);

                c.applyLayout(mainLayout);

                SimpleQuad  quad = new SimpleQuad(mSxrContext);
                quad.setSimpleColor(Color.RED);
                c.addChild(quad);

                quad = new SimpleQuad(mSxrContext);
                quad.setSimpleColor(Color.YELLOW);
                c.addChild(quad);

                quad = new SimpleQuad(mSxrContext);
                quad.setSimpleColor(Color.GREEN);
                c.addChild(quad);

                return c;
            }

        });
        mTestUtils.waitForXFrames(5);
        mTestUtils.screenShot(getClass().getSimpleName(), "simpleList", mWaiter, mDoCompare);
    }

    @Test
    public void simpleGrid() throws TimeoutException
    {
        mContentSceneController.goTo(new BaseContentScene(mTestUtils.getSxrContext()) {
            protected Widget createContent() {
                Log.init(mSxrContext.getContext(), true);
                GroupWidget c = new GroupWidget(mSxrContext, 0, 0);

                GridLayout mainLayout = new GridLayout(2, 2);
                mainLayout.setOrientation(OrientedLayout.Orientation.VERTICAL);
                mainLayout.setDividerPadding(0.5f, Layout.Axis.X);
                mainLayout.setDividerPadding(0.5f, Layout.Axis.Y);
                mainLayout.setVerticalGravity(LinearLayout.Gravity.TOP);
                mainLayout.setHorizontalGravity(LinearLayout.Gravity.LEFT);
                mainLayout.enableClipping(true);
                mainLayout.enableOuterPadding(true);

                c.setViewPortWidth(5.5f);
                c.setViewPortHeight(5.5f);
                c.applyLayout(mainLayout);

                SimpleQuad  quad = new SimpleQuad(mSxrContext);
                quad.setSimpleColor(Color.RED);
                c.addChild(quad);

                quad = new SimpleQuad(mSxrContext);
                quad.setSimpleColor(Color.YELLOW);
                c.addChild(quad);

                quad = new SimpleQuad(mSxrContext);
                quad.setSimpleColor(Color.GREEN);
                c.addChild(quad);

                quad = new SimpleQuad(mSxrContext);
                quad.setSimpleColor(Color.BLUE);
                c.addChild(quad);

                return c;
            }

        });
        mTestUtils.waitForXFrames(10);
        mTestUtils.screenShot(getClass().getSimpleName(), "simpleGrid", mWaiter, mDoCompare);
    }

    abstract class BaseContentScene implements ContentSceneController.ContentScene {
        public BaseContentScene(SXRContext sxrContext) {
            mSxrContext = sxrContext;

            mMainWidget = new GroupWidget(sxrContext, 0, 0);
            mMainWidget.setPositionZ(-5);
            mMainWidget.setName("MainWidget < " + TAG + " >");
        }

        abstract protected Widget createContent();

        protected void setContentWidget(Widget content) {
            if (mContent != null) {
                mMainWidget.removeChild(mContent);
            }

            mContent = content;
            if (mContent != null) {
                mMainWidget.addChild(mContent, 0);
            }
        }

        @Override
        public FlingHandler getFlingHandler() { return null; }

        @Override
        public String getName() {
            return TAG;
        }

        @Override
        public void show() {
            if (mFirstShow) {
                setContentWidget(createContent());
                mFirstShow = false;
            }

            mIsShowing = true;
            mMainScene.addNode(mMainWidget);
        }

        @Override
        public void hide() {
            mMainScene.removeNode(mMainWidget);
            mIsShowing = false;
        }

        protected boolean isShowing() {
            return  mIsShowing;
        }

        @Override
        public void onSystemDialogRemoved() {

        }

        @Override
        public void onSystemDialogPosted() {

        }

        @Override
        public void onProximityChange(boolean b) {

        }

        private boolean mIsShowing = false;
        protected final SXRContext mSxrContext;
        private Widget mContent;
        protected GroupWidget mMainWidget;
        protected boolean mFirstShow = true;

        private final String TAG = tag(BaseContentScene.class);
    }
}
