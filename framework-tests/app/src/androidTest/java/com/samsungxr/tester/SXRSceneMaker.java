package com.samsungxr.tester;

import android.opengl.GLES30;
import android.util.ArrayMap;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRDirectLight;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRPointLight;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRSpotLight;
import com.samsungxr.SXRTexture;
import com.samsungxr.SXRTransform;
import com.samsungxr.nodes.SXRCubeNode;
import com.samsungxr.nodes.SXRSphereNode;
import com.samsungxr.unittestutils.SXRTestUtils;
import com.samsungxr.utility.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*

scene:
{
    id: "scene name"
    bgcolor: {red: [0.0-1.0], green: [0.0-1.0], blue: [0.0-1.0], alpha: [0.0-1.0]}
    lights: [...]
    objects: [...]
}

light:
{
        type: ("spot" | "directional" | "point")
        castshadow: ("true" | "false")
        position: {x: 0, y: 0, z: 0}
        rotation: {w: 0, x: 0, y: 0, z: 0}
        ambientintensity: {r: [0.0-1.0], g: [0.0-1.0], b: [0.0-1.0], a: [0.0-1.0]}
        diffuseintensity:  {r: [0.0-1.0], g: [0.0-1.0], b: [0.0-1.0], a: [0.0-1.0]}
        specularintensity:  {r: [0.0-1.0], g: [0.0-1.0], b: [0.0-1.0], a: [0.0-1.0]}
        innerconeangle: [0.0-9.0]+
        outerconeangle: [0.0-9.0]+
}

object:
{
    name: "object name"
    geometry: {...}
    material: {..}
    position: {x: [0.0-9.0]+, y: [0.0-9.0]+, z: [0.0-9.0]+}
    rotation: {x: [0.0-9.0]+, y: [0.0-9.0]+, z: [0.0-9.0]+}
    scale: {x: [0.0-9.0]+, y: [0.0-9.0]+, z: [0.0-9.0]+}
    renderconfig: {...}
}

geometry:
{
    type: ("quad" | "cylinder" | "sphere" | "cube" | "polygon")
    width: [0.0-9.0]+
    height: [0.0-9.0]+
    depth: [0.0-9.0]+
    radius: [0.0-9.0]+
    vertices: [[0.0-9.0]+, ...]
    normals: [[0.0-9.0]+, ...]
    texcoords: [[0.0-9.0]+, ...]
    triangles:  [[0-9]+, ...]
    bone_weights:  [[0.0-9.0]+, ...]
    bone_indices:  [[0-9]+, ...]
}

material:
{
    shader: ("phong" | "texture" | "cube")
    color: {r: [0.0-1.0], g: [0.0-1.0], b: [0.0-1.0], a: [0.0-1.0]}
    textures:[...]
}

texture:
{
    id: "texture id"
    name: ("u_texture" | "diffuseTexture")
    type: ("bitmap", "cube", "compressed")
    resourceid: [0-9]+
}

renderconfig:
{
   drawmode: (GL_TRIANGLES | GL_TRIANGLE_STRIP | GL_TRIANGLE_FAN
              | GL_LINES | GL_LINE_STRIP | GL_LINE_LOOP)
}
 */

/**
 * Parse a JSON object to build the equivalent SXRScene.
 */
public class SXRSceneMaker {
    private static SXRTestUtils Tester = null;
    private  Map<Integer, SXRTexture> TextureCache = new HashMap<Integer, SXRTexture>();

    private static class RGBAColor
    {
        public final float r;
        public final float g;
        public final float b;
        public final float a;

        public RGBAColor(float r, float g, float b, float a){
            this. r = r;
            this. g = g;
            this. b = b;
            this. a = a;
        }
    }

    public SXRSceneMaker(SXRTestUtils tester)
    {
        Tester = tester;
    }

    private static float[] jsonToFloatArray(JSONArray jsonArray) throws JSONException {
        float[] array = new float[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            array[i] = (float) jsonArray.getDouble(i);
        }

        return array;
    }

