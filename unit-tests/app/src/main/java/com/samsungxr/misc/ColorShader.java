package com.samsungxr.misc;
/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.samsungxr.SXRContext;
import com.samsungxr.SXRCustomMaterialShaderId;
import com.samsungxr.SXRMaterialMap;
import com.samsungxr.SXRMaterialShaderManager;

public class ColorShader {

    public static final String COLOR_KEY = "color";

    private static final String VERTEX_SHADER = "attribute vec4 a_position;\n"
            + "uniform mat4 u_mvp;\n" //
            + "void main() {\n" //
            + "  gl_Position = u_mvp * a_position;\n" //
            + "}\n";

    private static final String FRAGMENT_SHADER = "precision mediump float;\n"
            + "uniform vec4 u_color;\n" //
            + "void main() {\n" //
            + "  gl_FragColor = u_color;\n" //
            + "}\n";

    private SXRCustomMaterialShaderId mShaderId;
    private SXRMaterialMap mCustomShader = null;

    public ColorShader(SXRContext sxrContext) {
        final SXRMaterialShaderManager shaderManager = sxrContext
                .getMaterialShaderManager();
        mShaderId = shaderManager.addShader(VERTEX_SHADER, FRAGMENT_SHADER);
        mCustomShader = shaderManager.getShaderMap(mShaderId);
        mCustomShader.addUniformVec4Key("u_color", COLOR_KEY);
    }

    public SXRCustomMaterialShaderId getShaderId() {
        return mShaderId;
    }
}
