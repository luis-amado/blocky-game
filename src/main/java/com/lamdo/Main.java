package com.lamdo;

import com.lamdo.render.Window;

import static org.lwjgl.opengl.GL33.*;

public class Main {

    public static void main(String[] args) {

        Window window = new Window();

        while(!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 0, 0, 1);
            window.update();
        }

        window.terminate();

    }
}