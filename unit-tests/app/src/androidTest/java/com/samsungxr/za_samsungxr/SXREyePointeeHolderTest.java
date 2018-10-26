package com.samsungxr.za_samsungxr;

import com.samsungxr.ActivityInstrumentationSXRf;
import com.samsungxr.SXREyePointee;
import com.samsungxr.SXREyePointeeHolder;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRMeshEyePointee;
import com.samsungxr.SXRNode;
import com.samsungxr.viewmanager.TestDefaultSXRViewManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by j.elidelson on 8/31/2015.
 */
public class SXREyePointeeHolderTest extends ActivityInstrumentationSXRf {

    public void testConstructor(){
        SXREyePointeeHolder sxrEyePointeeHolder = new SXREyePointeeHolder(TestDefaultSXRViewManager.mSXRContext);
        assertNotNull(sxrEyePointeeHolder);
    }

    public void testaddPointee(){
        SXREyePointeeHolder sxrEyePointeeHolder = new SXREyePointeeHolder(TestDefaultSXRViewManager.mSXRContext);
        Future<SXREyePointee> sxrEyePointeeFuture = new Future<SXREyePointee>() {
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
            public SXREyePointee get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public SXREyePointee get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
        sxrEyePointeeHolder.addPointee(sxrEyePointeeFuture);
        SXRMesh sxrMesh = new SXRMesh(TestDefaultSXRViewManager.mSXRContext);
        SXRMeshEyePointee sxrEyePointee = new SXRMeshEyePointee(sxrMesh);
        sxrEyePointeeHolder.removePointee(sxrEyePointee);
    }

    public void testsetgetEnable(){
        SXREyePointeeHolder sxrEyePointeeHolder = new SXREyePointeeHolder(TestDefaultSXRViewManager.mSXRContext);

        sxrEyePointeeHolder.setEnable(true);
        assertEquals(true,sxrEyePointeeHolder.getEnable());
    }

    public void testgetHit(){
        SXREyePointeeHolder sxrEyePointeeHolder = new SXREyePointeeHolder(TestDefaultSXRViewManager.mSXRContext);

        float gh[] = sxrEyePointeeHolder.getHit();
        assertNotNull(gh);
    }

    public void ignoretestgetOwnerObject(){
        SXREyePointeeHolder sxrEyePointeeHolder = new SXREyePointeeHolder(TestDefaultSXRViewManager.mSXRContext);

        SXRNode sxrNode = sxrEyePointeeHolder.getOwnerObject();
        assertNotNull(sxrNode);
    }

}
