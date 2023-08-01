package com.lamdo;

import com.lamdo.entity.player.Camera;
import com.lamdo.entity.player.Player;
import com.lamdo.gui.UIBlock;
import com.lamdo.gui.constraints.AspectRatioConstraint;
import com.lamdo.gui.constraints.PixelConstraint;
import com.lamdo.gui.constraints.RelativeConstraint;
import com.lamdo.gui.constraints.RelativePlusPixelsConstraint;
import com.lamdo.render.Loader;
import com.lamdo.render.Window;
import com.lamdo.render.model.RawModel;
import com.lamdo.render.model.VoxelModel;
import com.lamdo.render.renderer.GUIRenderer;
import com.lamdo.render.renderer.MasterRenderer;
import com.lamdo.render.shader.VoxelShader;
import com.lamdo.world.Chunk;
import com.lamdo.world.World;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL33.*;

public class Main {

    public static void main(String[] args) {

        // Create the game window
        Window window = new Window();

        // Set the texture that the voxels will use
        VoxelModel.setTexture(Loader.loadTextureAtlas("/textures/atlas.png"), 4);
        World world = new World();

        MasterRenderer renderer = new MasterRenderer();
        Player player = new Player(new Vector3d(0, 45, 0), world);
        Camera camera = new Camera(player);

        world.generateTerrains();
        world.generateMeshes();

        UIBlock hotbar = new UIBlock()
                .width(new PixelConstraint(300))
                .centerX()
                .bottom(new PixelConstraint(20))
                .height(new PixelConstraint(50));

        UIBlock crosshair1 = new UIBlock()
                .width(new PixelConstraint(2))
                .height(new PixelConstraint(10))
                .centerX()
                .centerY();

        UIBlock crosshair2 = new UIBlock()
                .width(new PixelConstraint(10))
                .height(new PixelConstraint(2))
                .centerX()
                .centerY();

        UIBlock main = new UIBlock()
                .right(new PixelConstraint(20))
                .height(new RelativeConstraint(0.5f))
                .width(new AspectRatioConstraint(1))
                .top(new PixelConstraint(20));

        UIBlock child1 = new UIBlock(main)
                .right(new PixelConstraint(10))
                .height(new RelativePlusPixelsConstraint(0.5f, -15))
                .width(new RelativePlusPixelsConstraint(0.5f, -15))
                .top(new PixelConstraint(10))
                .color(new Vector4f(0.5f, 0.5f, 0.5f, 1));
        UIBlock child2 = new UIBlock(main)
                .left(new PixelConstraint(10))
                .height(new RelativePlusPixelsConstraint(0.5f, -15))
                .width(new RelativePlusPixelsConstraint(0.5f, -15))
                .top(new PixelConstraint(10))
                .color(new Vector4f(0.5f, 0.5f, 0.5f, 1));
        UIBlock child3 = new UIBlock(main)
                .left(new PixelConstraint(10))
                .height(new RelativePlusPixelsConstraint(0.5f, -15))
                .width(new RelativePlusPixelsConstraint(0.5f, -15))
                .bottom(new PixelConstraint(10))
                .color(new Vector4f(0.5f, 0.5f, 0.5f, 1));
        UIBlock child4 = new UIBlock(main)
                .right(new PixelConstraint(10))
                .height(new RelativePlusPixelsConstraint(0.5f, -15))
                .width(new RelativePlusPixelsConstraint(0.5f, -15))
                .bottom(new PixelConstraint(10))
                .color(new Vector4f(0.5f, 0.5f, 0.5f, 1));

        while(!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 0, 0, 1);
//            GUIRenderer.processUIBlock(hotbar);
//            GUIRenderer.processUIBlock(crosshair1);
//            GUIRenderer.processUIBlock(crosshair2);
            GUIRenderer.processUIBlock(child1);
            GUIRenderer.processUIBlock(child2);
            GUIRenderer.processUIBlock(child3);
            GUIRenderer.processUIBlock(child4);
            GUIRenderer.processUIBlock(main);

            player.move();

            renderer.render(world, camera);

            window.update();
        }

        window.terminate();

    }
}