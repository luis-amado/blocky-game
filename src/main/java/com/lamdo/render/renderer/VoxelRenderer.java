package com.lamdo.render.renderer;

import com.lamdo.entity.player.Camera;
import com.lamdo.render.Window;
import com.lamdo.render.model.RawModel;
import com.lamdo.render.model.ShapeModel;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.shader.VoxelShader;
import com.lamdo.world.World;
import com.lamdo.world.Chunk;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL33.*;

public class VoxelRenderer {

    private VoxelShader shader;
    private ShapeModel chunkBoundaries = new ShapeModel();

    public VoxelRenderer(VoxelShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(World world) {
        bindTexture();
        for (Chunk chunk : world.getChunks()) {
            if (chunk.meshApplied()) {
                prepareVoxelModel(chunk.getVoxelModel());
                glDrawElements(GL_TRIANGLES, chunk.getVoxelModel().getModel().indexCount(), GL_UNSIGNED_INT, 0);
            }
        }
        unbindVoxelModel();
    }

    public void updateProjectionMatrix(Matrix4f projectionMatrix) {
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    private void bindTexture() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, VoxelModel.getTexture());
    }

    private void prepareVoxelModel(VoxelModel voxelModel) {
        RawModel model = voxelModel.getModel();
        glBindVertexArray(model.vaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // also load the transformation of the voxel
        Matrix4f transformMat = new Matrix4f();
        transformMat.translate(voxelModel.getPosition());
        shader.loadTransformationMatrix(transformMat);
    }

    private void unbindVoxelModel() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
