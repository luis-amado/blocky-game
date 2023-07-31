package com.lamdo.entity;

import com.lamdo.block.Block;
import com.lamdo.block.util.Blockstate;
import com.lamdo.physics.AABB;
import com.lamdo.physics.BoundingBox;
import com.lamdo.physics.IntersectData;
import com.lamdo.render.renderer.ShapeRenderer;
import com.lamdo.util.Time;
import com.lamdo.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.List;

public abstract class PhysicsEntity extends Entity {

    protected World world;
    protected BoundingBox boundingBox;

    protected Vector3f velocity;
    public boolean grounded;

    public PhysicsEntity(Vector3f position, World world) {
        super(position, new Vector2f());
        velocity = new Vector3f();
        this.world = world;
        this.boundingBox = setBoundingBox();
    }

    protected void processMovement() {
        Vector3f scaledVelocity = new Vector3f(velocity);
        scaledVelocity.mul((float)Time.getDeltaTime());
        position.add(scaledVelocity);

        AABB aabb = boundingBox.toAABB(position);
        List<Vector3i> collidingBlocks = aabb.collidingBlocks(new Vector3f());
        for(Vector3i blockPos: collidingBlocks) {
            if(world.getBlockstate(blockPos).getBlock().isSolid()) {
                ShapeRenderer.drawBox(new Vector3f(blockPos), new BoundingBox(1, 1), new Vector3f(1, 0, 0));
            }
        }
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    protected abstract BoundingBox setBoundingBox();
    public abstract float getEyeHeight();

}
