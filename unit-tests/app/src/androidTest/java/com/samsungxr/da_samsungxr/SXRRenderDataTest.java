package com.samsungxr.da_samsungxr;

import com.samsungxr.SXRLight;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRTestActivity;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.SXRRenderData;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by santhyago on 2/26/15.
 * Modified by Elidelson Cravlho on 09/09/2015
 */
public class SXRRenderDataTest extends ActivityInstrumentationSXRf {

    public SXRRenderDataTest() {
        super(SXRTestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testGetAlphaBlend() {
        //TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        //getActivity().setScript(viewManager, "sxr.xml");
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        //SXRNode object = TestDefaultSXRViewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        //assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        assertTrue(renderDataLeftScreen.getAlphaBlend());
    }

    public void testGetDepthTest() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        assertTrue(renderDataLeftScreen.getDepthTest());*/

        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        assertTrue(renderDataLeftScreen.getDepthTest());

    }

    public void testGetOffsetUnits() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        assertEquals(0.0f, renderDataLeftScreen.getOffsetUnits());*/

        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        assertEquals(0.0f, renderDataLeftScreen.getOffsetUnits());
    }

    public void testGetOffsetFactor() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        assertEquals(0.0f, renderDataLeftScreen.getOffsetFactor());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        assertEquals(0.0f, renderDataLeftScreen.getOffsetFactor());
    }

    public void testGetOffset() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        assertTrue(!renderDataLeftScreen.getOffset());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        assertTrue(!renderDataLeftScreen.getOffset());
    }

    public void testGetCullTest() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        assertTrue(renderDataLeftScreen.getCullTest());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        assertEquals(true, renderDataLeftScreen.getCullTest());
        assertNotNull(renderDataLeftScreen.getCullFace());
    }

    public void testGetRenderingOrder() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        assertTrue(renderDataLeftScreen.getRenderingOrder() > 0);*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        assertTrue(renderDataLeftScreen.getRenderingOrder() > 0);
    }

    public void testGetRenderMask() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setRenderMask(2);
        assertTrue(renderDataLeftScreen.getRenderMask() > 0);*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        assertTrue(renderDataLeftScreen.getRenderMask() > 0);
    }

    public void testSetAlphaBlend() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setAlphaBlend(false);
        assertTrue(!renderDataLeftScreen.getAlphaBlend());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setAlphaBlend(false);
        assertTrue(!renderDataLeftScreen.getAlphaBlend());
    }

    public void testSetDepthTest() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setDepthTest(false);
        assertTrue(!renderDataLeftScreen.getDepthTest());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setDepthTest(false);
        assertTrue(!renderDataLeftScreen.getDepthTest());
    }

    public void testSetOffsetUnits() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setOffsetUnits(2.0f);
        assertEquals(2.0f, renderDataLeftScreen.getOffsetUnits());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setOffsetUnits(2.0f);
        assertEquals(2.0f, renderDataLeftScreen.getOffsetUnits());
    }

    public void testSetOffsetFactor() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setOffsetFactor(2.0f);
        assertEquals(2.0f, renderDataLeftScreen.getOffsetFactor());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setOffsetFactor(2.0f);
        assertEquals(2.0f, renderDataLeftScreen.getOffsetFactor());
    }

    public void testSetOffset() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setOffset(true);
        assertTrue(renderDataLeftScreen.getOffset());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setOffset(true);
        assertTrue(renderDataLeftScreen.getOffset());
    }

    public void testSetCullTest() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setCullTest(false);
        assertTrue(!renderDataLeftScreen.getCullTest());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setCullTest(false);
        assertTrue(!renderDataLeftScreen.getCullTest());
        renderDataLeftScreen.setCullTest(true);
        assertTrue(renderDataLeftScreen.getCullTest());
        renderDataLeftScreen.setCullFace(SXRRenderPass.SXRCullFaceEnum.Back);
        renderDataLeftScreen.setCullFace(SXRRenderPass.SXRCullFaceEnum.Back,0);
    }

    public void testSetRenderingOrder() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setRenderingOrder(2);
        assertEquals(2, renderDataLeftScreen.getRenderingOrder());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setRenderingOrder(2);
        assertEquals(2, renderDataLeftScreen.getRenderingOrder());
    }

    public void testSetRenderMask() {
        /*TestDefaultSXRViewManager viewManager = new TestDefaultSXRViewManager();
        getActivity().setScript(viewManager, "sxr.xml");
        SXRNode object = viewManager.mSXRContext.getMainScene().getWholeNodes()[0];
        SXRRenderData renderDataLeftScreen = object.getRenderData();
        assertNotNull(viewManager);
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setRenderMask(2);
        assertEquals(2, renderDataLeftScreen.getRenderMask());*/
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setRenderMask(2);
        assertEquals(2, renderDataLeftScreen.getRenderMask());
    }

    public void testLight() {
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        SXRLight sxrLight = new SXRLight(TestDefaultSXRViewManager.mSXRContext);
        try {
            renderDataLeftScreen.enableLight();
        }catch (UnsupportedOperationException e){}
        try {
            renderDataLeftScreen.disableLight();
        }catch (UnsupportedOperationException e){}
        renderDataLeftScreen.setLight(sxrLight);
        assertNotNull(renderDataLeftScreen.getLight());
        renderDataLeftScreen.disableLight();
        assertTrue(!renderDataLeftScreen.isLightEnabled());
        renderDataLeftScreen.enableLight();
        assertTrue(renderDataLeftScreen.isLightEnabled());
    }

    public void testDrawMode() {
        assertNotNull(TestDefaultSXRViewManager.mSXRContext);
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.setDrawMode(1);
        assertEquals(1, renderDataLeftScreen.getDrawMode());
        try {
            renderDataLeftScreen.setDrawMode(20);
        }catch (IllegalArgumentException e){}
    }

    public void testMaterial() {
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        SXRMaterial sxrMaterial = new SXRMaterial(TestDefaultSXRViewManager.mSXRContext);
        renderDataLeftScreen.setMaterial(sxrMaterial);
        assertNotNull(renderDataLeftScreen.getMaterial());
        renderDataLeftScreen.setMaterial(sxrMaterial, 1);
        assertEquals(null, renderDataLeftScreen.getMaterial(1));
    }

    public void testMesh() {
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        SXRMesh sxrMesh = new SXRMesh(TestDefaultSXRViewManager.mSXRContext);
        renderDataLeftScreen.setMesh(sxrMesh);
        assertNotNull(renderDataLeftScreen.getMesh());
        Future<SXRMesh> sxrMeshFuture = new Future<SXRMesh>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public SXRMesh get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public SXRMesh get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
        renderDataLeftScreen.setMesh(sxrMeshFuture);

    }

    public void testGetPass() {
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        renderDataLeftScreen.getPass(0);
        renderDataLeftScreen.getPass(20);
    }

    public void testEyePointee() {
        SXRCubeNode sxrCubeNode = new SXRCubeNode(TestDefaultSXRViewManager.mSXRContext);
        TestDefaultSXRViewManager.mSXRContext.getMainScene().addNode(sxrCubeNode);
        SXRRenderData renderDataLeftScreen = sxrCubeNode.getRenderData();
        assertNotNull(renderDataLeftScreen);
        assertNotNull(renderDataLeftScreen.getMeshEyePointee());
    }


}
