package com.samsungxr.periodic;

import com.samsungxr.DefaultSXRTestActivity;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRDrawFrameListener;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.animation.SXRAnimationEngine;
import com.samsungxr.animation.SXRRepeatMode;
import com.samsungxr.animation.SXRScaleAnimation;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.periodic.SXRPeriodicEngine;
//import org.junit.Test;
//import org.junit.runner.RunWith;

/**
 * Created by nathan.a on 24/02/2015.
 */

public class SXRPeriodicEngineTest extends ActivityInstrumentationSXRf {

    private String DELAY_ERROR_MESSAGE = "delay must be >= 0";
    private String PERIOD_ERROR_MESSAGE = "period must be > 0";

     //@Test
     public void ignoretestCreateNewSXRPeriodicEngineTestWithNullContext() {
        try {
            SXRPeriodicEngine check;
            check=SXRPeriodicEngine.getInstance(null);
            fail("Cannot create SXRPeriodicEngine instance with null context");
        } catch (NullPointerException e) {
        }
    }


    //@Test
    public void testCreateNewSXRPeriodicEngineTestWithNullContext() {
        try {
            SXRPeriodicEngine check;
            check = SXRPeriodicEngine.getInstance(null);
            fail("Cannot create SXRPeriodicEngine instance with null context");
        }catch (NullPointerException e) {

        }
    }

    //@Test
    public void testRunAfterNegativeDelay() {
        float delay = -200f;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        try {
            periodicEngine.runAfter(new Runnable() {
                @Override
                public void run() {

                }
            }, delay);
            fail("Delay cannot be negative");
        } catch (IllegalArgumentException e) {
            assertEquals(DELAY_ERROR_MESSAGE, e.getMessage());
        }
    }

    //@Test
    public void testRunAfterZeroDelay() {
        float delay = 0;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        periodicEngine.runAfter(new Runnable() {
            @Override
            public void run() {

            }
        }, delay);
    }

    /**
     * valid values for decimal points are:
     * .0
     * .0625
     * .125
     * .1875
     * .25
     * .3125
     * .375
     * .4375
     * .5
     * .5625
     * .625
     * .6875
     * .75
     * .8125
     * .875
     * .9375
     */
    //@Test
    public void testRunAfterPositiveDelay() {
        //float delay = 28.125f;
        float delay = 28.122f;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        assertEquals(delay, periodicEngine.runAfter(new Runnable() {
            @Override
            public void run() {

            }
        }, delay).getCurrentWait(), 0.1);
    }

    //@Test
    public void testRunEveryNegativeDelay() {
        float delay = -200f;
        float period = 3.234f;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        try {
            periodicEngine.runEvery(new Runnable() {
                @Override
                public void run() {

                }
            }, delay, period);
            fail("Delay cannot be negative");
        } catch (IllegalArgumentException e) {
            assertEquals(DELAY_ERROR_MESSAGE, e.getMessage());
        }
    }

    //@Test
    public void testRunEveryZeroDelay() {
        float delay = 0;
        float period = 3.234f;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        periodicEngine.runEvery(new Runnable() {
            @Override
            public void run() {

            }
        }, delay, period);
    }

    //@Test
    public void testRunEveryPositiveDelay() {
        float delay = 28.124985f;
        float period = 3.234f;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        assertEquals(delay, periodicEngine.runEvery(new Runnable() {
            @Override
            public void run() {

            }
        }, delay, period).getCurrentWait(), 0.005);
    }

    //@Test
    public void testRunEveryNegativePeriod() {
        float delay = 0f;
        float period = -8.2564f;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        try {
            periodicEngine.runEvery(new Runnable() {
                @Override
                public void run() {

                }
            }, delay, period);
            fail("Period cannot be negative");
        } catch (IllegalArgumentException e) {
            assertEquals(PERIOD_ERROR_MESSAGE, e.getMessage());
        }
    }

    //@Test
    public void testRunEveryZeroPeriod() {
        float delay = 0f;
        float period = 0f;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        try {
            periodicEngine.runEvery(new Runnable() {
                @Override
                public void run() {

                }
            }, delay, period);
            fail("Delay cannot be zero");
        } catch (IllegalArgumentException e) {
            assertEquals(PERIOD_ERROR_MESSAGE, e.getMessage());
        }
    }

    //@Test
    //TODO: Remover assertEquals
    public void testRunEveryPositivePeriod() {
        float delay = 0f;
        float period = 12.25f;
        SXRPeriodicEngine periodicEngine = SXRPeriodicEngine.getInstance(TestDefaultSXRViewManager.mSXRContext);
        periodicEngine.runEvery(new Runnable() {
            @Override
            public void run() {

            }
        }, delay, period);


        periodicEngine.runEvery(new Runnable() {
            @Override
            public void run() {

            }
        }, delay, period);

        periodicEngine.runAfter((new Runnable() {
            @Override
            public void run() {}
        }),2.0f);

        SXRPeriodicEngine.PeriodicEvent periodicEvent = periodicEngine.runEvery(new Runnable() {
            @Override
            public void run() {

            }
        }, delay, period, 3);

        SXRPeriodicEngine.PeriodicEvent periodicEvent1 = periodicEngine.runEvery(new Runnable() {
            @Override
            public void run() {

            }
        }, delay, period, 1);

        SXRPeriodicEngine.PeriodicEvent periodicEvent2 = periodicEngine.runEvery(new Runnable() {
            @Override
            public void run() {

            }
        }, delay, period, 0);


        periodicEvent.getCurrentWait();
        periodicEvent.getRunCount();
        periodicEvent.runAfter(1.0f);
        periodicEvent.runEvery(1.0f, 1.0f);
        periodicEvent.runEvery(1.0f, 1.0f, 0);
        periodicEvent.runEvery(1.0f, 1.0f, 1);
        periodicEvent.runEvery(1.0f, 1.0f, new SXRPeriodicEngine.KeepRunning() {
            @Override
            public boolean keepRunning(SXRPeriodicEngine.PeriodicEvent event) {
                return true;
            }
        });
        periodicEvent.runEvery(1.0f, 1.0f, 2);
        periodicEvent.runEvery(1.0f, 1.0f, 2);
        periodicEvent.cancel();
    }


    public void testQueued() {

        final SXRSceneObject sxrSceneObject = new SXRSceneObject(TestDefaultSXRViewManager.mSXRContext);
        final SXRAnimationEngine animationEngine = TestDefaultSXRViewManager.mSXRContext.getAnimationEngine();
        Runnable pulse = new Runnable() {
            public void run() {
                new SXRScaleAnimation(sxrSceneObject.getTransform(), 0.5f, 2f) //
                        .setRepeatMode(SXRRepeatMode.PINGPONG) //
                        .start(animationEngine);
            }
        };
        TestDefaultSXRViewManager.mSXRContext.getPeriodicEngine().runEvery(pulse, 1f, 2f, 10);
        TestDefaultSXRViewManager.mSXRContext.getPeriodicEngine().runEvery(pulse, 1f, 2f, 10);

        SXRContext sxrContext = TestDefaultSXRViewManager.mSXRContext;
        sxrContext.registerDrawFrameListener(new SXRDrawFrameListener() {
            @Override
            public void onDrawFrame(float frameTime) {

            }
        });
    }

}
