/*
 * Copyright 2016. Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsungxr.tester;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import net.jodah.concurrentunit.Waiter;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRMesh;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.unittestutils.SXRTestUtils.OnInitCallback;
import com.samsungxr.unittestutils.SXRTestableActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class SXRContextTests {
    private static final String TAG = SXRContextTests.class.getSimpleName();
    private static final int NUM_NORMALS_IN_QUAD = 12;
    private SXRTestUtils sxrTestUtils;

    public SXRContextTests() {
        super();
    }

    @Rule
    public ActivityTestRule<SXRTestableActivity> mActivityRule = new
            ActivityTestRule<SXRTestableActivity>(SXRTestableActivity.class);

    @Before
    public void setUp() throws Exception {
        sxrTestUtils = new SXRTestUtils(mActivityRule.getActivity());
    }

    @Test(timeout = SXRTestUtils.TEST_TIMEOUT)
    public void contextNonNull() throws TimeoutException {
        final Waiter waiter = new Waiter();
        // Execute tests in onInit
        sxrTestUtils.setOnInitCallback(new OnInitCallback() {
            @Override
            public void onInit(SXRContext sxrContext) {
                waiter.assertNotNull(sxrContext.getEventManager());
                waiter.assertNotNull(sxrContext.getEventReceiver());
                waiter.assertNotNull(sxrContext.getInputManager());
                waiter.assertNotNull(sxrContext.getAssetLoader());
                waiter.assertNotNull(sxrContext.getAnimationEngine());
                waiter.assertNotNull(sxrContext.getActivity());
                waiter.resume();
            }
        });
        waiter.await(SXRTestUtils.TEST_TIMEOUT);
    }

    @Test(timeout = SXRTestUtils.TEST_TIMEOUT)
    public void createQuad() throws TimeoutException {
        SXRContext sxrContext = sxrTestUtils.waitForOnInit();
        // Execute tests after onInit is called.
        SXRMesh mesh = sxrContext.createQuad(2.0f, 2.0f);
        sxrTestUtils.waitForXFrames(3);
        Assert.assertTrue(mesh.getNormals().length == NUM_NORMALS_IN_QUAD);
    }
}
