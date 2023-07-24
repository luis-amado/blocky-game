package com.lamdo.render.renderer;

import com.lamdo.entity.player.Camera;
import com.lamdo.render.Window;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.shader.VoxelShader;
import com.lamdo.util.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL33.*;

public class MasterRenderer {

    private static final float FOV = 90;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private static final Vector3f SKY_COLOR = new Vector3f(0, 0.5f, 0.5f);

    private Matrix4f projectionMatrix;

    private VoxelShader voxelShader = new VoxelShader();
    private VoxelRenderer voxelRenderer;

    public MasterRenderer() {
        enableCulling();
        createProjectionMatrix();
        voxelRenderer = new VoxelRenderer(voxelShader, projectionMatrix);
    }

    public void render(VoxelModel model, Camera camera) {
        Matrix4f viewMatrix = MathUtil.createViewMatrix(camera);
        prepare();
        voxelShader.start();
        voxelShader.loadViewMatrix(viewMatrix);
        voxelRenderer.render(model);
        voxelShader.stop();
    }

    public void cleanUp() {
        voxelShader.cleanUp();
    }

    public void prepare() {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1);
    }

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    private void createProjectionMatrix() {
        Matrix4f matrix = new Matrix4f();
        this.projectionMatrix = matrix.setPerspective(FOV, Window.getAspectRatio(), NEAR_PLANE, FAR_PLANE);
    }

}