package com.samsungxr.tester;

import android.content.Context;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRShader;
import com.samsungxr.utility.TextFile;

public class VertexColorShader extends SXRShader
{
    private static String fragTemplate = null;
    private static String vtxTemplate = null;

    public VertexColorShader(SXRContext sxrcontext)
    {
        super("", "", "float3 a_position float2 a_texcoord float4 a_color", GLSLESVersion.VULKAN);
        Context context = sxrcontext.getContext();
        fragTemplate = TextFile.readTextFile(context, R.raw.fragmentshader);
        vtxTemplate = TextFile.readTextFile(context, R.raw.vertexshader);
        setSegment("FragmentTemplate", fragTemplate);
        setSegment("VertexTemplate", vtxTemplate);
    }
}
