package com.lamdo.entity.player;

import com.lamdo.block.Blocks;
import com.lamdo.entity.PhysicsEntity;
import com.lamdo.physics.AABB;
import com.lamdo.physics.BoundingBox;
import com.lamdo.render.Window;
import com.lamdo.render.renderer.ShapeRenderer;
import com.lamdo.util.*;
import com.lamdo.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;

import java.lang.annotation.Target;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends PhysicsEntity {

    private float speed = 4f;
    private float sensitivity = 0.1f;

    private float gravity = 22f;
    private float jumpStrength = 7.3f;

    private Vector3f input;

    public Player (Vector3f position, World world) {
        super(position, world);
    }

    @Override
    protected BoundingBox setBoundingBox() {
        return new BoundingBox(0.7f, 1.75f);
    }

    @Override
    public float getEyeHeight() {
        return 1.71f;
    }

    public void move() {

        getInput();

        velocity.x = 0;
        velocity.z = 0;
        if(velocity.y > -25f)
            velocity.y -= gravity * Time.getDeltaTime();
        velocity = velocity.add(MathUtil.forwardVector(rotation.y).mul(input.z * speed)).add(MathUtil.rightVector(rotation.y).mul(input.x * speed));

        processMovement();

        // draw bounding box
        if(Window.debugMode) {
            ShapeRenderer.drawBoxCenteredBottom(position, boundingBox, new Vector4f(1, 1, 1, 1));
        }
        ShapeRenderer.drawDebugCrosshair();

        playerInteraction();
    }

    private void playerInteraction() {
        TargetedBlock targetBlock = findLookingAtBlock();
        Vector3i lookingAt = targetBlock.position();
        Direction face = targetBlock.face();

        if(lookingAt != null) {
            ShapeRenderer.drawBox(new Vector3f(lookingAt), new BoundingBox(1, 1), new Vector4f(0f, 0f, 0f, 0.5f));

            if(Window.getMouseButton(GLFW_MOUSE_BUTTON_LEFT)) {
                world.updateBlock(lookingAt, Blocks.AIR);
                lookingAt.sub(face.getNormal());
            }
            if(Window.getMouseButton(GLFW_MOUSE_BUTTON_RIGHT)) {
                Vector3i placePos = new Vector3i(lookingAt);
                placePos.add(face.getNormal());
                AABB aabb = boundingBox.toAABB(position);
                AABB blockAABB = new AABB(new Vector3f(placePos), new Vector3f(placePos.x+1, placePos.y+1, placePos.z+1));
                if(!aabb.checkCollision(blockAABB))
                    world.updateBlock(placePos, Blocks.DEEPSLATE_BRICKS);
            }

        }
    }

    private TargetedBlock findLookingAtBlock() {
        Vector3f head = new Vector3f(position);
        head.add(new Vector3f(0, getEyeHeight(), 0));
        Vector3f prevPos = null;
        Vector3i lookingAt = null;
        Direction face = null;

        // This implementation is fine for now but it might have to change at some point
        for(float d = 0f; d < 5; d+=0.1f) {
            Vector3f currPos = new Vector3f(head);
            currPos.add(MathUtil.forwardVector(getYaw(), getPitch()).mul(d));
            Vector3i currBlockPos = Vector3Util.floorToInt(currPos);
            if(prevPos != null && world.getBlockstate(currBlockPos.x, currBlockPos.y, currBlockPos.z).getBlock().isSolid()) {
                // run binary search between outpos and currpos to find the closest block
                for(int i = 0; i < 100; i++) {
                    Vector3f distance = new Vector3f(currPos);
                    if(distance.sub(prevPos).lengthSquared() < 0.00001f * 0.00001f) {
                        break;
                    }
                    Vector3f middle = new Vector3f((currPos.x+prevPos.x)/2, (currPos.y+prevPos.y)/2, (currPos.z+prevPos.z)/2);
                    Vector3i middlePos = Vector3Util.floorToInt(middle);
                    if(world.getBlockstate(middlePos.x, middlePos.y, middlePos.z).getBlock().isSolid()) {
                        currPos = middle;
                    } else {
                        prevPos = middle;
                    }
                }
                currBlockPos = Vector3Util.floorToInt(currPos);
                Vector3i prevPosBlock = Vector3Util.floorToInt(prevPos);
                Vector3f faceNormal = new Vector3f(currBlockPos);
                faceNormal.sub(new Vector3f(prevPosBlock)).negate();
                face = Direction.fromFaceNormal(faceNormal);
                lookingAt = new Vector3i(currBlockPos.x, currBlockPos.y, currBlockPos.z);
                break;
            }
            prevPos = currPos;
        }
        return new TargetedBlock(lookingAt, face);
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

        if(Window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            position.y -= 0.1f;
        }

        //changing speed
        speed += Window.getMouseDWheel() * 0.5f;
        speed = Math.max(0, speed);
    }

}
