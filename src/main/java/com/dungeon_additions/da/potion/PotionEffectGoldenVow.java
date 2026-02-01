package com.dungeon_additions.da.potion;

import com.dungeon_additions.da.config.PotionTrinketConfig;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public class PotionEffectGoldenVow extends PotionBase{

    double amount = PotionTrinketConfig.golden_vow_additive_amount;
    double speed_amount = PotionTrinketConfig.golden_vow_speed_amount;

    public PotionEffectGoldenVow(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(1,0);
        setBeneficial();
        this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "901E0DFB-90AE-3094-0012-831010E049A7",3F, 0);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "054E0DFB-67AE-5093-1029-831010E209A0", speed_amount, 1);
    }


 //   @Override
  //  public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
   //     return amount * (double)(amplifier + 1);
  //  }

}
