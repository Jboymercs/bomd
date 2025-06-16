package com.dungeon_additions.da.entity.projectiles.puzzle;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.tileEntity.TileEntityGrumBlocker;
import com.dungeon_additions.da.entity.tileEntity.TileEntityPuzzleMirror;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;

public class ProjectilePuzzleBall extends Projectile {

    public static final int PARTICLE_AMOUNT = 1;

    private Block inTile;

    private int ticksInGround;

    public ProjectilePuzzleBall(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setSize(0.25F, 0.25F);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public ProjectilePuzzleBall(World worldIn) {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public ProjectilePuzzleBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setSize(0.25F, 0.25F);
        this.setNoGravity(true);
        this.noClip = true;
    }

    @Override
    protected void spawnParticles() {
        if(this.ticksExisted % 3 == 0) {
            for (int i = 0; i < PARTICLE_AMOUNT; i++) {
                Main.proxy.spawnParticle(4, this.posX, this.posY, this.posZ, 0, 0, 0);
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if(this.ticksExisted > 7) {
            if(result.entityHit == null) {
                boolean playSound = false;
                if (world.getBlockState(result.getBlockPos()).getBlock() == ModBlocks.PUZZLE_DISPLACER && !world.isRemote) {
                    BlockPos blockpos = new BlockPos(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ());
                    //spawns one going up
                    if (world.isAirBlock(blockpos.up())) {
                        ProjectilePuzzleBall ball = new ProjectilePuzzleBall(world);
                        ball.setPosition(blockpos.getX() + 0.5, blockpos.getY() + 1.25, blockpos.getZ() + 0.5);
                        //ball.shoot(blockpos.getX(), blockpos.getY() + 8, blockpos.getZ(), 0.25F, 0F);
                        ball.motionY = 0.25;
                        // ball.shoot(this, this.rotationPitch - 179,0, 0, 0.25F, 0F);
                        world.spawnEntity(ball);
                        playSound = true;
                    }
                    //spawns one going down
                    if (world.isAirBlock(blockpos.down())) {
                        ProjectilePuzzleBall ball = new ProjectilePuzzleBall(world);
                        ball.setPosition(blockpos.getX() + 0.5, blockpos.getY() - 0.25, blockpos.getZ() + 0.5);
                        // ball.shoot(blockpos.getX(), blockpos.getY() - 8, blockpos.getZ(), 0.25F, 0F);
                        // ball.shoot(this, this.rotationPitch + 179, 0, 0, 0.25F, 0F);
                        ball.motionY = -0.25;
                        world.spawnEntity(ball);
                        playSound = true;
                    }
                    if(playSound) {
                        this.playSound(SoundsHandler.V_MIRROR_PLING, 0.5f, 1.0f / (rand.nextFloat() * 0.8F + 0.4f));
                    }
                    this.setDead();
                } else if (world.getBlockState(result.getBlockPos()).getBlock() == ModBlocks.PUZZLE_MIRROR && !world.isRemote) {
                    BlockPos blockpos = new BlockPos(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ());
                    TileEntity te = world.getTileEntity(blockpos);
                    if (te instanceof TileEntityPuzzleMirror) {
                        TileEntityPuzzleMirror mirror = ((TileEntityPuzzleMirror) te);
                        int skullRot = mirror.serverSkullRotation;
                        System.out.println(skullRot);
                        float yawRot = (float) (skullRot * 360 / 16);
                        ProjectilePuzzleBall ball = new ProjectilePuzzleBall(world);
                        ball.setPosition(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
                        ball.shootWithYaw(0, (float) (yawRot), 0, 0.4F, 0F);
                        world.spawnEntity(ball);
                        this.playSound(SoundsHandler.MIRROR_PLING, 0.5f, 1.0f / (rand.nextFloat() * 0.8F + 0.4f));

                    }
                    this.setDead();
                } else if (world.getBlockState(result.getBlockPos()).getBlock() == ModBlocks.PUZZLE_BLOCKER && !world.isRemote) {
                    BlockPos blockpos = new BlockPos(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ());
                    TileEntity te = world.getTileEntity(blockpos);
                    if(te instanceof TileEntityGrumBlocker) {
                        TileEntityGrumBlocker blocker = ((TileEntityGrumBlocker) te);
                        blocker.doExplodeBlock();
                        this.world.newExplosion(this, this.posX, this.posY, this.posZ, 1, false, false);
                    }
                    this.setDead();
                }
            }

                this.setDead();
                super.onHit(result);

        }

    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }


    private double getYawOffsetForRot(int skullVal) {
        if(skullVal == 16 || skullVal == 0) {
            return 0;
        } else if (skullVal == 8) {
            return 180;
        } else if (skullVal == 1) {
            return 35;
        }  else if (skullVal == 2) {
            return 63;
        }  else if (skullVal == 3) {
            return 85;
        }  else if (skullVal == 4) {
            return 115;
        }  else if (skullVal == 5) {
            return 140;
        }  else if (skullVal == 6) {
            return 153;
        }  else if (skullVal == 7) {
            return 165;
        }  else if (skullVal == 9) {
            return 195;
        }  else if (skullVal == 10) {
            return 207;
        }  else if (skullVal == 11) {
            return 220;
        }   else if (skullVal == 12) {
            return 245;
        }   else if (skullVal == 13) {
            return 275;
        }  else if (skullVal == 14) {
            return 297;
        }  else if (skullVal == 15) {
            return 323;
        }
        return 0;
    }

}
