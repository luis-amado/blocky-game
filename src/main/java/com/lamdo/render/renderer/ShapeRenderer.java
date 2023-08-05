package com.lamdo.render.renderer;

import com.lamdo.entity.player.Camera;
import com.lamdo.physics.BoundingBox;
import com.lamdo.render.Loader;
import com.lamdo.render.model.RawModel;
import com.lamdo.render.model.ShapeModel;
import com.lamdo.render.shader.ShapeShader;
import com.lamdo.util.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL33.*;

public class ShapeRenderer {

    private ShapeShader shader;
    private static List<ShapeModel> shapes = new ArrayList<ShapeModel>();

    private static ShapeModel debugCrosshair = new ShapeModel();

    public ShapeRenderer(ShapeShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Camera camera) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for(ShapeModel shape: shapes) {
            prepareModel(shape, camera);
            glDrawArrays(shape.glDrawMode(), 0, shape.model().indexCount());
        }
        unbindModel();
        shapes.clear();
    }

    private void prepareModel(ShapeModel shape, Camera camera) {
        RawModel model = shape.model();
        glBindVertexArray(model.vaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        Matrix4f viewMatrix;
        // static shapes dont move with respect to the camera
        if(shape.isStatic()) {
            viewMatrix = MathUtil.create3rdPersonStaticViewMatrix(camera, 1f);
            glDisable(GL_DEPTH_TEST);
        } else {
            viewMatrix = MathUtil.createViewMatrix(camera);
            glEnable(GL_DEPTH_TEST);
        }

        shader.loadViewMatrix(viewMatrix);
        Matrix4f transformMat = new Matrix4f();
        transformMat.translate(shape.position());
        shader.loadTransformationMatrix(transformMat);
    }

    private void unbindModel() {
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private static void processShape(ShapeModel s) {
        shapes.add(s);
    }

    public void updateProjectionMatrix(Matrix4f projectionMatrix) {
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public static void drawDebugCrosshair() {
        float length = 0.05f;
        float[] positions = new float[] {
                0, 0, 0, length, 0, 0,
                0, 0, 0, 0, length, 0,
                0, 0, 0, 0, 0, length
        };
        float[] colors = new float[] {
                1, 0, 0, 1, 1, 0, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1,
                0, 0, 1, 1, 0, 0, 1, 1
        };
        debugCrosshair.update(new Vector3f(0, 0, 0), GL_LINES, true);
        Loader.updateShapeVAO(debugCrosshair.model(), positions, colors);
        processShape(debugCrosshair);
    }

    public static void drawBoxCenteredBottom(ShapeModel shape, Vector3f position, BoundingBox box, Vector4f color) {
        float hw = box.getWidth() / 2f;
        float h = box.getHeight();
        float zprev = 0.001f;
        float[] positions = new float[] {
                //12 lines need to be drawn
                //4 bottom lines
                -hw, zprev, -hw, -hw, zprev, hw,
                -hw, zprev, hw, hw, zprev, hw,
                hw, zprev, hw, hw, zprev, -hw,
                -hw, zprev, -hw, hw, zprev, -hw,

                //4 vertical lines
                -hw, zprev, hw, -hw, h, hw,
                hw, zprev, hw, hw, h, hw,
                hw, zprev, -hw, hw, h, -hw,
                -hw, zprev, -hw, -hw, h, -hw,

                //4 top lines
                -hw, h, -hw, -hw, h, hw,
                -hw, h, hw, hw, h, hw,
                hw, h, hw, hw, h, -hw,
                -hw, h, -hw, hw, h, -hw
        };
        float[] colors = new float[] {
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w
        };
        shape.update(position, GL_LINES, false);
        Loader.updateShapeVAO(shape.model(), positions, colors);
        processShape(shape);
    }

    public static void drawBox(ShapeModel shape, Vector3f position, BoundingBox box, Vector4f color) {
        float z = -0.002f;
        float w = box.getWidth()+0.002f;
        float h = box.getHeight()+0.002f;
        float[] positions = new float[] {
                //12 lines need to be drawn
                //4 bottom lines
                z, z, z, z, z, w,
                z, z, w, w, z, w,
                w, z, w, w, z, z,
                z, z, z, w, z, z,

                //4 vertical lines
                z, z, w, z, h, w,
                w, z, w, w, h, w,
                w, z, z, w, h, z,
                z, z, z, z, h, z,

                //4 top lines
                z, h, z, z, h, w,
                z, h, w, w, h, w,
                w, h, w, w, h, z,
                z, h, z, w, h, z
        };
        float[] colors = new float[] {
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w,
                color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w, color.x, color.y, color.z, color.w
        };
        shape.update(position, GL_LINES, false);
        Loader.updateShapeVAO(shape.model(), positions, colors);
        processShape(shape);
    }

}
