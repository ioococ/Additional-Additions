package dqu.additions.mixin;

import dqu.additions.Additions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Minecraft minecraft;
    private double multiplier = 1.0F;
    private double lastMultiplier = 1.0F;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        double zoomMultiplier = Additions.zoom ? 0.1F : 1.0F;
        if (!minecraft.options.getCameraType().isFirstPerson()) {
            zoomMultiplier = 1.0F;
        }
        if (!Additions.zoom || !minecraft.options.getCameraType().isFirstPerson())
            Additions.spyglassOverlay = 0.5F;
        lastMultiplier = multiplier;
        multiplier += (zoomMultiplier - multiplier) * 0.66F;
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> callbackInfo) {
        double fov = callbackInfo.getReturnValue();
        double fovMultiplier = Mth.lerp(tickDelta, lastMultiplier, multiplier);
        double zoomedFov = fov * fovMultiplier;
        Additions.spyglassOverlay = Mth.lerp(0.5F * tickDelta, Additions.spyglassOverlay, 1.125F);

        if (Math.abs(fov-zoomedFov) > 0.5)
            callbackInfo.setReturnValue(zoomedFov);
    }
}