    private static int[] jsonToIntArray(JSONArray jsonArray) throws JSONException {
        int[] array = new int[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            array[i] = jsonArray.getInt(i);
        }

        return array;
    }

    private SXRTexture createBitmapTexture(SXRContext sxrContext, JSONObject jsonTexture) throws JSONException {

        int resourceId = jsonTexture.optInt("resource_id", -1);
        if (resourceId == -1)
        {
            return null;
        }
        if (TextureCache.containsKey(resourceId))
        {
            return TextureCache.get(resourceId);
        }
        return loadTexture(sxrContext, resourceId);
    }

    public SXRTexture loadTexture(SXRContext sxrContext, int resourceId) {
        if (TextureCache.containsKey(resourceId))
        {
            return TextureCache.get(resourceId);
        }
        SXRAndroidResource resource = new SXRAndroidResource(sxrContext, resourceId);
        TextureEventHandler waitForTextureLoad = new TextureEventHandler(Tester, 1);
        sxrContext.getEventReceiver().addListener(waitForTextureLoad);
        SXRTexture tex = sxrContext.getAssetLoader().loadTexture(resource);
        TextureCache.put(resourceId, tex);
        Tester.waitForAssetLoad();
        sxrContext.getEventReceiver().removeListener(waitForTextureLoad);
        return tex;
    }

    /*
     {
      r: 1, g: 1, b: 1, a: 0
     }
     */
    private static RGBAColor getColorCoordinates(JSONObject jsonObject) throws
            JSONException {

        float cordR = (float) jsonObject.optDouble("r", 0.0f);
        float cordG = (float) jsonObject.optDouble("g", 0.0f);
        float cordB = (float) jsonObject.optDouble("b", 0.0f);
        float cordA = (float) jsonObject.optDouble("a", 1.0f);

        RGBAColor coordinates = new RGBAColor(cordR, cordG, cordB, cordA);
        return coordinates;
    }

    /*
     {
      id: "texture id"
      name: ("u_texture" | "diffuseTexture")
      type: ("bitmap", "cube", "compressed")
      resourceid: [0-9]+
     }
     */
    private SXRTexture createTexture(SXRContext sxrContext, JSONObject jsonTexture) throws JSONException {
        SXRTexture texture = null;

        String type = jsonTexture.optString("type");
        if (type.equals("compressed")) {
        } else if (type.equals("cube")) {
        } else {
            // type.equals("bitmap") || type.isEmpty()
            texture = createBitmapTexture(sxrContext, jsonTexture);
        }

        return texture;
    }

