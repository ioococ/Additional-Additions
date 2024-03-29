package dqu.additions.mixin;

import dqu.additions.AdditionalRegistry;
import dqu.additions.config.Config;
import dqu.additions.config.ConfigValues;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @ModifyVariable(method = "performShooting", at = @At("HEAD"), index = 5, argsOnly = true)
    private static float shootAll(float original, Level world, LivingEntity entity, InteractionHand hand, ItemStack stack, float speed, float divergence) {
        if (!Config.getBool(ConfigValues.ENCHANTMENT_PRECISION)) return original;
        int level = EnchantmentHelper.getItemEnchantmentLevel(AdditionalRegistry.ENCHANTMENT_PRECISION.get(), stack);
        if (level <= 0) return original;
        float precision = (float) ( (level * 3) * 0.1 );
        return original - precision;
    }
}
