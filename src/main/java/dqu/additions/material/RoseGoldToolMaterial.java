package dqu.additions.material;

import dqu.additions.AdditionalRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class RoseGoldToolMaterial implements Tier {
    public static final RoseGoldToolMaterial MATERIAL = new RoseGoldToolMaterial();

    @Override
    public int getUses() {
        return 900;
    }

    @Override
    public float getSpeed() {
        return 12.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 3.0F;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public int getEnchantmentValue() {
        return 17;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(AdditionalRegistry.ROSE_GOLD_ALLOY.get());
    }

    @Override
    public String toString() {
        return "ROSE_GOLD";
    }
}