    /*
     {
      shader: ("phong" | "texture" | "cube")
      color: {r: [0.0-1.0], g: [0.0-1.0], b: [0.0-1.0], a: [0.0-1.0]}
      textures:[...]
     }
     */
    private SXRMaterial createMaterial(SXRContext sxrContext,
                                       ArrayMap<String, SXRTexture> textures,
                                       JSONObject jsonObject) throws JSONException {
        SXRMaterial material;
        String shader_type = jsonObject.optString("shader", "texture");

        if (shader_type.equals("phong")) {
            material = new SXRMaterial(sxrContext, SXRMaterial.SXRShaderType.Phong.ID);
        } else if (shader_type.equals("cube")) {
            material = new SXRMaterial(sxrContext, SXRMaterial.SXRShaderType.Cubemap.ID);
        } else {
            material = new SXRMaterial(sxrContext, SXRMaterial.SXRShaderType.Texture.ID);
        }

        JSONArray jsonArray = jsonObject.optJSONArray("textures");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTexture = jsonArray.getJSONObject(i);

                String sharedId = jsonTexture.optString("shared");
                SXRTexture texture = sharedId.isEmpty() ? createTexture(sxrContext, jsonTexture) :
                        textures.get(sharedId);

                String texture_name = jsonTexture.optString("name");
                if (texture_name.isEmpty()) {
                    if (material.getShaderType() == SXRMaterial.SXRShaderType.Phong.ID) {
                        material.setTexture("diffuseTexture", texture);
                    } else {
                        material.setMainTexture(texture);
                    }
                } else {
                    material.setTexture(texture_name, texture);
                }
            }
        }

        JSONObject jsonColor = jsonObject.optJSONObject("color");
        if (jsonColor != null) {
            RGBAColor color = getColorCoordinates(jsonColor);
            if (material.getShaderType() == SXRMaterial.SXRShaderType.Texture.ID) {
                material.setColor(color.r, color.g, color.b);
            } else {
                material.setDiffuseColor(color.r, color.g, color.b, color.a);
            }
        }

        return material;
    }

    private void setScale(SXRTransform transform, JSONObject jsonScale)
            throws JSONException {

        float x = (float) jsonScale.optDouble("x", 1.0f);
        float y = (float) jsonScale.optDouble("y", 1.0f);
        float z = (float) jsonScale.optDouble("z", 1.0f);

        transform.setScale(x, y, z);
    }

    private void setPosition(SXRTransform transform, JSONObject jsonPosition)
            throws JSONException {

        float x = (float) jsonPosition.optDouble("x", 0.0f);
        float y = (float) jsonPosition.optDouble("y", 0.0f);
        float z = (float) jsonPosition.optDouble("z", 0.0f);

        transform.setPosition(x, y, z);
    }

    private void setRotation(SXRTransform transform, JSONObject jsonRotation)
            throws JSONException {

        float x = (float) jsonRotation.optDouble("x", 0.0f);
        float y = (float) jsonRotation.optDouble("y", 0.0f);
        float z = (float) jsonRotation.optDouble("z", 0.0f);
        float w = (float) jsonRotation.optDouble("w", 0.0f);

        transform.setRotation(w, x, y, z);
    }

    /*
     {
      position: {x: 0, y: 0, z: 0}
      scale: {x: 0, y: 0, z: 0}
      rotation: {w: 0, x: 0, y: 0, z: 0}
     }
     */
    private void setTransform(SXRTransform transform, JSONObject jsonObject) throws JSONException {

        JSONObject jsonPosition = jsonObject.optJSONObject("position");
        if (jsonPosition != null) {
            setPosition(transform, jsonPosition);
        }

        JSONObject jsonRotation = jsonObject.optJSONObject("rotation");
        if (jsonRotation != null) {
            setRotation(transform, jsonRotation);
        }

        JSONObject jsonScale = jsonObject.optJSONObject("scale");
        if (jsonScale != null) {
            setScale(transform, jsonScale);
        }
    }

    /*
     {
      drawmode: (GL_TRIANGLES | GL_TRIANGLE_STRIP | GL_TRIANGLE_FAN
              | GL_LINES | GL_LINE_STRIP | GL_LINE_LOOP)
     }
     */
    private void setRenderConfig(SXRRenderData renderData, JSONObject jsonConfig)
            throws JSONException {
        renderData.setDrawMode(jsonConfig.optInt("drawmode", GLES30.GL_TRIANGLES));
    }

    private void setPointLightIntensity(SXRPointLight light, JSONObject jsonLight)
            throws JSONException {

        JSONObject jsonAmbientIntensity = jsonLight.optJSONObject("ambientintensity");
        if (jsonAmbientIntensity != null) {
            RGBAColor ambientCoord = getColorCoordinates(jsonAmbientIntensity);
            light.setAmbientIntensity(ambientCoord.r, ambientCoord.g, ambientCoord.b,
                    ambientCoord.a);
        }

        JSONObject jsonDiffuseIntensity = jsonLight.optJSONObject("diffuseintensity");
        if (jsonDiffuseIntensity != null) {
            RGBAColor diffuseCoord = getColorCoordinates(jsonDiffuseIntensity);
            light.setDiffuseIntensity(diffuseCoord.r, diffuseCoord.g, diffuseCoord.b,
                    diffuseCoord.a);
        }

        JSONObject jsonSpecularIntensity = jsonLight.optJSONObject("specularintensity");
        if (jsonSpecularIntensity != null) {
            RGBAColor specularCoord = getColorCoordinates(jsonSpecularIntensity);
            light.setSpecularIntensity(specularCoord.r, specularCoord.g, specularCoord.b,
                    specularCoord.a);
        }
    }

    private void setDirectLightIntensity(SXRDirectLight light, JSONObject jsonLight)
            throws JSONException {

        JSONObject jsonAmbientIntensity = jsonLight.optJSONObject("ambientintensity");
        if (jsonAmbientIntensity != null) {
            RGBAColor ambientCoord = getColorCoordinates(jsonAmbientIntensity);
            light.setAmbientIntensity(ambientCoord.r, ambientCoord.g, ambientCoord.b,
                    ambientCoord.a);
        }

        JSONObject jsonDiffuseIntensity = jsonLight.optJSONObject("diffuseintensity");
        if (jsonDiffuseIntensity != null) {
            RGBAColor diffuseCoord = getColorCoordinates(jsonDiffuseIntensity);
            light.setDiffuseIntensity(diffuseCoord.r, diffuseCoord.g, diffuseCoord.b,
                    diffuseCoord.a);
        }

        JSONObject jsonSpecularIntensity = jsonLight.optJSONObject("specularintensity");
        if (jsonSpecularIntensity != null) {
            RGBAColor specularCoord = getColorCoordinates(jsonSpecularIntensity);
            light.setSpecularIntensity(specularCoord.r, specularCoord.g, specularCoord.b,
                    specularCoord.a);
        }
    }

    private void setLightConeAngle(SXRSpotLight light, JSONObject jsonLight)
            throws JSONException {

        float innerAngle = (float) jsonLight.optDouble("innerconeangle");
        if (!Double.isNaN(innerAngle)) {
            light.setInnerConeAngle(innerAngle);
        }

        float outAngle = (float) jsonLight.optDouble("outerconeangle");
        if (!Double.isNaN(outAngle)) {
            light.setOuterConeAngle(outAngle);
        }
    }

    private SXRNode createSpotLight(SXRContext sxrContext, JSONObject jsonLight)
            throws JSONException {

        SXRNode lightObj = new SXRNode(sxrContext);
        SXRSpotLight spotLight = new SXRSpotLight(sxrContext);
        setPointLightIntensity(spotLight, jsonLight);
        setLightConeAngle(spotLight, jsonLight);
        lightObj.attachLight(spotLight);

        return lightObj;
    }

    private SXRNode createDirectLight(SXRContext sxrContext, JSONObject jsonLight)
            throws JSONException {

        SXRNode lightObj = new SXRNode(sxrContext);
        lightObj.setName("lightNode");
        SXRDirectLight directLight = new SXRDirectLight(sxrContext);
        setDirectLightIntensity(directLight, jsonLight);
        lightObj.attachLight(directLight);

        return lightObj;
    }

    private SXRNode createPointLight(SXRContext sxrContext, JSONObject jsonLight)
            throws JSONException {

        SXRNode lightObj = new SXRNode(sxrContext);
        SXRPointLight pointLight = new SXRPointLight(sxrContext);
        setPointLightIntensity(pointLight, jsonLight);
        lightObj.attachLight(pointLight);

        return lightObj;
    }

    /*
     {
      type: ("spot" | "directional" | "point")
      castshadow: ("true" | "false")
      position: {x: 0, y: 0, z: 0}
      rotation: {w: 0, x: 0, y: 0, z: 0}
      ambientintensity: {r: [0.0-1.0], g: [0.0-1.0], b: [0.0-1.0], a: [0.0-1.0]}
      diffuseintensity:  {r: [0.0-1.0], g: [0.0-1.0], b: [0.0-1.0], a: [0.0-1.0]}
      specularintensity:  {r: [0.0-1.0], g: [0.0-1.0], b: [0.0-1.0], a: [0.0-1.0]}
      innerconeangle: [0.0-9.0]+
      outerconeangle: [0.0-9.0]+
     }
     */
    private SXRNode createLight(SXRContext sxrContext, JSONObject jsonLight) throws
            JSONException {

        SXRNode light = null;
        String type = jsonLight.optString("type");

        if (type.equals("spot")) {
            light = createSpotLight(sxrContext, jsonLight);
        } else if (type.equals("directional")) {
            light = createDirectLight(sxrContext, jsonLight);
        } else if (type.equals("point")) {
            light = createPointLight(sxrContext, jsonLight);
        }

        if (light != null) {
            light.getLight().setCastShadow(jsonLight.optBoolean("castshadow"));
            setTransform(light.getTransform(), jsonLight);
        }

        return light;
    }

    /*
     {
      vertices: [0, ... n]
      normals: [0, ... n]
      texcoords: [[0, ... n], [0, ... n]]
      triangles: [0, ... n]
     }
     */
    private SXRMesh createPolygonMesh(SXRContext sxrContext, JSONObject jsonObject)
            throws JSONException {
        String descriptor = "float3 a_position";

        if (jsonObject.has( "texcoords")) {
            descriptor += " float2 a_texcoord";
        }

        if (jsonObject.has("normals")) {
            descriptor += " float3 a_normal";
        }

        SXRMesh mesh = new SXRMesh(sxrContext, descriptor);

        if (jsonObject.has("vertices")) {
            mesh.setVertices(jsonToFloatArray(jsonObject.optJSONArray("vertices")));
        }

        if (jsonObject.has("normals")) {
            mesh.setNormals(jsonToFloatArray(jsonObject.optJSONArray("normals")));
        }

        if (jsonObject.has("triangles")) {
            mesh.setIndices(jsonToIntArray(jsonObject.optJSONArray("triangles")));
        }

        if (jsonObject.has("texcoords")) {
            JSONArray jsonCorrds = jsonObject.optJSONArray("texcoords");
            if (jsonCorrds != null) {
                for (int i = 0; i < jsonCorrds.length(); i++) {
                    mesh.setTexCoords(jsonToFloatArray(jsonCorrds.optJSONArray(i)), i);
                }
            }
        }

        return mesh;
    }

    private SXRNode createQuad(SXRContext sxrContext, JSONObject jsonObject)
            throws JSONException {
        float width = 1.0f;
        float height = 1.0f;
        String descriptor = "float3 a_position float2 a_texcoord float3 a_normal";

        if (jsonObject != null) {
            width = (float) jsonObject.optDouble("width", width);
            height = (float) jsonObject.optDouble("height", height);
            descriptor = jsonObject.optString("descriptor", descriptor);
        }

        return new SXRNode(sxrContext,
                SXRMesh.createQuad(sxrContext, descriptor, width, height));
    }

    private SXRNode createPolygon(SXRContext sxrContext, JSONObject jsonObject)
            throws JSONException {
        return new SXRNode(sxrContext, createPolygonMesh(sxrContext, jsonObject));
    }

    private SXRNode createCube(SXRContext sxrContext, JSONObject jsonObject)
            throws JSONException {
        String descriptor = "float3 a_position float2 a_texcoord float3 a_normal";

        float width = (float) jsonObject.optDouble("width", 1.0f);
        float height = (float) jsonObject.optDouble("height", 1.0f);
        float depth = (float) jsonObject.optDouble("depth", 1.0f);
        boolean facing_out = jsonObject.optBoolean("facing_out", true);
        descriptor = jsonObject.optString("descriptor", descriptor);

        SXRMesh mesh = SXRCubeNode.createCube(sxrContext, descriptor, facing_out,
                new org.joml.Vector3f(width, height, depth));

        return new SXRNode(sxrContext, mesh);
    }

    private SXRNode createCylinder(SXRContext sxrContext, JSONObject jsonObject)
            throws JSONException {
        return null;
    }

    private SXRNode createSphere(SXRContext sxrContext, JSONObject jsonObject)
            throws JSONException {
        float radius = (float) jsonObject.optDouble("radius", 1.0f);
        boolean facing_out = jsonObject.optBoolean("facing_out", true);

        return new SXRSphereNode(sxrContext, facing_out, radius);
    }

    /*
     {
      type: ("quad" | "cylinder" | "sphere" | "cube" | "polygon")
      width: [0.0-9.0]+
      height: [0.0-9.0]+
      depth: [0.0-9.0]+
      radius: [0.0-9.0]+
      vertices: [[0.0-9.0]+, ...]
      normals: [[0.0-9.0]+, ...]
      texcoords: [[0.0-9.0]+, ...]
      triangles:  [[0-9]+, ...]
      bone_weights:  [[0.0-9.0]+, ...]
      bone_indices:  [[0-9]+, ...]
     }
     */
    private SXRNode createGeometry(SXRContext sxrContext, JSONObject jsonObject)
            throws JSONException {
        SXRNode sceneObject = null;

        String type = jsonObject.optString("type");

        if (type.equals("polygon")) {
            sceneObject = createPolygon(sxrContext, jsonObject);
        } else if (type.equals("cube")) {
            sceneObject = createCube(sxrContext, jsonObject);
            sceneObject.setName("cubeSceneObj");
        } else if (type.equals("cylinder")) {
            sceneObject = createCylinder(sxrContext, jsonObject);
        } else if (type.equals("sphere")) {
            sceneObject = createSphere(sxrContext, jsonObject);
        } else {
            sceneObject = createQuad(sxrContext, jsonObject);
        }

        return sceneObject;
    }

    /*
     {
      name: "object name"
      geometry: {...}
      material: {..}
      position: {x: [0.0-9.0]+, y: [0.0-9.0]+, z: [0.0-9.0]+}
      rotation: {x: [0.0-9.0]+, y: [0.0-9.0]+, z: [0.0-9.0]+}
      scale: {x: [0.0-9.0]+, y: [0.0-9.0]+, z: [0.0-9.0]+}
     }
     */
    private SXRNode createChildObject(SXRContext sxrContext,
                                                    ArrayMap<String, SXRTexture> textures,
                                                    ArrayMap<String, SXRMaterial> materials,
                                                    JSONObject jsonObject) throws JSONException {

        JSONObject jsonGeometry = jsonObject.optJSONObject("geometry");
        SXRNode child = (jsonGeometry != null) ? createGeometry(sxrContext, jsonGeometry) :
                createQuad(sxrContext, null);

        String objectName = jsonObject.optString("name");
        if (!objectName.isEmpty()){
            child.setName(objectName);
        }

        setTransform(child.getTransform(), jsonObject);

        JSONObject jsonMaterial = jsonObject.optJSONObject("material");
        if (jsonMaterial != null) {
            String sharedId = jsonMaterial.optString("shared");
            SXRMaterial material = sharedId.isEmpty() ?
                    createMaterial(sxrContext, textures, jsonMaterial) :  materials.get(sharedId);

            child.getRenderData().setMaterial(material);
        }

        JSONObject jsonRenderConf = jsonObject.optJSONObject("renderconfig");

        if (jsonRenderConf != null) {
            setRenderConfig(child.getRenderData(), jsonRenderConf);
        }

        return child;
    }

    private void addChildrenObjects(SXRContext sxrContext, SXRNode root,
                                           ArrayMap<String, SXRTexture> textures,
                                           ArrayMap<String, SXRMaterial> materials,
                                           JSONArray jsonChildren) throws JSONException {
        for (int i = 0; i < jsonChildren.length(); i++) {
            SXRNode child = createChildObject(sxrContext,
                    textures, materials, jsonChildren.getJSONObject(i));
            root.addChildObject(child);
        }
    }

    private void addChildrenLights(SXRContext sxrContext, SXRNode root, JSONArray
            jsonChildren) throws JSONException {
        for (int i = 0; i < jsonChildren.length(); i++) {
            SXRNode child = createLight(sxrContext, jsonChildren.getJSONObject(i));
            root.addChildObject(child);
        }
    }

    private void createShareables(SXRContext sxrContext,
                                         ArrayMap<String, SXRTexture> textures,
                                         ArrayMap<String, SXRMaterial> materials,
                                         JSONObject jsonObject) throws JSONException {
        // Shared textures
        JSONArray texArray = jsonObject.optJSONArray("textures");
        if (texArray != null) {
            for (int i = 0; i < texArray.length(); i++) {
                String id = texArray.optJSONObject(i).optString("id");
                if (!id.isEmpty()) {
                    textures.put(id, createTexture(sxrContext, texArray.getJSONObject(i)));
                }
            }
        }

        // Shared materials
        JSONArray matArray = jsonObject.optJSONArray("materials");
        if (matArray != null) {
            for (int i = 0; i < matArray.length(); i++) {
                String id = matArray.optJSONObject(i).optString("id");
                if (!id.isEmpty()) {
                    materials.put(id,
                            createMaterial(sxrContext, textures, matArray.getJSONObject(i)));
                }
            }
        }
    }



    public SXRNode makeScene(SXRContext sxrContext, SXRScene scene, JSONObject jsonScene,
                                 JSONObject jsonShareables) throws JSONException {
        ArrayMap<String, SXRTexture> textures = new ArrayMap<>();
        ArrayMap<String, SXRMaterial> materials = new ArrayMap<>();
        SXRNode root  = new SXRNode(sxrContext);
        root.setName("root");
        Log.d("SceneMaker", jsonScene.toString());

        if (jsonShareables != null) {
            createShareables(sxrContext, textures, materials, jsonShareables);
        }

        createShareables(sxrContext, textures, materials, jsonScene);

        JSONObject jsonBgColor = jsonScene.optJSONObject("bgcolor");
        if (jsonBgColor != null) {
            RGBAColor bgcolorCoord = getColorCoordinates(jsonBgColor);
            scene.setBackgroundColor(bgcolorCoord.r, bgcolorCoord.g, bgcolorCoord.b,
                    bgcolorCoord.a);
        }

        JSONArray jsonChildrenLights = jsonScene.optJSONArray("lights");
        if (jsonChildrenLights != null) {
            addChildrenLights(sxrContext, root, jsonChildrenLights);
        }

        JSONArray jsonChildrenObjects = jsonScene.optJSONArray("objects");
        if (jsonChildrenObjects != null) {
            addChildrenObjects(sxrContext, root, textures, materials,
                    jsonChildrenObjects);
        }
        return root;
    }


    /*
     {
      id: "scene name"
      bgcolor: {red: [0.0-1.0], green: [0.0-1.0], blue: [0.0-1.0], alpha: [0.0-1.0]}
      lights: [...]
      objects: [...]
     }
     */
    public void makeScene(SXRContext sxrContext, SXRScene scene,
                          JSONObject jsonScene) throws JSONException {
        SXRNode root = makeScene(sxrContext, scene, jsonScene, null);
        scene.addNode(root);
    }

    public SXRNode makeScene(SXRTestUtils tester, JSONObject jsonScene)
            throws JSONException
    {
        SXRScene scene = tester.getMainScene();
        SXRNode root = makeScene(tester.getSxrContext(), scene, jsonScene, null);
        ChangeScene sceneChanger = new ChangeScene(scene);
        sceneChanger.setRoot(root);
        return root;
    }

    public SXRNode makeScene(SXRTestUtils tester, JSONObject jsonScene, Runnable callback)
            throws JSONException
    {
        SXRScene scene = tester.getMainScene();
        SXRNode root = makeScene(tester.getSxrContext(), scene, jsonScene, null);
        ChangeScene sceneChanger = new ChangeScene(scene, callback);
        sceneChanger.setRoot(root);
        return root;
    }

    static class ChangeScene implements Runnable
    {
        private SXRScene mScene;
        private SXRNode mRoot;
        private Runnable mCallback = null;

        public ChangeScene(SXRScene scene)
        {
            mScene = scene;
        }

        public ChangeScene(SXRScene scene, Runnable callback)
        {
            mScene = scene;
            mCallback = callback;
        }

        public void setRoot(SXRNode root)
        {
            mRoot = root;
            mScene.getSXRContext().runOnGlThread(this);
        }

        public void run()
        {
            SXRNode root = mScene.getNodeByName("root");

            if (root != null)
            {
                mScene.clear();
            }
            mScene.addNode(mRoot);
            if (mCallback != null)
            {
                mCallback.run();
            }
        }
    }
}
