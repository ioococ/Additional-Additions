package dqu.additions.misc;

import dqu.additions.config.Config;
import dqu.additions.config.ConfigValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PocketMusicSoundInstance extends AbstractTickableSoundInstance {
    private final Player playerEntity;
    private final ItemStack stack;
    public static PocketMusicSoundInstance instance;

    public PocketMusicSoundInstance(SoundEvent soundEvent, Player playerEntity, ItemStack stack, boolean repeat, float volume) {
        super(soundEvent, SoundSource.RECORDS, RandomSource.create());
        this.playerEntity = playerEntity;
        this.stack = stack;
        this.looping = repeat;
        this.volume = volume;
        this.x = this.playerEntity.getX();
        this.y = this.playerEntity.getY();
        this.z = this.playerEntity.getZ();
    }

    @Override
    public void tick() {
        ItemStack cursor = this.playerEntity.containerMenu.getCarried();
        if (cursor == null) cursor = ItemStack.EMPTY;
        boolean hasDisc = ItemStack.matches(cursor, stack) || this.playerEntity.getInventory().contains(stack);

        if (this.playerEntity.isDeadOrDying() || !hasDisc || !Config.getBool(ConfigValues.POCKET_JUKEBOX)) {
            this.finish();
        } else {
            this.x = this.playerEntity.getX();
            this.y = this.playerEntity.getY();
            this.z = this.playerEntity.getZ();
        }
    }

    public void finish() {
        this.stop();
    }

    public void play() {
        Minecraft.getInstance().getSoundManager().play(this);
    }
 }
