package com.dungeon_additions.da.entity.trader;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.gaelon_dungeon.EntityReAnimateAttackAI;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkRoyal;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityReAnimate;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EntityMysteriousTrader extends EntityAbstractBase implements IAnimatable, IAnimationTickable {

    private static final String ANIM_IDLE = "idle";
    private static final String ANIM_WALK = "walk";

    private static final String ANIM_NOD_NO = "nod_no";
    private static final String ANIM_THROW_ITEM = "throw_item";

    private static final DataParameter<Boolean> NOD_NO = EntityDataManager.createKey(EntityMysteriousTrader.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THROW_ITEM_PLAYER = EntityDataManager.createKey(EntityMysteriousTrader.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityMysteriousTrader.class, DataSerializers.ITEM_STACK);
    private void setNodNo(boolean value) {this.dataManager.set(NOD_NO, Boolean.valueOf(value));}
    public boolean isNodNo() {return this.dataManager.get(NOD_NO);}
    private void setThrowItemPlayer(boolean value) {this.dataManager.set(THROW_ITEM_PLAYER, Boolean.valueOf(value));}
    private boolean isThrowItemPlayer() {return this.dataManager.get(THROW_ITEM_PLAYER);}

    private final AnimationFactory factory = new AnimationFactory(this);

    private long lootTableCopperSeed;
    private long lootTableSilverSeed;
    private long lootTableGoldSeed;

    public EntityMysteriousTrader(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 0;
        this.setSize(0.6F, 1.95F);
    }

    public EntityMysteriousTrader(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 0;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Nod_No", this.isNodNo());
        nbt.setBoolean("Throw_Item", this.isThrowItemPlayer());
        nbt.setLong("copper_table_seed", lootTableCopperSeed);
        nbt.setLong("silver_table_seed", lootTableSilverSeed);
        nbt.setLong("gold_table_seed", lootTableGoldSeed);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setNodNo(nbt.getBoolean("Nod_No"));
        this.setThrowItemPlayer(nbt.getBoolean("Throw_Item"));
        this.lootTableCopperSeed = nbt.getLong("copper_table_seed");
        this.lootTableSilverSeed = nbt.getLong("silver_table_seed");
        this.lootTableGoldSeed = nbt.getLong("gold_table_seed");
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ITEM_HAND, ItemStack.EMPTY);
        this.dataManager.register(NOD_NO, Boolean.valueOf(false));
        this.dataManager.register(THROW_ITEM_PLAYER, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 5));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    private EntityPlayer selectedPlayer = null;
    private int nodCooldown = 0;
    private int traderCooldown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        nodCooldown--;
        traderCooldown--;
        if(!world.isRemote) {

           selectedPlayer = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 5, Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.notRiding(this)));
            //allows trading
            if(selectedPlayer != null && traderCooldown < 0) {
                //disables the player trading if they get out of trading distance
                    ItemStack playerStack = selectedPlayer.getHeldItemMainhand();

                    if(playerStack.isEmpty()) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM));
                    } else if (playerStack.getItem() == ModItems.COPPER_COIN || playerStack.getItem() == ModItems.SILVER_COIN || playerStack.getItem() == ModItems.GOLDEN_COIN) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.TRADER_BAG));
                    } else if (playerStack.getItem() == ModItems.FROST_INGOT) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.COPPER_COIN));
                    } else if (playerStack.getItem() == ModItems.DARK_CORE) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.SILVER_COIN));
                    } else if (playerStack.getItem() == ModItems.LITIC_INGOT) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.GOLDEN_COIN));
                    } else if (playerStack.getItem() == ModItems.NOVIK_PLATE) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.DIAMOND));
                    } else if (playerStack.getItem() == ModItems.AEGYPTIA_METAL_INGOT) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.ROT_KNIGHT_INGOT));
                    } else if (playerStack.getItem() == ModItems.ROT_KNIGHT_FRAGMENT) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.ADVENTURE_METAL));
                    } else if (playerStack.getItem() == ModItems.ROT_KNIGHT_INGOT) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.GAELON_LOCATOR));
                    } else if (playerStack.getItem() == ModItems.FROST_SHARD) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.MAGIC_LEATHER));
                    } else if (playerStack.getItem() == ModItems.ABBERRANT_EYE) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.DARK_MANA));
                    } else if (playerStack.getItem() == ModItems.DRAUGR_METAL) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.BOW));
                    } else if (playerStack.getItem() == ModItems.CRYPT_PLATE) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.BLAZE_ROD));
                    } else if (playerStack.getItem() == ModItems.DARK_MANA) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.NETHER_WART));
                    } else if (playerStack.getItem() == ModItems.AEGYPTIA_BONE) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.BONE));
                    } else if (playerStack.getItem() == ModItems.AEGYPTIA_METAL) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.ENDER_EYE));
                    } else if (playerStack.getItem() == ModItems.BEETLE_SHELL) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.PRISMARINE_SHARD));
                    } else if (playerStack.getItem() == ModItems.DRAUGR_INGOT) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.FORGOTTEN_TEMPLE_LOCATOR));
                    } else if (playerStack.getItem() == ModItems.LIGHT_MANA) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.EXPERIENCE_BOTTLE));
                    } else if (playerStack.getItem() == ModItems.VOIDIANT_CORE) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.GHAST_TEAR));
                    } else if (playerStack.getItem() == ModItems.LITIC_SHARD) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.DIAMOND));
                    } else if (playerStack.getItem() == ModItems.SENTINEL_PART) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.GOLD_INGOT));
                    } else if (playerStack.getItem() == ModItems.INCENDIUM_CORE) {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(Items.MAGMA_CREAM));
                    }else {
                        this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM));
                        if(nodCooldown < 0) {
                            this.NodToPlayerNo();
                        }
                    }
                    Vec3d playerPos = selectedPlayer.getPositionVector();
                    this.getLookHelper().setLookPosition(playerPos.x, playerPos.y + selectedPlayer.getEyeHeight(), playerPos.z, 30, 30);


               // this.faceEntity(selectedPlayer, 30, 30);
               // this.getLookHelper().setLookPositionWithEntity(selectedPlayer, 30, 30);

                //does trade handling
            } else {
                this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM));
                selectedPlayer = null;
            }
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(!world.isRemote && selectedPlayer != null && !this.isNodNo() && !this.isThrowItemPlayer() && traderCooldown < 0) {
            if(stack.getItem() == ModItems.ROT_KNIGHT_FRAGMENT) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.ADVENTURE_METAL, 1), selectedPlayer);
            } else if(stack.getItem() == ModItems.FROST_INGOT) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.COPPER_COIN), selectedPlayer);
            } else if(stack.getItem() == ModItems.DARK_CORE) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.SILVER_COIN), selectedPlayer);
            } else if(stack.getItem() == ModItems.LITIC_INGOT) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.GOLDEN_COIN), selectedPlayer);
            } else if(stack.getItem() == ModItems.NOVIK_PLATE) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.DIAMOND), selectedPlayer);
            } else if(stack.getItem() == ModItems.AEGYPTIA_METAL_INGOT) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.ROT_KNIGHT_INGOT), selectedPlayer);
            } else if(stack.getItem() == ModItems.NOVIK_PLATE) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.DIAMOND), selectedPlayer);
            } else if(stack.getItem() == ModItems.ROT_KNIGHT_INGOT) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.GAELON_LOCATOR), selectedPlayer);
            } else if(stack.getItem() == ModItems.FROST_SHARD) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.MAGIC_LEATHER, 2), selectedPlayer);
            } else if(stack.getItem() == ModItems.ABBERRANT_EYE) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.DARK_CORE), selectedPlayer);
            } else if(stack.getItem() == ModItems.DRAUGR_METAL) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.BOW), selectedPlayer);
            } else if(stack.getItem() == ModItems.CRYPT_PLATE) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.BLAZE_ROD), selectedPlayer);
            } else if(stack.getItem() == ModItems.DARK_MANA) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.NETHER_WART, 4), selectedPlayer);
            } else if(stack.getItem() == ModItems.AEGYPTIA_BONE) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Item.getItemFromBlock(Blocks.BONE_BLOCK)), selectedPlayer);
            } else if(stack.getItem() == ModItems.AEGYPTIA_METAL) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.ENDER_EYE), selectedPlayer);
            } else if(stack.getItem() == ModItems.BEETLE_SHELL) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.PRISMARINE_SHARD, 4), selectedPlayer);
            } else if(stack.getItem() == ModItems.DRAUGR_INGOT) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(ModItems.FORGOTTEN_TEMPLE_LOCATOR), selectedPlayer);
            } else if(stack.getItem() == ModItems.LIGHT_MANA) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.EXPERIENCE_BOTTLE), selectedPlayer);
            } else if(stack.getItem() == ModItems.VOIDIANT_CORE) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.GHAST_TEAR), selectedPlayer);
            } else if(stack.getItem() == ModItems.LITIC_SHARD) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.DIAMOND, 2), selectedPlayer);
            } else if(stack.getItem() == ModItems.SENTINEL_PART) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.GOLD_INGOT, 8), selectedPlayer);
            } else if(stack.getItem() == ModItems.INCENDIUM_CORE) {
                stack.shrink(1);
                this.throwItemAfterTrade(new ItemStack(Items.MAGMA_CREAM, 2), selectedPlayer);
            } else if (stack.getItem() == ModItems.COPPER_COIN) {
                stack.shrink(1);
                this.throwItemAfterTrade(getCopperLootTable(), selectedPlayer);
            } else if (stack.getItem() == ModItems.SILVER_COIN) {
                stack.shrink(1);
                this.throwItemAfterTrade(getSilverLootTable(), selectedPlayer);
            } else if (stack.getItem() == ModItems.GOLDEN_COIN) {
                stack.shrink(1);
                this.throwItemAfterTrade(getGoldenLootTable(), selectedPlayer);
            }
        }
        return super.processInteract(player, hand);
    }

    private static final ResourceLocation COPPER_TIER = new ResourceLocation(ModReference.MOD_ID, "copper_loot_bag");
    private static final ResourceLocation SILVER_TIER = new ResourceLocation(ModReference.MOD_ID, "silver_loot_bag");
    private static final ResourceLocation GOLDEN_TIER = new ResourceLocation(ModReference.MOD_ID, "golden_loot_bag");
    private List<ItemStack> copper_trade_items = Lists.newArrayList();
    private List<ItemStack> silver_trade_items = Lists.newArrayList();
    private List<ItemStack> golden_trade_items = Lists.newArrayList();

    protected ItemStack getCopperLootTable() {
        if(!world.isRemote) {
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) this.world)).withLootedEntity(this);
            copper_trade_items = this.world.getLootTableManager().getLootTableFromLocation(COPPER_TIER).generateLootForPools(this.lootTableCopperSeed == 0 ? new Random() :new Random(this.lootTableCopperSeed), lootcontext$builder.build());
            for (ItemStack item : copper_trade_items) {
                return item;
                //this.world.getLootTableManager().reloadLootTables();
            }
        }
        return ItemStack.EMPTY;
    }

    protected ItemStack getSilverLootTable() {
        if(!world.isRemote) {
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) this.world)).withLootedEntity(this);
            silver_trade_items = this.world.getLootTableManager().getLootTableFromLocation(SILVER_TIER).generateLootForPools(this.lootTableSilverSeed == 0 ? new Random() :new Random(this.lootTableSilverSeed), lootcontext$builder.build());
            for (ItemStack item : silver_trade_items) {
                return item;
                //this.world.getLootTableManager().reloadLootTables();
            }
        }
        return ItemStack.EMPTY;
    }

    protected ItemStack getGoldenLootTable() {
        if(!world.isRemote) {
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) this.world)).withLootedEntity(this);
            golden_trade_items = this.world.getLootTableManager().getLootTableFromLocation(GOLDEN_TIER).generateLootForPools(this.lootTableGoldSeed == 0 ? new Random() :new Random(this.lootTableGoldSeed), lootcontext$builder.build());
            for (ItemStack item : golden_trade_items) {
                return item;
                //this.world.getLootTableManager().reloadLootTables();
            }
        }
        return ItemStack.EMPTY;
    }


    private void NodToPlayerNo() {
        this.setNodNo(true);
        this.nodCooldown = 140;

        addEvent(()-> {
        this.setNodNo(false);
        }, 50);
    }

    private void throwItemAfterTrade(ItemStack stack, EntityPlayer player) {
        this.setThrowItemPlayer(true);
        this.setFullBodyUsage(true);

        addEvent(()-> {
            //throws item in hand too player
            EntityItem itemToThrow = new EntityItem(world, this.posX, this.posY + this.getEyeHeight() - 0.1, this.posZ, stack);
            traderCooldown = 20;
            Vec3d lookScale = player.getPositionVector();
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.6, 0)));
            double d0 = lookScale.y + (double)player.getEyeHeight() - 1.100000023841858D;
            double d1 = lookScale.x - relPos.x;
            double d2 = d0 - this.posY;
            double d3 = lookScale.z - relPos.z;
            float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
            this.shoot(itemToThrow, d1, d2 + (double)(f * 0.1F), d3, 0.3F, 1.0F);
            itemToThrow.velocityChanged = true;
           // itemToThrow.addVelocity(lookScale.x, lookScale.y, lookScale.z);
            world.spawnEntity(itemToThrow);
            this.equipItemInHand(TRADER_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM));
        }, 15);
        addEvent(()-> {
        this.setThrowItemPlayer(false);
        this.setFullBodyUsage(false);
        }, 30);
    }

    public void shoot(Entity entityIn, double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        entityIn.motionX = x;
        entityIn.motionY = y;
        entityIn.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        entityIn.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        entityIn.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        entityIn.prevRotationYaw = entityIn.rotationYaw;
        entityIn.prevRotationPitch = entityIn.rotationPitch;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "other_controller", 0, this::predicateOther));
    }

    private <E extends IAnimatable> PlayState predicateOther(AnimationEvent<E> event) {
        if(this.isNodNo()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_NOD_NO));
            return PlayState.CONTINUE;
        }
        if(this.isThrowItemPlayer()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THROW_ITEM));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            event.getController().setAnimationSpeed(1.0 + (0.001 * event.getLimbSwing()));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return  SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    public enum TRADER_HAND {
        HAND("ItemJoint");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        TRADER_HAND(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static EntityMysteriousTrader.TRADER_HAND getFromBoneName(String boneName) {
            if ("ItemJoint".equals(boneName)) {
                return HAND;
            }
            return null;
        }

        public int getIndex() {
            if (this == TRADER_HAND.HAND) {
                return 1;
            }
            return 0;
        }
    }

    public void equipItemInHand(EntityMysteriousTrader.TRADER_HAND head, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (head == TRADER_HAND.HAND) {
            this.dataManager.set(ITEM_HAND, state);
        }
    }

    public ItemStack getItemFromRoyalHand(EntityMysteriousTrader.TRADER_HAND head) {
        if (head == TRADER_HAND.HAND) {
            return this.dataManager.get(ITEM_HAND);
        }
        return null;
    }
}
