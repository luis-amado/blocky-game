package com.lamdo.entity.player;

import com.lamdo.entity.PhysicsEntity;
import com.lamdo.physics.BoundingBox;
import com.lamdo.render.Window;
import com.lamdo.util.MathUtil;
import com.lamdo.util.Time;
import com.lamdo.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends PhysicsEntity {

    private float speed = 3f;
    private float sensitivity = 0.1f;

    private float gravity = 22f;
    private float jumpStrength = 7.3f;

    private Vector3f input;

    public Player (Vector3f position, World world) {
        super(position, world);
    }

    @Override
    protected BoundingBox setBoundingBox() {
        return new BoundingBox(0.6f, 1.75f);
    }

    @Override
    public float getEyeHeight() {
        return 1.71f;
    }

    public void move() {

        getInput();

        velocity.x = 0;
        velocity.z = 0;
        velocity.y -= gravity * Time.getDeltaTime();
        velocity = velocity.add(MathUtil.forwardVector(rotation.y).mul(input.z * speed)).add(MathUtil.rightVector(rotation.y).mul(input.x * speed));

        processMovement();

    }

    private void getInput() {
        Vector2f mousePos = Window.getMousePosition();
        rotation.y = mousePos.x * sensitivity;
        rotation.x = mousePos.y * sensitivity;
        rotation.x = Math.min(rotation.x, 90);
        rotation.x = Math.max(rotation.x, -90);

        input = new Vector3f();

        //horizontal movement
        if(Window.isKeyPressed(GLFW_KEY_W)) {
            input.z += 1;
        }
        if(Window.isKeyPressed(GLFW_KEY_S)) {
            input.z -= 1;
        }
        if(Window.isKeyPressed(GLFW_KEY_D)) {
            input.x += 1;
        }
        if(Window.isKeyPressed(GLFW_KEY_A)) {
            input.x -= 1;
        }
        if(input.lengthSquared() > 0)
            input.normalize();

        //jumping
        if(grounded && Window.isKeyPressed(GLFW_KEY_SPACE)) {
            velocity.y = jumpStrength;
        }
    }

}
