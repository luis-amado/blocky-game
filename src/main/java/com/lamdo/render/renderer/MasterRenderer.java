package com.lamdo.render.renderer;

import com.lamdo.entity.player.Camera;
import com.lamdo.render.Window;
import com.lamdo.render.shader.GUIShader;
import com.lamdo.render.shader.ShapeShader;
import com.lamdo.render.shader.VoxelShader;
import com.lamdo.util.Color;
import com.lamdo.util.MathUtil;
import com.lamdo.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL33.*;

public class MasterRenderer {

    private static final float FOV = 90;
    private static final float NEAR_PLANE = 0.01f;
    private static final float FAR_PLANE = 1000f;

    private static final Vector3f SKY_COLOR = Color.fromHexCode("#87ceeb");
    private static final Vector3f VOID_COLOR = Color.fromHexCode("#000000");

    private VoxelShader voxelShader = new VoxelShader();
    private VoxelRenderer voxelRenderer;

    private ShapeShader shapeShader = new ShapeShader();
    private ShapeRenderer shapeRenderer;

    private GUIShader guiShader = new GUIShader();
    private GUIRenderer guiRenderer;

    public MasterRenderer() {
        enableCulling();
        Matrix4f projectionMatrix = createProjectionMatrix();
        voxelRenderer = new VoxelRenderer(voxelShader, projectionMatrix);
        shapeRenderer = new ShapeRenderer(shapeShader, projectionMatrix);
        guiRenderer = new GUIRenderer(guiShader);
    }

    public void render(World world, Camera camera) {
        if(Window.wasWindowResized()) {
            Matrix4f projectionMatrix = createProjectionMatrix();
            voxelRenderer.updateProjectionMatrix(projectionMatrix);
            shapeRenderer.updateProjectionMatrix(projectionMatrix);
        }

        Matrix4f viewMatrix = MathUtil.createViewMatrix(camera);
        Vector3f skyColor = getSkyColor(camera);

        prepare(skyColor);

        voxelShader.start();
        voxelShader.loadViewMatrix(viewMatrix);
        voxelShader.loadSkyColor(skyColor);
        voxelRenderer.render(world);
        voxelShader.stop();

        shapeShader.start();
        shapeRenderer.render(camera);
        shapeShader.stop();

        guiRenderer.render();
    }

    public void cleanUp() {
        voxelShader.cleanUp();
        shapeShader.cleanUp();
    }

    private Vector3f getSkyColor(Camera camera) {
        float cameraHeight = camera.getPosition().y;
        float transitionPoint = 20;
        float skyColorAmount = MathUtil.clamp01(cameraHeight - transitionPoint + 0.5f);
        return Color.mix(VOID_COLOR, SKY_COLOR, skyColorAmount);
    }

    public void prepare(Vector3f skyColor) {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
    }

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    private Matrix4f createProjectionMatrix() {
        Matrix4f matrix = new Matrix4f();
        matrix.setPerspective((float)Math.toRadians(FOV), Window.getAspectRatio(), NEAR_PLANE, FAR_PLANE);
        return matrix;
    }

}
