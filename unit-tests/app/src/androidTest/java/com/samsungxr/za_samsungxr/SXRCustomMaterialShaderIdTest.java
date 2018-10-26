package com.samsungxr.za_samsungxr;

import com.samsungxr.viewmanager.TestDefaultSXRViewManager;
import com.samsungxr.ActivityInstrumentationSXRf;

import com.samsungxr.SXRCustomMaterialShaderId;
import com.samsungxr.SXRCustomPostEffectShaderId;
import com.samsungxr.SXRMaterialShaderManager;
import com.samsungxr.SXRPostEffectShaderManager;

/**
 * Created by j.elidelson on 5/20/2015.
 */
public class SXRCustomMaterialShaderIdTest extends ActivityInstrumentationSXRf {


    public void testCustomMaterialShaderId() {

        String s1="#version 330 core\n" +
                "layout(location = 0) in vec3 vertexPosition_modelspace;\n" +
                "void main(){\n" +
                "  gl_Position.xyz = vertexPosition_modelspace;\n" +
                "  gl_Position.w = 1.0;\n" +
                "}";
        String s2="#version 330 core\n" +
                "out vec3 color;\n" +
                " \n" +
                "void main(){\n" +
                "    color = vec3(1,0,0);\n" +
                "}";

        //s1="";
        //s2="";

        SXRMaterialShaderManager sxrMaterialShaderManager = TestDefaultSXRViewManager.mSXRContext.getMaterialShaderManager();
        SXRCustomMaterialShaderId sxrCustomMaterialShaderId = sxrMaterialShaderManager.addShader(s1,s2);
        assertNotNull("Resource was null: ", sxrCustomMaterialShaderId);
    }

    public void testCustomPostEffectShaderId() {

        String s1="#version 330 core\n" +
                "layout(location = 0) in vec3 vertexPosition_modelspace;\n" +
                "void main(){\n" +
                "  gl_Position.xyz = vertexPosition_modelspace;\n" +
                "  gl_Position.w = 1.0;\n" +
                "}";
        String s2="#version 330 core\n" +
                "out vec3 color;\n" +
                " \n" +
                "void main(){\n" +
                "    color = vec3(1,0,0);\n" +
                "}";

        //s1="";
        //s2="";

        SXRPostEffectShaderManager sxrPostEffectShaderManager = TestDefaultSXRViewManager.mSXRContext.getPostEffectShaderManager();
        SXRCustomPostEffectShaderId sxrCustomPostEffectShaderId = sxrPostEffectShaderManager.addShader(s1,s2);
        assertNotNull("Resource was null: ", sxrCustomPostEffectShaderId);
    }


}
