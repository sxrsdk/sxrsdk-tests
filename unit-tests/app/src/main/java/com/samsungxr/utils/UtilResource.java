package com.samsungxr.utils;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;

/**
 * Created by danielnogueira on 4/16/15.
 */
public class UtilResource {

    public static SXRAndroidResource androidResource(SXRContext context, int resource){
       SXRAndroidResource result = null;

        if(context != null ){

            result  = new SXRAndroidResource(context, resource);
        }

        return result;
    }
}
