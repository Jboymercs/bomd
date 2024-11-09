package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Consumer;

public class EntityRotKnightBoss extends EntityAbstractBase implements IAnimatable, IAttack {
    private Consumer<EntityLivingBase> prevAttack;
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> PHASE_TRANSITION = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);

    private boolean isPhaseTransition() {return this.dataManager.get(PHASE_TRANSITION);}
    private void setPhaseTransition(boolean value) {this.dataManager.set(PHASE_TRANSITION, Boolean.valueOf(value));}
    public EntityRotKnightBoss(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.75F, 1.95F);
    }

    public EntityRotKnightBoss(World worldIn) {
        super(worldIn);
        this.setSize(0.75F, 1.95F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Phase", this.isPhaseTransition());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setPhaseTransition(nbt.getBoolean("Pierce"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(PHASE_TRANSITION, Boolean.valueOf(false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        return 0;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "rot_knight_boss");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }
}
