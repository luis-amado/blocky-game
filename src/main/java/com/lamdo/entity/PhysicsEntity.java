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

        grounded = false;

        // objective: modify the velocity that will be aplied to prevent collision

        // create a copy of the velocity, multiplied by delta time to represent the desired amount to move in this frame
        Vector3f processVelocity = new Vector3f(velocity);
        processVelocity.mul((float)Time.getDeltaTime());

        // repeat a maximum of 3 times
        // this is because after a collision, a slide is applied and that slide can cause another collision
        // the max number of collisions is 3 because you can only slide in 3 directions however this may need to be tweaked
        for(int i = 0; i < 3; i++) {
            // find the closest collision
            IntersectData closestCollision = IntersectData.noCollision();
            AABB aabb = boundingBox.toAABB(position);

            // loop through all the blocks that collide with the bounding box from its current position, to its desired position
            Vector3f finalPosition = new Vector3f(position);
            finalPosition.add(processVelocity);
            AABB finalAABB = boundingBox.toAABB(finalPosition);
            for(Vector3i blockPos: aabb.collidingBlocks(finalAABB)) {
                Blockstate blockstate = world.getBlockstate(blockPos);
                if(!blockstate.getBlock().hasCollision()) continue; // Skip blocks with no collision

                AABB blockAABB = new AABB(new Vector3f(blockPos), new Vector3f(blockPos.x+1, blockPos.y+1, blockPos.z+1));
                IntersectData collision = aabb.sweptAABB(blockAABB, processVelocity);

                // store the closest collision
                if(collision.getCollisionTime() < closestCollision.getCollisionTime()) {
                    closestCollision = collision;
                }
            }

            // stop if a collision didnt occur
            if(!closestCollision.isIntersecting()) break;

            // if the collision occured on a face that points up, it means we hit the ground and therefore can be grounded
            if(closestCollision.getCollisionNormal().y > 0) {
                grounded = true;
                velocity.y = 0;
            }

            // store the remaining time and velocity
            float remainingTime = 1f - closestCollision.getCollisionTime();
            Vector3f remainingVelocity = new Vector3f(processVelocity);
            remainingVelocity.mul(remainingTime);

            // calculate sliding by projecting the remaining velocity onto the collision surface (normal)
            Vector3f normal = new Vector3f(closestCollision.getCollisionNormal());
            Vector3f slideProjection = normal.mul(remainingVelocity.dot(closestCollision.getCollisionNormal()));
            Vector3f slideVelocity = new Vector3f(remainingVelocity);
            slideVelocity.sub(slideProjection);

            // modify the velocity by the collisionTime and also the calculated slide velocity
            processVelocity.mul(closestCollision.getCollisionTime());
            processVelocity.add(closestCollision.getCollisionNormal().mul(0.0001f));
            processVelocity.add(slideVelocity);
        }

        // update position
        position.add(processVelocity);


    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    protected abstract BoundingBox setBoundingBox();
    public abstract float getEyeHeight();

}
