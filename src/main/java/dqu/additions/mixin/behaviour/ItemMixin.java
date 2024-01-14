package dqu.additions.mixin.behaviour;

import dqu.additions.Additions;
import dqu.additions.behaviour.BehaviourManager;
import dqu.additions.behaviour.BehaviourValues;
import dqu.additions.material.GildedNetheriteArmorMaterial;
import dqu.additions.material.RoseGoldArmorMaterial;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void modifyItemDurability(CallbackInfoReturnable<Integer> cir) {
        ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey((Item) (Object) this);
        if (resourceLocation.getNamespace().equals(Additions.namespace)) {
            String path = resourceLocation.getPath();
            if (path.startsWith(GildedNetheriteArmorMaterial.NAME)) {
                String name = GildedNetheriteArmorMaterial.NAME + "/" + path.substring(GildedNetheriteArmorMaterial.NAME.length() + 1);
                Integer durability = BehaviourManager.INSTANCE.getBehaviourValue(name, BehaviourValues.DURABILITY);
                if (durability != null) cir.setReturnValue(durability);
            } else if (path.startsWith(RoseGoldArmorMaterial.NAME)) {
                String name = RoseGoldArmorMaterial.NAME + "/" + path.substring(RoseGoldArmorMaterial.NAME.length() + 1);
                Integer durability = BehaviourManager.INSTANCE.getBehaviourValue(name, BehaviourValues.DURABILITY);
                if (durability != null) cir.setReturnValue(durability);
            }
        }
    }
}
