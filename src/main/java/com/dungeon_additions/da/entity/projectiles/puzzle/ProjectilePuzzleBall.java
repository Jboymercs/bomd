package com.dungeon_additions.da.entity.projectiles.puzzle;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntitySharedDesertBoss;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.tileEntity.TileEntityGrumBlocker;
import com.dungeon_additions.da.entity.tileEntity.TileEntityPuzzleMirror;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
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
            } else if(result.entityHit != null) {
                if(result.entityHit instanceof EntitySharedDesertBoss) {
                    EntitySharedDesertBoss boss = ((EntitySharedDesertBoss) result.entityHit);
                    if(boss.isShielded()) {
                        DamageSource source = ModDamageSource.builder()
                                .indirectEntity(this)
                                .directEntity(this)
                                .type(ModDamageSource.MAGIC)
                                .stoppedByArmorNotShields().build();
                        boss.attackEntityFrom(source, 100);
                        this.setDead();
                        super.onHit(result);
                    }
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



}
