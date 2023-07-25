package com.lamdo.entity;

import com.lamdo.block.Block;
import com.lamdo.block.util.Blockstate;
import com.lamdo.physics.AABB;
import com.lamdo.physics.BoundingBox;
import com.lamdo.physics.IntersectData;
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
        Vector3f velocityPrev = new Vector3f(velocity);
        velocity = velocity.mul((float)Time.getDeltaTime());

        grounded = false;

        while(true) {
            AABB aabb = boundingBox.toAABB(position);
            List<Vector3i> collidingBlocks = aabb.collidingBlocks(velocity);

            IntersectData closestCollision = new IntersectData(false, 1f, new Vector3f());

            for(Vector3i pos: collidingBlocks) {
                Blockstate blockstate = world.getBlockstate(pos.x, pos.y, pos.z);
                Block block = blockstate.getBlock();
                if(!block.hasCollision()) continue;
                AABB blockAABB = block.getAABB(pos.x, pos.y, pos.z);

                IntersectData currentCollision = aabb.sweptAABB(blockAABB, velocity);
                currentCollision.setBlock(blockstate, pos);
                if(currentCollision.getCollisionTime() < closestCollision.getCollisionTime()) closestCollision = currentCollision;
            }

            if(closestCollision.getCollisionNormal().y > 0) {
                grounded = true;
            }

            float ep = 0.0001f;
            position.x += closestCollision.getCollisionTime() * velocity.x + ep * closestCollision.getCollisionNormal().x;
            position.z += closestCollision.getCollisionTime() * velocity.z + ep * closestCollision.getCollisionNormal().z;
            position.y += closestCollision.getCollisionTime() * velocity.y + ep * closestCollision.getCollisionNormal().y;

            if(closestCollision.getCollisionTime() == 1) break;

//			Debug.drawBox(closestCollision.getBlockpos().toVector3f(), new BoundingBox(1, 1), new Vector3f(1, 0, 0));


            if(closestCollision.getCollisionNormal().x != 0) velocityPrev.x = 0;
            if(closestCollision.getCollisionNormal().y != 0) velocityPrev.y = 0;
            if(closestCollision.getCollisionNormal().z != 0) velocityPrev.z = 0;

            //sliding
            Vector3f normal = closestCollision.getCollisionNormal();
            float BdotB = normal.x*normal.x + normal.y*normal.y + normal.z*normal.z;
            float remainingTime = 1-closestCollision.getCollisionTime();
            if(BdotB != 0) {
                float AdotB = remainingTime * (velocity.x * normal.x + velocity.y * normal.y + velocity.z * normal.z);
                velocity.x = remainingTime * velocity.x - (AdotB/BdotB)*normal.x;
                velocity.y = remainingTime * velocity.y - (AdotB/BdotB)*normal.y;
                velocity.z = remainingTime * velocity.z - (AdotB/BdotB)*normal.z;
            }
        }
        velocity = velocityPrev;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    protected abstract BoundingBox setBoundingBox();
    public abstract float getEyeHeight();

}
